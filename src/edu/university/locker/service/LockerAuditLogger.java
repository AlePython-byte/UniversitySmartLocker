package edu.university.locker.service;

import edu.university.locker.model.StateTransitionRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LockerAuditLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> entries = new ArrayList<>();
    private final List<StateTransitionRecord> transitions = new ArrayList<>();

    public synchronized void log(String message) {
        entries.add(timestamp() + " - " + message);
    }

    public synchronized void logTransition(String fromState, String toState, String action, String message) {
        StateTransitionRecord record = new StateTransitionRecord(fromState, toState, action, message, timestamp());
        transitions.add(record);
        log("Transicion registrada: " + fromState + " -> " + toState + " por " + action + ". " + message);
    }

    public synchronized List<String> getEntries() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    public synchronized List<StateTransitionRecord> getTransitions() {
        return Collections.unmodifiableList(new ArrayList<>(transitions));
    }

    public synchronized StateTransitionRecord getLastTransition() {
        if (transitions.isEmpty()) {
            return null;
        }
        return transitions.get(transitions.size() - 1);
    }

    private String timestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
