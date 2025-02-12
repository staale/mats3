package io.mats3.test;

import java.util.Arrays;
import java.util.Objects;

final class AttributeKey {
    private final Class<?> _type;
    private final String[] _name;

    public AttributeKey(Class<?> type, String... name) {
        _type = type;
        _name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttributeKey)) {
            return false;
        }
        AttributeKey that = (AttributeKey) o;
        return Objects.equals(_type, that._type) && Objects.deepEquals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_type, Arrays.hashCode(_name));
    }

    @Override
    public String toString() {
        return "AttributeKey(" + _type.getSimpleName() + ":" + Arrays.toString(_name) + ")";
    }
}
