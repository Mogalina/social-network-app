package org.example.models;

import java.util.Objects;

/**
 * A generic base class for entities, identified by a unique identifier of type ID.
 *
 * This class provides common functionality such as equality checks, hash code generation and string representation
 * based on the {@code id} field.
 *
 * @param <ID> the type of the entity's identifier
 */
public class Entity<ID>  {

    protected ID id; // The type of the ID is specified by the generic type

    /**
     * Returns the identifier of the entity.
     *
     * @return the ID of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the identifier for the entity.
     *
     * @param id the new identifier for the entity
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Compares this entity with another object for equality.
     * Two entities are considered equal if their identifiers are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this entity is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    /**
     * Returns the hash code of this entity, based on its identifier.
     *
     * @return the hash code value of the entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of the entity.
     * The string contains the class name and the identifier of the entity.
     *
     * @return a string representation of the entity
     */
    @Override
    public String toString() {
        return "Entity { " +
                "id=" + id +
                " }";
    }
}
