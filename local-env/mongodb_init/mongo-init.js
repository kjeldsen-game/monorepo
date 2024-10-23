db = db.getSiblingDB('admin');

const collections = db.getCollectionNames();
collections.forEach(collection => {
    if (!collection.startsWith('system.')) {
        db[collection].drop();
    }
});


db.User.insertMany([
    {
        "_id": ObjectId("66cd9fd57a0b1808f7ba3f6d"),
        "email": "exampleUser1",
        "password": "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=", // password
        "_class": "com.kjeldsen.auth.User"
    },
    {
        "_id": ObjectId("66cd9fd57a0b1808f7ba3f6e"),
        "email": "exampleUser2",
        "password": "$shiro1$SHA-256$500000$6uW2xlP4v1b/42nrF+htiQ==$AXRIkxDiWC9tVcTw+awJmhcBUUKq63Hi2INZZd2UJ4Q=", // password
        "_class": "com.kjeldsen.auth.User"
    }
]);

db.Teams.insertMany([
    {
        "_id" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "userId" : "66cd9fd57a0b1808f7ba3f6d",
        "name" : "exampleTeam1",
        "economy" : {
            "balance" : NumberDecimal("10000000"),
            "prices" : {
                "SEASON_TICKET" : NumberInt(14),
                "DAY_TICKET" : NumberInt(14),
                "RESTAURANT" : NumberInt(10),
                "MERCHANDISE" : NumberInt(25)
            },
            "sponsors" : {
                "WEEKLY" : null,
                "ANNUAL" : null
            }
        },
        "cantera" : {
            "score" : 0.0,
            "economyLevel" : NumberInt(0),
            "traditionLevel" : NumberInt(0),
            "buildingsLevel" : NumberInt(0)
        },
        "fans" : {
            "fanTiers" : {
                "1" : {
                    "totalFans" : NumberInt(10000),
                    "loyalty" : 50.0
                }
            }
        },
        "leagueStats" : {
            "1" : {
                "tablePosition" : NumberInt(12),
                "points" : NumberInt(0)
            }
        },
        "buildings" : {
            "freeSlots" : NumberInt(25),
            "facilities" : {
                "YOUTH_PITCH" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "SCOUTS" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "SPORTS_DOCTORS" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "VIDEO_ROOM" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "TRAINING_CENTER" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                }
            },
            "stadium" : {
                "seats" : NumberInt(5000),
                "level" : NumberInt(1),
                "maintenanceCost" : "5000"
            }
        },
        "_class" : "Team"
    },
    {
        "_id" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "userId" : "66cd9fd57a0b1808f7ba3f6e",
        "name" : "exampleTeam2",
        "economy" : {
            "balance" : NumberDecimal("819898"),
            "prices" : {
                "SEASON_TICKET" : NumberInt(14),
                "DAY_TICKET" : NumberInt(14),
                "RESTAURANT" : NumberInt(10),
                "MERCHANDISE" : NumberInt(25)
            },
            "sponsors" : {
                "WEEKLY" : null,
                "ANNUAL" : null
            }
        },
        "cantera" : {
            "score" : 0.0,
            "economyLevel" : NumberInt(0),
            "traditionLevel" : NumberInt(0),
            "buildingsLevel" : NumberInt(0)
        },
        "fans" : {
            "fanTiers" : {
                "1" : {
                    "totalFans" : NumberInt(10000),
                    "loyalty" : 50.0
                }
            }
        },
        "leagueStats" : {
            "1" : {
                "tablePosition" : NumberInt(12),
                "points" : NumberInt(0)
            }
        },
        "buildings" : {
            "freeSlots" : NumberInt(25),
            "facilities" : {
                "YOUTH_PITCH" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "SCOUTS" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "SPORTS_DOCTORS" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "VIDEO_ROOM" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                },
                "TRAINING_CENTER" : {
                    "level" : NumberInt(1),
                    "maintenanceCost" : "100000"
                }
            },
            "stadium" : {
                "seats" : NumberInt(5000),
                "level" : NumberInt(1),
                "maintenanceCost" : "5000"
            }
        },
        "_class" : "Team"
    }
]);

