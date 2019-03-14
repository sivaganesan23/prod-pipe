variable "dbname" {
    default = "studentapp"
}

variable "dbuser" {
    default = "student"
}

variable "dbpass" {
    default = "student1"
}


### Variables inherited from MAIN
variable "proj" {}
variable "application" {}
variable "private_subnets" {
    type = "list"
}

variable "vpcid" {}
variable "vpc_cidr" {}