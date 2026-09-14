package cn.iocoder.yudao.module.zc.enums;

/**
 * ZC 智仓操作日志常量
 *
 * <p>统一管理操作日志 type、subType、success 模板，避免 Service 层散落魔法字符串</p>
 *
 * @author 芋道源码
 */
public interface LogRecordConstants {

    // ======================= ZC_BRAND 品牌 =======================

    String ZC_BRAND_TYPE = "ZC 品牌";
    String ZC_BRAND_CREATE_SUB_TYPE = "创建品牌";
    String ZC_BRAND_CREATE_SUCCESS = "创建了品牌【{{#brand.name}}】";
    String ZC_BRAND_UPDATE_SUB_TYPE = "更新品牌";
    String ZC_BRAND_UPDATE_SUCCESS = "更新了品牌【{{#brandName}}】: {_DIFF{#updateReqVO}}";
    String ZC_BRAND_DELETE_SUB_TYPE = "删除品牌";
    String ZC_BRAND_DELETE_SUCCESS = "删除了品牌【{{#brandName}}】";

    // ======================= ZC_SUPPLIER 供应商 =======================

    String ZC_SUPPLIER_TYPE = "ZC 供应商";
    String ZC_SUPPLIER_CREATE_SUB_TYPE = "创建供应商";
    String ZC_SUPPLIER_CREATE_SUCCESS = "创建了供应商【{{#supplier.shortName}}】";
    String ZC_SUPPLIER_UPDATE_SUB_TYPE = "更新供应商";
    String ZC_SUPPLIER_UPDATE_SUCCESS = "更新了供应商【{{#supplierName}}】: {_DIFF{#updateReqVO}}";
    String ZC_SUPPLIER_DELETE_SUB_TYPE = "删除供应商";
    String ZC_SUPPLIER_DELETE_SUCCESS = "删除了供应商【{{#supplierName}}】";

    // ======================= ZC_WAREHOUSE 仓库 =======================

    String ZC_WAREHOUSE_TYPE = "ZC 仓库";
    String ZC_WAREHOUSE_CREATE_SUB_TYPE = "创建仓库";
    String ZC_WAREHOUSE_CREATE_SUCCESS = "创建了仓库【{{#warehouse.name}}】";
    String ZC_WAREHOUSE_UPDATE_SUB_TYPE = "更新仓库";
    String ZC_WAREHOUSE_UPDATE_SUCCESS = "更新了仓库【{{#warehouseName}}】: {_DIFF{#updateReqVO}}";
    String ZC_WAREHOUSE_DELETE_SUB_TYPE = "删除仓库";
    String ZC_WAREHOUSE_DELETE_SUCCESS = "删除了仓库【{{#warehouseName}}】";

    // ======================= ZC_LOGISTICS 物流公司 =======================

    String ZC_LOGISTICS_TYPE = "ZC 物流公司";
    String ZC_LOGISTICS_CREATE_SUB_TYPE = "创建物流公司";
    String ZC_LOGISTICS_CREATE_SUCCESS = "创建了物流公司【{{#logistics.name}}】";
    String ZC_LOGISTICS_UPDATE_SUB_TYPE = "更新物流公司";
    String ZC_LOGISTICS_UPDATE_SUCCESS = "更新了物流公司【{{#logisticsName}}】: {_DIFF{#updateReqVO}}";
    String ZC_LOGISTICS_DELETE_SUB_TYPE = "删除物流公司";
    String ZC_LOGISTICS_DELETE_SUCCESS = "删除了物流公司【{{#logisticsName}}】";

    // ======================= ZC_PRODUCT 产品 =======================

    String ZC_PRODUCT_TYPE = "ZC 产品";
    String ZC_PRODUCT_CREATE_SUB_TYPE = "创建产品";
    String ZC_PRODUCT_CREATE_SUCCESS = "创建了产品【{{#product.name}}】";
    String ZC_PRODUCT_UPDATE_SUB_TYPE = "更新产品";
    String ZC_PRODUCT_UPDATE_SUCCESS = "更新了产品【{{#productName}}】: {_DIFF{#updateReqVO}}";
    String ZC_PRODUCT_DELETE_SUB_TYPE = "删除产品";
    String ZC_PRODUCT_DELETE_SUCCESS = "删除了产品【{{#productName}}】";

