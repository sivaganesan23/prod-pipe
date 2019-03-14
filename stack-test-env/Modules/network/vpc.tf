resource "aws_vpc" "main" {
  cidr_block       = "${var.vpc_cidr}"
  instance_tenancy = "default"

  tags = {
    Name        = "${var.proj}-VPC"
    Application = "${var.application}"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = "${aws_vpc.main.id}"

  tags = {
    Name        = "${var.proj}-IGW"
    Application = "${var.application}"
  }
}

### Peering connection
resource "aws_vpc_peering_connection" "peer" {
  vpc_id        = "${aws_vpc.main.id}"
  peer_vpc_id   = "vpc-49101621"
  peer_owner_id = "${data.aws_caller_identity.peer.account_id}"
  peer_region   = "us-east-2"
  auto_accept   = false

  tags = {
    Name = "Default-to-Student-VPC-Peer"
  }
}

resource "aws_vpc_peering_connection_accepter" "peer" {
  vpc_peering_connection_id = "${aws_vpc_peering_connection.peer.id}"
  auto_accept               = true

  tags = {
    Side = "Accepter"
  }
}

resource "aws_route_table" "pub-rt" {
  vpc_id    = "${aws_vpc.main.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.gw.id}"
  }

  route {
    cidr_block = "172.31.0.0/16"
    vpc_peering_connection_id = "${aws_vpc_peering_connection.peer.id}"
  }

  tags = {
    Name        = "${var.proj}-Public-RT"
    Application = "${var.application}"
  }
}

resource "aws_route_table" "priv-rt" {
  vpc_id    = "${aws_vpc.main.id}"

  route {
    cidr_block = "0.0.0.0/0"
    vpc_peering_connection_id = "${aws_vpc_peering_connection.peer.id}"
  }

  tags = {
    Name        = "${var.proj}-Private-RT"
    Application = "${var.application}"
  }
}

resource "aws_subnet" "public" {
    count       = "${length(var.pub-subnets)}"
    vpc_id      = "${aws_vpc.main.id}"
    cidr_block  = "${element(var.pub-subnets, count.index)}"
    availability_zone = "${data.aws_availability_zones.az.names[count.index]}"
    map_public_ip_on_launch = true
  tags = {
    Name        = "Subnet-Public-${var.proj}-${element(var.az-single-char, count.index)}"
    Application = "${var.application}"
  }
}

resource "aws_subnet" "private" {
    count       = "${length(var.priv-subnets)}"
    vpc_id      = "${aws_vpc.main.id}"
    cidr_block  = "${element(var.priv-subnets, count.index)}"
    availability_zone = "${data.aws_availability_zones.az.names[count.index]}"
  tags = {
    Name        = "Subnet-Private-${var.proj}-${element(var.az-single-char, count.index)}"
    Application = "${var.application}"
  }
}

resource "aws_route_table_association" "pub-rta" {
    count           = "${length(var.pub-subnets)}"
    subnet_id       = "${element(aws_subnet.public.*.id, count.index)}"
    route_table_id  = "${aws_route_table.pub-rt.id}"
}

resource "aws_route_table_association" "priv-rta" {
    count           = "${length(var.priv-subnets)}"
    subnet_id       = "${element(aws_subnet.private.*.id, count.index)}"
    route_table_id  = "${aws_route_table.priv-rt.id}"
}


### Add route entry to existing VPC with peering connection
resource "aws_route" "r1" {
  route_table_id            = "rtb-765c931d"
  destination_cidr_block    = "10.0.0.0/22"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.peer.id}"
}
