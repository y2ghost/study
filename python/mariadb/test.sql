DROP DATABASE IF EXISTS testdb;
CREATE DATABASE testdb;
use testdb;

CREATE TABLE contacts (
   id INT PRIMARY KEY AUTO_INCREMENT,
   first_name VARCHAR(25),
   last_name VARCHAR(25),
   email VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE accounts (
   id INT PRIMARY KEY AUTO_INCREMENT,
   first_name VARCHAR(25),
   last_name VARCHAR(25),
   email VARCHAR(100),
   amount DECIMAL(15,2) CHECK (amount >= 0.0),
   UNIQUE (email)
) ENGINE=InnoDB;

DROP USER IF EXISTS 'db_user'@'localhost';
CREATE USER 'db_user'@'localhost' IDENTIFIED BY 'test123456';
GRANT SELECT, INSERT, UPDATE, DELETE, DROP, ALTER
   ON testdb.contacts
   TO 'db_user'@'localhost';

GRANT SELECT, INSERT, UPDATE, DELETE, DROP
   ON testdb.accounts
   TO 'db_user'@'localhost';

INSERT INTO contacts VALUES
(1, "f1", "l1", "f1@l1.dev"),
(2, "f2", "l2", "f2@l2.dev");

