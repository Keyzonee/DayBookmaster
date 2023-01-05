package su.svn.daybook.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@ApplicationScoped
public class PageService {

    private static final Logger LOG = Logger.getLogger(PageService.class);

    public <T> Uni<Page<Answer>> getPage(
            @Nonnull PageRequest pageRequest,
            @Nonnull Supplier<Uni<Optional<Long>>> countSupplier,
            @Nonnull BiFunction<Long, Short, Multi<T>> toMultiEntries,
            @Nonnull Function<T, Answer> toAnswer) {

        LOG.tracef("getPage(%s, Supplier, BiFunction)", pageRequest);
        BiFunction<Long, Short, Uni<List<Answer>>> toUniListAnswer =
                (Long offset, Short limit) -> toMultiEntries.apply(offset, limit)
                        .onItem()
                        .transform(toAnswer)
                        .collect()
                        .asList();
        return countSupplier.get()
                .flatMap(o -> getPageUni(pageRequest, toUniListAnswer, o));
    }

    private Uni<Page<Answer>> getPageUni(
            PageRequest pageRequest,
            BiFunction<Long, Short, Uni<List<Answer>>> toUniListAnswer,
            Optional<Long> o) {
        return o.map(count -> fetchPageAnswer(count, pageRequest, toUniListAnswer))
                .orElse(getItem(pageRequest));
    }

    private Uni<Page<Answer>> getItem(PageRequest pageRequest) {
        return Uni.createFrom()
                .item(Page.<Answer>builder().pageNumber(pageRequest.pageNumber()).build());
    }

    private Uni<Page<Answer>> fetchPageAnswer(
            long count,
            PageRequest pageRequest,
            BiFunction<Long, Short, Uni<List<Answer>>> toUniListAnswer) {

        LOG.tracef("fetchPageAnswer(%d, %s, BiFunction)", count, pageRequest);
        var limit = pageRequest.limit();
        var offset = pageRequest.pageNumber() * limit;
        LOG.tracef("fetchPageAnswer: limit=%d, offset=%d", limit, offset);
        var uniCount = Uni.createFrom().item(count);
        var uniPageRequest = Uni.createFrom().item(pageRequest);
        var uniListAnswer = toUniListAnswer.apply(offset, limit);

        return Uni.combine()
                .all()
                .unis(uniPageRequest, uniListAnswer, uniCount)
                .asTuple()
                .onItem()
                .transform(tuple -> preparePageAnswer(tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));
    }

    private Page<Answer> preparePageAnswer(PageRequest request, List<Answer> posts, long totalElements) {

        var pageNumber = request.pageNumber();
        var totalPages = request.calculateTotalPages(totalElements);
        var currentPageSize = posts.size();
        var hasPrevPage = pageNumber > 0 && totalElements > 0;
        var hasNextPage = pageNumber < (totalPages - 1);
        LOG.infof(
                "preparePageAnswer: pageNumber=%d, totalPages=%d, currentPageSize=%d, hasPrevPage=%s, hasNextPage=%s",
                pageNumber, totalPages, currentPageSize, hasPrevPage, hasNextPage
        );
        return Page.<Answer>builder()
                .pageNumber(pageNumber)
                .pageSize((short) currentPageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .prevPage(hasPrevPage)
                .nextPage(hasNextPage)
                .content(posts)
                .build();
    }
}
