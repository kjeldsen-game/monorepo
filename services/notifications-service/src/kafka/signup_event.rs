use kafka::consumer::Message;
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct SignupEvent {
    key: String,
    value: String,
}

impl SignupEvent {
    pub fn from(m: &Message) -> SignupEvent {
        let kafka_message_as_string: String = match std::str::from_utf8(&m.value) {
            Ok(str) => str.to_string(),
            Err(error) => panic!("Problem converting kafka message to string: {}", error),
        };

        let event: SignupEvent = match serde_json::from_str(&kafka_message_as_string) {
            Ok(message) => message,
            Err(error) => panic!(
                "Problem converting kafka message as string to struct: {}",
                error
            ),
        };

        event
    }
}
