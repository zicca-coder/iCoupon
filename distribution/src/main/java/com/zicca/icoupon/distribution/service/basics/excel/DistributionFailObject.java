package com.zicca.icoupon.distribution.service.basics.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分发失败对象 | 优惠券分发失败记录写入 Excel
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionFailObject {

    @ColumnWidth(20)
    @ExcelProperty("行号")
    private String rowNum;

    @ColumnWidth(50)
    @ExcelProperty("失败原因")
    private String cause;


}
