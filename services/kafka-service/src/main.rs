use std::env;
use std::fmt;
use std::fmt::Error;
use std::fs;

use dotenv::dotenv;

use rdkafka::config::FromClientConfig;
use serde::{Deserialize, Serialize};

use rdkafka::admin::AdminClient;
use rdkafka::admin::AdminOptions;
use rdkafka::admin::NewTopic;
use rdkafka::admin::TopicReplication;
use rdkafka::client::DefaultClientContext;
use rdkafka::config::ClientConfig;

#[tokio::main]
async fn main() -> Result<(), Error> {
    println!("Started Kafka Service");

    dotenv().ok(); // Load environment variables
    println!("Loaded environment variables");

    let kafka_connection = env::var("KAFKA_HOST_AND_PORT").unwrap().to_string();
    let topics_file_path = env::var("TOPICS_FILE_PATH").unwrap().to_string();

    let mut client_config: ClientConfig = ClientConfig::new();
    client_config.set("bootstrap.servers", kafka_connection);

    let admin_client: AdminClient<DefaultClientContext> =
        match AdminClient::from_config(&client_config) {
            Ok(client) => {
                println!("Kafka client loaded correctly");
                client
            }
            Err(error) => panic!("Problem loading Kafka client: {}", error),
        };

    let json_file = fs::read_to_string(topics_file_path).unwrap();
    let topic_file: TopicFile = serde_json::from_str(&json_file).unwrap();

    println!("Topics file readed");

    for topic in topic_file.topics {
        let test_topic = NewTopic {
            name: &topic.name,
            num_partitions: topic.num_partitions,
            replication: TopicReplication::Fixed(topic.replication_factor),
            config: vec![],
        };

        match admin_client
            .create_topics([test_topic].iter(), &AdminOptions::new())
            .await
        {
            Ok(result) => {
                println!("Topic {} has been processed: {:?} ", &topic.name, &result);
                result
            }
            Err(error) => panic!("Error creating topic {}: {}", &topic.name, error),
        };
    }

    Ok(())
}

#[derive(Serialize, Deserialize, Debug)]
struct TopicFile {
    topics: Vec<Topic>,
}

#[derive(Serialize, Deserialize, Debug)]
struct Topic {
    name: String,
    num_partitions: i32,
    replication_factor: i32,
}

impl fmt::Display for Topic {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}
