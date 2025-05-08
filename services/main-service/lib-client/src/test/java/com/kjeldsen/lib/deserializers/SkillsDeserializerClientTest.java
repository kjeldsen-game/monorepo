package com.kjeldsen.lib.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kjeldsen.lib.model.player.PlayerSkillsClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SkillsDeserializerClientTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        String json = """
            {
              "PlayerSkills": {
                "actual": 70,
                "potential": 85,
                "playerSkillRelevance": "CORE"
              }
            }
            """;

        SimpleModule module = new SimpleModule();
        module.addDeserializer(PlayerSkillsClient.class, new SkillsDeserializerClient());
        objectMapper.registerModule(module);

        PlayerSkillsClient playerSkillsClient = objectMapper.readValue(json, PlayerSkillsClient.class);
        assertNotNull(playerSkillsClient);
        assertEquals(70, playerSkillsClient.getActual());
        assertEquals(85, playerSkillsClient.getPotential());
        assertEquals("CORE", playerSkillsClient.getPlayerSkillRelevance());
    }

}