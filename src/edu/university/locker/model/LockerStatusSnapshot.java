package edu.university.locker.model;

import java.util.List;

public record LockerStatusSnapshot(
        String state,
        String studentName,
        String packageCode,
        String securityCode,
        String lastMessage,
        List<String> allowedActions,
        StateTransitionRecord lastTransition
) {
}
