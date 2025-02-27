/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.WordCacheProvider;
import su.svn.daybook.services.domain.WordDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@PrincipalLogging
@ApplicationScoped
public class WordService extends AbstractService<String, Word> {

    @Inject
    WordCacheProvider wordCacheProvider;

    @Inject
    WordDataService wordDataService;

    /**
     * This is method a Vertx message consumer and Word creater
     *
     * @param request - Word
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> add(Request<Word> request) {
        return wordDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(wordCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Word deleter
     *
     * @param request - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param request - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> get(Request<String> request) {
        return wordCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Word
     *
     * @return - the Answer's Multi-flow with all entries of Word
     */
    public Multi<Answer> getAll() {
        return wordDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(value = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return wordCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Word updater
     *
     * @param request - Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> put(Request<Word> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
