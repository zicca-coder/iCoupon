-- Lua 脚本：回滚优惠券模板库存、用户领券数量

-- KEYS[1]：优惠券库存键/优惠券模板键 (coupon_stock_key)
-- KEYS[2]：用户领取记录键/用户领取优惠券数量缓存 (user_coupon_key)


-- 减少用户领取优惠券数量
tonumber(redis.call('DECR', KEYS[2]))

-- 增加优惠券模板库存
tonumber(redis.call('HINCRBY', KEYS[1], 'stock', 1))

return 1







