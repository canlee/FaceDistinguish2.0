create table userinfo(
id number primary key,
username varchar2(20),
password varchar2(20)
)
;
create table imageinfo(
id number,
image long raw,
foreign key(id) references userinfo(id)
)
;
create table wopt(
array clob
)
;
create table mean(
allmean clob
)
;
create table project(
id number,
pro clob,
foreign key(id) references userinfo(id)
)
;
create table sign(
id number,
signdate varchar2(20),
foreign key(id) references userinfo(id)
)
;
create table classmean(
id number,
mean clob,
foreign key(id) references userinfo(id)
)
;
create table peoplemean(
id number,
mean clob,
foreign key(id) references userinfo(id)
)
;
create sequence userid 
increment by 1 
start with 1 
nomaxvalue 
nocycle 
nocache
;
