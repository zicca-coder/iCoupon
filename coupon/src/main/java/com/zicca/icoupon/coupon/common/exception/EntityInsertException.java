package com.zicca.icoupon.coupon.common.exception;

import com.zicca.icoupon.framework.exception.ServiceException;

/**
 * 实体插入异常
 *
 * @author zicca
 */
public class EntityInsertException extends ServiceException {
    public EntityInsertException(String message) {
        super(message);
    }
}
