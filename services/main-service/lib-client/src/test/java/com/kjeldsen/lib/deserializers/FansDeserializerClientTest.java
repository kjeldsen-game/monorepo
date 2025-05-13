package com.kjeldsen.lib.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kjeldsen.lib.model.team.FansClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FansDeserializerClientTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Should deserialize json to FansClient object")
    void should_deserialize_json_to_FansClient() throws IOException {
        String json = """
        {
          "1": { "totalFans": 500, "loyalty": 75.5 },
          "2": { "totalFans": 300, "loyalty": 60.0 }
        }""";

        SimpleModule module = new SimpleModule();
        module.addDeserializer(FansClient.class, new FansDeserializerClient());
        mapper.registerModule(module);

        FansClient result = mapper.readValue(json, FansClient.class);

        assertThat(result).isNotNull();
        assertThat(result.getFanTiers().size()).isEqualTo(2);
        assertThat(result.getFanTiers().containsKey("1")).isEqualTo(true);
        assertThat(result.getFanTiers().containsKey("2")).isEqualTo(true);
        assertThat(result.getFanTiers().get("1").getTotalFans()).isEqualTo(500);
        assertThat(result.getFanTiers().get("1").getLoyalty()).isEqualTo(75.5);
    }
}