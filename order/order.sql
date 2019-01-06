CREATE DATABASE IF NOT EXISTS SERVICEORDER ;
use SERVICEORDER
create table IF NOT EXISTS `service_order` (
`id` INT AUTO_INCREMENT,
`order_no` VARCHAR(50),
`user_id` int,
`service_id` int,
`service_name` varchar(100),
`buy_time` timestamp,
`format` VARCHAR(50),
`apikey` VARCHAR(100),
`quantity` INT,
`total_call` int,
`can_call` INT,
`use_call` INT,
`score` decimal,
`total_price` decimal,
`status` int,
`create_time` timestamp,
`update_time` timestamp,
 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
