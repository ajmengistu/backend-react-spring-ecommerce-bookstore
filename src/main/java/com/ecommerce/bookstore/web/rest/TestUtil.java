package com.ecommerce.bookstore.web.rest;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for testing REST controllers.
 * 
 * Source: Jhipster.
 */

public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    /**
     * MediaType for JSON.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        return mapper;
    }

    /**
     * Convert an object to JSON byte array.
     * 
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }
}