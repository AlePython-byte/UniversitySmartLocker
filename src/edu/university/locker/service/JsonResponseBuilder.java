package edu.university.locker.service;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerStatusSnapshot;
import edu.university.locker.model.OperationResult;
import edu.university.locker.model.StateTransitionRecord;

public final class JsonResponseBuilder {

    private JsonResponseBuilder() {
    }

    public static String buildLockerResponse(OperationResult result, SmartLocker smartLocker) {
        LockerStatusSnapshot snapshot = smartLocker.getStatusSnapshot();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"success\":").append(result.success()).append(",");
        appendStringField(builder, "message", result.message());
        builder.append(",");
        appendStringField(builder, "state", snapshot.state());
        builder.append(",");
        appendNullableField(builder, "studentName", snapshot.studentName());
        builder.append(",");
        appendNullableField(builder, "packageCode", snapshot.packageCode());
        builder.append(",");
        appendNullableField(builder, "securityCode", snapshot.securityCode());
        builder.append(",");
        appendStringField(builder, "lastMessage", snapshot.lastMessage());
        builder.append(",");
        appendStringArrayField(builder, "allowedActions", snapshot.allowedActions());
        builder.append(",");
        appendTransitionField(builder, "lastTransition", snapshot.lastTransition());
        builder.append("}");
        return builder.toString();
    }

    public static String buildBasicResponse(boolean success, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"success\":").append(success).append(",");
        appendStringField(builder, "message", message);
        builder.append("}");
        return builder.toString();
    }

    private static void appendStringField(StringBuilder builder, String fieldName, String value) {
        builder.append("\"").append(fieldName).append("\":\"").append(escape(value)).append("\"");
    }

    private static void appendNullableField(StringBuilder builder, String fieldName, String value) {
        builder.append("\"").append(fieldName).append("\":");
        if (value == null) {
            builder.append("null");
            return;
        }
        builder.append("\"").append(escape(value)).append("\"");
    }

    private static void appendStringArrayField(StringBuilder builder, String fieldName, Iterable<String> values) {
        builder.append("\"").append(fieldName).append("\":[");
        boolean first = true;
        for (String value : values) {
            if (!first) {
                builder.append(",");
            }
            builder.append("\"").append(escape(value)).append("\"");
            first = false;
        }
        builder.append("]");
    }

    private static void appendTransitionField(StringBuilder builder, String fieldName, StateTransitionRecord transition) {
        builder.append("\"").append(fieldName).append("\":");
        if (transition == null) {
            builder.append("null");
            return;
        }

        builder.append("{");
        appendStringField(builder, "fromState", transition.fromState());
        builder.append(",");
        appendStringField(builder, "toState", transition.toState());
        builder.append(",");
        appendStringField(builder, "action", transition.action());
        builder.append(",");
        appendStringField(builder, "message", transition.message());
        builder.append(",");
        appendStringField(builder, "timestamp", transition.timestamp());
        builder.append("}");
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
