package su.svn.daybook.converters.buildparts;

import su.svn.daybook.converters.records.MethodRecord;
import su.svn.daybook.models.Identification;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractBuildParts<P extends Identification<? extends Comparable<? extends Serializable>>>
        implements BuildParts {

    private final Class<P> pClass;

    private final Supplier<?> builderFactory;

    private final Map<String, MethodRecord> map;

    protected abstract Stream<Field> streamMethods(Class<P> pClass);

    protected abstract MethodRecord extractMethodRecord(Field field, Method method);

    @Override
    public Class<P> getPClass() {
        return pClass;
    }

    @Override
    public Map<String, MethodRecord> getBuildParts() {
        return map;
    }

    @Override
    public Supplier<?> getBuilderFactory() {
        return builderFactory;
    }

    @Override
    public void forEach(@Nonnull Consumer<Map.Entry<String, MethodRecord>> action) {
        map.entrySet().forEach(action);
    }

    AbstractBuildParts(Class<P> pClass, Supplier<?> builderFactory) {
        this.pClass = pClass;
        this.builderFactory = builderFactory;
        this.map = Collections.unmodifiableMap(buildParts(pClass));
    }

    private Map<String, MethodRecord> buildParts(Class<P> pClass) {
        return streamMethods(pClass)
                .map(this::optionalEntryStringMethod)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Optional<Map.Entry<String, MethodRecord>> optionalEntryStringMethod(Field field) {
        return searchBuildPart(field)
                .map(method -> new AbstractMap.SimpleEntry<>(field.getName(), extractMethodRecord(field, method)));
    }

    private Optional<Method> searchBuildPart(Field field) {
        var builder = builderFactory.get();
        return Arrays
                .stream(builder.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals(field.getName()))
                .findFirst();
    }
}
