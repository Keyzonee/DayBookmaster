/*
 * This file was last modified at 2023.01.11 18:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Logged.java
 * $Id$
 */

package su.svn.daybook.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Deprecated
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Principled {
}
