package edu.university.locker.model;

public record StateTransitionRecord(
        String fromState,
        String toState,
        String action,
        String message,
        String timestamp
) {
}
