package cn.iocoder.yudao.module.zc.controller.admin.salesorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 成品订单-用料明细分页 Request VO")
@Data
public class ZCSalesOrderMaterialPageReqVO extends PageParam {

    @Schema(description = "销售单", example = "19882")
    private Long orderId;

    @Schema(description = "结构行", example = "29364")
    private Long orderStructureId;

    @Schema(description = "客户编号", example = "1024")
    private Long customerId;

    @Schema(description = "产品版本编号", example = "2048")
    private Long versionId;

    @Schema(description = "产品编号", example = "1024")
    private Long productId;

    @Schema(description = "规格", example = "2.8m")
    private String spec;

    @Schema(description = "组件类型编号", example = "1024")
    private Long elementId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}