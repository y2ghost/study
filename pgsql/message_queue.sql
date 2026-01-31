create table message (
  id serial primary key,
  message text not null,
  done boolean default false
);

insert into message(message) values ('message 1'),('message 2'),('message 3');

start transaction;
select * from message
where done = false
order by id
for update skip locked
limit 1;

update message
set done = true
where id = 1;
commit;

-- 创建一个存储过程来处理消息
CREATE OR REPLACE PROCEDURE process_message()
LANGUAGE plpgsql
AS $$
DECLARE
    message_row message%ROWTYPE;
BEGIN
    -- 选择一条未处理的消息
    SELECT * INTO message_row
    FROM message
    WHERE done = FALSE
    ORDER BY id
    FOR UPDATE SKIP LOCKED
    LIMIT 1;

    -- 如果有消息，则处理
    IF FOUND THEN
        -- 模拟处理消息
        RAISE INFO 'Processing message: %', message_row.message;
        -- 标记消息为已处理
        UPDATE message
        SET done = TRUE
        WHERE id = message_row.id;
    END IF;
END;
$$;

-- 调用存储过程
CALL process_message();

-- cron插件配置每分钟调用一次 process_message 存储过程
-- SELECT cron.schedule('* * * * *', 'CALL process_message();');

