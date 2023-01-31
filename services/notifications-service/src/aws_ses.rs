use aws_sdk_sesv2::error::UpdateEmailTemplateError;
use aws_sdk_sesv2::model::EmailTemplateContent;
use aws_sdk_sesv2::output::UpdateEmailTemplateOutput;
use aws_sdk_sesv2::types::SdkError;
use aws_sdk_sesv2::Client;

pub async fn send_email_from_template() {
    let shared_config = aws_config::load_from_env().await;
    let client = Client::new(&shared_config);

    let update_email_template_output: UpdateEmailTemplateOutput =
        match send_update_email_template(client).await {
            Ok(output) => output,
            Err(error) => panic!("Problem updating template: {}", error),
        };

    println!("asdasd {:?}", update_email_template_output);
}

async fn send_update_email_template(
    client: Client,
) -> Result<UpdateEmailTemplateOutput, SdkError<UpdateEmailTemplateError>> {
    let email_template_content = EmailTemplateContent::builder()
        .subject(String::from("asdasd"))
        .text(String::from("asdasd"))
        .html(String::from("asdasd"))
        .build();

    client
        .update_email_template()
        .template_name(String::from("asdasd"))
        .template_content(email_template_content)
        .send()
        .await
}
