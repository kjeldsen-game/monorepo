use kafka::error::Error as KafkaError;
use kafka::producer::{Producer, Record, RequiredAcks};
use tokio::time::{Duration};

pub fn produce_message<'a, 'b>(
    data: &'a str,
    topic: &'b str,
    brokers: Vec<String>,
) -> Result<(), KafkaError> {
    println!("About to publish a message at {:?} to: {}", brokers, topic);

    let mut producer = Producer::from_hosts(brokers)
        .with_ack_timeout(Duration::from_secs(1))
        .with_required_acks(RequiredAcks::One)
        .create()?;

    producer.send(&Record {
        topic,
        partition: -1,
        key: (),
        value: data,
    })?;

    producer.send(&Record::from_value(topic, data))?;

    println!("Message sent");

    Ok(())
}