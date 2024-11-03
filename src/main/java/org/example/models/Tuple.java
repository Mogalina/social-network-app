package org.example.models;

import java.util.Objects;

/**
 * Represents a generic tuple with two elements of the same type.
 *
 * @param <E> the type of elements in the tuple
 */
public class Tuple<E> {

    // The first element of the tuple
    private E first;

    // The second element of the tuple
    private E second;

    /**
     * Constructs a Tuple with the specified elements.
     *
     * @param first the first element of the tuple
     * @param second the second element of the tuple
     */
    public Tuple(E first, E second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of the tuple.
     *
     * @return the first element
     */
    public E getFirst() {
        return first;
    }

    /**
     * Sets the first element of the tuple.
     *
     * @param first the new first element
     */
    public void setFirst(E first) {
        this.first = first;
    }

    /**
     * Returns the second element of the tuple.
     *
     * @return the second element
     */
    public E getSecond() {
        return second;
    }

    /**
     * Sets the second element of the tuple.
     *
     * @param second the new second element
     */
    public void setSecond(E second) {
        this.second = second;
    }

    /**
     * Returns a string representation of the tuple.
     *
     * @return a string describing the tuple
     */
    @Override
    public String toString() {
        return "@TUPLE | " +
                "FIRST <" + first + ">" +
                "\n     SECOND <" + second + ">";
    }

    /**
     * Compares this tuple to another object for equality.
     * Tuples are considered equal if their elements are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is a Tuple with equal elements, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?> tuple = (Tuple<?>) o;
        return Objects.equals(first, tuple.getFirst()) && Objects.equals(second, tuple.getSecond());
    }

    /**
     * Returns the hash code for this tuple.
     *
     * @return a hash code value based on the tuple elements
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
