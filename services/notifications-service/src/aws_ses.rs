use aws_sdk_ses::error::{CreateTemplateError, UpdateTemplateError};
use aws_sdk_ses::model::Template;
use aws_sdk_ses::output::{CreateTemplateOutput, UpdateTemplateOutput};
use aws_sdk_ses::types::SdkError;
use aws_sdk_ses::{Client, Region};

pub async fn send_email_from_template() {
    let config_loader = aws_config::from_env()
        .region(Region::new("eu-west-1"))
        .endpoint_url(String::from("http://localhost:4566/"));

    let shared_config = config_loader.load().await;

    println!(
        "Loaded AWS config {:?},{:?}",
        shared_config.endpoint_resolver(),
        shared_config.endpoint_url()
    );
    let client = ses_client(&shared_config);

    create_or_update_template(client).await;
}

fn ses_client(conf: &aws_config::SdkConfig) -> Client {
    let mut ses_config_builder = aws_sdk_ses::config::Builder::from(conf);
    if use_localstack() {
        println!(
            "Using localstack {}",
            std::env::var("LOCALSTACK_ENDPOINT").unwrap().to_string()
        );
        ses_config_builder = ses_config_builder
            .endpoint_url(std::env::var("LOCALSTACK_ENDPOINT").unwrap().to_string())
    }
    Client::from_conf(ses_config_builder.build())
}

fn use_localstack() -> bool {
    std::env::var("LOCALSTACK").unwrap_or_default() == "false"
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

async fn create_or_update_template(client: Client) {
    let template = Template::builder()
        .template_name(String::from("asdasd"))
        .subject_part(String::from("asdasd"))
        .text_part(String::from("asdasd"))
        .html_part(String::from("asdasd"))
        .build();

    if let Err(error) = update_template(&client, &template).await {
        let update_template_error = error.into_service_error();
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
    /*
    match update_template(client, template).await {
        Ok(output) => Ok(output),
        Err(error) => {
            if error
                .into_service_error()
                .is_template_does_not_exist_exception()
            {
                create_template(client, template).await;
            }
        }
    }*/
}
