package cn.iocoder.yudao.module.zc.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(100001, "参数配置不存在");
    ErrorCode BRAND_NOT_EXISTS = new ErrorCode(100002, "品牌不存在");
    ErrorCode CURTAIN_NOT_EXISTS = new ErrorCode(100003, "窗帘不存在");
    ErrorCode CURTAIN_STRUCTURE_NOT_EXISTS = new ErrorCode(100005, "窗帘结构不存在");
    ErrorCode CURTAIN_STRUCTURE_ELEMENT_NOT_EXISTS = new ErrorCode(100006, "窗帘结构元素不存在");
    ErrorCode CURTAIN_TEMPLATE_NOT_EXISTS = new ErrorCode(100007, "窗帘模板不存在");
    ErrorCode LOGISTICS_NOT_EXISTS = new ErrorCode(100008, "物流不存在");
    ErrorCode SUPPLIER_NOT_EXISTS = new ErrorCode(100009, "供应商不存在");
    ErrorCode WAREHOUSE_NOT_EXISTS = new ErrorCode(100010, "仓库不存在");
    ErrorCode CURTAIN_PLEAT_RATIO_NOT_EXISTS = new ErrorCode(100011, "窗帘 pleat ratio 不存在");
    ErrorCode PRODUCT_CATEGORY_NOT_EXISTS = new ErrorCode(100012, "产品类别不存在");
    ErrorCode PRODUCT_VERSION_NOT_EXISTS = new ErrorCode(100013, "产品版本不存在");
    ErrorCode PRODUCT_SPEC_NOT_EXISTS = new ErrorCode(100014, "产品规格不存在");
    ErrorCode INVENTORY_RECORD_NOT_EXISTS = new ErrorCode(100015, "盘点记录不存在");
    ErrorCode CUSTOMER_PRODUCT_PRICE_NOT_EXISTS = new ErrorCode(100016, "客户产品销售授权价不存在");
    ErrorCode PRODUCT_BATCH_NOT_EXISTS = new ErrorCode(100017, "产品批次不存在");
    ErrorCode PRODUCT_NOT_EXISTS = new ErrorCode(100018, "产品不存在");
    ErrorCode PRODUCT_HAS_BATCH = new ErrorCode(100019, "产品存在批次记录，禁止删除");
    ErrorCode CURTAIN_INSTALL_PROCESS_NOT_EXISTS = new ErrorCode(100020, "安装工艺不存在");
    ErrorCode SALES_ORDER_NOT_EXISTS = new ErrorCode(100021, "销售订单不存在");
    ErrorCode SALES_ORDER_STRUCTURE_NOT_EXISTS = new ErrorCode(100022, "成品订单-结构不存在");
    ErrorCode ZC_SALES_ORDER_MATERIAL_NOT_EXISTS = new ErrorCode(100023, "成品订单-用料明细不存在");
    ErrorCode SALES_ORDER_CURTAIN_NOT_EXISTS = new ErrorCode(100024, "成品订单-窗帘行不存在");
    /**
     * 确认订单时，订单状态不是待确认
     */
    ErrorCode SALES_ORDER_STATUS_NOT_UNCONFIRMED = new ErrorCode(100025, "订单状态不是待确认，无法执行确认操作");
    /**
     * 取消确认时，订单状态不是已确认
     */
    ErrorCode SALES_ORDER_STATUS_NOT_CONFIRMED = new ErrorCode(100026, "订单状态不是已确认，无法取消确认");
    ErrorCode BILLS_NOT_EXISTS = new ErrorCode(100027, "收支账单不存在");
    ErrorCode BILL_METHODS_NOT_EXISTS = new ErrorCode(100028, "收款方式不存在");
    /** 系统内置收款方式（group=0）不允许编辑 */
    ErrorCode BILL_METHODS_SYSTEM_CANNOT_MODIFY = new ErrorCode(100079, "系统内置收款方式不允许编辑");
    /** 收款方式名称已存在 */
    ErrorCode BILL_METHODS_NAME_EXISTS = new ErrorCode(100080, "收款方式名称已存在");
    ErrorCode PROCESS_NODE_NOT_EXISTS = new ErrorCode(100029, "工序节点配置不存在");
    ErrorCode ORDER_PROCESS_RECORD_NOT_EXISTS = new ErrorCode(100030, "工序记录不存在");
    /** 员工未绑定该工序节点，无权操作 */
    ErrorCode USER_PROCESS_NODE_NOT_AUTHORIZED = new ErrorCode(100031, "您没有操作该工序节点的权限，请联系管理员分配");
    /** 订单不处于生产流程中（非 pending/processing），不能新增工序记录 */
    ErrorCode SALES_ORDER_STATUS_CANNOT_PROCESS = new ErrorCode(100032, "订单不在生产流程中，无法新增工序记录");
    /** 工序记录已完成，需先撤销才能删除，防止历史数据被篡改 */
    ErrorCode ORDER_PROCESS_RECORD_ALREADY_COMPLETED = new ErrorCode(100033, "工序记录已完成，请先撤销后再删除");
    /** 工序记录已撤销，不允许重复撤销 */
    ErrorCode ORDER_PROCESS_RECORD_ALREADY_REVOKED = new ErrorCode(100075, "工序记录已撤销，不允许重复撤销");
    /** 该工序节点已执行过，不允许重复记录 */
    ErrorCode ORDER_PROCESS_RECORD_NODE_DUPLICATED = new ErrorCode(100076, "该工序节点已执行过，不允许重复记录");
    /**
     * 取消确认时，订单已存在收款记录，不允许取消：否则客户余额会凭空增加
     */
    ErrorCode SALES_ORDER_HAS_RECEIVED_AMOUNT = new ErrorCode(100034, "订单已有收款记录，取消确认将导致账目异常，请先撤销相关收款单");
    /**
     * 创建/更新收款单时，分摊到各订单的金额合计与实收+优惠不一致
     */
    ErrorCode BILL_ALLOCATED_AMOUNT_NOT_MATCH = new ErrorCode(100035, "分摊到各订单的金额合计与实收金额+优惠金额不一致，请检查后重试");
    /** 实收金额+优惠金额不能小于订单分摊金额合计 */
    ErrorCode BILL_ACTUAL_AMOUNT_LESS_THAN_ALLOCATED = new ErrorCode(100081, "实收金额与优惠金额合计不能小于订单分摊金额合计");
    /**
     * 删除客户时，该客户下存在销售订单，禁止删除
     */
    ErrorCode CUSTOMER_HAS_ORDERS = new ErrorCode(100036, "该客户存在销售订单，禁止删除");
    /**
     * 删除客户时，该客户下存在收支账单，禁止删除
     */
    ErrorCode CUSTOMER_HAS_BILLS = new ErrorCode(100037, "该客户存在收支账单，禁止删除");
    /**
     * 删除产品版本时，该版本下存在绑定产品，禁止删除
     */
    ErrorCode PRODUCT_VERSION_HAS_PRODUCTS = new ErrorCode(100038, "该产品版本下存在绑定产品，禁止删除");
    /**
     * 删除产品批次时，该批次已被订单用料明细引用，禁止删除
     */
    ErrorCode PRODUCT_BATCH_HAS_ORDER_MATERIALS = new ErrorCode(100039, "该批次已被订单引用，禁止删除");
    /**
     * 产品类销售订单行不存在
     */
    ErrorCode SALES_ORDER_PRODUCT_NOT_EXISTS = new ErrorCode(100040, "销售订单产品行不存在");
    /**
     * 已确认的订单禁止删除（confirm_time 不为空）
     */
    ErrorCode SALES_ORDER_CONFIRMED_CANNOT_DELETE = new ErrorCode(100041, "已确认的订单禁止删除");
    /**
     * 产品版本名称已存在（创建/更新时唯一性校验）
     */
    ErrorCode PRODUCT_VERSION_NAME_EXISTS = new ErrorCode(100042, "产品版本名称已存在");
    /**
     * 产品名称已存在（创建/更新时唯一性校验）
     */
    ErrorCode PRODUCT_NAME_EXISTS = new ErrorCode(100043, "产品名称已存在");
    /**
     * 产品规格名称已存在（创建/更新时唯一性校验）
     */
    ErrorCode PRODUCT_SPEC_VALUE_EXISTS = new ErrorCode(100044, "产品规格名称已存在");
    /**
     * 产品类别名称已存在（创建/更新时唯一性校验）
     */
    ErrorCode PRODUCT_CATEGORY_VALUE_EXISTS = new ErrorCode(100045, "产品类别名称已存在");
    /** 仓库名称已存在 */
    ErrorCode WAREHOUSE_NAME_EXISTS = new ErrorCode(100046, "仓库名称已存在");
    /** 物流公司名称已存在 */
    ErrorCode LOGISTICS_NAME_EXISTS = new ErrorCode(100047, "物流公司名称已存在");
    /** 供应商简称已存在 */
    ErrorCode SUPPLIER_SHORT_NAME_EXISTS = new ErrorCode(100048, "供应商简称已存在");
    /** 品牌名称已存在 */
    ErrorCode BRAND_NAME_EXISTS = new ErrorCode(100049, "品牌名称已存在");
    /** 客户简称已存在 */
    ErrorCode CUSTOMER_SHORT_NAME_EXISTS = new ErrorCode(100050, "客户简称已存在");
    /** 褶倍值已存在 */
    ErrorCode CURTAIN_PLEAT_RATIO_VALUE_EXISTS = new ErrorCode(100051, "褶倍值已存在");
    /** 安装工艺名称已存在 */
    ErrorCode CURTAIN_INSTALL_PROCESS_NAME_EXISTS = new ErrorCode(100052, "安装工艺名称已存在");
    /** 窗帘结构名称已存在 */
    ErrorCode CURTAIN_STRUCTURE_NAME_EXISTS = new ErrorCode(100053, "窗帘结构名称已存在");
    /** 窗帘组件名称已存在 */
    ErrorCode CURTAIN_STRUCTURE_ELEMENT_NAME_EXISTS = new ErrorCode(100054, "窗帘组件名称已存在");
    /** 窗帘款式名称已存在 */
    ErrorCode CURTAIN_NAME_EXISTS = new ErrorCode(100055, "窗帘款式名称已存在");
    /**
     * 删除窗帘结构组件时，该组件已被窗帘模板配置引用，禁止删除
     */
    ErrorCode CURTAIN_STRUCTURE_ELEMENT_HAS_TEMPLATE = new ErrorCode(100056, "该组件已在窗帘模板中配置，禁止删除");
    ErrorCode CUSTOMER_BALANCE_LOG_NOT_EXISTS = new ErrorCode(100057, "客户余额变动流水不存在");
    /**
     * 已确认的订单禁止修改（confirm_time 不为空）
     */
    ErrorCode SALES_ORDER_CONFIRMED_CANNOT_UPDATE = new ErrorCode(100058, "已确认的订单禁止修改");
    ErrorCode WORKSHOP_USER_NOT_EXISTS = new ErrorCode(100059, "车间员工不存在");
    /** 产品批次库存不足，无法完成裁剪操作 */
    ErrorCode PRODUCT_BATCH_INSUFFICIENT_QUANTITY = new ErrorCode(100060, "产品批次库存不足，无法裁剪");
    /** 用料明细状态不是已配料，无法撤销裁剪 */
    ErrorCode SALES_ORDER_MATERIAL_NOT_PEILIAO = new ErrorCode(100061, "该用料明细尚未配料，无需撤销");
    /** 用料明细已裁剪（HAVE_PEILIAO），禁止重复裁剪 */
    ErrorCode SALES_ORDER_MATERIAL_ALREADY_CUT = new ErrorCode(100082, "该用料明细已裁剪，请先撤销裁剪后再操作");
    /**
     * 取消确认时，订单下存在已裁剪（HAVE_PEILIAO）的用料明细，禁止取消：库存已出库，须先逐条撤销裁剪
     */
    ErrorCode SALES_ORDER_HAS_CUT_MATERIAL = new ErrorCode(100062, "订单存在已裁剪的用料，请先撤销全部裁剪后再取消确认");
    /** 打包时，该窗帘行已存在未撤销的打包记录 */
    ErrorCode SALES_ORDER_CURTAIN_ALREADY_PACKED = new ErrorCode(100063, "该窗帘已打包");
    /** 发货时，该窗帘行已存在未撤销的发货记录 */
    ErrorCode SALES_ORDER_CURTAIN_ALREADY_SHIPPED = new ErrorCode(100064, "该窗帘已发货");
    /** 取消打包时，该窗帘行尚未打包 */
    ErrorCode SALES_ORDER_CURTAIN_NOT_PACKED = new ErrorCode(100065, "该窗帘尚未打包，无需取消");
    /** 取消发货时，该窗帘行尚未发货 */
    ErrorCode SALES_ORDER_CURTAIN_NOT_SHIPPED = new ErrorCode(100066, "该窗帘尚未发货，无需取消");
    /** 面料单产品行尚未配料（cutStatus != HAVE_PEILIAO），无法撤销裁剪 */
    ErrorCode SALES_ORDER_PRODUCT_NOT_PEILIAO = new ErrorCode(100067, "该产品行尚未完成裁剪，无需撤销");
    /** 面料单产品行已发货（shipTime 不为 null），禁止重复发货 */
    ErrorCode SALES_ORDER_PRODUCT_ALREADY_SHIPPED = new ErrorCode(100068, "该产品行已发货，请勿重复操作");
    /** 面料单产品行尚未发货（shipTime 为 null），无需撤销 */
    ErrorCode SALES_ORDER_PRODUCT_NOT_SHIPPED = new ErrorCode(100069, "该产品行尚未发货，无需撤销");
    /**
     * 完成订单时，订单处于未确认状态，无法执行完成操作
     */
    ErrorCode SALES_ORDER_UNCONFIRMED_CANNOT_COMPLETE = new ErrorCode(100070, "未确认的订单无法标记为完成");
    /**
     * 完成订单时，订单已经是完成状态，禁止重复操作
     */
    ErrorCode SALES_ORDER_ALREADY_COMPLETE = new ErrorCode(100071, "订单已完成，请勿重复操作");
    /** 系统内置工序节点（group=0）不允许编辑或删除 */
    ErrorCode PROCESS_NODE_SYSTEM_CANNOT_MODIFY = new ErrorCode(100072, "系统内置工序节点不允许编辑或删除");
    /** 工序节点名称已存在 */
    ErrorCode PROCESS_NODE_NAME_EXISTS = new ErrorCode(100073, "工序节点名称已存在，请使用其他名称");
    /** 码注册记录不存在 */
    ErrorCode BARCODE_REGISTRY_NOT_EXISTS = new ErrorCode(100074, "码注册记录不存在");
    /**
     * 整单更新时，待删除的用料明细中存在已裁剪（HAVE_PEILIAO）行，禁止删除；
     * 需先逐条撤销裁剪归还库存，才能从订单中移除该行
     */
    ErrorCode SALES_ORDER_MATERIAL_CANNOT_DELETE_WHEN_CUT = new ErrorCode(100077, "存在已裁剪的用料明细，请先撤销裁剪后再保存订单");
    /** 导入客户时，传入的列表为空 */
    ErrorCode CUSTOMER_IMPORT_LIST_IS_EMPTY = new ErrorCode(100078, "导入客户列表不能为空");
    /** 工序记录定位 ID 与订单层级不一致 */
    ErrorCode ORDER_PROCESS_RECORD_SCOPE_MISMATCH = new ErrorCode(100083, "工序记录定位 ID 层级不一致，请检查窗帘/结构/用料关联");
    /** 废弃的订单禁止查看 */
    ErrorCode SALES_ORDER_DISCARDED_CANNOT_VIEW = new ErrorCode(100084, "废弃订单禁止查看");
    /** 订单已被废弃，禁止重复废弃操作 */
    ErrorCode SALES_ORDER_ALREADY_DISCARDED = new ErrorCode(100085, "订单已废弃，请勿重复操作");
}

