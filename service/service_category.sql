CREATE DATABASE IF NOT EXISTS Service;
use service
create table IF NOT EXISTS `service_category` (
`id` INT AUTO_INCREMENT,
`category_name` VARCHAR(30) NOT NULL,
`parent_id` INT,
`status` INT,
`create_time` TIMESTAMP,
`update_time` TIMESTAMP,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
