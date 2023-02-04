use aws_sdk_ses::error::{CreateTemplateError, UpdateTemplateError};
use aws_sdk_ses::model::Template;
use aws_sdk_ses::output::{CreateTemplateOutput, UpdateTemplateOutput};
use aws_sdk_ses::types::SdkError;
use aws_sdk_ses::Client;

use std::fs;

use crate::aws_ses_client;
use crate::ses_template;

pub async fn upload_templates() {
    let client = aws_ses_client::new().await;
    process_templates(client).await;
}

async fn process_templates(client: Client) {
    let templates_dir = fs::read_dir(String::from("resources/ses_templates")).unwrap();

    for dir in templates_dir {
        let template_file = fs::read_to_string(dir.unwrap().path()).unwrap();
        let ses_template: ses_template::SESTemplate = serde_json::from_str(&template_file).unwrap();

        create_or_update_template(&client, ses_template).await;
    }
}

async fn create_or_update_template(client: &Client, ses_template: ses_template::SESTemplate) {
    let template = Template::builder()
        .template_name(ses_template.name)
        .subject_part(ses_template.subject)
        .text_part(ses_template.text)
        .html_part(ses_template.html)
        .build();

    if let Err(error) = update_template(&client, &template).await {
        let update_template_error = error.into_service_error();

        println!("update template error {:?}", update_template_error);

        if update_template_error.is_template_does_not_exist_exception() {
            if let Err(ss) = create_template(&client, &template).await {
                let create_template_error = ss.into_service_error();

                panic!(
                    "Problem creating template: {:?}",
                    create_template_error.message()
                )
            } else {
                println!("created template {:?}", template.template_name());
            }
        } else {
            panic!(
                "Problem updating template: {:?}",
                update_template_error.message()
            )
        }
    } else {
        println!("updated template {:?}", template.template_name());
    }
}

async fn create_template(
    client: &Client,
    template: &Template,
) -> Result<CreateTemplateOutput, SdkError<CreateTemplateError>> {
    client
        .create_template()
        .template(template.clone())
        .send()
        .await
}

async fn update_template(
    client: &Client,
    template: &Template,
) -> Result<UpdateTemplateOutput, SdkError<UpdateTemplateError>> {
    client
        .update_template()
        .template(template.clone())
        .send()
        .await
}
