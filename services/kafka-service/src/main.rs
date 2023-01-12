use std::env;
use std::fmt;
use std::fmt::Error;
use std::fs;

use dotenv::dotenv;

use serde::{Deserialize, Serialize};

use kafka::client::metadata::TopicNames;
use kafka::client::KafkaClient;
use rskafka::client::controller::ControllerClient;
use rskafka::client::ClientBuilder;

#[tokio::main]
async fn main() -> Result<(), Error> {
    dotenv().ok(); // Load environment variables

    let kafka_connection = env::var("KAFKA_HOST_AND_PORT").unwrap().to_string();
    let topics_file_path = env::var("TOPICS_FILE_PATH").unwrap().to_string();

    let rs_kafka_client = ClientBuilder::new(vec![kafka_connection.clone()])
        .build()
        .await
        .unwrap();

    let controller_client: ControllerClient = rs_kafka_client.controller_client().unwrap();

    let mut kafka_client = KafkaClient::new(vec![kafka_connection.to_owned()]);

    let json_file = fs::read_to_string(topics_file_path).unwrap();
    let topic_file: TopicFile = serde_json::from_str(&json_file).unwrap();

    for topic in topic_file.topics {
        kafka_client.load_metadata_all().unwrap();
        let current_topics = kafka_client.topics();
        let mut current_topic_names: TopicNames = current_topics.names();
        update_topic(&controller_client, &mut current_topic_names, &topic).await;
    }

    Ok(())
}

async fn update_topic<'a>(
    controller_client: &'a ControllerClient,
    _current_topic_names: &'a mut TopicNames<'_>,
    topic: &'a Topic,
) {
    if _current_topic_names.find(|topic_name| topic_name.eq(&topic.name)) == None {
        println!("Topic {} to be created...", topic);
        create_topic(&controller_client, &topic).await;
        println!("Topic {} created...", topic);
    } else {
        println!("Topic {} already exists, ignored.", topic);
    }
}

async fn create_topic<'a>(controller_client: &'a ControllerClient, topic: &'a Topic) {
    controller_client
        .create_topic(
            &topic.name,
            topic.num_partitions,
            topic.replication_factor,
            topic.timeout_ms,
        )
        .await
        .unwrap();
}

#[derive(Serialize, Deserialize, Debug)]
struct TopicFile {
    topics: Vec<Topic>,
}

#[derive(Serialize, Deserialize, Debug)]
struct Topic {
    name: String,
    num_partitions: i32,
    replication_factor: i16,
    timeout_ms: i32,
}

impl fmt::Display for Topic {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}
