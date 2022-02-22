CREATE TABLE IF NOT EXISTS `user`(
   `user_id` INT UNSIGNED AUTO_INCREMENT,
   `user_name` VARCHAR(100) NOT NULL,
   `user_password` VARCHAR(40) NOT NULL,
   `create_date` DATE,
   PRIMARY KEY ( `user_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `room`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `room_No` VARCHAR(100) NOT NULL,
   `floor` VARCHAR(100) NOT NULL,
   `building` VARCHAR(100) NOT NULL,
   `community_id` VARCHAR(100) NOT NULL,
   `location_id` VARCHAR(100) NOT NULL,
   `area` decimal NOT NULL,
   `create_date` DATE,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;