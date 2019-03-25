resource "aws_db_parameter_group" "default" {
  name   = "mariadb-student-pg"
  family = "mariadb10.3"
  parameter {
    name  = "lower_case_table_names"
    value = "1"
    apply_method = "pending-reboot"
  }
}

resource "aws_db_subnet_group" "default" {
  name       = "studentapp-subnet-group"
  subnet_ids = ["${var.private_subnets}"]

  tags = {
    Name = "MariaDB-SubnetGroup"
  }
}

resource "aws_security_group" "db-sg" {
  name        = "Student-Proj-DB-SG"
  description = "Student-Proj-DB-SG"
  vpc_id      = "${var.vpcid}"

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "TCP"
    cidr_blocks = ["${var.vpc_cidr}"]
  }

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "TCP"
    cidr_blocks = ["172.31.0.0/16"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "student-rds" {
  allocated_storage       = 10
  storage_type            = "gp2"
  engine                  = "mariadb"
  engine_version          = "10.3"
  instance_class          = "db.t2.micro"
  username                = "${var.dbuser}"
  password                = "${var.dbpass}"
  parameter_group_name    = "${aws_db_parameter_group.default.id}"
  db_subnet_group_name    = "${aws_db_subnet_group.default.name}"
  skip_final_snapshot     = true
  identifier              = "rds-studentapp"
  vpc_security_group_ids  = ["${aws_security_group.db-sg.id}"]
  tags = {
    Name        = "${var.proj}-rds"
    Application = "${var.application}"
  }
}

resource "null_resource" "schema" {
    provisioner "local-exec" {
        command = <<EOF
            wget -O /tmp/schema.sql https://raw.githubusercontent.com/citb33/project-documentation/master/rds-schema.sql
            mysql -h ${aws_db_instance.student-rds.address} -u ${var.dbuser} -p${var.dbpass} </tmp/schema.sql
            EOF
    }
}
