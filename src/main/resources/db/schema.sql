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
INSERT INTO Websites VALUES(1, 'Google', 'https://www.google.cm/', 1, 'USA');
INSERT INTO Websites VALUES(2, '淘宝', 'https://www.taobao.com/', 13, 'CN');
INSERT INTO Websites VALUES(3, '菜鸟教程', 'http://www.runoob.com/', 4689, 'CN');
INSERT INTO Websites VALUES(4, '微博', 'http://weibo.com/', 20, 'CN');
INSERT INTO Websites VALUES(5, 'Facebook', 'https://www.facebook.com/', 3, 'USA');

DROP TABLE IF EXISTS TEST_ROW_NUMBER_OVER;
create table TEST_ROW_NUMBER_OVER(
       id varchar(10) not null,
       name varchar(10) null,
       age varchar(10) null,
       salary int null
);
select * from TEST_ROW_NUMBER_OVER t;

insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(1,'a',10,8000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(1,'a2',11,6500);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(2,'b',12,13000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(2,'b2',13,4500);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(3,'c',14,3000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(3,'c2',15,20000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(4,'d',16,30000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(5,'d2',17,1800);

DROP TABLE IF EXISTS TEST_ONE;
create table TEST_ONE (
`id` INT UNSIGNED AUTO_INCREMENT,
name varchar(10) not null
);
insert into TEST_ONE(id, name) values(1, '/');
insert into TEST_ONE(id, name) values(2, 'A');
insert into TEST_ONE(id, name) values(3, 'B');
insert into TEST_ONE(id, name) values(4, 'C');
insert into TEST_ONE(id, name) values(5, '/');
insert into TEST_ONE(id, name) values(6, 'D');
insert into TEST_ONE(id, name) values(7, 'E');
insert into TEST_ONE(id, name) values(8, '/');
insert into TEST_ONE(id, name) values(9, 'F');
insert into TEST_ONE(id, name) values(10, 'G');
insert into TEST_ONE(id, name) values(11, 'H');

