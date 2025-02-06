package com.kjeldsen.match.domain.clients.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kjeldsen.match.domain.clients.models.team.Fans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FansDeserializer extends JsonDeserializer<Fans> {

    @Override
    public Fans deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Fans fans = Fans.builder().build();
        Map<String, Fans.FanTier> fanTiers = new HashMap<>();

        ObjectNode node = p.getCodec().readTree(p);

        node.fieldNames().forEachRemaining(fieldName -> {
            try {
                ObjectNode fanNode = (ObjectNode) node.get(fieldName);
                Integer totalFans = fanNode.get("totalFans").asInt();
                Double loyalty = fanNode.get("loyalty").asDouble();

                Fans.FanTier fanTier = Fans.FanTier.builder()
                    .totalFans(totalFans)
                    .loyalty(loyalty).build();

                fanTiers.put(fieldName, fanTier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fans.setFanTiers(fanTiers);

        return fans;
    }
}
