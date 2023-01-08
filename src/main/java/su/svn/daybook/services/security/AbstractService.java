/*
 * This file was last modified at 2023.01.08 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractService.java
 * $Id$
 */

package su.svn.daybook.services.security;

import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.models.security.AuthResponse;

abstract class AbstractService {

    protected Answer apiResponseAcceptedAnswer(AuthResponse response) {
        return Answer.of(ApiResponse.auth(response));
    }

    protected Answer apiResponseUnauthorizedAnswer() {
        return Answer.of(ApiResponse.auth(401));
    }
}
