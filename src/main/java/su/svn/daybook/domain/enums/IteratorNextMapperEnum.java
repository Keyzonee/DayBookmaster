package su.svn.daybook.domain.enums;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import su.svn.daybook.models.Constants;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

public enum IteratorNextMapperEnum {
    Null(null),
    LongIdIterator(it -> it.hasNext() ? Optional.of(it.next().getLong(Constants.ID)) : Optional.empty()),
    StringIdIterator(it -> it.hasNext() ? Optional.of(it.next().getString(Constants.ID)) : Optional.empty()),
    UUIDIdIterator(it -> it.hasNext() ? Optional.of(it.next().getUUID(Constants.ID)) : Optional.empty());

    private final Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> mapper;

    IteratorNextMapperEnum(Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> mapper) {
        this.mapper = mapper;
    }

    public Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> getMapper() {
        return mapper;
    }
}