db.Players.insertMany([
    {
        "_id" : {
            "value" : "09e82194-c7cc-42a5-b1a7-843351e4525e"
        },
        "name" : "Jeffry McGlynn",
        "age" : {
            "years" : NumberInt(15),
            "months" : 28.82,
            "days" : 1.18
        },
        "position" : "RIGHT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(53),
                "potential" : NumberInt(72),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(38),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(17),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10585.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a7f32f12-1f1a-4f86-a557-f637d70a1dba"
        },
        "name" : "Laticia Goodwin",
        "age" : {
            "years" : NumberInt(15),
            "months" : 1.1,
            "days" : 1.64
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(51),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(17),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10580.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "aa09418c-36fb-450b-b131-4bdb83f14dfb"
        },
        "name" : "Lakiesha Jast",
        "age" : {
            "years" : NumberInt(21),
            "months" : 29.22,
            "days" : 0.33
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10190.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "b2530105-7ad1-4eb5-9dda-7e91a2ce81c7"
        },
        "name" : "Marhta Towne",
        "age" : {
            "years" : NumberInt(19),
            "months" : 13.07,
            "days" : 0.52
        },
        "position" : "AERIAL_STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(60),
                "potential" : NumberInt(80),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11195.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "37128d0a-253e-4b8d-b17f-61237be7d261"
        },
        "name" : "Hettie Morissette",
        "age" : {
            "years" : NumberInt(17),
            "months" : 10.01,
            "days" : 0.44
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(8),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(55),
                "potential" : NumberInt(73),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12435.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "123c9df5-b05a-47a2-8e68-cf2e68907238"
        },
        "name" : "Clare Hegmann",
        "age" : {
            "years" : NumberInt(32),
            "months" : 27.69,
            "days" : 0.88
        },
        "position" : "LEFT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "31295330-1cf3-48ce-add7-7895f05f9029"
            },
            "playerId" : {
                "value" : "123c9df5-b05a-47a2-8e68-cf2e68907238"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "AERIAL",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(2),
            "pointsAfterTraining" : NumberInt(2),
            "currentDay" : NumberInt(3),
            "occurredAt" : ISODate("2024-10-07T07:44:05.670+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10465.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "8e3ba59c-cb61-4a02-a9af-d24c3117874f"
        },
        "name" : "Clint Kunze",
        "age" : {
            "years" : NumberInt(18),
            "months" : 22.94,
            "days" : 3.13
        },
        "position" : "LEFT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(51),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "8985.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "58ecf76e-eca1-4e3d-9487-a137eb6a6c7a"
        },
        "name" : "Monte Koch",
        "age" : {
            "years" : NumberInt(22),
            "months" : 13.68,
            "days" : 3.22
        },
        "position" : "SWEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(49),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(49),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10680.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "0fc094b1-86ff-4b69-9128-f94e6f766878"
        },
        "name" : "Sherilyn Casper",
        "age" : {
            "years" : NumberInt(17),
            "months" : 18.01,
            "days" : 1.16
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(38),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(53),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11240.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "8e640d70-3f8f-4a5a-8eb6-a7236752f4e8"
        },
        "name" : "Onie Raynor",
        "age" : {
            "years" : NumberInt(21),
            "months" : 14.99,
            "days" : 0.68
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10100.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "73ed5aac-cf61-49fa-a920-302aa9116618"
        },
        "name" : "Christinia Ryan",
        "age" : {
            "years" : NumberInt(17),
            "months" : 22.66,
            "days" : 2.56
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(47),
                "potential" : NumberInt(67),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(21),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9930.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "720941e1-a33f-4e74-8e40-190a06e25087"
        },
        "name" : "Asley Rogahn",
        "age" : {
            "years" : NumberInt(19),
            "months" : 25.44,
            "days" : 3.38
        },
        "position" : "OFFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(8),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(77),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11080.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "4ce21c80-0af4-4c6c-a2b9-8dbbea22adbe"
        },
        "name" : "Chester Stiedemann",
        "age" : {
            "years" : NumberInt(22),
            "months" : 5.95,
            "days" : 3.33
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10755.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "b908d565-ce49-4f2c-b75b-a9b2a49d685a"
        },
        "name" : "Johnie Paucek DVM",
        "age" : {
            "years" : NumberInt(16),
            "months" : 7.39,
            "days" : 0.36
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(60),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(60),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(47),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11375.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "7ee0460a-7e96-4b63-beee-751fac4fadda"
        },
        "name" : "Carroll Hermiston",
        "age" : {
            "years" : NumberInt(21),
            "months" : 14.12,
            "days" : 3.82
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10945.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "2ef1771c-5b7a-462f-a083-c3f6f969aebf"
        },
        "name" : "Grazyna VonRueden IV",
        "age" : {
            "years" : NumberInt(17),
            "months" : 23.97,
            "days" : 0.23
        },
        "position" : "LEFT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(51),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(30),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "8345.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "7f8b4a92-c120-4d41-acc8-96b866c3eeaa"
        },
        "name" : "Erwin Kertzmann II",
        "age" : {
            "years" : NumberInt(31),
            "months" : 7.98,
            "days" : 2.62
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(1),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(1),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "7f96d622-e943-413d-b828-ba62480b7965"
            },
            "playerId" : {
                "value" : "7f8b4a92-c120-4d41-acc8-96b866c3eeaa"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "PASSING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(1),
            "pointsAfterTraining" : NumberInt(1),
            "currentDay" : NumberInt(7),
            "occurredAt" : ISODate("2024-10-07T07:44:05.701+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10255.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "baf6cd50-7652-4fd0-b982-440801dd9cc7"
        },
        "name" : "Anton Mertz",
        "age" : {
            "years" : NumberInt(17),
            "months" : 19.63,
            "days" : 2.87
        },
        "position" : "RIGHT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(43),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "7880.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "8805cee4-4778-4a55-98bc-d0072ff94fcc"
        },
        "name" : "Otto Franecki",
        "age" : {
            "years" : NumberInt(28),
            "months" : 11.35,
            "days" : 3.06
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "b4424973-f151-42ad-af06-1e464fa43d4e"
            },
            "playerId" : {
                "value" : "8805cee4-4778-4a55-98bc-d0072ff94fcc"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "OFFENSIVE_POSITIONING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(16),
            "pointsAfterTraining" : NumberInt(16),
            "currentDay" : NumberInt(5),
            "occurredAt" : ISODate("2024-10-07T07:44:05.572+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8955.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "b3701d89-1dd7-40ee-9090-f9a5ed32243e"
        },
        "name" : "Gretta Reilly",
        "age" : {
            "years" : NumberInt(15),
            "months" : 2.67,
            "days" : 2.48
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "8900.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "12d92430-87fb-43d8-bc6e-415c9cecfcb4"
        },
        "name" : "Victorina Bruen",
        "age" : {
            "years" : NumberInt(25),
            "months" : 13.69,
            "days" : 3.54
        },
        "position" : "SWEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10120.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "48d0ac41-6b10-498d-9c61-53fcea9b504e"
        },
        "name" : "Merlyn Torphy",
        "age" : {
            "years" : NumberInt(16),
            "months" : 13.76,
            "days" : 0.95
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(34),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9610.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "19c58405-f0eb-4d2d-921e-fe855de5f1ba"
        },
        "name" : "Sharice Bode",
        "age" : {
            "years" : NumberInt(23),
            "months" : 3.66,
            "days" : 0.15
        },
        "position" : "SWEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(63),
                "potential" : NumberInt(81),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "15765.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "00b9a998-3b2a-4009-8fc8-6fa75c6ba117"
        },
        "name" : "Mayola Grady V",
        "age" : {
            "years" : NumberInt(16),
            "months" : 16.99,
            "days" : 2.44
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(47),
                "potential" : NumberInt(61),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(34),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9785.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f96e343d-3f88-4850-8c1e-6f425f0f8893"
        },
        "name" : "Evangelina Beer",
        "age" : {
            "years" : NumberInt(26),
            "months" : 22.99,
            "days" : 1.75
        },
        "position" : "OFFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(61),
                "potential" : NumberInt(79),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "14725.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "beae53c4-8fe1-437f-9b37-888f67569fe2"
        },
        "name" : "Moises Davis",
        "age" : {
            "years" : NumberInt(19),
            "months" : 25.66,
            "days" : 3.65
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(12),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9380.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "0547329d-18ac-4c75-bccd-8b5bce225329"
        },
        "name" : "Donny Heathcote",
        "age" : {
            "years" : NumberInt(15),
            "months" : 20.93,
            "days" : 0.85
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(8),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(17),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(38),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(63),
                "potential" : NumberInt(85),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "14005.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "e38d7c4e-4d14-4ba3-bc2c-e8b44b6ec1c4"
        },
        "name" : "Leano Batz",
        "age" : {
            "years" : NumberInt(23),
            "months" : 6.9,
            "days" : 16.04
        },
        "position" : "AERIAL_CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(55),
                "potential" : NumberInt(71),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12150.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "523d14b8-be66-4b19-9aea-2c03b0520e86"
        },
        "name" : "Miss Florentino Hamill",
        "age" : {
            "years" : NumberInt(18),
            "months" : 15.82,
            "days" : 3.77
        },
        "position" : "GOALKEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "ONE_ON_ONE" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "SECONDARY"
            },
            "ORGANIZATION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "REFLEXES" : {
                "actual" : NumberInt(58),
                "potential" : NumberInt(76),
                "playerSkillRelevance" : "CORE"
            },
            "GOALKEEPER_POSITIONING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "CORE"
            },
            "INTERCEPTIONS" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONTROL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11665.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "c03e11a2-684f-4018-87b1-54208b75e3b4"
        },
        "name" : "Evelyn Rippin III",
        "age" : {
            "years" : NumberInt(32),
            "months" : 27.99,
            "days" : 0.37
        },
        "position" : "CENTRE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(66),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "efee1c16-bf78-445d-8abe-cedf90bc2900"
            },
            "playerId" : {
                "value" : "c03e11a2-684f-4018-87b1-54208b75e3b4"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "SCORING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(3),
            "pointsAfterTraining" : NumberInt(3),
            "currentDay" : NumberInt(5),
            "occurredAt" : ISODate("2024-10-07T07:44:05.616+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10435.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "4c4a3cb1-8973-46a7-ab86-8f69cccd6946"
        },
        "name" : "Miss Pauletta Orn",
        "age" : {
            "years" : NumberInt(18),
            "months" : 4.67,
            "days" : 3.27
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(56),
                "potential" : NumberInt(75),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(38),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10780.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "da821502-9e90-4a5c-9a3a-ed8223436c4a"
        },
        "name" : "Kenyatta Dicki",
        "age" : {
            "years" : NumberInt(28),
            "months" : 11.78,
            "days" : 3.37
        },
        "position" : "GOALKEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "ONE_ON_ONE" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            },
            "ORGANIZATION" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "REFLEXES" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(70),
                "playerSkillRelevance" : "CORE"
            },
            "GOALKEEPER_POSITIONING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "INTERCEPTIONS" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONTROL" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "25599a6b-831c-457f-b93f-55748c8e2b4e"
            },
            "playerId" : {
                "value" : "da821502-9e90-4a5c-9a3a-ed8223436c4a"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "ONE_ON_ONE",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(22),
            "pointsAfterTraining" : NumberInt(22),
            "currentDay" : NumberInt(7),
            "occurredAt" : ISODate("2024-10-07T07:44:05.654+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12740.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "3e3ec7fa-c014-430f-aeb3-9c3996e81f7a"
        },
        "name" : "Michaela Bechtelar",
        "age" : {
            "years" : NumberInt(18),
            "months" : 27.24,
            "days" : 0.96
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(64),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(21),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(47),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9345.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f1625784-6fc0-40ed-8941-775becc44ce2"
        },
        "name" : "Paris Bogan",
        "age" : {
            "years" : NumberInt(25),
            "months" : 4.03,
            "days" : 0.89
        },
        "position" : "CENTRE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10430.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "33ffd6d2-032e-489e-9ec7-f83220df8580"
        },
        "name" : "Brande McClure",
        "age" : {
            "years" : NumberInt(19),
            "months" : 0.4,
            "days" : 1.97
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(56),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(12),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(64),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10715.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a252332e-ed9d-49ad-9260-daf62f0f1bc3"
        },
        "name" : "Darline Windler",
        "age" : {
            "years" : NumberInt(32),
            "months" : 5.59,
            "days" : 2.44
        },
        "position" : "AERIAL_CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(1),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "3f105531-32b0-498d-ad3d-410954c6b606"
            },
            "playerId" : {
                "value" : "a252332e-ed9d-49ad-9260-daf62f0f1bc3"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "SCORING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(0),
            "pointsAfterTraining" : NumberInt(0),
            "currentDay" : NumberInt(3),
            "occurredAt" : ISODate("2024-10-07T07:44:05.686+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12775.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "40f518dc-af92-4543-9c4c-0a9e768666cd"
        },
        "name" : "Rod Terry DDS",
        "age" : {
            "years" : NumberInt(17),
            "months" : 3.71,
            "days" : 1.03
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(54),
                "potential" : NumberInt(72),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12140.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "8e1bfe83-af62-4b1c-a998-52df0cadf6b5"
        },
        "name" : "Birgit Batz",
        "age" : {
            "years" : NumberInt(32),
            "months" : 16.5,
            "days" : 0.11
        },
        "position" : "SWEEPER",
        "status" : "FOR_SALE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(51),
                "potential" : NumberInt(76),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(72),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "d0ef3301-9284-4da1-b70e-d1b4b8d4e071"
            },
            "playerId" : {
                "value" : "8e1bfe83-af62-4b1c-a998-52df0cadf6b5"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "OFFENSIVE_POSITIONING",
            "pointsToSubtract" : NumberInt(2),
            "pointsBeforeTraining" : NumberInt(0),
            "pointsAfterTraining" : NumberInt(0),
            "currentDay" : NumberInt(8),
            "occurredAt" : ISODate("2024-10-07T07:44:05.635+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "14155.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a3b25d07-0539-4d2c-afd7-a77cf83d04c7"
        },
        "name" : "Vicenta Streich IV",
        "age" : {
            "years" : NumberInt(18),
            "months" : 12.86,
            "days" : 0.91
        },
        "position" : "LEFT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(8),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(56),
                "potential" : NumberInt(74),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(61),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(8),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(12),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12740.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "915cc6b7-7f1a-4757-b10e-8db6c72c0408"
        },
        "name" : "Bryce Sipes",
        "age" : {
            "years" : NumberInt(29),
            "months" : 27.41,
            "days" : 0.09
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "4ca75cb4-ecf0-4c9e-afef-5531d07737f6"
            },
            "playerId" : {
                "value" : "915cc6b7-7f1a-4757-b10e-8db6c72c0408"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "AERIAL",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(7),
            "pointsAfterTraining" : NumberInt(7),
            "currentDay" : NumberInt(4),
            "occurredAt" : ISODate("2024-10-07T07:44:05.716+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9495.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f11ce414-f23d-47f3-a4be-ced05fc657ff"
        },
        "name" : "Joey Torphy",
        "age" : {
            "years" : NumberInt(18),
            "months" : 14.68,
            "days" : 3.26
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(67),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(43),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(12),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9130.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f440d2fd-ad99-4837-8319-799cc3ee0765"
        },
        "name" : "Shaunna Rau",
        "age" : {
            "years" : NumberInt(28),
            "months" : 3.25,
            "days" : 0.23
        },
        "position" : "SWEEPER",
        "status" : "FOR_SALE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(66),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(1),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "37ea0cfd-9a54-4411-8eb6-e3e94a01926b"
            },
            "playerId" : {
                "value" : "f440d2fd-ad99-4837-8319-799cc3ee0765"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "OFFENSIVE_POSITIONING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(8),
            "pointsAfterTraining" : NumberInt(8),
            "currentDay" : NumberInt(2),
            "occurredAt" : ISODate("2024-10-07T07:44:05.516+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "13885.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a23f5a20-cdd4-40ea-8e60-b639122feb85"
        },
        "name" : "Tari Wisozk",
        "age" : {
            "years" : NumberInt(15),
            "months" : 26.5,
            "days" : 1.84
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(52),
                "potential" : NumberInt(70),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11000.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "996d8b75-a05e-4828-b637-607532b50464"
        },
        "name" : "Deshawn Cronin",
        "age" : {
            "years" : NumberInt(30),
            "months" : 24.67,
            "days" : 0.55
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(51),
                "potential" : NumberInt(66),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "bdb72a81-68a4-4343-bcce-ca5410367ed2"
            },
            "playerId" : {
                "value" : "996d8b75-a05e-4828-b637-607532b50464"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "DEFENSIVE_POSITIONING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(0),
            "pointsAfterTraining" : NumberInt(0),
            "currentDay" : NumberInt(8),
            "occurredAt" : ISODate("2024-10-07T07:44:05.732+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12445.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "50a42b20-4552-4648-93bc-a8d4ec505b54"
        },
        "name" : "Winston Robel",
        "age" : {
            "years" : NumberInt(27),
            "months" : 22.92,
            "days" : 1.04
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(62),
                "potential" : NumberInt(80),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "15950.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "ad357228-0c65-4665-83d2-1d6a1f00b16a"
        },
        "name" : "Meridith Schowalter",
        "age" : {
            "years" : NumberInt(19),
            "months" : 212.52999999999986,
            "days" : 28.069999999999993
        },
        "position" : "LEFT_BACK",
        "status" : "FOR_SALE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(100),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(54),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "65c9ac64-8d6b-437b-ba6a-bbc99f529708"
            },
            "playerId" : {
                "value" : "ad357228-0c65-4665-83d2-1d6a1f00b16a"
            },
            "declineSpeed" : NumberInt(350),
            "skill" : "DEFENSIVE_POSITIONING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(42),
            "pointsAfterTraining" : NumberInt(42),
            "currentDay" : NumberInt(12),
            "occurredAt" : ISODate("2024-10-03T16:44:14.085+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10685.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "6ddac3f4-8de7-4f02-918d-402237ca0f48"
        },
        "name" : "Miss Kalyn Ruecker",
        "age" : {
            "years" : NumberInt(30),
            "months" : 15.31,
            "days" : 2.73
        },
        "position" : "SWEEPER",
        "status" : "FOR_SALE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(67),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(52),
                "potential" : NumberInt(75),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "e4a713e6-c19a-407f-8fa6-ab61916f5c68"
            },
            "playerId" : {
                "value" : "6ddac3f4-8de7-4f02-918d-402237ca0f48"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "TACKLING",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(52),
            "pointsAfterTraining" : NumberInt(52),
            "currentDay" : NumberInt(1),
            "occurredAt" : ISODate("2024-10-07T07:44:05.549+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "14290.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "7bcf37b6-1b89-4ba6-9d92-14bce6ea513d"
        },
        "name" : "Cornelius Miller",
        "age" : {
            "years" : NumberInt(32),
            "months" : 6.42,
            "days" : 1.11
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(1),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "PASSING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(0),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "decline" : {
            "_id" : {
                "value" : "cfda5316-8f38-4e0b-8458-1a46cbbd5882"
            },
            "playerId" : {
                "value" : "7bcf37b6-1b89-4ba6-9d92-14bce6ea513d"
            },
            "declineSpeed" : NumberInt(1),
            "skill" : "CONSTITUTION",
            "pointsToSubtract" : NumberInt(0),
            "pointsBeforeTraining" : NumberInt(22),
            "pointsAfterTraining" : NumberInt(22),
            "currentDay" : NumberInt(10),
            "occurredAt" : ISODate("2024-10-07T07:44:05.594+0000")
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8415.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "62607989-bb24-439e-8425-b074bd216486"
        },
        "name" : "Kenyetta Abernathy",
        "age" : {
            "years" : NumberInt(23),
            "months" : 24.54,
            "days" : 1.73
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(54),
                "potential" : NumberInt(70),
                "playerSkillRelevance" : "CORE"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "PASSING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(58),
                "potential" : NumberInt(75),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "13650.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "779cd98c-03ae-48ed-9c96-fb424274a345"
        },
        "name" : "Layne O'Conner",
        "age" : {
            "years" : NumberInt(17),
            "months" : 8.53,
            "days" : 0.65
        },
        "position" : "AERIAL_STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(12),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            },
            "PASSING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(61),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(55),
                "potential" : NumberInt(74),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "51a96286-eb0e-48bb-a82d-93defef51068"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12475.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "8926d800-d291-4edd-bae6-f0b6381d226a"
        },
        "name" : "Inger Halvorson",
        "age" : {
            "years" : NumberInt(32),
            "months" : 24.96,
            "days" : 2.0
        },
        "position" : "CENTRE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8270.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "316df176-a0fb-43fe-9f6e-bf434ff1cdeb"
        },
        "name" : "Steve Schuppe",
        "age" : {
            "years" : NumberInt(24),
            "months" : 2.78,
            "days" : 0.53
        },
        "position" : "OFFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(56),
                "potential" : NumberInt(72),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(51),
                "potential" : NumberInt(66),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "14175.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "78e2fced-1a42-412a-8233-01b426082359"
        },
        "name" : "Georgette Hand",
        "age" : {
            "years" : NumberInt(32),
            "months" : 3.67,
            "days" : 3.8
        },
        "position" : "LEFT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8215.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "16e7bfd1-0326-47e9-9e5e-7e4424b4d5ef"
        },
        "name" : "Alan Smitham",
        "age" : {
            "years" : NumberInt(31),
            "months" : 6.8,
            "days" : 2.96
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9435.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "32104323-4a10-4c54-9205-5e2993f8efa1"
        },
        "name" : "Kasi Mraz",
        "age" : {
            "years" : NumberInt(27),
            "months" : 19.77,
            "days" : 1.5
        },
        "position" : "RIGHT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8035.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "ea2751d2-029e-4aa6-9dc7-730a2b8a260e"
        },
        "name" : "Issac Volkman",
        "age" : {
            "years" : NumberInt(22),
            "months" : 0.21,
            "days" : 3.67
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "11515.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "847704a9-bcf8-452a-a9cf-451f609d0b28"
        },
        "name" : "Efren Stiedemann",
        "age" : {
            "years" : NumberInt(23),
            "months" : 11.46,
            "days" : 3.7
        },
        "position" : "LEFT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9570.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "d1b0114c-5077-4cef-96c5-e6d4d14dd59e"
        },
        "name" : "Lily Bergnaum",
        "age" : {
            "years" : NumberInt(30),
            "months" : 22.91,
            "days" : 2.36
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(53),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12180.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "4095f152-8850-4b38-8543-1c7d8ed85398"
        },
        "name" : "Aleida Kutch",
        "age" : {
            "years" : NumberInt(21),
            "months" : 20.74,
            "days" : 3.1
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8335.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "0cfa45a3-3d87-47f9-ba1d-f534cfd4b076"
        },
        "name" : "Dong Ebert",
        "age" : {
            "years" : NumberInt(27),
            "months" : 18.22,
            "days" : 3.61
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(47),
                "potential" : NumberInt(61),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "11420.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "c296a7fd-dd5a-4c43-a53f-1d7fd23b38f3"
        },
        "name" : "Eli Macejkovic",
        "age" : {
            "years" : NumberInt(31),
            "months" : 22.77,
            "days" : 3.9
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(55),
                "potential" : NumberInt(71),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12635.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "2c24fba3-9cdc-4efc-a869-90bd0e0d5dbb"
        },
        "name" : "Jeannette McDermott",
        "age" : {
            "years" : NumberInt(25),
            "months" : 11.59,
            "days" : 2.95
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10635.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "3b12cae0-1db8-4afb-936c-5f3a15b4f87d"
        },
        "name" : "Anya Borer",
        "age" : {
            "years" : NumberInt(30),
            "months" : 7.68,
            "days" : 3.66
        },
        "position" : "LEFT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9155.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "b1c4b109-c6c5-443b-817d-f97059af8c76"
        },
        "name" : "Nadene Bartell",
        "age" : {
            "years" : NumberInt(22),
            "months" : 9.58,
            "days" : 0.25
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9870.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "e4f5ac56-dfe2-4826-bd50-e8f518c8d315"
        },
        "name" : "Bradly Hegmann",
        "age" : {
            "years" : NumberInt(27),
            "months" : 25.4,
            "days" : 2.28
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "9690.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "fb987d2b-1709-49dd-9097-140fb294d8ef"
        },
        "name" : "Yun Walsh",
        "age" : {
            "years" : NumberInt(24),
            "months" : 11.76,
            "days" : 1.89
        },
        "position" : "STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(54),
                "potential" : NumberInt(70),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "11875.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "724e3e8a-9f59-411f-bfda-bb0aeb12cd40"
        },
        "name" : "Young Hermiston",
        "age" : {
            "years" : NumberInt(24),
            "months" : 29.16,
            "days" : 2.28
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(53),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(47),
                "potential" : NumberInt(61),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "12395.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "2416e24b-6f06-41ec-b420-120344f8fd2f"
        },
        "name" : "Phil Bednar",
        "age" : {
            "years" : NumberInt(27),
            "months" : 28.58,
            "days" : 0.67
        },
        "position" : "LEFT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(49),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10130.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "9c37ed23-100a-4546-8449-4286067a95c9"
        },
        "name" : "Warren Beer",
        "age" : {
            "years" : NumberInt(28),
            "months" : 8.18,
            "days" : 3.94
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(67),
                "potential" : NumberInt(87),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "16365.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "518e61a2-92aa-4203-93de-8f1405c94a50"
        },
        "name" : "Lanita Green",
        "age" : {
            "years" : NumberInt(31),
            "months" : 27.32,
            "days" : 1.67
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(60),
                "potential" : NumberInt(78),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "11800.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f3776bef-0d8b-4e83-b4ab-e56995d9f9bf"
        },
        "name" : "Epifania White",
        "age" : {
            "years" : NumberInt(24),
            "months" : 10.41,
            "days" : 0.46
        },
        "position" : "CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(53),
                "potential" : NumberInt(68),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(49),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "11925.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "0e0073ee-3d3c-4363-a61e-cd75652208e5"
        },
        "name" : "Portia Turner",
        "age" : {
            "years" : NumberInt(26),
            "months" : 2.86,
            "days" : 0.86
        },
        "position" : "SWEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(65),
                "potential" : NumberInt(84),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "16105.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "d55140f1-5c75-499b-a1c0-74fc8d109f26"
        },
        "name" : "Arturo Zemlak",
        "age" : {
            "years" : NumberInt(22),
            "months" : 5.66,
            "days" : 2.19
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10365.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "af26d61f-8aee-44f9-9a16-47e5e25b5d72"
        },
        "name" : "Gregory Koepp",
        "age" : {
            "years" : NumberInt(29),
            "months" : 4.16,
            "days" : 1.6
        },
        "position" : "LEFT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "10760.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "c3315158-91de-46df-865f-3675283eb376"
        },
        "name" : "Anton Bins",
        "age" : {
            "years" : NumberInt(24),
            "months" : 17.52,
            "days" : 1.67
        },
        "position" : "LEFT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "SENIOR",
        "economy" : {
            "salary" : "8920.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "909593da-e469-4ee3-8a37-44da92f23546"
        },
        "name" : "Maisie Brown",
        "age" : {
            "years" : NumberInt(17),
            "months" : 25.98,
            "days" : 2.02
        },
        "position" : "LEFT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "8370.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "dc95ab7f-bf1d-4d9c-adc4-bd9cdc1d77f9"
        },
        "name" : "Deidre Lubowitz",
        "age" : {
            "years" : NumberInt(16),
            "months" : 29.35,
            "days" : 1.73
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9485.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "386f7027-ae73-4a33-aef3-a285ab160328"
        },
        "name" : "Mike Williamson",
        "age" : {
            "years" : NumberInt(19),
            "months" : 27.74,
            "days" : 1.36
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9860.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "32096c7e-b01a-4fdd-ba7e-9ba6f9d09d33"
        },
        "name" : "Jeremiah Toy",
        "age" : {
            "years" : NumberInt(16),
            "months" : 16.24,
            "days" : 4.0
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(59),
                "potential" : NumberInt(76),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(38),
                "potential" : NumberInt(49),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11450.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "e600df74-f811-4f3e-8396-e193c8804e67"
        },
        "name" : "Cliff Howell",
        "age" : {
            "years" : NumberInt(16),
            "months" : 10.87,
            "days" : 0.58
        },
        "position" : "AERIAL_CENTRE_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(60),
                "potential" : NumberInt(78),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12925.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "c2e39ea7-0eb7-4862-937c-b21c6d471b54"
        },
        "name" : "Mary Yundt",
        "age" : {
            "years" : NumberInt(19),
            "months" : 26.82,
            "days" : 0.57
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "8920.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f714544d-724c-47b2-9a92-88a9b2d5178d"
        },
        "name" : "Rodrigo Kihn",
        "age" : {
            "years" : NumberInt(18),
            "months" : 13.56,
            "days" : 1.25
        },
        "position" : "OFFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(57),
                "potential" : NumberInt(74),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12740.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "93c58c6a-d08b-46dc-a17a-7e1cd105f9d5"
        },
        "name" : "Eloy Bahringer",
        "age" : {
            "years" : NumberInt(19),
            "months" : 25.13,
            "days" : 2.91
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(37),
                "potential" : NumberInt(48),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9135.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "cf45c83b-346c-46e3-b484-d9c310823bf9"
        },
        "name" : "Yu Grady",
        "age" : {
            "years" : NumberInt(16),
            "months" : 21.32,
            "days" : 2.8
        },
        "position" : "RIGHT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9660.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "f797d63b-1431-4cf3-b4ee-ea05ecff40d2"
        },
        "name" : "Adam Mayert",
        "age" : {
            "years" : NumberInt(15),
            "months" : 29.61,
            "days" : 2.06
        },
        "position" : "FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(10),
                "potential" : NumberInt(13),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(2),
                "potential" : NumberInt(2),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(50),
                "potential" : NumberInt(65),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11750.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "58e19f50-4f5e-4d0e-8a58-d8f2bd27d047"
        },
        "name" : "Krystal Nitzsche",
        "age" : {
            "years" : NumberInt(17),
            "months" : 3.62,
            "days" : 1.45
        },
        "position" : "RIGHT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(49),
                "potential" : NumberInt(63),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(52),
                "potential" : NumberInt(67),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12100.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "01c9ef99-6f4b-4113-85c6-6bbad5c01e6d"
        },
        "name" : "Demetrius Haag",
        "age" : {
            "years" : NumberInt(16),
            "months" : 22.37,
            "days" : 0.18
        },
        "position" : "CENTRE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(12),
                "potential" : NumberInt(15),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9300.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "32e00687-eb23-4612-80af-52070c9b2d27"
        },
        "name" : "Alpha Johnston",
        "age" : {
            "years" : NumberInt(19),
            "months" : 6.73,
            "days" : 3.46
        },
        "position" : "AERIAL_STRIKER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(44),
                "potential" : NumberInt(57),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(60),
                "potential" : NumberInt(78),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "12765.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a7330c12-7d21-428f-b02c-a38c4c603799"
        },
        "name" : "Marti Kemmer",
        "age" : {
            "years" : NumberInt(19),
            "months" : 23.13,
            "days" : 0.82
        },
        "position" : "LEFT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(28),
                "potential" : NumberInt(36),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(7),
                "potential" : NumberInt(9),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9160.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "a993f807-e34c-4ea1-8d20-53cd665a79e0"
        },
        "name" : "Kip Fisher",
        "age" : {
            "years" : NumberInt(15),
            "months" : 4.38,
            "days" : 3.59
        },
        "position" : "LEFT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(26),
                "potential" : NumberInt(33),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(52),
                "potential" : NumberInt(67),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(17),
                "potential" : NumberInt(22),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11770.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "44833b63-07ec-496a-9a42-6d09b6cc3260"
        },
        "name" : "Shara Hansen",
        "age" : {
            "years" : NumberInt(19),
            "months" : 18.44,
            "days" : 2.16
        },
        "position" : "RIGHT_WINGBACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(9),
                "potential" : NumberInt(11),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "7730.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "abd478d5-ae84-4d79-8ce4-bc181efc2f8f"
        },
        "name" : "Jerold Treutel",
        "age" : {
            "years" : NumberInt(17),
            "months" : 18.32,
            "days" : 2.36
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(18),
                "potential" : NumberInt(23),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(55),
                "potential" : NumberInt(71),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "11220.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "3eb2fdb7-4355-44ce-a654-73cf5321906f"
        },
        "name" : "Joesph Considine",
        "age" : {
            "years" : NumberInt(16),
            "months" : 17.71,
            "days" : 1.22
        },
        "position" : "AERIAL_FORWARD",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(30),
                "potential" : NumberInt(39),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(45),
                "potential" : NumberInt(58),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10675.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "17b86b0b-d2f0-491b-b131-1da04199ceda"
        },
        "name" : "Rodney Bechtelar",
        "age" : {
            "years" : NumberInt(17),
            "months" : 19.25,
            "days" : 1.29
        },
        "position" : "GOALKEEPER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "GOALKEEPER_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "CORE"
            },
            "INTERCEPTIONS" : {
                "actual" : NumberInt(22),
                "potential" : NumberInt(28),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONTROL" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "ORGANIZATION" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "REFLEXES" : {
                "actual" : NumberInt(71),
                "potential" : NumberInt(92),
                "playerSkillRelevance" : "CORE"
            },
            "ONE_ON_ONE" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "20520.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "ea1ab3ec-37ed-40e3-b157-10b032fae258"
        },
        "name" : "Tim Nienow",
        "age" : {
            "years" : NumberInt(17),
            "months" : 1.96,
            "days" : 1.51
        },
        "position" : "LEFT_WINGER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(3),
                "potential" : NumberInt(3),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "AERIAL" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(4),
                "potential" : NumberInt(5),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(40),
                "potential" : NumberInt(52),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10480.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "20a421f8-30e2-433b-9dc4-21da4a53fb62"
        },
        "name" : "Rueben Fay",
        "age" : {
            "years" : NumberInt(17),
            "months" : 0.47,
            "days" : 2.11
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(32),
                "potential" : NumberInt(41),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(41),
                "potential" : NumberInt(53),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(35),
                "potential" : NumberInt(45),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(8),
                "potential" : NumberInt(10),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(27),
                "potential" : NumberInt(35),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9795.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "c9c23552-f694-4eb7-ba47-a9daab825fad"
        },
        "name" : "Flo Stamm",
        "age" : {
            "years" : NumberInt(17),
            "months" : 2.8,
            "days" : 2.99
        },
        "position" : "DEFENSIVE_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(23),
                "potential" : NumberInt(29),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "CORE"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(14),
                "potential" : NumberInt(18),
                "playerSkillRelevance" : "CORE"
            },
            "TACKLING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "CORE"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(33),
                "potential" : NumberInt(42),
                "playerSkillRelevance" : "CORE"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9580.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "2aff902a-a561-45f4-b359-d99dabf68e21"
        },
        "name" : "Ilana Rice",
        "age" : {
            "years" : NumberInt(19),
            "months" : 27.04,
            "days" : 0.97
        },
        "position" : "LEFT_BACK",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(36),
                "potential" : NumberInt(46),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(42),
                "potential" : NumberInt(54),
                "playerSkillRelevance" : "CORE"
            },
            "AERIAL" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "TACKLING" : {
                "actual" : NumberInt(48),
                "potential" : NumberInt(62),
                "playerSkillRelevance" : "CORE"
            },
            "SCORING" : {
                "actual" : NumberInt(6),
                "potential" : NumberInt(7),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(15),
                "potential" : NumberInt(19),
                "playerSkillRelevance" : "RESIDUAL"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "10400.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "565be1c7-9c99-40eb-bb5b-863a94ee8d46"
        },
        "name" : "Jaymie Oberbrunner",
        "age" : {
            "years" : NumberInt(16),
            "months" : 14.29,
            "days" : 3.48
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(43),
                "potential" : NumberInt(55),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(20),
                "potential" : NumberInt(26),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(39),
                "potential" : NumberInt(50),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(21),
                "potential" : NumberInt(27),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(11),
                "potential" : NumberInt(14),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(19),
                "potential" : NumberInt(24),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(13),
                "potential" : NumberInt(16),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(34),
                "potential" : NumberInt(44),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9185.00"
        },
        "_class" : "Player"
    },
    {
        "_id" : {
            "value" : "33329c46-730a-4ae2-9695-8fe451f051ad"
        },
        "name" : "Randy Pacocha",
        "age" : {
            "years" : NumberInt(17),
            "months" : 23.17,
            "days" : 2.77
        },
        "position" : "RIGHT_MIDFIELDER",
        "status" : "INACTIVE",
        "playerOrder" : "NONE",
        "actualSkills" : {
            "PASSING" : {
                "actual" : NumberInt(46),
                "potential" : NumberInt(59),
                "playerSkillRelevance" : "CORE"
            },
            "CONSTITUTION" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "OFFENSIVE_POSITIONING" : {
                "actual" : NumberInt(31),
                "potential" : NumberInt(40),
                "playerSkillRelevance" : "SECONDARY"
            },
            "DEFENSIVE_POSITIONING" : {
                "actual" : NumberInt(25),
                "potential" : NumberInt(32),
                "playerSkillRelevance" : "SECONDARY"
            },
            "AERIAL" : {
                "actual" : NumberInt(16),
                "potential" : NumberInt(20),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "TACKLING" : {
                "actual" : NumberInt(24),
                "potential" : NumberInt(31),
                "playerSkillRelevance" : "SECONDARY"
            },
            "SCORING" : {
                "actual" : NumberInt(5),
                "potential" : NumberInt(6),
                "playerSkillRelevance" : "RESIDUAL"
            },
            "BALL_CONTROL" : {
                "actual" : NumberInt(29),
                "potential" : NumberInt(37),
                "playerSkillRelevance" : "SECONDARY"
            }
        },
        "teamId" : {
            "value" : "709aad9c-8517-44ee-91d6-51226be6e210"
        },
        "category" : "JUNIOR",
        "economy" : {
            "salary" : "9095.00"
        },
        "_class" : "Player"
    }
])

