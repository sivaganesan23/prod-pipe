variable "ami" {
    default = "ami-0eb061071b4d437b8"
}

variable "az-single-char" {
    default = ["A","B","C","D","E","F"]
}

variable "vpcid" {}
variable "keyname" {
    default = "devops"
}

variable "public_subnets" {
    type = "list"
}

variable "proj" {}
variable "application" {}

variable "dbpass" {}
variable "dbname" {}
variable "dbip" {}
variable "dbuser" {}

data "aws_availability_zones" "az" {}
