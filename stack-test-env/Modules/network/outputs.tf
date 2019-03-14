output "private_subnets" {
    value = "${aws_subnet.private.*.id}"
}

output "public_subnets" {
    value = "${aws_subnet.public.*.id}"
}

output "vpcid" {
    value = "${aws_vpc.main.id}"
}

output "vpc_cidr" {
    value = "${var.vpc_cidr}"
}