package com.github.lecho.conference.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Leszek on 2015-09-30.
 */
public final class Optional<T> {


    private static final Optional<?> EMPTY = new Optional<>();
    private final T value;

    private Optional() {
        this.value = null;
    }

    public static <T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(T value) {
        if (value == null) {
            return empty();
        } else {
            return new Optional<>(value);
        }
    }

    /**
     * If a value is present in this {@code Optional}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws IllegalStateException if there is no value present
     * @see Optional#isPresent()
     */
    public @NonNull T get() {
        if (value == null) {
            throw new IllegalStateException("No value present");
        }
        return value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may
     *              be null
     * @return the value, if present, otherwise {@code other}
     */
    public @Nullable T orElse(T other) {
        return value != null ? value : other;
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }
}