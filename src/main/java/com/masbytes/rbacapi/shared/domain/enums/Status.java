package com.masbytes.rbacapi.shared.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.EnumSet;

/**
 * Enumeration representing the lifecycle states of an entity. Provides JSON
 * serialization/deserialization support and enforces allowed transitions
 * between states.
 */
public enum Status {

    /**
     * Possible states:
     * 
     * - PENDING: Initial state, awaiting activation. 
     * - ACTIVE: Currently enabled and in use. 
     * - INACTIVE: Disabled but may be reactivated. 
     * - SUSPENDED: Temporarily disabled, can be reactivated or archived. 
     * - ARCHIVED: Permanently closed, no further transitions allowed.
     */
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED"),
    ARCHIVED("ARCHIVED");

    /**
     * The string label representing the status value. Used for JSON
     * serialization and human-readable output.
     */
    private final String label;

    Status(String label) {
        this.label = label;
    }

    /**
     * Returns the string label of the status.
     *
     * @return the label of the status
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the label as the string representation of the status. Annotated
     * with @JsonValue for JSON serialization.
     */
    @JsonValue
    @Override
    public String toString() {
        return label;
    }

    /**
     * Creates a Status enum from a string value. Case-insensitive match against
     * the label.
     *
     * @param value the string representation of the status
     * @return the corresponding Status enum
     * @throws IllegalArgumentException if the value does not match any status
     */
    @JsonCreator
    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.label.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }

    /**
     * Determines if the current status can transition to the target status.
     *
     * @param target the target status
     * @return true if the transition is allowed, false otherwise
     */
    public boolean canTransitionTo(Status target) {
        return getAllowedTransitions().contains(target);
    }

    /**
     * Defines the allowed transitions for each status.
     *
     * @return an EnumSet of valid target statuses
     */
    private EnumSet<Status> getAllowedTransitions() {
        return switch (this) {
            case PENDING ->
                EnumSet.of(ACTIVE, ARCHIVED);
            case ACTIVE ->
                EnumSet.of(INACTIVE, SUSPENDED);
            case INACTIVE ->
                EnumSet.of(ACTIVE, ARCHIVED);
            case SUSPENDED ->
                EnumSet.of(ACTIVE, ARCHIVED);
            case ARCHIVED ->
                EnumSet.noneOf(Status.class);
        };
    }

}
