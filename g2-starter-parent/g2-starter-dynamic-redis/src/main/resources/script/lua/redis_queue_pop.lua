---- KEY[1]: key
---- KEY[2]: key:ack
---- KEY[3]: key:ack_expire
---- ARGV[1]: now
---- ARGV[2]: expire_time

-- 处理未回滚数据的
local rollbacks = redis.call('zremrangebyscore', KEY[3], 0, ARGV[1])
for i,v ipairs(rollbacks) do
    redis.call('lrem', KEY[2], 0, v);
    redis.call('rpush', KEY[1], v);
end

-- 查询数据
local data = redis.call('lpop', KEY[1]);
if data == nil
    return nil;
end

redis.call('rpush', KEY[2], data);
redis.call('zadd', KEY[3], ARGV[2], data);
return data;
