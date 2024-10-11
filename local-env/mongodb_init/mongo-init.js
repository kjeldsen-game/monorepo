// Connect to the admin database
db = db.getSiblingDB('admin');

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