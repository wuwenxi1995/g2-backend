---- KEY[1]: key
---- KEY[2]: key:ack
---- KEY[3]: key:ack_expire
---- ARGV[1]: data

-- 回滚数据
redis.call('lrem', KEY[2], 0, data);
redis.call('zrem', KEY[3], data);
redis.call('rpush', KEY[1], data);