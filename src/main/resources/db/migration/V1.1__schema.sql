CREATE TABLE IF NOT EXISTS USERS (
  userid INT PRIMARY KEY auto_increment,
  username VARCHAR(20),
  salt VARCHAR(20),
  password VARCHAR(20),
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId INT PRIMARY KEY auto_increment,
    filename VARCHAR(255),
    contenttype VARCHAR(255),
    filesize VARCHAR(20),
    userid INT,
    filedata BLOB,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY auto_increment,
    url VARCHAR(100),
    username VARCHAR (30),
    `key` VARCHAR(20),
    password VARCHAR(255),
    userid INT(20),
    foreign key (userid) references USERS(userid)
);