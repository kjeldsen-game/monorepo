use kafka::consumer::{Consumer, FetchOffset, GroupOffsetStorage};

fn main() {

    let mut consumer =
       Consumer::from_hosts(vec!("localhost:9092".to_owned()))
        .with_topic_partitions("my-topic".to_owned(), &[0, 1])
        .with_fallback_offset(FetchOffset::Earliest)
        .with_group("my-group".to_owned())
        .with_offset_storage(GroupOffsetStorage::Kafka)
        .create()
        .unwrap();

    loop {
        for ms in consumer.poll().unwrap().iter() {
            for m in ms.messages() {
                println!("Ok message {:?}", m);
            }
            consumer.consume_messageset(ms);
        }
        consumer.commit_consumed().unwrap();
    }  

}

