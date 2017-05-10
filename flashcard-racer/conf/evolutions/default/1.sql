# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student (
  id                            bigint not null,
  hash                          blob,
  salt                          blob,
  name                          varchar(255),
  constraint pk_student primary key (id)
);
create sequence student_seq;

create table teacher (
  id                            bigint not null,
  name                          varchar(255),
  hash                          blob,
  salt                          blob,
  constraint pk_teacher primary key (id)
);
create sequence teacher_seq;


# --- !Downs

drop table if exists student;
drop sequence if exists student_seq;

drop table if exists teacher;
drop sequence if exists teacher_seq;

