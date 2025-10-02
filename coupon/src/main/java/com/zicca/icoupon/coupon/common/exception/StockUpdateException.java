package com.zicca.icoupon.coupon.common.exception;

import com.zicca.icoupon.framework.exception.ServiceException;

/**
 * 库存更新异常
 *
 * @author zicca
 */
public class StockUpdateException extends ServiceException {
    public StockUpdateException(String message) {
        super(message);
    }
}
