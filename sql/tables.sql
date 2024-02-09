create database if not exists myhacking character set utf8mb4 COLLATE utf8mb4_general_ci;
use myhacking;

create user 'myhack'@'%' identified by '1234';
grant all privileges on myhacking.* to 'myhack'@'%' identified by '1234';
FLUSH PRIVILEGES;

create table if not exists myuser(
	myname varchar(30),
	myid varchar(20) primary key,
	mypw varchar(32),
	myemail varchar(50),
	mylocation varchar(300),
	myphone varchar(64),
	mysid varchar(64)
);

create table if not exists myboard(
    mydate timestamp not null ,
    mypriority int(11) not null unique auto_increment,
    myreadcount int(11) default null ,
    mycontent text default null,
    myip varchar(40) ,
    myid varchar(20) ,
    mysubject varchar(150) ,
    myfilepath text default null,
    mytext text
);

create table if not exists myacc(
	myacc varchar(64) primary key,
	myid varchar(20),
	mymoney int,
	mybank varchar(30),
	myaccpw varchar(32),
    myaccregdate timestamp default current_timestamp
);

create table if not exists myacchistory(
	myacchistnum int AUTO_INCREMENT primary key,
	myacc varchar(64),
	myaccdate timestamp default current_timestamp,
    mysendbank varchar(30),
    mysendacc varchar(64),
	myaccin int,
	myaccout int,
	myaccbalance int,
	myaccioname varchar(30),
	myaccmemo varchar(1500)
);

create table if not exists myaddr(
	myzip int,
    myaddr varchar(200)
);

# myboard 테이블에 데이터 추가
insert into myboard(mydate, mypriority, myreadcount, mycontent, myip,  myid, mysubject, myfilepath, mytext) values
(now(), 0, 0, '게시글유형', '192.168.0.1', '아이디', '게시판제목', '/절대경로/파일1;/절대경로/파일2;', '게시판내용');

# myacchistory 테이블에 데이터 추가
INSERT INTO myacchistory (myacc, mysendbank, mysendacc, myaccin, myaccout, myaccbalance, myaccioname, myaccmemo)
VALUES 
('7193aa59f58433ffb9576f18d201c614bc66c62f178c5891620693c0aa873d74', 'MNST', '0eca208a206085d0c001bae43a175c0955af17be18d168fc0ab85a3822850735', 0, 99999000, 10000, 'send1', 'Receive1'),
('0eca208a206085d0c001bae43a175c0955af17be18d168fc0ab85a3822850735', 'MNST', '7193aa59f58433ffb9576f18d201c614bc66c62f178c5891620693c0aa873d74', 200010000, 0, 10000, 'send1', ''),
('73430ed60859abd0e87c38ffb44d0e7232d6749f11a29e35ff776a78a1449f36', 'MNST', 'c49179047a3e51690db26476f88e98dc034f48761d8895b501859d65bef5ac65', 0, 299985000, 15000, 'send2', 'Receive2'),
('c49179047a3e51690db26476f88e98dc034f48761d8895b501859d65bef5ac65', 'MNST', '73430ed60859abd0e87c38ffb44d0e7232d6749f11a29e35ff776a78a1449f36', 400015000, 0, 15000, 'send2', ''),
('ecc1a72bec56fb08d33f84758bf9061b00660a8bbc718d547f1ce2b3df2da864', 'MNST', '137802c9adc063e71f2b8f97175c42f0bcd3fb047c85e068805e460d4145765d', 0, 499980000, 20000, 'send3', 'Receive3'),
('137802c9adc063e71f2b8f97175c42f0bcd3fb047c85e068805e460d4145765d', 'MNST', 'ecc1a72bec56fb08d33f84758bf9061b00660a8bbc718d547f1ce2b3df2da864', 600020000, 0, 20000, 'send3', ''),
('e6ed66d4fcc5fb129f28cec3466a3410744a2e05113e28644cd3f79f2150cb28', 'MNST', '1e23b64fcaffd67d17f2b465da4ec8ad9e1af0d79ce39730d7e0bb4840e10e27', 0, 699975000, 25000, 'send4', 'Receive4'),
('1e23b64fcaffd67d17f2b465da4ec8ad9e1af0d79ce39730d7e0bb4840e10e27', 'MNST', 'e6ed66d4fcc5fb129f28cec3466a3410744a2e05113e28644cd3f79f2150cb28', 800025000, 0, 25000, 'send4', ''),
('369459dbf0d852e204916ce16b69f180e527058e735dd584b803e9822eaf6a34', 'MNST', '4cc272b6448f5a8915d2ad02d11fe05fa5834ade0a9bb31fd18d2ff243465fb0', 0, 899970000, 30000, 'send5', 'Receive5'),
('4cc272b6448f5a8915d2ad02d11fe05fa5834ade0a9bb31fd18d2ff243465fb0', 'MNST', '369459dbf0d852e204916ce16b69f180e527058e735dd584b803e9822eaf6a34', 130000, 0, 30000, 'send5', ''),
('7193aa59f58433ffb9576f18d201c614bc66c62f178c5891620693c0aa873d74', 'MNST', 'ecc1a72bec56fb08d33f84758bf9061b00660a8bbc718d547f1ce2b3df2da864', 0, 99964000, 35000, 'send6', 'Receive6'),
('ecc1a72bec56fb08d33f84758bf9061b00660a8bbc718d547f1ce2b3df2da864', 'MNST', '7193aa59f58433ffb9576f18d201c614bc66c62f178c5891620693c0aa873d74', 200045000, 0, 35000, 'send6', '');