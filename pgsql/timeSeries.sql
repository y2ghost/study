-- 格式化
SELECT id, DATE_FORMAT(create_time, '%Y-%m-%d') AS fmt_time
FROM file;

-- 计算示例
SELECT id, DATEDIFF(update_time, create_time) AS days
FROM file
where update_time is not null;

-- 按天聚合计算
SELECT DATE(create_time) AS day, SUM(file_size) AS daily_size
FROM file
GROUP BY day
ORDER BY create_time;

-- 按月聚合计算
SELECT DATE_FORMAT(create_time, '%Y-%m') AS month, SUM(file_size) AS month_size
FROM file
GROUP BY month

-- 按年聚合计算
SELECT EXTRACT(YEAR FROM create_time) AS year, SUM(file_size) AS year_size
FROM file
GROUP BY year
ORDER BY year;

-- 七天平均值
SELECT create_time, file_size,
AVG(file_size) OVER (ORDER BY create_time ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS avg_moving
FROM file;

-- 趋势分析
SELECT create_time, file_size,
AVG(file_size) OVER (ORDER BY create_time) AS avg_size,
(file_size - AVG(file_size) OVER (ORDER BY create_time)) / STDDEV(file_size) OVER (ORDER BY
create_time) AS zscore
FROM file;

