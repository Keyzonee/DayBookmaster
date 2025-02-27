/*
 * This file was last modified at 2022.01.12 17:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordServiceTest.java
 * $Id$
 */

package su.svn.daybook.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class StringUtilTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        Assertions.assertDoesNotThrow(StringUtil::generateStringId);
        var test = StringUtil.generateStringId();
        System.out.println("test = " + test);
    }
}