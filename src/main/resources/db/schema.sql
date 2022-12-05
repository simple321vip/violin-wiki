drop table IF EXISTS t_blog_seq;
create table t_blog_seq (
    blog_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );
drop table IF EXISTS t_blog_type_seq;
create table t_blog_type_seq (
    blog_type_seq_id INT primary key AUTO_INCREMENT NOT NULL
  );