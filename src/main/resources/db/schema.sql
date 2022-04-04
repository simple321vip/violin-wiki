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

DROP TABLE IF EXISTS Websites;
CREATE TABLE Websites(
id INT PRIMARY KEY,
name VARCHAR(255),
url VARCHAR(255),
alexa int,
country char(3)
);

DROP TABLE IF EXISTS TEST_ROW_NUMBER_OVER;
create table TEST_ROW_NUMBER_OVER(
       id varchar(10) not null,
       name varchar(10) null,
       age varchar(10) null,
       salary int null
);

DROP TABLE IF EXISTS TEST_ONE;
create table TEST_ONE (
`id` INT UNSIGNED AUTO_INCREMENT,
name varchar(10) not null
);

DROP TABLE IF EXISTS bookmark;
create table bookmark(
         bk_id long primary key AUTO_INCREMENT,
         bk_type_id long default 0 not null,
         comment varchar(25) default '' not null,
         delete_flg char(1) default '0' not null,
         url varchar(250)
  );

DROP TABLE IF EXISTS bookmark_type;
create table bookmark_type(
         bk_type_id long primary key AUTO_INCREMENT,
         bk_type_name char(20) default '' not null
  );


