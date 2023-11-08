package com.kjeldsen.match.utils;

import static com.monitorjbl.json.Match.match;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import lombok.SneakyThrows;

public final class JsonUtils {

    static private final ObjectMapper mapper =
        new ObjectMapper()
            .setSerializationInclusion(Include.NON_NULL)
            .registerModule(new JsonViewModule());

    @SneakyThrows
    public static <T> String prettyPrint(T object) {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> String prettyPrintExclude(T object, String... ignoreProperties) {
        JsonView<T> view =
            JsonView.with(object).onClass(object.getClass(), match().exclude(ignoreProperties));
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(view);
    }
}
