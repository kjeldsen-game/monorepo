package com.kjeldsen.match.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "Match")
public class Match {

    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    String id;
    Team home;
    Team away;
    LocalDateTime dateTime;
    Status status;
    MatchReport matchReport;
}
