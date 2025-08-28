package com.zicca.icoupon.admin.merchant.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.util.ListUtils;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * 百万 Excel 文件生成单元测试
 *
 * @author zicca
 */
public final class ExcelGenerateTests {

    private final int writeNum = 5000;
    private final Faker faker = new Faker(Locale.CHINA);
    private final String excelPath = Paths.get("").toAbsolutePath().getParent() + "/tmp";

    @Test
    public void testExcelGenerate() {
        if (!FileUtil.exist(excelPath)) {
            FileUtil.mkdir(excelPath);
        }
        String fileName = excelPath + "/推送名单.xlsx";
        EasyExcel.write(fileName, ExcelGenerateDemoData.class).sheet("优惠券推送列表").doWrite(data());
    }


    private List<ExcelGenerateDemoData> data() {
        List<ExcelGenerateDemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < writeNum; i++) {
            ExcelGenerateDemoData data = ExcelGenerateDemoData.builder()
                    .mail(faker.number().digits(10) + "@163.com")
                    .phone(faker.phoneNumber().cellPhone())
                    .userId(IdUtil.getSnowflakeNextIdStr())
                    .build();
            list.add(data);
        }
        return list;
    }

    /**
     * 测试数据模型
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ExcelGenerateDemoData {

        @ColumnWidth(30)
        @ExcelProperty("用户ID")
        private String userId;

        @ColumnWidth(20)
        @ExcelProperty("手机号")
        private String phone;

        @ColumnWidth(30)
        @ExcelProperty("邮箱")
        private String mail;
    }

}
