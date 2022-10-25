INSERT INTO bookmark_type(bk_type_name) VALUES( '未分类');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Java');
INSERT INTO bookmark_type(bk_type_name) VALUES( '数据库');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'python');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'JavaScripts');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'kubernetes');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'Public cloud');
INSERT INTO bookmark_type(bk_type_name) VALUES( 'archive');

INSERT INTO section(user_id, section_name) VALUES(2, '日常メモ');
INSERT INTO section(user_id, section_name) VALUES(2, '技術関連');
INSERT INTO page(section_id, page_name, page_path) VALUES(1, 'myholiday', '/pwds/');
INSERT INTO page(section_id, page_name, page_path) VALUES(1, 'セキュリティ', '/sec/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'java', '/ddd/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'Xml', '/xxxx/');
INSERT INTO page(section_id, page_name, page_path) VALUES(2, 'Xml', '111');


delete from g_user;
INSERT INTO g_user (user_id, user_name, user_password) VALUES('xiaoguan', '胡乱写的', 'mb83201048');

delete from auth_master;
INSERT INTO auth_master (phone_number) VALUES('13332247026');