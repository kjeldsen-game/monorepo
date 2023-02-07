use aws_sdk_ses::error::SendTemplatedEmailError;
use aws_sdk_ses::model::Destination;
use aws_sdk_ses::output::SendTemplatedEmailOutput;
use aws_sdk_ses::types::SdkError;

use super::client;

pub async fn send_email() -> Result<SendTemplatedEmailOutput, SdkError<SendTemplatedEmailError>> {
    let client = client::new().await;

    client
        .send_templated_email()
        .destination(
            Destination::builder()
                .to_addresses(String::from("receiver@test.com"))
                .build(),
        )
        .configuration_set_name(String::from("test_configuration_set_name"))
        .source(String::from("source@test.com"))
        .template(String::from("testing_template"))
        .send()
        .await
}
