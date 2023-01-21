/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.Principled;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.Codifier;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.CodifierCacheProvider;
import su.svn.daybook.services.domain.CodifierDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
@Logged
public class CodifierService extends AbstractService<String, Codifier> {

    @Inject
    CodifierCacheProvider codifierCacheProvider;

    @Inject
    CodifierDataService codifierDataService;

    /**
     * This is method a Vertx message consumer and Codifier creater
     *
     * @param request - Codifier
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Codifier code as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.CODIFIER_ADD)
    public Uni<Answer> add(Request<Codifier> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(codifierCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Codifier deleter
     *
     * @param request - code of the Codifier
     * @return - a LAA with the Answer containing Codifier code as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Codifier provider by code
     *
     * @param request - code of the Codifier
     * @return - a lazy asynchronous action with the Answer containing the Codifier as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_GET)
    public Uni<Answer> get(Request<String> request) {
        //noinspection DuplicatedCode
        return codifierCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Codifier
     *
     * @return - the Answer's Multi-flow with all entries of Codifier
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return codifierDataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return codifierCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param request - Codifier
     * @return - a LAA with the Answer containing Codifier code as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_PUT)
    public Uni<Answer> put(Request<Codifier> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
