package com.zicca.icoupon.coupon.toolkit;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 优惠券编码解码
 *
 * @author zicca
 */
public final class SnappyCouponCodec {



    /**
     * 解码字符串数据
     * 
     * @param data 待解码的字符串数据
     * @return 解码后的字符串
     * @throws IOException 解码失败时抛出
     */
    public static String decode(String data) throws IOException {
        return decode(data.getBytes(StandardCharsets.ISO_8859_1));
    }
    
    
    /**
     * 解码字节数组数据
     * 首先尝试使用Snappy解压缩，如果失败则检查是否为未压缩的原始数据
     * 
     * @param data 待解码的字节数组
     * @return 解码后的字符串
     * @throws IOException 解码失败时抛出
     */
    public static String decode(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            throw new IOException("Input data is null or empty");
        }

        try {
            // 尝试Snappy解压缩
            return new String(Snappy.uncompress(data), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // 检查是否为未压缩的原始数据
            try {
                String result = new String(data, StandardCharsets.ISO_8859_1);
                // 简单验证是否为有效的JSON格式
                if (result.trim().startsWith("{") && result.trim().endsWith("}") ||
                        result.trim().startsWith("[") && result.trim().endsWith("]")) {
                    return result;
                }
            } catch (Exception utf8Exception) {
                // 忽略UTF-8转换异常
            }
            // 重新抛出原始的Snappy异常
            throw new IOException("Failed to uncompress data (length: " + data.length + "): " + e.getMessage(), e);
        }
    }

    
    /**
     * 将对象编码为压缩的字节数组
     * 使用Jackson将对象序列化为JSON字符串，然后使用Snappy进行压缩
     * 
     * @param obj 待编码的对象
     * @param <T> 对象类型
     * @return 编码后的字节数组
     * @throws IOException 编码失败时抛出
     */
    public static <T> byte[] encode(T obj) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(obj);
            return Snappy.compress(json.getBytes(StandardCharsets.ISO_8859_1));
        } catch (Exception e) {
            throw new IOException("Failed to encode object: " + e.getMessage(), e);
        }
    }

    /**
     * 将对象编码为压缩的字符串
     * 
     * @param obj 待编码的对象
     * @param <T> 对象类型
     * @return 编码后的字符串
     * @throws IOException 编码失败时抛出
     */
    public static <T> String encodeToStr(T obj) throws IOException {
        return new String(encode(obj), StandardCharsets.ISO_8859_1);
    }
    
    /**
     * 解码字符串数据并转换为指定类型的对象
     * 
     * @param data 待解码的字符串数据
     * @param clazz 目标对象类型
     * @param <T> 对象类型
     * @return 解码后的指定类型对象
     * @throws IOException 解码失败时抛出
     */
    public static <T> T decodeFromStr(String data, Class<T> clazz) throws IOException {
        return decode(data.getBytes(StandardCharsets.ISO_8859_1), clazz);
    }
    
    
    /**
     * 解码字节数组数据并转换为指定类型的对象
     * 
     * @param data 待解码的字节数组
     * @param clazz 目标对象类型
     * @param <T> 对象类型
     * @return 解码后的指定类型对象
     * @throws IOException 解码失败时抛出
     */
    public static <T> T decode(byte[] data, Class<T> clazz) throws IOException {
        return JSON.parseObject(decode(data), clazz);
    }
    


}
