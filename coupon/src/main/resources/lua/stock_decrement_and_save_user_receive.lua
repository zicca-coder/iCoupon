-- Lua 脚本：检查用户是否达到优惠券领取上限并记录领取次数

-- 参数列表：
-- KEYS[1]：优惠券库存键/优惠券模板键 (coupon_stock_key)
-- KEYS[2]：用户领取记录键/用户领取优惠券数量缓存 (user_coupon_key)
-- ARGV[1]：优惠券有效期结束时间，过期淘汰 (timestamp)
-- ARGV[2]：用户领取上限 (limit)

-- 函数：将两个字段组合成一个整数
-- firstField: 0-领取成功 1-优惠券模板库存不足 2-用户已经达到领取上限
-- secondField: 用户领取的优惠券次数
local function combineFields(firstField, secondField)
    -- 确定 SECOND_FIELD_BITS 为 14，因为 secondField 最大为 9999
    local SECOND_FIELD_BITS = 14

    -- 根据 firstField 的实际值，计算其对应的二进制表示
    -- 由于 firstField 的范围是0-2，我们可以直接使用它的值
    local firstFieldValue = firstField

    -- 模拟位移操作，将 firstField 的值左移 SECOND_FIELD_BITS 位
    local shiftedFirstField = firstFieldValue * (2 ^ SECOND_FIELD_BITS)

    -- 将 secondField 的值与移位后的 firstField 的值相加
    return shiftedFirstField + secondField
end

-- 获取当前优惠券模板库存
local stock = tonumber(redis.call('HGET', KEYS[1], 'stock'))

-- 判断优惠券模板库存是否大于0
if stock <= 0 then
    return combineFields(1, 0) -- 库存不足
end

-- 获取用户领取的优惠券次数
local userCouponCount = tonumber(redis.call('GET', KEYS[2]))

-- 如果用户领取次数不存在/用户第一次领取优惠券，则初始化为0
if userCouponCount == nil then
    userCouponCount = 0
end

-- 判断用户是否已经达到领取上限
if userCouponCount >= tonumber(ARGV[2]) then
    return combineFields(2, userCouponCount) -- 用户已经达到领取上限
end

-- 增加用户领取的优惠券次数
if userCouponCount == 0 then
    -- 如果用户第一次领取，则需要添加过期时间
    redis.call('SET', KEYS[2], 1)
    redis.call('EXPIRE', KEYS[2], ARGV[1])
else
    -- 因为第一次领取已经设置了过期时间，第二次领取沿用之前即可
    redis.call('INCR', KEYS[2])
end

-- 减少优惠券模板库存
redis.call('HINCRBY', KEYS[1], 'stock', -1)

return combineFields(0, userCouponCount + 1)
