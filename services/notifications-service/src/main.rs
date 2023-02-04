use dotenv::dotenv;
use std::fmt::Error;

mod aws_ses;
mod aws_ses_client;
mod kafka;
mod ses_template;
mod signup_event;

#[tokio::main]
async fn main() -> Result<(), Error> {
    println!("Started Notifications Service");

    dotenv().ok(); // Load environment variables
    println!("Loaded environment variables");

    aws_ses::upload_templates().await;
    kafka::start_consuming_events().await;

    Ok(())
}
