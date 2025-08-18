package com.icbt.cis6003.pahanaedubookshop.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

/**
 * Utility class for JSON response operations
 */
public class JsonUtil {

    /**
     * Create success response JSON
     */
    public static JsonObject createSuccessResponse(String message, JsonObject data) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("success", true)
                .add("message", message);

        if (data != null) {
            builder.add("data", data);
        }

        return builder.build();
    }

    /**
     * Create success response JSON with array data
     */
    public static JsonObject createSuccessResponse(String message, JsonArrayBuilder data) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("success", true)
                .add("message", message);

        if (data != null) {
            builder.add("data", data);
        }

        return builder.build();
    }

    /**
     * Create error response JSON
     */
    public static JsonObject createErrorResponse(String message) {
        return Json.createObjectBuilder()
                .add("success", false)
                .add("error", message)
                .build();
    }
}
