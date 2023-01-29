use dotenv::dotenv;
use kafka::consumer::{Consumer, FetchOffset, MessageSets};
use std::env;
use std::{thread, time};

mod signup_event;

fn main() {
    println!("Started Notifications Service");

    dotenv().ok(); // Load environment variables
    println!("Loaded environment variables");

    let kafka_connection = env::var("KAFKA_HOST_AND_PORT").unwrap().to_string();

    start_consuming_events(kafka_connection);
}

fn start_consuming_events(kafka_connection: String) {
    consume_signup_event(kafka_connection);
    // add new events to consume here
}

fn consume_signup_event(kafka_connection: String) {
    let topic_signup_event = env::var("TOPIC_SIGNUP_EVENT").unwrap().to_string();
    let mut consumer = build_consumer(kafka_connection, topic_signup_event);
    consume_event(&mut consumer);
}

fn build_consumer(kafka_connection: String, topic_name: String) -> Consumer {
    Consumer::from_hosts(vec![kafka_connection])
        .with_topic(topic_name)
        .with_fallback_offset(FetchOffset::Latest)
        .create()
        .unwrap()
}

fn consume_event(consumer: &mut Consumer) {
    loop {
        println!(
            "Polling messages for consumer with partitions {:?}",
            consumer.subscriptions()
        );

        let message_sets: MessageSets = consumer.poll().expect("Polling OK but it failed");

        for ms in message_sets.iter() {
            println!("iter 1");

            for m in ms.messages() {
                signup_event::SignupEvent::process_kafka_message(&m);
            }
            consumer
                .consume_messageset(ms)
                .expect("Consuming messageset");
        }
        consumer.commit_consumed().expect("Commiting consumed");

        thread::sleep(time::Duration::from_millis(10000));
    }
}
