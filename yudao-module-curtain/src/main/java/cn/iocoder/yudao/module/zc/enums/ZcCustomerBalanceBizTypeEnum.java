package cn.iocoder.yudao.module.zc.enums;

import lombok.Getter;

/**
 * 客户余额变动业务类型枚举
 *
 * <p>对应字典类型 {@code zc_customer_balance_biz_type}</p>
 */
@Getter
public enum ZcCustomerBalanceBizTypeEnum {

    /** 订单确认扣减：订单确认时从客户余额扣除订单金额 */
    ORDER_CONFIRM("订单确认扣减"),

    /** 取消确认回退：撤销订单确认时退回余额 */
    ORDER_UNCONFIRM("取消确认回退"),

    /** 订单废弃退回：订单废弃时退回未支付的已扣减余额 */
    ORDER_DISCARD("订单废弃退回"),

    /** 订单更新调整：订单金额变更时补差额 */
    ORDER_CHANGE("订单更新调整"),

    /** 收款入账：收款单创建，客户余额增加 */
    COLLECTION("收款入账"),

    /** 收款作废冲回：收款单删除/作废，冲回已增加的余额 */
    COLLECTION_VOID("收款作废冲回"),

    /** 手工调整：后台人工直接调整余额 */
    MANUAL_ADJUST("手工调整"),

    /** 其他：不属于以上类型的余额变动 */
    OTHER("其他");

    /** 中文名称 */
    private final String label;

    ZcCustomerBalanceBizTypeEnum(String label) {
        this.label = label;
    }

}
