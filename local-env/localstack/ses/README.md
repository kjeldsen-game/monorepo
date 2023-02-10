## SES

Amazon Simple Email Service (Amazon SES) is flexible, and scalable email service that enables developers to send mail from within any application.

Usefull commands when working with SES *(--endpoint-url only needed if working locally)*:

- `aws --endpoint-url http://localhost:4566 ses create-template --cli-input-json file://ses/testing-template.json`

- `aws --endpoint-url http://localhost:4566 ses list-templates`

- `aws --endpoint-url http://localhost:4566 ses create-configuration-set --configuration-set '{"Name":"DEV_Configuration_Set"}'`

- `aws --endpoint-url=http://localhost:4566 ses verify-email-identity --email-address source@test.com`
