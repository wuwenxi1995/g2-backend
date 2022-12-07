---- KEY[1]: key:ack
---- KEY[2]: key:ack_expire
---- ARGV[1]: data

-- 提交ack数据
redis.call('lrem', KEY[1], 0, data);
redis.call('zrem', KEY[2], data);