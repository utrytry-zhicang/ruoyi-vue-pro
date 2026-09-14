package cn.iocoder.yudao.module.zc.enums;

import lombok.Getter;

/**
 * 销售订单状态枚举
 *
 * <p>对应字典类型 {@code zc_order_status}</p>
 */
@Getter
public enum ZcSalesOrderStatusEnum {

    /** 未确认：订单刚创建，尚未审核 */
    UNCONFIRMED("未确认"),

    /** 已确认：订单已审核确认，进入生产流程 */
    CONFIRMED("已确认"),

    /** 未配料：窗帘行确认后初始状态，尚未开始裁剪 */
    NOT_PEILIAO("未配料"),

    /** 部分配料：部分用料明细已完成裁剪出库 */
    BUFEN_PEILIAO("部分配料"),

    /** 已配料：全部用料明细已完成裁剪出库 */
    HAVE_PEILIAO("已配料"),

    /** 部分打包：部分窗帘行已完成打包 */
    BUFEN_DABAO("部分打包"),

    /** 已打包：生产完成，已完成打包备货 */
    DABAO("已打包"),

    /** 部分发货：部分窗帘行/产品行已发货 */
    BUFEN_FAHUO("部分发货"),

    /** 已发货：货物已发出，等待客户签收 */
    FAHUO("已发货"),

    /** 完成：订单履约结束 */
    COMPLETE("完成"),

    /** 废弃：订单标记废弃，终止履约且禁止查看 */
    DISCARDED("废弃");

    /** 中文名称 */
    private final String label;

    ZcSalesOrderStatusEnum(String label) {
        this.label = label;
    }

}
