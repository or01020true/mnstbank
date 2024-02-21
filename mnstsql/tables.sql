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
	myphone varchar(24),
	mysid varchar(24),
	mylevel int default 0
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
	myacc varchar(24) primary key,
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
# INSERT INTO myacchistory (myacc, mysendbank, mysendacc, myaccin, myaccout, myaccbalance, myaccioname, myaccmemo)
# VALUES 
# ('Yd5HQVDiTY/R0oCDId+hwQ==', 'MNST', 'pAQ29e/J3enWjGf1GtXrvg==', 0, 99999000, 10000, 'send1', 'Receive1'),
# ('pAQ29e/J3enWjGf1GtXrvg==', 'MNST', 'Yd5HQVDiTY/R0oCDId+hwQ==', 200010000, 0, 10000, 'send1', ''),
# ('ZqXteAtg0nHQ46aVQYHVJw==', 'MNST', 'ExqA6MoAMnIBJwvzHHsVUw==', 0, 299985000, 15000, 'send2', 'Receive2'),
# ('ExqA6MoAMnIBJwvzHHsVUw==', 'MNST', 'ZqXteAtg0nHQ46aVQYHVJw==', 400015000, 0, 15000, 'send2', ''),
# ('JQNaGkJ4+g9yxWsennA7aw==', 'MNST', 'FrRbOg/blAPoFz0sPdUanA==', 0, 499980000, 20000, 'send3', 'Receive3'),
# ('FrRbOg/blAPoFz0sPdUanA==', 'MNST', 'JQNaGkJ4+g9yxWsennA7aw==', 600020000, 0, 20000, 'send3', ''),
# ('7Vb5hpyHYYh2vGf2+6t4hg==', 'MNST', 'mrXa6xjGAUhrji7Wk/hcJQ==', 0, 699975000, 25000, 'send4', 'Receive4'),
# ('mrXa6xjGAUhrji7Wk/hcJQ==', 'MNST', '7Vb5hpyHYYh2vGf2+6t4hg==', 800025000, 0, 25000, 'send4', ''),
# ('mSmy7dvFBDnDadpsY79eag==', 'MNST', 'RuW9A/1jW3vWf5aaABN3qw==', 0, 899970000, 30000, 'send5', 'Receive5'),
# ('RuW9A/1jW3vWf5aaABN3qw==', 'MNST', 'mSmy7dvFBDnDadpsY79eag==', 130000, 0, 30000, 'send5', ''),
# ('Yd5HQVDiTY/R0oCDId+hwQ==', 'MNST', 'JQNaGkJ4+g9yxWsennA7aw==', 0, 99964000, 35000, 'send6', 'Receive6'),
# ('JQNaGkJ4+g9yxWsennA7aw==', 'MNST', 'Yd5HQVDiTY/R0oCDId+hwQ==', 200045000, 0, 35000, 'send6', '');