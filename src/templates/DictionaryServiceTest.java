/*
 * This file was last modified at 2021.12.15 13:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@ServiceTest.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class @Name@ServiceTest {

    @Inject
    @Name@Service service;

    static @Name@Dao mock;

    static final Uni<Optional<@Name@Table>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.@TABLE@.TABLE_0));

    static final Multi<@Name@Table> MULTI_TEST = Multi.createFrom().item(TestData.@TABLE@.TABLE_0);

    static final Multi<@Name@Table> MULTI_WITH_NULL = TestData.createMultiWithNull(@Name@Table.class);

    static final Multi<@Name@Table> MULTI_EMPTIES = TestData.createMultiEmpties(@Name@Table.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(@Name@Dao.class);
        Mockito.when(mock.findById(0L)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, @Name@Dao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.@TABLE@.MODEL_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    @ActivateRequestContext
    void testWhenGetAllThenCountMinusOne() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_MINUS_ONE_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertThrows(
                java.lang.IndexOutOfBoundsException.class,
                () -> result.addAll(service.getAll()
                        .subscribe()
                        .asStream()
                        .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenNull() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_WITH_NULL);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_TEST);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .totalPages(1L)
                .totalElements(1)
                .pageSize((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.@TABLE@.MODEL_0)))
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenEmpty() {

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_EMPTIES);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalElements(0)
                .pageSize((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenZeroPage() {

        Mockito.when(mock.findRange(0L, 0)).thenReturn(MULTI_EMPTIES);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0, (short) 0);
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalElements(1)
                .pageSize((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(0L)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.@TABLE@.MODEL_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenGetThenNoNumber() {
        Assertions.assertDoesNotThrow(() -> service.get("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.noNumber("For input string: \"noNumber\""), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(0L))
                .build();
        Mockito.when(mock.insert(TestData.@TABLE@.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.@TABLE@.MODEL_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.@TABLE@.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_LONG);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.@TABLE@.MODEL_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(0L))
                .build();
        Mockito.when(mock.update(TestData.@TABLE@.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.@TABLE@.MODEL_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.@TABLE@.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(TestData.@TABLE@.MODEL_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        var expected = Answer.of(new ApiResponse<>(0L));
        Assertions.assertDoesNotThrow(() -> service.delete(0L)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNoNumber() {
        Assertions.assertDoesNotThrow(() -> service.delete("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.noNumber("For input string: \"noNumber\""), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNullParameter() {
        Assertions.assertDoesNotThrow(() -> service.delete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }
}