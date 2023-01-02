use std::fmt::Error;
use dotenv::dotenv;
use std::env;
use std::fmt;

use kafka::client::KafkaClient;
use kafka::client::metadata::TopicNames;

use rskafka::client::ClientBuilder;
use rskafka::client::controller::ControllerClient;

use serde::{Serialize, Deserialize};

mod adapter_file_system;

use adapter_file_system::main::read_file_to_string;

#[derive(Serialize, Deserialize, Debug)]
struct TopicFile {
    topics: Vec<Topic>
}

#[derive(Serialize, Deserialize, Debug)]
struct Topic {
    name: String
}

impl fmt::Display for Topic {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}


#[tokio::main]
async fn main() -> Result<(), Error> {

    dotenv().ok(); // Load environment variables

    let kafka_connection = env::var("KAFKA_HOST_AND_PORT").unwrap().to_string();

    let rs_kafka_client = ClientBuilder::new(vec![kafka_connection.clone()]).build().await.unwrap();

    let controller_client: ControllerClient = rs_kafka_client.controller_client().unwrap();

    let mut kafka_client = KafkaClient::new(vec!(kafka_connection.to_owned()));


    let json_file = read_file_to_string("topics.json".to_string());

    let topic_file: TopicFile = serde_json::from_str(&json_file).unwrap();

    for topic in topic_file.topics {
        kafka_client.load_metadata_all().unwrap();
        let current_topics = kafka_client.topics();
        let mut current_topic_names: TopicNames = current_topics.names();
        update_topic(&controller_client, &mut current_topic_names, &topic.to_string()).await;
    }

    Ok(())
}

async fn update_topic<'a>(controller_client:  &'a ControllerClient, _current_topic_names:  &'a mut TopicNames<'_>, topic: &'a String) {
  
    if _current_topic_names.find(|topic_name| topic_name.eq(topic)) == None {
        println!("Topic {:?} to be created...", topic);
        create_topic(&controller_client, &topic).await;
    } else {
        println!("Topic {:?} already exists", topic);
    }
}

async fn create_topic<'a>(controller_client: &'a ControllerClient, topic: &'a String) {
    controller_client.create_topic(
        topic,
        2,      // partitions
        1,      // replication factor
        5_000,  // timeout (ms)
    ).await.unwrap();
}

