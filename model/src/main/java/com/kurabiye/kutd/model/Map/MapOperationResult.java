package com.kurabiye.kutd.model.Map;

public class MapOperationResult {
    private final boolean success;
    private final String message;

    public MapOperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}