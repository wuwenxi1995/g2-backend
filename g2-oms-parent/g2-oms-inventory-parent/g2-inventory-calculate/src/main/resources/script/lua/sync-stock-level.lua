-- ARGV[1] = value
-- ARGV[2] = g2inv:stock:posCode:skuCode
-- ARGV[3] = stockLevel
-- ARGV[4] = extend_reserved
-- ARGV[5] = extend_ats

local value = tonumber(ARGV[1]);
local stockLevelKey = ARGV[2]
local stockLevel = ARGV[3]
local extend_reserved = ARGV[4]
local extend_ats = ARGV[5]

local reserved = tonumber(redis.call('hexists',stockLevelKey,extend_reserved)) or 0;
local ats = value - reserved;

redis.call('hmset',stockLevelKey,stockLevel,value,extend_reserved,reserved,extend_ats,ats)
return true;
