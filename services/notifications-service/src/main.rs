use dotenv::dotenv;
use std::fmt::Error;

mod kafka;
mod ses;

#[tokio::main]
async fn main() -> Result<(), Error> {
    println!("Started Notifications Service");

    dotenv().ok(); // Load environment variables
    println!("Loaded environment variables");

    ses::template_processor::upload_templates().await;
    kafka::consumers::init().await;

    Ok(())
}
