-- 是否成功
local success = false
-- key
local key = AVG[1]
-- 当前缓存中次数
local current = AVG[2]
-- 下一次缓存中数量
local next = AVG[3]

-- 如果index没有变化
if current == redis.call('get', key) then
    redis.call('set', key, next)
    success = true;
end

return success