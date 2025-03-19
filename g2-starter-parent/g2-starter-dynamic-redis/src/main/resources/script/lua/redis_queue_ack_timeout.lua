---- KEY[1]: key
---- KEY[2]: key:ack
---- ARGV[1]: ack_timeout

-- 处理未回滚数据的
local ackTimeout = redis.call('zrangebyscore', KEY[2], '-inf', ARGV[1])
if ackTimeout == nil then
 return false;
end

for i,v ipairs(ackTimeout) do
    redis.call('zrem', KEY[2], v);
    redis.call('rpush', KEY[1], v);
end

return true;