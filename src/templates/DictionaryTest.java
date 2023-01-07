package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class @Name@Test {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new @Name@());
        Assertions.assertDoesNotThrow(() -> new @Name@(
                null, @KType@.ZERO, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new @Name@();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::get@Key@);
        Assertions.assertDoesNotThrow(entry::get@Value@);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(@Name@.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new @Name@();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(@Name@.builder()
                .id(null)
                .@key@(@KType@.ZERO)
                .@value@(null)
                .visible(true)
                .flags(0)
                .build()));
    }
}