CREATE TABLE char_data_types (
    char_column char(10),
    varchar_column varchar(10),
    text_column text
);

INSERT INTO char_data_types
VALUES
    ('abc', 'abc', 'abc'),
    ('aaa', 'baa', 'bba');

COPY char_data_types TO '/tmp/charData.txt'
WITH (FORMAT CSV, HEADER, DELIMITER '|');

-- ---
-- 金融建议使用numeric类型 
-- 尽可能的使用整数类型
-- ---
CREATE TABLE number_data_types (
    numeric_column numeric(20,5),
    real_column real,
    double_column double precision
);

INSERT INTO number_data_types
VALUES
    (.7, .7, .7),
    (2.13579, 2.13579, 2.13579),
    (2.1357987654, 2.1357987654, 2.1357987654);

SELECT * FROM number_data_types;

SELECT numeric_column * 10000000 AS fixed,
    real_column * 10000000 AS floating
FROM number_data_types WHERE numeric_column = .7;

SELECT numeric_column,
       CAST(numeric_column AS integer),
       CAST(numeric_column AS text)
FROM number_data_types;


CREATE TABLE date_time_types (
    timestamp_column timestamp with time zone,
    interval_column interval
);

-- 可以指定时区
INSERT INTO date_time_types
VALUES
    ('2022-12-31 01:00 EST','2 days'),
    ('2022-12-31 01:00 -8','1 month'),
    ('2022-12-31 01:00 Australia/Melbourne','1 century'),
    (now(),'1 week');

SELECT * FROM date_time_types;

SELECT timestamp_column, interval_column,
    timestamp_column - interval_column AS new_date
FROM date_time_types;

-- 支持时间类型转换为字符串
SELECT timestamp_column, CAST(timestamp_column AS varchar(10))
FROM date_time_types;

SELECT timestamp_column::varchar(10)
FROM date_time_types;

