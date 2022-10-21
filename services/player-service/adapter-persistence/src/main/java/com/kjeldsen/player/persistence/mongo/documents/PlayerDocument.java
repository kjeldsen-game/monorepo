package com.kjeldsen.player.persistence.mongo.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Builder
@Data
@Document(collection = "Players")
public class PlayerDocument {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String position;
    private Map<String, Integer> abilities;
}
