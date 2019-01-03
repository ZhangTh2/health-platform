CREATE DATABASE IF NOT EXISTS USER;
use user
create table IF NOT EXISTS `user` (
`id` INT AUTO_INCREMENT,
`username` VARCHAR(30) NOT NULL,
`password` VARCHAR(30) NOT NULL,
`role_id` INT,
`phone` VARCHAR(20) NOT NULL,
`email` VARCHAR(30),
`industry` VARCHAR(40),
`organization_id` INT,
`checked` INT,
`remarks` VARCHAR(20),
`create_time` TIMESTAMP ,
`update_time` TIMESTAMP,
 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;