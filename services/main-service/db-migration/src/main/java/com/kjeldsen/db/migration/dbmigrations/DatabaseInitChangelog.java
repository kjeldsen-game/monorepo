package com.kjeldsen.db.migration.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@ChangeLog(order = "001")
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitChangelog {

    private CreateTeamUseCase createTeamUseCase;

    @ChangeSet(order = "002", id = "init_teams2", author = "Peter Likavec")
    public void initTeams(MongoDatabase mongoDatabase) {
        try {
            mongoDatabase.getCollection("User").insertMany(Arrays.asList(
                new Document("_id", new ObjectId())
                    .append("email", "exampleUser1")
                    .append("password", "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=")
                    .append("_class", "com.kjeldsen.auth.User"),
                new Document("_id", new ObjectId())
                    .append("email", "exampleUser2")
                    .append("password", "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=")
                    .append("_class", "com.kjeldsen.auth.User")
            ));
            log.info("Users inserted successfully.");
        } catch (Exception e) {
            log.error("Error inserting users: ", e);
        }
    }

//    @ChangeSet(order = "001", id = "init_teams", author = "Peter Likavec")
//    public void initTeams(MongoDatabase mongoDatabase) {
//        mongoDatabase.getCollection("User").insertMany(Arrays.asList(
//            new Document("_id", new ObjectId("66cd9fd57a0b1808f7ba3f6d"))
//                .append("email", "exampleUser1")
//                .append("password", "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=")
//                .append("_class", "com.kjeldsen.auth.User"),
//            new Document("_id", new ObjectId("66cd9fd57a0b1808f7ba3f6e"))
//                .append("email", "exampleUser2")
//                .append("password", "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=")
//                .append("_class", "com.kjeldsen.auth.User")
//        ));
//        //log.info("We are droping transactions collection");
//        //MongoCollection<Document> playersCollection = mongoDatabase.getCollection("Transactions");
//
//        //playersCollection.drop();
//
//    }
}
