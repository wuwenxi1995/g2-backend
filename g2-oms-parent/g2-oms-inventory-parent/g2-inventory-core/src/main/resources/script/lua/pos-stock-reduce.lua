-- KEYS[1] = g2inv:stock:posCode:
-- ARGV[1] = stock_level
-- ARGV[2] = extend_reserved
-- ARGV[3] = extend_ats
-- ARGV[4] = data
-- ARGV[5] = posCode
-- ARGV[6] = g2inv:queue:trigger_pos_stock

local KEYS[1] = stockLevelFormat;
local platformSkuWithAmounts = cjson.decode(ARGV[4]);

for skuCode,quantity in pairs(platformSkuWithAmounts) do
    local stockLevel = stockLevelFormat .. skuCode;
    local data = {};
    data.posCode = ARGV[5];
    data.skuCode = skuCode;
    data.originAts = tonumber(redis.call('hget', stockLevel, ARGV[3])) or 0;
    -- 减少库存
    local stock = tonumber(redis.call('hincrby', stockLevel, ARGV[1], -quantity)) or 0;
    data.onHand = stock;
    -- 减少保留量
    local reserved = tonumber(redis.call('hincrby', stockLevel, ARGV[2], -quantity)) or 0;
    data.reserved = reserved;
    -- 库存可用量
    local ats = stock - reserved;
    if ats < 0 then
       ats = 0;
    end
    data.ats = ats;
    redis.call('hset', stockLevel, ARGV[3], ats);
    -- 触发服务点库存计算
    local trigger = {};
    trigger.content = data;
    trigger.type = 'DEFAULT';
    redis.call('rpush', ARGV[6], cjson.encode(trigger))
end
return true;