    // ======================= ZC_CUSTOMER_VERSION_SPEC_PRICE 客户版本销售授权价 =======================

    String ZC_CUSTOMER_VERSION_SPEC_PRICE_TYPE = "ZC 客户版本授权价";
    String ZC_CUSTOMER_VERSION_SPEC_PRICE_BATCH_SAVE_SUB_TYPE = "批量保存客户版本授权价";
    String ZC_CUSTOMER_VERSION_SPEC_PRICE_BATCH_SAVE_SUCCESS = "批量保存了客户版本规格授权价，共 {{#saveReqVOList.size()}} 条";

    // ======================= ZC_PRODUCT_VERSION 产品版本 =======================

    String ZC_PRODUCT_VERSION_TYPE = "ZC 产品版本";
    String ZC_PRODUCT_VERSION_CREATE_SUB_TYPE = "创建产品版本";
    String ZC_PRODUCT_VERSION_CREATE_SUCCESS = "创建了产品版本【{{#productVersion.name}}】";
    String ZC_PRODUCT_VERSION_UPDATE_SUB_TYPE = "更新产品版本";
    String ZC_PRODUCT_VERSION_UPDATE_SUCCESS = "更新了产品版本【{{#productVersionName}}】: {_DIFF{#updateReqVO}}";
    String ZC_PRODUCT_VERSION_DELETE_SUB_TYPE = "删除产品版本";
    String ZC_PRODUCT_VERSION_DELETE_SUCCESS = "删除了产品版本【{{#productVersionName}}】";

    // ======================= ZC_PRODUCT_CATEGORY 产品分类 =======================

    String ZC_PRODUCT_CATEGORY_TYPE = "ZC 产品分类";
    String ZC_PRODUCT_CATEGORY_CREATE_SUB_TYPE = "创建产品分类";
    String ZC_PRODUCT_CATEGORY_CREATE_SUCCESS = "创建了产品分类【{{#productCategory.value}}】";
    String ZC_PRODUCT_CATEGORY_UPDATE_SUB_TYPE = "更新产品分类";
    String ZC_PRODUCT_CATEGORY_UPDATE_SUCCESS = "更新了产品分类【{{#productCategoryName}}】: {_DIFF{#updateReqVO}}";
    String ZC_PRODUCT_CATEGORY_DELETE_SUB_TYPE = "删除产品分类";
    String ZC_PRODUCT_CATEGORY_DELETE_SUCCESS = "删除了产品分类【{{#productCategoryName}}】";

    // ======================= ZC_PRODUCT_SPEC 产品规格 =======================

    String ZC_PRODUCT_SPEC_TYPE = "ZC 产品规格";
    String ZC_PRODUCT_SPEC_CREATE_SUB_TYPE = "创建产品规格";
    String ZC_PRODUCT_SPEC_CREATE_SUCCESS = "创建了产品规格【{{#productSpec.value}}】";
    String ZC_PRODUCT_SPEC_UPDATE_SUB_TYPE = "更新产品规格";
    String ZC_PRODUCT_SPEC_UPDATE_SUCCESS = "更新了产品规格【{{#productSpecName}}】: {_DIFF{#updateReqVO}}";
    String ZC_PRODUCT_SPEC_DELETE_SUB_TYPE = "删除产品规格";
    String ZC_PRODUCT_SPEC_DELETE_SUCCESS = "删除了产品规格【{{#productSpecName}}】";

    // ======================= ZC_PRODUCT_BATCH 产品批次 =======================

    String ZC_PRODUCT_BATCH_TYPE = "ZC 产品批次";
    String ZC_PRODUCT_BATCH_CREATE_SUB_TYPE = "创建产品批次";
    String ZC_PRODUCT_BATCH_CREATE_SUCCESS = "创建了产品批次【{{#productBatch.batchNo}}】";
    String ZC_PRODUCT_BATCH_UPDATE_SUB_TYPE = "更新产品批次";
    String ZC_PRODUCT_BATCH_UPDATE_SUCCESS = "更新了产品批次【{{#batchNo}}】: {_DIFF{#updateReqVO}}";
    String ZC_PRODUCT_BATCH_DELETE_SUB_TYPE = "删除产品批次";
    String ZC_PRODUCT_BATCH_DELETE_SUCCESS = "删除了产品批次【{{#batchNo}}】";

