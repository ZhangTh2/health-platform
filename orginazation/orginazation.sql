CREATE DATABASE IF NOT EXISTS ORGINAZATION;
use orginazation
drop table orginazation;
create table IF NOT EXISTS `orginazation` (
`id` INT AUTO_INCREMENT,
`orginazation_name` VARCHAR(30) NOT NULL,
`orginazation_address` VARCHAR(100) NOT NULL,
`description` VARCHAR (100),
`phone` VARCHAR(20) NOT NULL,
`email` VARCHAR(30),
`parent_id` int,
`checked` INT,
`remarks` VARCHAR(50),
`update_time` TIMESTAMP ,
`create_time` TIMESTAMP,
 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

