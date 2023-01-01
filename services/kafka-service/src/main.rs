use std::fmt::Error;

use kafka::client::KafkaClient;
use kafka::client::metadata::TopicNames;

use rskafka::client::ClientBuilder;
use rskafka::client::controller::ControllerClient;

#[tokio::main]
async fn main() -> Result<(), Error> {

    let kafka_connection = "localhost:9092".to_string();

    let rs_kafka_client = ClientBuilder::new(vec![kafka_connection.clone()]).build().await.unwrap();

    let controller_client: ControllerClient = rs_kafka_client.controller_client().unwrap();

    let mut kafka_client = KafkaClient::new(vec!(kafka_connection.to_owned()));
    kafka_client.load_metadata_all().unwrap();

    let ss = kafka_client.topics();

    let current_topic_names: TopicNames = ss.names();

    for _n in 1..5 {
        update_topic(&controller_client, &current_topic_names, &"topic".to_string()).await;
    }

    Ok(())
}

async fn update_topic<'a>(
    controller_client:  &'a ControllerClient,
    _current_topic_names:  &'a TopicNames<'_>,
    topic: &'a String) {
    create_topic(&controller_client, &topic).await;
}

async fn create_topic<'a>(controller_client: &'a ControllerClient, topic: &'a String) {
    controller_client.create_topic(
        topic,
        2,      // partitions
        1,      // replication factor
        5_000,  // timeout (ms)
    ).await.unwrap();
}

