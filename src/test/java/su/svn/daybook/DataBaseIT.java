/*
 * This file was last modified at 2022.12.24 21:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DatabaseIT.java
 * $Id$
 */

package su.svn.daybook;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.model.UserName;
import su.svn.daybook.domain.model.Vocabulary;
import su.svn.daybook.domain.model.Word;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@QuarkusTest
@QuarkusTestResource(PostgresDatabaseTestResource.class)
public class DataBaseIT {

    @Inject
    UserNameDao userNameDao;

    @Inject
    VocabularyDao vocabularyDao;

    @Inject
    WordDao wordDao;

    @Nested
    @DisplayName("UserNameDao")
    class UserNameDaoTest {

        UUID uuid1 = new UUID(0, 1);

        UserName userName1;

        @BeforeEach
        void setUp() throws ExecutionException, InterruptedException {
            userName1 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("user")
                    .withPassword("password")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.insert(userName1)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.delete(uuid1)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() throws ExecutionException, InterruptedException {
            var expected1 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("user")
                    .withPassword("password")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(uuid1)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("none")
                    .withPassword("oops")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(uuid1)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected2, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNotNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
        }
    }


    @Nested
    @DisplayName("VocabularyDao")
    class VocabularyDaoAndWordDaoTest {

        String veryLongWordIdForTest = "veryLongWordIdForTest";

        Long vocabularyId;

        Vocabulary vocabulary;

        Word word;

        @BeforeEach
        void setUp() throws ExecutionException, InterruptedException {
            vocabulary = Vocabulary.builder()
                    .withWord(veryLongWordIdForTest)
                    .build();
            word = Word.builder()
                    .withWord(veryLongWordIdForTest)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.insert(word)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

            Assertions.assertDoesNotThrow(
                    () -> vocabularyId = vocabularyDao.insert(vocabulary)
                            .subscribeAsCompletionStage()
                            .get()
                            .orElse(null)
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            vocabularyId, vocabularyDao.delete(vocabularyId.longValue())
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.delete(veryLongWordIdForTest)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void testWordDao() throws ExecutionException, InterruptedException {
            var expected1 = Word.builder()
                    .withWord(veryLongWordIdForTest)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findByWord(veryLongWordIdForTest)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = Word.builder()
                    .withWord(veryLongWordIdForTest)
                    .withEnabled(true)
                    .withVisible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findByWord(veryLongWordIdForTest)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected2, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNotNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
        }

        @Test
        void testVocabularyDao() throws ExecutionException, InterruptedException {
            var expected1 = Vocabulary.builder()
                    .withId(1L)
                    .withWord(veryLongWordIdForTest)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findById(vocabularyId)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            Thread.sleep(20_000);
            var expected2 = Vocabulary.builder()
                    .withId(1L)
                    .withWord(veryLongWordIdForTest)
                    .withValue("value")
                    .withEnabled(true)
                    .withVisible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            vocabularyId, vocabularyDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findById(vocabularyId)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected2, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNotNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Thread.sleep(10_000);
        }
    }
}
