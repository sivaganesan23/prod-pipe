output "dbip" {
    value = "${aws_db_instance.student-rds.address}"
}

output "dbuser" {
    value = "${var.dbuser}"
}

output "dbpass" {
    value = "${var.dbpass}"
}

output "dbname" {
    value = "${var.dbname}"
}