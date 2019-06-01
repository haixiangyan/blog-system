-- auto-generated definition
create table blog
(
  id                  int auto_increment primary key,
  user_id             bigint,
  title               varchar(100),
  description         varchar(100),
  content             TEXT,
  created_at         datetime,
  updated_at         datetime
);