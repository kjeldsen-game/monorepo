use kafka::consumer::{Consumer, FetchOffset, MessageSets};
use std::env;
use std::{thread, time};

use super::signup_event::SignupEvent;

use crate::ses::template_email;

pub async fn init() {
    let kafka_connection = env::var("KAFKA_HOST_AND_PORT").unwrap().to_string();

    consume_signup_event(kafka_connection).await;
    // add new events to consume here
}

async fn consume_signup_event(kafka_connection: String) {
    let topic_notifications_email_template = env::var("TOPIC_NOTIFICATIONS_EMAIL_TEMPLATE")
        .unwrap()
        .to_string();
    let mut consumer = build_consumer(kafka_connection, topic_notifications_email_template);
    consume_event(&mut consumer).await;
}

fn build_consumer(kafka_connection: String, topic_name: String) -> Consumer {
    Consumer::from_hosts(vec![kafka_connection])
        .with_topic(topic_name)
        .with_fallback_offset(FetchOffset::Latest)
        .create()
        .unwrap()
}

async fn consume_event(consumer: &mut Consumer) {
    loop {
        println!(
            "Polling messages for consumer with partitions {:?}",
            consumer.subscriptions()
        );

        let message_sets: MessageSets = consumer.poll().expect("Polling OK but it failed");

        for ms in message_sets.iter() {
            println!("iter 1");

            for m in ms.messages() {
                let new_event = SignupEvent::from(m);
                println!("new wevent received {:?}", new_event);
                let sent_email = template_email::send_email().await;
                println!("sent_email {:?}", sent_email);
            }
            consumer
                .consume_messageset(ms)
                .expect("Consuming messageset");
        }
        consumer.commit_consumed().expect("Commiting consumed");

        thread::sleep(time::Duration::from_millis(10000));
    }
}
