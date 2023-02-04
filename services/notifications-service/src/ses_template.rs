use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct SESTemplate {
    pub name: String,
    pub subject: String,
    pub text: String,
    pub html: String,
}
