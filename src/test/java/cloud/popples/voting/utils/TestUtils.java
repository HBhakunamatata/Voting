package cloud.popples.voting.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String mapToString(Object object) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }

}
