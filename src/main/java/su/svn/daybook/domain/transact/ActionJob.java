/*
 * This file was last modified at 2023.04.23 15:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ActionJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import su.svn.daybook.annotations.TransactionAction;
import su.svn.daybook.annotations.TransactionActions;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ActionJob {

    protected Map<String, Map<String, Action>> getActionsOfMethods() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(this::testAnnotationTransactionActions)
                .map(this::createActionsEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Nonnull
    private Map.Entry<String, Map<String, Action>> createActionsEntry(Method method) {
        return new AbstractMap.SimpleEntry<>(method.getName(), createActionEntries(method));
    }

    @Nonnull
    private Map<String, Action> createActionEntries(Method method) {
        TransactionActions transactionActions = method.getAnnotation(TransactionActions.class);
        var result = Stream
                .of(transactionActions.value())
                .map(this::createActionEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Collections.unmodifiableMap(result);
    }

    @Nonnull
    private Map.Entry<String, Action> createActionEntry(TransactionAction transactionAction) {
        return new AbstractMap.SimpleEntry<>(transactionAction.name(), Action.of(transactionAction));
    }

    private boolean testAnnotationTransactionActions(Method method) {
        return method.isAnnotationPresent(TransactionActions.class)
                && Modifier.isPublic(method.getModifiers())
                && !method.isBridge();
    }
}
