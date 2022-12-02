drop table IF EXISTS section;
create table section(
         section_id long primary key AUTO_INCREMENT,
         user_id long default 0 not null,
         section_name char(20) default '未分类' not null
  );

drop table IF EXISTS page;
create table page(
         page_id long primary key AUTO_INCREMENT,
         section_id long default 0 not null,
         page_name char(20) default '未分类' not null,
         page_path char(20) default '' not null
  );

drop table IF EXISTS t_blog_seq;
create table t_blog_seq (
    blog_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );
drop table IF EXISTS t_blog_type_seq;
create table t_blog_type_seq (
    blog_type_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );
drop table IF EXISTS t_bookmark_seq;
create table t_bookmark_seq (
    bk_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );
drop table IF EXISTS t_blog_seq;
create table t_blog_seq (
    blog_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );

