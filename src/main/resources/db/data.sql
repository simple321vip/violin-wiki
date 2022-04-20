INSERT INTO bookmark_type(bk_type_name) VALUES( '未分类');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Java');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Xml');
INSERT INTO bookmark_type(bk_type_name) VALUES( '厨艺');

--INSERT INTO bookmark(bk_type_id, comment, delete_flg, url) VALUES(1, '【尚硅谷】2022版Kafka3.x教程', '0', 'https://www.bilibili.com/video/BV1vr4y1677k?p=10');
--INSERT INTO blog_type(blog_type_name) VALUES( '天道');
--INSERT INTO blog_type(blog_type_name) VALUES( '天幕红尘');
--INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex, blog_text_path) VALUES(1, '心理学', '0', '天道', 'asdasdasd');
--INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(1, '医学科', '0', '非自然死亡');
--INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(2, '精神科', '0', '私人定制');
--INSERT INTO blog(blog_type_id, blog_title, delete_flg, blog_prex) VALUES(2, '外壳', '0', '外壳风云');

INSERT INTO section(user_id, section_name) VALUES(2, '日常メモ');
INSERT INTO section(user_id, section_name) VALUES(2, '技術関連');
INSERT INTO page(section_id, page_name, page_path) VALUES(1, 'myholiday', '/pwds/');
INSERT INTO page(section_id, page_name, page_path) VALUES(1, 'セキュリティ', '/sec/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'java', '/ddd/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'Xml', '/xxxx/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'Xml', '111');


delete from g_user;
INSERT INTO g_user (user_id, user_name, user_password) VALUES('xiaoguan', '胡乱写的', 'mb83201048');