    // ======================= ZC_INVENTORY_RECORD 库存盘点 =======================

    String ZC_INVENTORY_RECORD_TYPE = "ZC 库存盘点";
    String ZC_INVENTORY_RECORD_CREATE_SUB_TYPE = "创建库存盘点";
    String ZC_INVENTORY_RECORD_CREATE_SUCCESS = "创建了库存盘点记录，批次【{{#batchNo}}】";

    // ======================= ZC_CUSTOMER 客户 =======================

    String ZC_CUSTOMER_TYPE = "ZC 客户";
    String ZC_CUSTOMER_CREATE_SUB_TYPE = "创建客户";
    String ZC_CUSTOMER_CREATE_SUCCESS = "创建了客户【{{#customer.shortName}}】";
    String ZC_CUSTOMER_UPDATE_SUB_TYPE = "更新客户";
    String ZC_CUSTOMER_UPDATE_SUCCESS = "更新了客户【{{#customerName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CUSTOMER_DELETE_SUB_TYPE = "删除客户";
    String ZC_CUSTOMER_DELETE_SUCCESS = "删除了客户【{{#customerName}}】";

    // ======================= ZC_CUSTOMER_PRODUCT_PRICE 客户专项定价 =======================

    String ZC_CUSTOMER_PRODUCT_PRICE_TYPE = "ZC 客户专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_CREATE_SUB_TYPE = "创建客户专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_CREATE_SUCCESS = "创建了客户【{getZcCustomerById{#customerProductPrice.customerId}}】的产品专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_BATCH_CREATE_SUB_TYPE = "批量创建客户专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_BATCH_CREATE_SUCCESS = "批量创建了客户【{getZcCustomerById{#customerId}}】的产品专项定价，共 {{#count}} 条";
    String ZC_CUSTOMER_PRODUCT_PRICE_UPDATE_SUB_TYPE = "更新客户专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_UPDATE_SUCCESS = "更新了客户专项定价: {_DIFF{#updateReqVO}}";
    String ZC_CUSTOMER_PRODUCT_PRICE_DELETE_SUB_TYPE = "删除客户专项定价";
    String ZC_CUSTOMER_PRODUCT_PRICE_DELETE_SUCCESS = "删除了客户专项定价【{{#priceId}}】";

    // ======================= ZC_CURTAIN 窗帘款式 =======================

    String ZC_CURTAIN_TYPE = "ZC 窗帘款式";
    String ZC_CURTAIN_CREATE_SUB_TYPE = "创建窗帘款式";
    String ZC_CURTAIN_CREATE_SUCCESS = "创建了窗帘款式【{{#curtain.name}}】";
    String ZC_CURTAIN_UPDATE_SUB_TYPE = "更新窗帘款式";
    String ZC_CURTAIN_UPDATE_SUCCESS = "更新了窗帘款式【{{#curtainName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CURTAIN_DELETE_SUB_TYPE = "删除窗帘款式";
    String ZC_CURTAIN_DELETE_SUCCESS = "删除了窗帘款式【{{#curtainName}}】";

    // ======================= ZC_CURTAIN_STRUCTURE 窗帘结构 =======================

    String ZC_CURTAIN_STRUCTURE_TYPE = "ZC 窗帘结构";
    String ZC_CURTAIN_STRUCTURE_CREATE_SUB_TYPE = "创建窗帘结构";
    String ZC_CURTAIN_STRUCTURE_CREATE_SUCCESS = "创建了窗帘结构【{{#curtainStructure.name}}】";
    String ZC_CURTAIN_STRUCTURE_UPDATE_SUB_TYPE = "更新窗帘结构";
    String ZC_CURTAIN_STRUCTURE_UPDATE_SUCCESS = "更新了窗帘结构【{{#curtainStructureName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CURTAIN_STRUCTURE_DELETE_SUB_TYPE = "删除窗帘结构";
    String ZC_CURTAIN_STRUCTURE_DELETE_SUCCESS = "删除了窗帘结构【{{#curtainStructureName}}】";

    // ======================= ZC_CURTAIN_STRUCTURE_ELEMENT 窗帘组件 =======================

    String ZC_CURTAIN_STRUCTURE_ELEMENT_TYPE = "ZC 窗帘组件";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_CREATE_SUB_TYPE = "创建窗帘组件";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_CREATE_SUCCESS = "创建了窗帘组件【{{#element.name}}】";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_UPDATE_SUB_TYPE = "更新窗帘组件";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_UPDATE_SUCCESS = "更新了窗帘组件【{{#elementName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_DELETE_SUB_TYPE = "删除窗帘组件";
    String ZC_CURTAIN_STRUCTURE_ELEMENT_DELETE_SUCCESS = "删除了窗帘组件【{{#elementName}}】";

    // ======================= ZC_CURTAIN_PLEAT_RATIO 褶倍配置 =======================

    String ZC_CURTAIN_PLEAT_RATIO_TYPE = "ZC 褶倍配置";
    String ZC_CURTAIN_PLEAT_RATIO_CREATE_SUB_TYPE = "创建褶倍配置";
    String ZC_CURTAIN_PLEAT_RATIO_CREATE_SUCCESS = "创建了褶倍配置【{{#pleatRatio.value}}】";
    String ZC_CURTAIN_PLEAT_RATIO_UPDATE_SUB_TYPE = "更新褶倍配置";
    String ZC_CURTAIN_PLEAT_RATIO_UPDATE_SUCCESS = "更新了褶倍配置【{{#pleatRatioName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CURTAIN_PLEAT_RATIO_DELETE_SUB_TYPE = "删除褶倍配置";
    String ZC_CURTAIN_PLEAT_RATIO_DELETE_SUCCESS = "删除了褶倍配置【{{#pleatRatioName}}】";

    // ======================= ZC_CURTAIN_INSTALL_PROCESS 安装工艺 =======================

    String ZC_CURTAIN_INSTALL_PROCESS_TYPE = "ZC 安装工艺";
    String ZC_CURTAIN_INSTALL_PROCESS_CREATE_SUB_TYPE = "创建安装工艺";
    String ZC_CURTAIN_INSTALL_PROCESS_CREATE_SUCCESS = "创建了安装工艺【{{#installProcess.name}}】";
    String ZC_CURTAIN_INSTALL_PROCESS_UPDATE_SUB_TYPE = "更新安装工艺";
    String ZC_CURTAIN_INSTALL_PROCESS_UPDATE_SUCCESS = "更新了安装工艺【{{#installProcessName}}】: {_DIFF{#updateReqVO}}";
    String ZC_CURTAIN_INSTALL_PROCESS_DELETE_SUB_TYPE = "删除安装工艺";
    String ZC_CURTAIN_INSTALL_PROCESS_DELETE_SUCCESS = "删除了安装工艺【{{#installProcessName}}】";

    // ======================= ZC_CURTAIN_TEMPLATE 窗帘工艺模板 =======================

    String ZC_CURTAIN_TEMPLATE_TYPE = "ZC 窗帘工艺模板";
    String ZC_CURTAIN_TEMPLATE_SAVE_SUB_TYPE = "保存窗帘工艺模板";
    String ZC_CURTAIN_TEMPLATE_SAVE_SUCCESS = "保存了窗帘【{getZcCurtainById{#curtainId}}】的工艺模板";

    // ======================= ZC_SALES_ORDER 销售订单（成品） =======================

    String ZC_SALES_ORDER_TYPE = "ZC 销售订单";
    String ZC_SALES_ORDER_CREATE_SUB_TYPE = "创建销售订单";
    String ZC_SALES_ORDER_CREATE_SUCCESS = "创建了销售订单【{{#salesOrder.orderNo}}】";
    String ZC_SALES_ORDER_UPDATE_SUB_TYPE = "更新销售订单";
    String ZC_SALES_ORDER_UPDATE_SUCCESS = "更新了销售订单【{{#orderNo}}】: {_DIFF{#updateReqVO}}";
    String ZC_SALES_ORDER_DELETE_SUB_TYPE = "删除销售订单";
    String ZC_SALES_ORDER_DELETE_SUCCESS = "删除了销售订单【{{#orderNo}}】";
    String ZC_SALES_ORDER_CONFIRM_SUB_TYPE = "确认销售订单";
    String ZC_SALES_ORDER_CONFIRM_SUCCESS = "确认了销售订单【{{#orderNo}}】";
    String ZC_SALES_ORDER_CANCEL_CONFIRM_SUB_TYPE = "取消确认销售订单";
    String ZC_SALES_ORDER_CANCEL_CONFIRM_SUCCESS = "取消确认了销售订单【{{#orderNo}}】";
    String ZC_SALES_ORDER_MARK_EXPEDITED_SUB_TYPE = "标记加急";
    String ZC_SALES_ORDER_MARK_EXPEDITED_SUCCESS = "将销售订单【{{#orderNo}}】标记为加急";
    String ZC_SALES_ORDER_CANCEL_EXPEDITED_SUB_TYPE = "取消加急";
    String ZC_SALES_ORDER_CANCEL_EXPEDITED_SUCCESS = "取消了销售订单【{{#orderNo}}】的加急标记";
    String ZC_SALES_ORDER_COMPLETE_SUB_TYPE = "完成销售订单";
    String ZC_SALES_ORDER_COMPLETE_SUCCESS = "完成了销售订单【{{#orderNo}}】";
    String ZC_SALES_ORDER_DISCARD_SUB_TYPE = "废弃销售订单";
    String ZC_SALES_ORDER_DISCARD_SUCCESS = "废弃了销售订单【{{#orderNo}}】";
    String ZC_SALES_ORDER_FABRIC_CREATE_SUB_TYPE = "创建面单（嵌套结构）";
    String ZC_SALES_ORDER_FABRIC_CREATE_SUCCESS = "创建了面单【{{#salesOrder.orderNo}}】";
    String ZC_SALES_ORDER_FABRIC_UPDATE_SUB_TYPE = "更新面单（嵌套结构）";
    String ZC_SALES_ORDER_FABRIC_UPDATE_SUCCESS = "更新了面单【{{#orderNo}}】: {_DIFF{#updateReqVO}}";

    // ======================= ZC_SALES_ORDER_PRODUCT 面料单 =======================

    String ZC_SALES_ORDER_PRODUCT_TYPE = "ZC 面料单";
    String ZC_SALES_ORDER_PRODUCT_CREATE_SUB_TYPE = "创建面料单";
    String ZC_SALES_ORDER_PRODUCT_CREATE_SUCCESS = "创建了面料单【{{#salesOrder.orderNo}}】";
    String ZC_SALES_ORDER_PRODUCT_UPDATE_SUB_TYPE = "更新面料单";
    String ZC_SALES_ORDER_PRODUCT_UPDATE_SUCCESS = "更新了面料单【{{#orderNo}}】: {_DIFF{#updateReqVO}}";
    String ZC_SALES_ORDER_PRODUCT_DELETE_SUB_TYPE = "删除面料单";
    String ZC_SALES_ORDER_PRODUCT_DELETE_SUCCESS = "删除了面料单【{{#orderNo}}】";
    String ZC_SALES_ORDER_PRODUCT_CUT_SUB_TYPE = "裁剪面料单产品行";
    String ZC_SALES_ORDER_PRODUCT_CUT_SUCCESS = "裁剪了面料单产品行【{{#reqVO.id}}】，批次【{{#batchNo}}】，裁剪数量 {{#reqVO.cutQuantity}}";
    String ZC_SALES_ORDER_PRODUCT_CANCEL_CUT_SUB_TYPE = "撤销裁剪面料单产品行";
    String ZC_SALES_ORDER_PRODUCT_CANCEL_CUT_SUCCESS = "撤销裁剪了面料单产品行【{{#reqVO.id}}】，批次【{{#batchNo}}】，回退数量 {{#cutQuantity}}";
    String ZC_SALES_ORDER_PRODUCT_SHIP_SUB_TYPE = "发货面料单产品行";
    String ZC_SALES_ORDER_PRODUCT_SHIP_SUCCESS = "将面料单产品行【{{#id}}】标记为已发货，订单状态联动更新为【{{#newOrderStatus}}】";
    String ZC_SALES_ORDER_PRODUCT_CANCEL_SHIP_SUB_TYPE = "取消发货面料单产品行";
    String ZC_SALES_ORDER_PRODUCT_CANCEL_SHIP_SUCCESS = "取消了面料单产品行【{{#id}}】的发货，订单状态联动更新为【{{#newOrderStatus}}】";

    // ======================= ZC_SALES_ORDER_CURTAIN 订单窗帘行 =======================

    String ZC_SALES_ORDER_CURTAIN_TYPE = "ZC 订单窗帘行";
    String ZC_SALES_ORDER_CURTAIN_CREATE_SUB_TYPE = "创建订单窗帘行";
    String ZC_SALES_ORDER_CURTAIN_CREATE_SUCCESS = "创建了订单窗帘行【{{#orderCurtain.id}}】";
    String ZC_SALES_ORDER_CURTAIN_UPDATE_SUB_TYPE = "更新订单窗帘行";
    String ZC_SALES_ORDER_CURTAIN_UPDATE_SUCCESS = "更新了订单窗帘行【{{#orderCurtainId}}】: {_DIFF{#updateReqVO}}";
    String ZC_SALES_ORDER_CURTAIN_DELETE_SUB_TYPE = "删除订单窗帘行";
    String ZC_SALES_ORDER_CURTAIN_DELETE_SUCCESS = "删除了订单窗帘行【{{#orderCurtainId}}】";
    String ZC_SALES_ORDER_CURTAIN_PACK_SUB_TYPE = "打包窗帘行";
    String ZC_SALES_ORDER_CURTAIN_PACK_SUCCESS = "将窗帘行【{{#id}}】标记为已打包，订单状态联动更新为【{{#newOrderStatus}}】";
    String ZC_SALES_ORDER_CURTAIN_CANCEL_PACK_SUB_TYPE = "取消打包窗帘行";
    String ZC_SALES_ORDER_CURTAIN_CANCEL_PACK_SUCCESS = "取消了窗帘行【{{#id}}】的打包，订单状态联动更新为【{{#newOrderStatus}}】";
    String ZC_SALES_ORDER_CURTAIN_SHIP_SUB_TYPE = "发货窗帘行";
    String ZC_SALES_ORDER_CURTAIN_SHIP_SUCCESS = "将窗帘行【{{#id}}】标记为已发货，订单状态联动更新为【{{#newOrderStatus}}】";
    String ZC_SALES_ORDER_CURTAIN_CANCEL_SHIP_SUB_TYPE = "取消发货窗帘行";
    String ZC_SALES_ORDER_CURTAIN_CANCEL_SHIP_SUCCESS = "取消了窗帘行【{{#id}}】的发货，订单状态联动更新为【{{#newOrderStatus}}】";

    // ======================= ZC_SALES_ORDER_STRUCTURE 订单结构行 =======================

    String ZC_SALES_ORDER_STRUCTURE_TYPE = "ZC 订单结构行";
    String ZC_SALES_ORDER_STRUCTURE_CREATE_SUB_TYPE = "创建订单结构行";
    String ZC_SALES_ORDER_STRUCTURE_CREATE_SUCCESS = "创建了订单结构行【{{#orderStructure.id}}】";
    String ZC_SALES_ORDER_STRUCTURE_UPDATE_SUB_TYPE = "更新订单结构行";
    String ZC_SALES_ORDER_STRUCTURE_UPDATE_SUCCESS = "更新了订单结构行【{{#orderStructureId}}】: {_DIFF{#updateReqVO}}";
    String ZC_SALES_ORDER_STRUCTURE_DELETE_SUB_TYPE = "删除订单结构行";
    String ZC_SALES_ORDER_STRUCTURE_DELETE_SUCCESS = "删除了订单结构行【{{#orderStructureId}}】";

    // ======================= ZC_SALES_ORDER_MATERIAL 订单用料明细 =======================

    String ZC_SALES_ORDER_MATERIAL_TYPE = "ZC 订单用料明细";
    String ZC_SALES_ORDER_MATERIAL_CUT_SUB_TYPE = "裁剪用料明细";
    String ZC_SALES_ORDER_MATERIAL_CUT_SUCCESS = "裁剪了用料明细【{{#reqVO.id}}】，批次【{{#batchNo}}】，裁剪数量 {{#reqVO.cutQuantity}}";
    String ZC_SALES_ORDER_MATERIAL_CANCEL_CUT_SUB_TYPE = "撤销裁剪用料明细";
    String ZC_SALES_ORDER_MATERIAL_CANCEL_CUT_SUCCESS = "撤销裁剪了用料明细【{{#materialId}}】，批次【{{#batchNo}}】，回退数量 {{#cutQuantity}}";

    // ======================= ZC_BILLS 收款单 =======================

    String ZC_BILLS_TYPE = "ZC 收款单";
    String ZC_BILLS_CREATE_SUB_TYPE = "创建收款单";
    String ZC_BILLS_CREATE_SUCCESS = "创建了收款单【{{#bill.billNo}}】";
    String ZC_BILLS_UPDATE_SUB_TYPE = "更新收款单";
    String ZC_BILLS_UPDATE_SUCCESS = "更新了收款单【{{#billNo}}】: {_DIFF{#updateReqVO}}";
    String ZC_BILLS_DELETE_SUB_TYPE = "删除收款单";
    String ZC_BILLS_DELETE_SUCCESS = "删除了收款单【{{#billNo}}】";

    // ======================= ZC_BILL_METHODS 收款方式 =======================

    String ZC_BILL_METHODS_TYPE = "ZC 收款方式";
    String ZC_BILL_METHODS_CREATE_SUB_TYPE = "创建收款方式";
    String ZC_BILL_METHODS_CREATE_SUCCESS = "创建了收款方式【{{#billMethod.name}}】";
    String ZC_BILL_METHODS_UPDATE_SUB_TYPE = "更新收款方式";
    String ZC_BILL_METHODS_UPDATE_SUCCESS = "更新了收款方式【{{#billMethodName}}】: {_DIFF{#updateReqVO}}";

    // ======================= ZC_PROCESS_NODE 工序节点 =======================

    String ZC_PROCESS_NODE_TYPE = "ZC 工序节点";
    String ZC_PROCESS_NODE_CREATE_SUB_TYPE = "创建工序节点";
    String ZC_PROCESS_NODE_CREATE_SUCCESS = "创建了工序节点【{{#processNode.name}}】";
    String ZC_PROCESS_NODE_UPDATE_SUB_TYPE = "更新工序节点";
    String ZC_PROCESS_NODE_UPDATE_SUCCESS = "更新了工序节点【{{#processNodeName}}】: {_DIFF{#updateReqVO}}";
    String ZC_PROCESS_NODE_DELETE_SUB_TYPE = "删除工序节点";
    String ZC_PROCESS_NODE_DELETE_SUCCESS = "删除了工序节点【{{#processNodeName}}】";

    // ======================= ZC_ORDER_PROCESS_RECORD 订单工序记录 =======================

    String ZC_ORDER_PROCESS_RECORD_TYPE = "ZC 订单工序记录";
    String ZC_ORDER_PROCESS_RECORD_CREATE_SUB_TYPE = "创建订单工序记录";
    String ZC_ORDER_PROCESS_RECORD_CREATE_SUCCESS = "创建了订单【{{#orderNo}}】的工序记录【{{#nodeName}}】";
    String ZC_ORDER_PROCESS_RECORD_REVOKE_SUB_TYPE = "撤销订单工序记录";
    String ZC_ORDER_PROCESS_RECORD_REVOKE_SUCCESS = "撤销了订单【{{#orderNo}}】的工序记录【{{#nodeName}}】";
    String ZC_ORDER_PROCESS_RECORD_DELETE_SUB_TYPE = "删除订单工序记录";
    String ZC_ORDER_PROCESS_RECORD_DELETE_SUCCESS = "删除了订单工序记录【{{#recordId}}】";

    // ======================= ZC_USER_PROCESS_NODE 员工工序授权 =======================

    String ZC_USER_PROCESS_NODE_TYPE = "ZC 员工工序授权";
    String ZC_USER_PROCESS_NODE_SAVE_SUB_TYPE = "保存员工工序授权";
    String ZC_USER_PROCESS_NODE_SAVE_SUCCESS = "保存了用户【{getAdminUserById{#userId}}】的工序授权，共 {{#nodeCount}} 个节点";

    // ======================= ZC_BARCODE_REGISTRY 码注册表 =======================

    String ZC_BARCODE_REGISTRY_TYPE = "ZC 码注册表";
    String ZC_BARCODE_REGISTRY_CREATE_SUB_TYPE = "生成二维码";
    String ZC_BARCODE_REGISTRY_CREATE_SUCCESS = "生成了【{{#registry.codeType}}】类型的二维码，codeId={{#registry.codeId}}";

}
