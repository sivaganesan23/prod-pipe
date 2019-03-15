provider "aws" {}

terraform {
  backend "s3" {
    bucket = "studproj"
    key    = "stack/terraform.tfstate"
    region = "us-east-2"
  }
}
