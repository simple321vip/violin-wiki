CREATE TABLE IF NOT EXISTS t_blog_type_seq(
     blog_type_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_type_seq_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_blog_seq(
     blog_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_seq_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;