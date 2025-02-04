provider "aws" {
}

terraform {
  backend "s3" {
    region = "us-east-1"
    bucket = "terraform-state-590183842175-us-east-1"
    key    = "irimo"
  }
}

resource "aws_iam_user" "textract_user" {
  name = "textract-user"
  path = "/"

  tags = {
    Description = "IAM user for Textract document text detection"
  }
}

resource "aws_iam_user_policy" "textract_policy" {
  name = "textract_detect_document_text_policy"
  user = aws_iam_user.textract_user.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "textract:DetectDocumentText"
        ]
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_access_key" "textract_user_key" {
  user = aws_iam_user.textract_user.name
}

data "aws_region" "current" {}

locals {
  textract_user_access_key_id     = aws_iam_access_key.textract_user_key.id
  textract_user_secret_access_key = nonsensitive(aws_iam_access_key.textract_user_key.secret)
  aws_region                      = data.aws_region.current.name
  aws_output_format               = "json" # Replace with your desired output format
}

resource "null_resource" "configure_aws_profile" {
  provisioner "local-exec" {
    command = <<-EOT
      if ! command -v op &> /dev/null || ! op account get &> /dev/null; then
        echo "1Password CLI (op) could not be found or is not signed in"
        exit 1
      fi

      op item create \
        --category "Login" \
        --vault "devops" \
        --title "AWS_TEXTRACT_USER" \
        'AWS_ACCESS_KEY_ID[text]=${local.textract_user_access_key_id}' \
        'AWS_SECRET_ACCESS_KEY[password]=${local.textract_user_secret_access_key}'
    EOT
  }

  provisioner "local-exec" {
    when    = destroy
    command = <<-EOT
      if command -v op &> /dev/null && op account get &> /dev/null; then
        op item delete "AWS_TEXTRACT_USER" --vault "devops" || true
      fi
    EOT
  }

  depends_on = [aws_iam_access_key.textract_user_key]
}
