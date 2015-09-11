-- create database
CREATE DATABASE IF NOT EXISTS ${hiveconf:db};
USE ${hiveconf:db};

-- create internal tables
-- e.g. those that will be shared as input/output among different scripts
CREATE TABLE IF NOT EXISTS input(
	f1 INT,
	f2 INT,
	f3 INT  
);

CREATE TABLE IF NOT EXISTS data_with_id(
  id STRING ,
  f1 INT ,
  f2 INT ,
  f3 INT 
);

CREATE TABLE IF NOT EXISTS sum(
	id STRING,
	sum INT
);
CREATE TABLE IF NOT EXISTS product(
  id STRING,
  product INT
);


-- load external data as external tables into internal tables
CREATE EXTERNAL TABLE IF NOT EXISTS input_ext(f1 int, f2 int, f3 int) 
	ROW FORMAT DELIMITED 
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\n' 
	STORED AS TEXTFILE
	LOCATION '${hiveconf:input_ext}';
INSERT OVERWRITE TABLE input SELECT * FROM input_ext;
