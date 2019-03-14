provider "aws" {}

terraform {
  backend "s3" {
    bucket = "terra-citb33"
    key    = "stack/terraform.tfstate"
    region = "us-east-2"
  }
}