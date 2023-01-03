package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class I18NTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new I18nTable());
        Assertions.assertDoesNotThrow(() -> new I18nTable(
                0L, 0L, null, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new I18nTable();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getLanguageId);
        Assertions.assertDoesNotThrow(entry::getMessage);
        Assertions.assertDoesNotThrow(entry::getTranslation);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(I18nTable.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new I18nTable();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(I18nTable.builder()
                .id(null)
                .languageId(0L)
                .message(null)
                .translation(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}