package com.kjeldsen.lib.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kjeldsen.lib.model.team.FansClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FansDeserializerClient extends JsonDeserializer<FansClient> {

    @Override
    public FansClient deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        FansClient fans = FansClient.builder().build();
        Map<String, FansClient.FanTierClient> fanTiers = new HashMap<>();

        ObjectNode node = p.getCodec().readTree(p);

        node.fieldNames().forEachRemaining(fieldName -> {
            try {
                ObjectNode fanNode = (ObjectNode) node.get(fieldName);
                Integer totalFans = fanNode.get("totalFans").asInt();
                Double loyalty = fanNode.get("loyalty").asDouble();

                FansClient.FanTierClient fanTier = FansClient.FanTierClient.builder()
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
