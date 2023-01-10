use tokio::time::{self, Duration};

#[tokio::main]
pub async fn start_crons() {

    let mut interval = time::interval(Duration::from_secs(5));

    loop { 
        interval.tick().await;
        tokio::spawn(async { on_cron().await; });
    }
}

async fn on_cron() {
    crate::adapter_kafka::heartbeat_event::init();
}
