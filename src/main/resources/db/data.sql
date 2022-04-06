delete from location;
insert into location (id, type, latitude, longtitude) values(11,'beijing',0,0);
insert into location (id, type, latitude, longtitude) values(1,'shanghai',0,0);

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


insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(1,'a',10,8000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(1,'a2',11,6500);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(2,'b',12,13000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(2,'b2',13,4500);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(3,'c',14,3000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(3,'c2',15,20000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(4,'d',16,30000);
insert into TEST_ROW_NUMBER_OVER(id,name,age,salary) values(5,'d2',17,1800);

INSERT INTO Websites VALUES(1, 'Google', 'https://www.google.cm/', 1, 'USA');
INSERT INTO Websites VALUES(2, '淘宝', 'https://www.taobao.com/', 13, 'CN');
INSERT INTO Websites VALUES(3, '菜鸟教程', 'http://www.runoob.com/', 4689, 'CN');
INSERT INTO Websites VALUES(4, '微博', 'http://weibo.com/', 20, 'CN');
INSERT INTO Websites VALUES(5, 'Facebook', 'https://www.facebook.com/', 3, 'USA');

INSERT INTO bookmark_type(bk_type_name) VALUES( '未分类');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Java');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Xml');
INSERT INTO bookmark_type(bk_type_name) VALUES( '厨艺');

INSERT INTO bookmark(bk_type_id, comment, delete_flg, url) VALUES(1, 'JAVA 面试精华', '0', 'www.hahaha/');
INSERT INTO blog_type(blog_type_name) VALUES( '天道');
INSERT INTO blog_type(blog_type_name) VALUES( '天幕红尘');
INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex, blog_text_path) VALUES(1, '心理学', '0', '天道', 'asdasdasd');
INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(1, '医学科', '0', '非自然死亡');
INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(2, '精神科', '0', '私人定制');
INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(2, '外壳', '0', '外壳风云');