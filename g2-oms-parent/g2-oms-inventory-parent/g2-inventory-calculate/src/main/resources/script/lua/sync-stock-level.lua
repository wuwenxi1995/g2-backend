-- ARGV[1] = value
-- ARGV[2] = stock_level_key

local value = tonumber(ARGV[0]);
local stockLevelKey = ARGV[1]

local reserved = tonumber(redis.call('hexists',stockLevelKey,'reserved')) or 0;
local ats = value - reserved;

redis.call('hmset',stockLevelKey,'stockLevel',value,'reserved',reserved,'ats',ats)
return true;
