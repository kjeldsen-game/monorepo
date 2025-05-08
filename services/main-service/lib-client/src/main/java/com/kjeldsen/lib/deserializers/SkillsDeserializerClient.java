package com.kjeldsen.lib.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.kjeldsen.lib.model.player.PlayerSkillsClient;

import java.io.IOException;


public class SkillsDeserializerClient extends JsonDeserializer<PlayerSkillsClient> {

    @Override
    public PlayerSkillsClient deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode node = rootNode.get("PlayerSkills");

        return new PlayerSkillsClient(node.get("actual").asInt(), node.get("potential").asInt(),
            node.get("playerSkillRelevance").asText());
    }
}
