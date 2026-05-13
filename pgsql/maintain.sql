CREATE TABLE vacuum_test (
    integer_column integer
);


SELECT pg_size_pretty(
           pg_total_relation_size('vacuum_test')
       );

SELECT pg_size_pretty(
           pg_database_size('analysis')
       );


INSERT INTO vacuum_test
SELECT * FROM generate_series(1,500000);

SELECT pg_size_pretty(
           pg_table_size('vacuum_test')
       );


UPDATE vacuum_test
SET integer_column = integer_column + 1;

SELECT pg_size_pretty(
           pg_table_size('vacuum_test')
       );


SELECT relname,
       last_vacuum,
       last_autovacuum,
       vacuum_count,
       autovacuum_count
FROM pg_stat_all_tables
WHERE relname = 'vacuum_test';

SELECT *
FROM pg_stat_all_tables
WHERE relname = 'vacuum_test';

-- 清理无用空间
VACUUM vacuum_test;
VACUUM;
VACUUM VERBOSE;
VACUUM FULL vacuum_test;

-- 验证表占用空间大小
SELECT pg_size_pretty(
           pg_table_size('vacuum_test')
       );
       

SHOW config_file;
SHOW data_directory;

-- 重新加载配置
SELECT pg_reload_conf(); 

-- 备份和恢复
pg_dump -d analysis -U [user_name] -Fc -v -f analysis_backup.dump
pg_dump -t 'train_rides' -d analysis -U [user_name] -Fc -v -f train_backup.dump
pg_restore -C -d postgres -U postgres analysis_backup.dump

