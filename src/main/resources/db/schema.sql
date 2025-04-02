CREATE TABLE IF NOT EXISTS t_blog_type_seq(
     blog_type_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_type_seq_id)
);

CREATE TABLE IF NOT EXISTS t_blog_seq(
     blog_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_seq_id)
);

CREATE TABLE IF NOT EXISTS T_PROFILE(
     blog_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_seq_id)
);

CREATE TABLE IF NOT EXISTS T_WIKI(
     blog_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_seq_id)
);

CREATE TABLE IF NOT EXISTS T_WIKI_BOX(
     blog_seq_id INT NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (blog_seq_id)
);