CREATE DATABASE IF NOT EXISTS Service;
use servicede
create table IF NOT EXISTS `service` (
`id` INT AUTO_INCREMENT,
`service_name` VARCHAR(30) NOT NULL,
`category_id` INT,
`status` INT,
`price` decimal,
`format` VARCHAR(50),
`introduction` VARCHAR(100),
`detail_introduction` VARCHAR(200),
`comment_count` INT,
`user_count` int,
`checked` INT,
`remarks` VARCHAR(30),
`score` decimal,
`is_other` INT,
`create_time` TIMESTAMP,
`update_time` TIMESTAMP,
`service_img` VARCHAR(100),
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;