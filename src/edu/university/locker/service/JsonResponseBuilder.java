package edu.university.locker.service;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public final class JsonResponseBuilder {

    private JsonResponseBuilder() {
    }

    public static String buildLockerResponse(OperationResult result, SmartLocker smartLocker) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"success\":").append(result.success()).append(",");
        appendStringField(builder, "message", result.message());
        builder.append(",");
        appendStringField(builder, "state", smartLocker.getStateDisplayName());
        builder.append(",");
        appendNullableField(builder, "studentName", smartLocker.getStudentName());
        builder.append(",");
        appendNullableField(builder, "packageCode", smartLocker.getPackageCode());
        builder.append(",");
        appendNullableField(builder, "securityCode", smartLocker.getSecurityCode());
        builder.append(",");
        appendStringField(builder, "lastMessage", smartLocker.getLastMessage());
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
