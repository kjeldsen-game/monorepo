package com.kjeldsen.match.domain.clients.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.kjeldsen.match.domain.clients.models.player.PlayerSkills;

import java.io.IOException;


public class SkillsDeserializer extends JsonDeserializer<PlayerSkills> {

    @Override
    public PlayerSkills deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode node = rootNode.get("PlayerSkills");

        return new PlayerSkills(node.get("actual").asInt(), node.get("potential").asInt(),
            node.get("playerSkillRelevance").toString());
    }
}
