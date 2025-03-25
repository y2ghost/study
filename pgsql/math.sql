-- ---
-- 场景的数学运算符
-- + 加法
-- - 减法
-- * 乘法
-- / 除法
-- % 求余数
-- ^ 幂乘
-- |/  平方根
-- |// 立方根
-- 数据结果类型
-- 两个整数返回整数类型
-- 任意操作数类型是numeric，则结果类型是numeric
-- 任意操作数类型是浮点数，则结果类型是double precision
-- ---
SELECT 2 + 2;
SELECT 9 - 1;
SELECT 3 * 4;
SELECT 11 / 6;
SELECT 11 % 6;
SELECT 11.0 / 6;
SELECT CAST(11 AS numeric(3,1)) / 6;

SELECT 3 ^ 4;
SELECT |/ 10;
SELECT sqrt(10);
SELECT ||/ 10;
SELECT factorial(4);

-- 注意运算符的优先级和结合性
SELECT 7 + 8 * 9;
SELECT (7 + 8) * 9;

SELECT 3 ^ 3 - 1;
SELECT 3 ^ (3 - 1);


SELECT county_name AS county,
       state_name AS state,
       pop_est_2019 AS pop,
       births_2019 AS births,
       deaths_2019 AS deaths,
       international_migr_2019 AS int_migr,
       domestic_migr_2019 AS dom_migr,
       residual_2019 AS residual
FROM us_counties_pop_est_2019;

SELECT county_name AS county,
       state_name AS state,
       births_2019 AS births,
       deaths_2019 AS deaths,
       births_2019 - deaths_2019 AS natural_increase
FROM us_counties_pop_est_2019
ORDER BY state_name, county_name;

SELECT county_name AS county,
       state_name AS state,
       pop_est_2019 AS pop,
       pop_est_2018 + births_2019 - deaths_2019 + 
           international_migr_2019 + domestic_migr_2019 +
           residual_2019 AS components_total,
       pop_est_2019 - (pop_est_2018 + births_2019 - deaths_2019 + 
           international_migr_2019 + domestic_migr_2019 +
           residual_2019) AS difference
FROM us_counties_pop_est_2019
ORDER BY difference DESC;

SELECT county_name AS county,
       state_name AS state,
       area_water::numeric / (area_land + area_water) * 100 AS pct_water
FROM us_counties_pop_est_2019
ORDER BY pct_water DESC;


CREATE TABLE percent_change (
    department text,
    spend_2019 numeric(10,2),
    spend_2022 numeric(10,2)
);

INSERT INTO percent_change
VALUES
    ('Assessor', 178556, 179500),
    ('Building', 250000, 289000),
    ('Clerk', 451980, 650000),
    ('Library', 87777, 90001),
    ('Parks', 250000, 223000),
    ('Water', 199000, 195000);

SELECT department,
       spend_2019,
       spend_2022,
       round( (spend_2022 - spend_2019) /
                    spend_2019 * 100, 1) AS pct_change
FROM percent_change;

SELECT sum(pop_est_2019) AS county_sum,
       round(avg(pop_est_2019), 0) AS county_average
FROM us_counties_pop_est_2019;


CREATE TABLE percentile_test (
    numbers integer
);

INSERT INTO percentile_test (numbers) VALUES
    (1), (2), (3), (4), (5), (6);

SELECT
    percentile_cont(.5)
    WITHIN GROUP (ORDER BY numbers),
    percentile_disc(.5)
    WITHIN GROUP (ORDER BY numbers)
FROM percentile_test;

SELECT sum(pop_est_2019) AS county_sum,
       round(avg(pop_est_2019), 0) AS county_average,
       percentile_cont(.5)
       WITHIN GROUP (ORDER BY pop_est_2019) AS county_median
FROM us_counties_pop_est_2019;

SELECT percentile_cont(ARRAY[.25,.5,.75])
       WITHIN GROUP (ORDER BY pop_est_2019) AS quartiles
FROM us_counties_pop_est_2019;

SELECT percentile_cont(ARRAY[.2,.4,.6,.8])
       WITHIN GROUP (ORDER BY pop_est_2019) AS quintiles
FROM us_counties_pop_est_2019;

SELECT percentile_cont(ARRAY[.1,.2,.3,.4,.5,.6,.7,.8,.9])
       WITHIN GROUP (ORDER BY pop_est_2019) AS deciles
FROM us_counties_pop_est_2019;

SELECT unnest(
            percentile_cont(ARRAY[.25,.5,.75])
            WITHIN GROUP (ORDER BY pop_est_2019)
            ) AS quartiles
FROM us_counties_pop_est_2019;

SELECT mode() WITHIN GROUP (ORDER BY births_2019)
FROM us_counties_pop_est_2019;

