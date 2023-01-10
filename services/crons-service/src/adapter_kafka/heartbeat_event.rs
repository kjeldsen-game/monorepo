use super::{producer, consumer};

pub fn init() {
    start_heartbeat_event();
}

fn start_heartbeat_event() {
    let broker = "localhost:9092";
    let topic = "my-topic";
    let group = "my-group";

    let data = "hello, kafka";

    if let Err(e) = producer::produce_message(data,
        topic,
        vec![broker.to_owned()]) {
        println!("Failed producing messages: {}", e);
    }

    if let Err(e) = consumer::consume_messages(group.to_string(),
        topic.to_string(),
        vec![broker.to_owned()]) {
        println!("Failed consuming messages: {}", e);
    }
}