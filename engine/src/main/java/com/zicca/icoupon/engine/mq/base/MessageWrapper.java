package com.zicca.icoupon.engine.mq.base;

import lombok.*;

import java.io.Serializable;

/**
 * 消息包装类
 *
 * @author zicca
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class MessageWrapper<T> implements Serializable {

    private static final long serialVersionUID = -2006039719612715558L;

    /**
     * 消息发送 keys
     */
    @NonNull
    private String keys;

    /**
     * 消息体
     */
    @NonNull
    private T message;

    /**
     * 消息发送时间戳
     */
    private Long timestamp = System.currentTimeMillis();


}
