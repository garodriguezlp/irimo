# Automating IAM User Creation with Terraform

## Overview

To streamline the execution of my `irimo` CLI tool, which utilizes the AWS Textract API, I aim to automate the creation of an IAM
user with minimal permissions. This approach adheres to the principle of least privilege and eliminates the need for repeated SSO
logins.

## Objective

### Steps

- Create an IAM user in AWS using Terraform.
- Define an inline policy with minimal permissions required for CLI execution.
- Configure access keys for the user.
- Test HTTP validation using the AWS Textract API.
- Verify successful API calls and access control.
- Store the credentials securely in 1Password.

## Execution

### AWS CLI Test Command

```bash
aws textract detect-document-text \
  --document "Bytes=$(base64 -i src/test/resources/nu_colombia.jpeg)" | jless
```

## Pending Tasks (@todo)

- Discover the process of creating a service account in 1Password to maintain the principle of least privilege.
- Develop `.envrc` configurations to load credentials in the terminal, considering custom field names in 1Password. Details can be
found in [main.tf](./main.tf).
