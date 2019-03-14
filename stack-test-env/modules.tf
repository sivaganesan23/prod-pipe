module "network" {
    source          = "./Modules/network"
    proj            = "${var.proj}"
    application     = "${var.application}"
}

module "infra" {
    source          = "./Modules/infra"
    dbuser          = "${module.database.dbuser}" 
    dbname          = "${module.database.dbname}" 
    dbpass          = "${module.database.dbpass}" 
    dbip            = "${module.database.dbip}"
    public_subnets  = "${module.network.public_subnets}"
    proj            = "${var.proj}"
    application     = "${var.application}"
    vpcid           = "${module.network.vpcid}"
}

module "database" {
    source = "./Modules/database"
    proj            = "${var.proj}"
    application     = "${var.application}"
    private_subnets = "${module.network.private_subnets}"
    vpcid           = "${module.network.vpcid}"
    vpc_cidr        = "${module.network.vpc_cidr}"
}
