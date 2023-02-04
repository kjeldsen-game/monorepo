use aws_sdk_ses::Client;

use std::env;

pub async fn new() -> Client {
    let mut config_loader = aws_config::from_env();

    if use_localstack() {
        println!(
            "Using localstack {}",
            env::var("AWS_ENDPOINT_URL").unwrap().to_string()
        );
        config_loader =
            config_loader.endpoint_url(env::var("AWS_ENDPOINT_URL").unwrap().to_string());
    }
    let shared_config = config_loader.load().await;

    Client::from_conf(aws_sdk_ses::config::Builder::from(&shared_config).build())
}

fn use_localstack() -> bool {
    env::var("AWS_LOCALSTACK").unwrap().to_string() == "true"
}
