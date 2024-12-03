# Platform Infrastructure

## Terminal/Console/Powershell/CMD

- Terminal/Console: A command-line interface (CLI) tool used to interact with the operating system by typing commands, 
commonly found in Unix/Linux systems.
- PowerShell: A powerful CLI and scripting language developed by Microsoft for task automation and configuration
management, primarily on Windows.
- CMD (Command Prompt): The default command-line interpreter for Windows, used for executing commands and running scripts.

### How to start command-line interface
#### Windows
Open the Start menu or press the Windows key + R. Type cmd or cmd.exe in the Run command box. Press Enter.

- Follow this article to open in specific directory: [Open CMD in specific directory](https://www.lifewire.com/open-command-prompt-in-folder-8681085)

#### Mac OS
On your Mac, do one of the following:
- Click the Launchpad icon  in the Dock, type Terminal in the search field, then click Terminal.
- In the Finder , open the /Applications/Utilities folder, then double-click Terminal.

- Follow this video to open in specific directory: [Open Terminal in specific directory](https://www.youtube.com/watch?v=B-F3_XtZOJw)

## Install AWS CLI

[Install AWS CLI - Official Documentation](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)

After the installation try to verify with executing command in console/terminal (explained in section #How to start command-line interface)
once you open only type `aws --version` and press enter. If something like `aws-cli/2.19.1 Python/3.12.7 Darwin/24.1.0 source/arm64` (won't be exactly same)
showed aws is installed.

## Install Git

[Install Git - Official Documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

After the installation try to verify with executing command in console/terminal (explained in section #How to start command-line interface)
once you open only type `git --version` and press enter. If something like `git version 2.39.5 (Apple Git-154)` (won't be exactly same)
showed git is installed.

## AWS Credentials

To obtain AWS credentials please send a message to the Slack channel #terraform, and we will provide
credentials for you in DM.

## Configure AWS

To configure AWS on your machine, open terminal/console (explained in section #How to start command-line interface, doesn't matter in what directory) and run command ```aws configure```.
After that you will have to enter/paste values. Use the values provided from the previous step from the document you will receive.
For region type `eu-west-1` and output format enter `json`. After each typed or pasted value, press enter.
- `AWS Access Key ID [****************RLBA]:` *****
- `AWS Secret Access Key [****************9gLV]:` *****
- `Default region name [eu-west-1]:` *****
- `Default output format [json]:` *****

## Cloning repository

Open Terminal/Powershell/CMD based on your operating system and in the directory where you want to clone repository enter: 
`git clone https://github.com/kjeldsen-game/monorepo.git`. It could ask you for the credentials for the Github, if that happened please send a message
in channel #help in Slack.

## Starting up Infrastructure w Terraform

1. Go to the directory with the cloned repository, and in the directory `/infrastructure/environments/prod` open Terminal/Powershell/CMD (explained in steps above).
2. In this directory create `(prod.tfvars)` or paste a file with environment variables. To get the env variables write a message in channel #terraform in Slack.
3. If you are running the Terraform first time from your machine execute `terraform init` in the directory mentioned above. 
If everything works fine you will see a green output.
4. After that type/copy command `terraform apply -var-file="prod.tfvars"` wait a moment and if you see `Enter a value` type `yes` and press enter. After this step
the infrastructure start going up. It will take few minutes till it start. After you see green output in console infrastructure started successfully.

## Stopping Infrastructure w Terraform
1. Go to the directory with the cloned repository, and in the directory `/infrastructure/environments/prod` open Terminal/Powershell/CMD (explained in steps above).
2. After that type/copy command `terraform destroy -var-file="prod.tfvars"` wait a moment and if you see `Enter a value` type `yes` and press enter. After this step
   the infrastructure start going up. It will take few minutes till it start. After you see green output in console infrastructure started successfully.


## Route 53 

We use it to handle our domains

## SES

We use it to send emails to users

## ECR 

We use it to store our custom Docker images

## Code Artifact

We use it to store our libraries code
