/*
 * 智仓 / 窗帘业务 — MySQL DDL（适配芋道 ruoyi-vue-pro）
 */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 全量删除（子表 → 父表，可重复执行本脚本）
-- ----------------------------
DROP TABLE IF EXISTS `zc_collection_order_alloc`;
DROP TABLE IF EXISTS `zc_collection_record`;
DROP TABLE IF EXISTS `zc_sales_order_progress_log`;
DROP TABLE IF EXISTS `zc_sales_order_production_queue`;
DROP TABLE IF EXISTS `zc_sales_order_curtain_structure_element`;
DROP TABLE IF EXISTS `zc_sales_order_curtain_structure`;
DROP TABLE IF EXISTS `zc_sales_order_curtain`;
DROP TABLE IF EXISTS `zc_sales_order_product`;
DROP TABLE IF EXISTS `zc_sales_order`;
DROP TABLE IF EXISTS `zc_progress_definition`;
DROP TABLE IF EXISTS `zc_inventory_record`;
DROP TABLE IF EXISTS `zc_product_batch`;
DROP TABLE IF EXISTS `zc_purchase_order`;
DROP TABLE IF EXISTS `zc_customer_product_sales_authorization`;
DROP TABLE IF EXISTS `zc_customer_version_sales_authorization`;
DROP TABLE IF EXISTS `zc_product`;
DROP TABLE IF EXISTS `zc_product_version`;
DROP TABLE IF EXISTS `zc_curtain_template`;
DROP TABLE IF EXISTS `zc_curtain_structure_element`;
DROP TABLE IF EXISTS `zc_curtain_structure`;
DROP TABLE IF EXISTS `zc_curtain`;
DROP TABLE IF EXISTS `zc_curtain_pleat_ratio`;
DROP TABLE IF EXISTS `zc_curtain_install_process`;
DROP TABLE IF EXISTS `zc_curtain_series`;
DROP TABLE IF EXISTS `zc_product_unit`;
DROP TABLE IF EXISTS `zc_product_spec`;
DROP TABLE IF EXISTS `zc_product_category`;
DROP TABLE IF EXISTS `zc_customer_balance_log`;
DROP TABLE IF EXISTS `zc_customer`;
DROP TABLE IF EXISTS `zc_bill_methods`;
DROP TABLE IF EXISTS `zc_warehouse`;
DROP TABLE IF EXISTS `zc_supplier`;
DROP TABLE IF EXISTS `zc_brand`;
DROP TABLE IF EXISTS `zc_logistics`;

-- ----------------------------
-- 1. 基础资料
-- ----------------------------

DROP TABLE IF EXISTS `zc_logistics`;
CREATE TABLE `zc_logistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '编码，例如：shunfeng',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '名称，例如：顺丰快递',
  `contact_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_code` (`tenant_id`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流公司';

DROP TABLE IF EXISTS `zc_brand`;
CREATE TABLE `zc_brand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
  `logo` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Logo URL',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌';

DROP TABLE IF EXISTS `zc_customer`;
CREATE TABLE `zc_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `short_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '简称',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '全称',
  `contact_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '联系人',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '固定地址',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '市区',
  `district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '县区',
  `delivery_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '送货地址',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '手机',
  `mobile2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `logistic_id` bigint NULL DEFAULT NULL COMMENT '默认物流 zc_logistics.id',
  `brand_id` bigint NULL DEFAULT NULL COMMENT '关联品牌 zc_brand.id',
  `balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当前账户余额（业务变更必须同步写入 zc_customer_balance_log）',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_brand` (`tenant_id`,`brand_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户资料';

DROP TABLE IF EXISTS `zc_customer_balance_log`;
CREATE TABLE `zc_customer_balance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint NOT NULL COMMENT '客户 zc_customer.id',
  `change_amount` decimal(20, 2) NOT NULL COMMENT '余额变动额（正数增加、负数减少）',
  `balance_before` decimal(20, 2) NOT NULL COMMENT '变动前余额',
  `balance_after` decimal(20, 2) NOT NULL COMMENT '变动后余额',
  `biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务类型',
  `ref_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联单据类型',
  `ref_id` bigint NULL DEFAULT NULL COMMENT '关联单据主键',
  `ref_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联单号快照',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_time` (`tenant_id`,`customer_id`,`create_time`) USING BTREE,
  KEY `idx_ref` (`tenant_id`,`ref_type`,`ref_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户余额变动流水';

DROP TABLE IF EXISTS `zc_supplier`;
CREATE TABLE `zc_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `short_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '简称',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '全称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商';

DROP TABLE IF EXISTS `zc_warehouse`;
CREATE TABLE `zc_warehouse` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '仓库名称',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '负责人（系统用户 ID）',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库';

DROP TABLE IF EXISTS `zc_bill_methods`;
CREATE TABLE `zc_bill_methods` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '名称：支付宝、微信、银行卡',
  `card_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '卡号',
  `image1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `group` int NOT NULL DEFAULT 1 COMMENT '分组：0=系统配置，1=手工配置',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收款方式';

-- ----------------------------
-- 2. 产品与价格资料
-- ----------------------------

DROP TABLE IF EXISTS `zc_product_category`;
CREATE TABLE `zc_product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '类别名称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品类别';

DROP TABLE IF EXISTS `zc_product_spec`;
CREATE TABLE `zc_product_spec` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '规格值',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品规格';

DROP TABLE IF EXISTS `zc_product_unit`;
CREATE TABLE `zc_product_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '单位',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品单位';

DROP TABLE IF EXISTS `zc_product_version`;
CREATE TABLE `zc_product_version` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '版本名称',
  `unit_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位（展示值，可后续改为 unit_id）',
  `spec_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格',
  `category_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物料类别',
  `selling_price_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'fixed_price' COMMENT 'fixed_price / sku_price',
  `inbound_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '进货价',
  `biz_type` tinyint NOT NULL DEFAULT 4 COMMENT '0壁纸 1运费 2样册 3其他 4窗帘 5窗纱 6成品',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_supplier` (`tenant_id`,`supplier_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品版本';

DROP TABLE IF EXISTS `zc_product`;
CREATE TABLE `zc_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '货号名称',
  `version_id` bigint NOT NULL COMMENT '版本 zc_product_version.id',
  `inbound_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '进货价',
  `a_price` decimal(20, 2) NULL DEFAULT NULL COMMENT 'A 类销售价',
  `b_price` decimal(20, 2) NULL DEFAULT NULL COMMENT 'B 类销售价',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商',
  `purchase_type` tinyint NOT NULL DEFAULT 0 COMMENT '0 整采 1 零采',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_version_id` (`version_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='货号档案';

DROP TABLE IF EXISTS `zc_customer_version_sales_authorization`;
CREATE TABLE `zc_customer_version_sales_authorization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint NOT NULL COMMENT '客户',
  `product_version_id` bigint NOT NULL COMMENT '版本',
  `authorized_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '授权价格',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_customer_version` (`tenant_id`,`customer_id`,`product_version_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户版本销售授权';

DROP TABLE IF EXISTS `zc_customer_product_sales_authorization`;
CREATE TABLE `zc_customer_product_sales_authorization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint NOT NULL COMMENT '客户',
  `product_id` bigint NOT NULL COMMENT '货号',
  `authorized_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '授权价格',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_customer_product` (`tenant_id`,`customer_id`,`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户产品销售授权';

-- ----------------------------
-- 3. 采购与库存
-- ----------------------------

DROP TABLE IF EXISTS `zc_purchase_order`;
CREATE TABLE `zc_purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '采购单号',
  `inbound_date` date NOT NULL COMMENT '入库日期',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商',
  `inbound_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'normal' COMMENT '入库类型',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作人',
  `po_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '进货收据单号',
  `audit_status` tinyint NOT NULL DEFAULT 0 COMMENT '0 未审核 1 已审核',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `auditor_id` bigint NULL DEFAULT NULL COMMENT '审核人',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_purchase_no` (`tenant_id`,`purchase_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购单';

DROP TABLE IF EXISTS `zc_product_batch`;
CREATE TABLE `zc_product_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_order_id` bigint NULL DEFAULT NULL COMMENT '采购单',
  `batch_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '批号',
  `inbound_date` date NOT NULL COMMENT '入库日期',
  `product_id` bigint NOT NULL COMMENT '货号',
  `inbound_quantity` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '入库数量',
  `quantity` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '剩余数量',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_product_batch` (`tenant_id`,`product_id`,`batch_no`) USING BTREE,
  KEY `idx_warehouse` (`tenant_id`,`warehouse_id`,`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品批次';

DROP TABLE IF EXISTS `zc_inventory_record`;
CREATE TABLE `zc_inventory_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` bigint NOT NULL COMMENT '货号',
  `batch_id` bigint NOT NULL COMMENT '批次',
  `old_quantity` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '盘点前数量',
  `new_quantity` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '盘点后数量',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_batch` (`tenant_id`,`product_id`,`batch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘点记录';

-- ----------------------------
-- 4. 窗帘主数据
-- ----------------------------

DROP TABLE IF EXISTS `zc_curtain_series`;
CREATE TABLE `zc_curtain_series` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '系列名称',
  `category` tinyint NOT NULL DEFAULT 0 COMMENT '0窗帘 1软装 2罗马帘 3百叶帘',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘系列';

DROP TABLE IF EXISTS `zc_curtain_install_process`;
CREATE TABLE `zc_curtain_install_process` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '工艺名称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `node_ids` json NULL COMMENT '关联工序节点 ID 列表，JSON 数组',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘工艺';

DROP TABLE IF EXISTS `zc_curtain_pleat_ratio`;
CREATE TABLE `zc_curtain_pleat_ratio` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `value` decimal(10, 2) NOT NULL COMMENT '褶倍',
  `rank` int NULL DEFAULT NULL COMMENT '排序',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_value` (`tenant_id`,`value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='褶倍';

DROP TABLE IF EXISTS `zc_curtain`;
CREATE TABLE `zc_curtain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '款式名称',
  `series_id` bigint NOT NULL COMMENT '系列',
  `paste_direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '粘贴方向',
  `open_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '打开方式',
  `install_process_id` bigint NULL DEFAULT NULL COMMENT '默认安装工艺',
  `process_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '加工类型',
  `pleat_ratio_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '默认褶倍',
  `pleats_distance` decimal(10, 2) NULL DEFAULT NULL COMMENT '褶距',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_series` (`series_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘';

DROP TABLE IF EXISTS `zc_curtain_structure`;
CREATE TABLE `zc_curtain_structure` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部位名称',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'other' COMMENT '帘头/帘身/飘窗垫/其他',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘结构部位';

DROP TABLE IF EXISTS `zc_curtain_structure_element`;
CREATE TABLE `zc_curtain_structure_element` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '组件名称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘结构配件';

DROP TABLE IF EXISTS `zc_curtain_template`;
CREATE TABLE `zc_curtain_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `curtain_id` bigint NOT NULL COMMENT '窗帘',
  `structure_id` bigint NOT NULL COMMENT '结构',
  `element_id` bigint NOT NULL COMMENT '配件',
  `unit_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_style_structure_element` (`tenant_id`,`curtain_id`,`structure_id`,`element_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗帘模板';

-- ----------------------------
-- 5. 订单进度 / 工序定义（可配置，支持订单类事件 + 生产线工序）
-- ----------------------------

DROP TABLE IF EXISTS `zc_sales_order_progress_log`;
DROP TABLE IF EXISTS `zc_sales_order_production_queue`;
DROP TABLE IF EXISTS `zc_progress_definition`;

CREATE TABLE `zc_progress_definition` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点编码，租户内唯一',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点名称（展示）',
  `progress_kind` tinyint NOT NULL DEFAULT 1 COMMENT '1 订单进度事件 2 生产工序 3 发货后勤 4 财务相关',
  `phase_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分组',
  `sort` int NOT NULL DEFAULT 0 COMMENT '同组内排序',
  `is_milestone` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否关键里程碑',
  `allow_repeat` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否允许同一订单重复记录',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0 启用 1 停用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_code` (`tenant_id`,`code`) USING BTREE,
  KEY `idx_kind_sort` (`tenant_id`,`progress_kind`,`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单进度/工序定义';

CREATE TABLE `zc_sales_order_progress_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单 zc_sales_order.id',
  `definition_id` bigint NULL DEFAULT NULL COMMENT '节点定义 zc_progress_definition.id',
  `progress_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点编码快照',
  `progress_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点名称快照',
  `action_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DONE' COMMENT 'DONE / START / CANCEL / EXCEPTION',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人姓名快照',
  `biz_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '业务发生时间',
  `detail_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '扩展 JSON',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_time` (`tenant_id`,`order_id`,`biz_time`) USING BTREE,
  KEY `idx_definition` (`definition_id`) USING BTREE,
  KEY `idx_progress_code` (`tenant_id`,`progress_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单进度/工序流水';

CREATE TABLE `zc_sales_order_production_queue` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `definition_id` bigint NOT NULL COMMENT '工序定义',
  `queue_status` tinyint NOT NULL DEFAULT 0 COMMENT '0 待开始 1 进行中 2 已完成 3 已跳过',
  `sequence_no` int NOT NULL DEFAULT 0 COMMENT '产线顺序',
  `started_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `completed_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '当前责任人',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_order_definition` (`tenant_id`,`order_id`,`definition_id`) USING BTREE,
  KEY `idx_order_seq` (`tenant_id`,`order_id`,`sequence_no`) USING BTREE,
  KEY `idx_status` (`tenant_id`,`queue_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单生产工序排队';

-- ----------------------------
-- 6. 销售订单
-- ----------------------------

CREATE TABLE `zc_sales_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `customer_id` bigint NOT NULL COMMENT '客户',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机',
  `brand_id` bigint NULL DEFAULT NULL COMMENT '品牌',
  `category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'normal' COMMENT '订单类别',
  `order_date` date NOT NULL COMMENT '下单日期',
  `logistic_id` bigint NULL DEFAULT NULL COMMENT '物流',
  `logistic_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流名字',
  `receiver` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人',
  `delivery_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '送货地址',
  `freight` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '运费',
  `types` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '成品帘、面料单',
  `amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  `amount_received` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '已收金额',
  `delivery_date` date NULL DEFAULT NULL COMMENT '交付日期',
  `pay_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unpaid' COMMENT '结算状态',
  `confirm_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unconfirmed' COMMENT '确认状态',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `is_expedited` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否加急',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_order_no` (`tenant_id`,`order_no`) USING BTREE,
  KEY `idx_customer` (`tenant_id`,`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售订单';

CREATE TABLE `zc_sales_order_curtain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `curtain_id` bigint NULL DEFAULT NULL COMMENT '款式',
  `room` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '房间',
  `pleat_ratio_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '褶倍',
  `discount_rate` decimal(6, 4) NULL DEFAULT NULL COMMENT '折扣率',
  `amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '本行应收',
  `image1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片1',
  `image2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片2',
  `mountings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配件多选',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '窗帘行状态，冗余自订单主表，参见 zc_order_status 字典',
  `index` smallint NULL DEFAULT NULL COMMENT '序号，同一订单内窗帘行的显示顺序，从 1 开始',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order` (`tenant_id`,`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成品订单-窗帘行';

CREATE TABLE `zc_sales_order_curtain_structure` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `order_curtain_id` bigint NOT NULL COMMENT '窗帘行',
  `structure_id` bigint NOT NULL COMMENT '结构',
  `height` decimal(12, 4) NULL DEFAULT NULL COMMENT '高',
  `width` decimal(12, 4) NULL DEFAULT NULL COMMENT '宽',
  `left_corner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '左转角',
  `right_corner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '右转角',
  `paste_direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '粘贴方向',
  `install_process_id` bigint NULL DEFAULT NULL COMMENT '安装工艺',
  `open_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '打开方式',
  `process_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '加工类型',
  `is_shaping` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否定型',
  `pleats_num` int NULL DEFAULT NULL COMMENT '总褶数',
  `pleats_distance` decimal(10, 2) NULL DEFAULT NULL COMMENT '褶距',
  `skirt_height` decimal(12, 4) NULL DEFAULT NULL COMMENT '裙摆高度',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_curtain` (`tenant_id`,`order_curtain_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成品订单-结构';

CREATE TABLE `zc_sales_order_curtain_structure_element` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `order_curtain_structure_id` bigint NOT NULL COMMENT '结构行',
  `element_id` bigint NULL DEFAULT NULL COMMENT '组件类型',
  `product_id` bigint NOT NULL COMMENT '货号',
  `batch_id` bigint NULL DEFAULT NULL COMMENT '批次',
  `price` decimal(20, 2) NULL DEFAULT NULL COMMENT '单价',
  `quantity` decimal(18, 4) NULL DEFAULT NULL COMMENT '用料',
  `unit_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `discount_rate` decimal(6, 4) NULL DEFAULT NULL COMMENT '折扣率',
  `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '小计',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_structure` (`tenant_id`,`order_curtain_structure_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成品订单-用料明细';

CREATE TABLE `zc_sales_order_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `product_id` bigint NOT NULL COMMENT '货号',
  `batch_id` bigint NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '销售数量',
  `price` decimal(20, 2) NULL DEFAULT NULL COMMENT '单价',
  `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '金额',
  `discount_rate` decimal(6, 4) NULL DEFAULT NULL COMMENT '折扣率',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `index` smallint NULL DEFAULT NULL COMMENT '序号，同一订单内产品行的显示顺序，从 1 开始',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品行状态，冗余自订单主表，参见 zc_order_status 字典',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order` (`tenant_id`,`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面料订单明细';

-- ----------------------------
-- 7. 收款
-- ----------------------------

CREATE TABLE `zc_collection_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `collection_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '收款单号',
  `collection_date` date NOT NULL COMMENT '收款日期',
  `collectioner_id` bigint NULL DEFAULT NULL COMMENT '收款人（系统用户 ID）',
  `customer_id` bigint NOT NULL COMMENT '客户',
  `amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '收款金额',
  `discount_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `payment_id` bigint NULL DEFAULT NULL COMMENT '收款方式',
  `image1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '凭证图1',
  `image2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '凭证图2',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_collection_no` (`tenant_id`,`collection_no`) USING BTREE,
  KEY `idx_customer` (`tenant_id`,`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收款记录';

CREATE TABLE `zc_collection_order_alloc` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `collection_id` bigint NOT NULL COMMENT '收款单',
  `order_id` bigint NOT NULL COMMENT '销售单',
  `pay_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '本次分摊实收',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_collection` (`collection_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收款分摊';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 字典：订单状态（zc_order_status） id=2301, data=3700~3745
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2301, '订单状态', 'zc_order_status', 0, '智仓销售订单状态', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3700, 1,  '未确认',   'UNCONFIRMED',  'zc_order_status', 0, 'info',    '', '订单刚创建，尚未审核确认',       'admin', NOW(), 'admin', NOW(), b'0'),
(3701, 2,  '已确认',   'CONFIRMED',    'zc_order_status', 0, 'primary', '', '订单已审核，进入生产流程',       'admin', NOW(), 'admin', NOW(), b'0'),
(3702, 3,  '未配料',   'NOT_PEILIAO',  'zc_order_status', 0, 'default', '', '窗帘行确认后初始状态',           'admin', NOW(), 'admin', NOW(), b'0'),
(3739, 4,  '部分配料', 'BUFEN_PEILIAO','zc_order_status', 0, 'warning', '', '部分用料明细已完成裁剪出库',     'admin', NOW(), 'admin', NOW(), b'0'),
(3740, 5,  '已配料',   'HAVE_PEILIAO', 'zc_order_status', 0, 'primary', '', '全部用料明细已完成裁剪出库',     'admin', NOW(), 'admin', NOW(), b'0'),
(3741, 6,  '部分打包', 'BUFEN_DABAO',  'zc_order_status', 0, 'warning', '', '部分窗帘行已完成打包',           'admin', NOW(), 'admin', NOW(), b'0'),
(3742, 7,  '已打包',   'DABAO',        'zc_order_status', 0, 'warning', '', '生产完成，已打包备货',           'admin', NOW(), 'admin', NOW(), b'0'),
(3743, 8,  '部分发货', 'BUFEN_FAHUO',  'zc_order_status', 0, 'warning', '', '部分窗帘行/产品行已发货',        'admin', NOW(), 'admin', NOW(), b'0'),
(3744, 9,  '已发货',   'FAHUO',        'zc_order_status', 0, 'success', '', '货物已发出，等待签收',           'admin', NOW(), 'admin', NOW(), b'0'),
(3745, 10, '完成',     'COMPLETE',     'zc_order_status', 0, 'success', '', '订单履约完成',                   'admin', NOW(), 'admin', NOW(), b'0'),
(3746, 11, '废弃',     'DISCARDED',    'zc_order_status', 0, 'danger',  '', '订单已标记废弃',                 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：订单支付状态（zc_order_pay_status） id=2302, data=3704~3706
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2302, '订单支付状态', 'zc_order_pay_status', 0, '智仓销售订单支付状态', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3704, 1, '未支付',   'UNPAID',      'zc_order_pay_status', 0, 'info',    '', '尚未收到任何款项', 'admin', NOW(), 'admin', NOW(), b'0'),
(3705, 2, '部分支付', 'PARTIALPAID', 'zc_order_pay_status', 0, 'warning', '', '已收部分款项',     'admin', NOW(), 'admin', NOW(), b'0'),
(3706, 3, '已支付',   'PAID',        'zc_order_pay_status', 0, 'success', '', '款项已全额到账',   'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：订单类型（zc_order_type） id=2303, data=3707~3708
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2303, '订单类型', 'zc_order_type', 0, '智仓销售订单类型', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3707, 1, '面料单', 'FABRIC',  'zc_order_type', 0, 'default', '', '直接购买产品批次，无工艺配置', 'admin', NOW(), 'admin', NOW(), b'0'),
(3708, 2, '成品单', 'CURTAIN', 'zc_order_type', 0, 'primary', '', '包含窗帘工艺配置的完整订单',   'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：出货价类型（zc_selling_price_type） id=2304, data=3709~3710
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2304, '出货价类型', 'zc_selling_price_type', 0, '产品版本出货价定价方式', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3709, 1, '统一价', 'FIXED_PRICE', 'zc_selling_price_type', 0, 'primary', '', '所有客户同一售价',  'admin', NOW(), 'admin', NOW(), b'0'),
(3710, 2, '型号价', 'SKU_PRICE',   'zc_selling_price_type', 0, 'warning', '', '按SKU规格单独定价', 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：物料分类（zc_product_classify） id=2305, data=3711~3718
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2305, '物料分类', 'zc_product_classify', 0, '产品版本物料分类', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3711, 1, '运费',   'YUNFEI',        'zc_product_classify', 0, 'default', '', '物流运费项',         'admin', NOW(), 'admin', NOW(), b'0'),
(3712, 2, '样册',   'YANGCE',        'zc_product_classify', 0, 'default', '', '产品样本册',         'admin', NOW(), 'admin', NOW(), b'0'),
(3713, 3, '其他',   'QITA',          'zc_product_classify', 0, 'default', '', '未归类物料',         'admin', NOW(), 'admin', NOW(), b'0'),
(3714, 4, '窗帘布', 'CHUANGLIANBU',  'zc_product_classify', 0, 'primary', '', '主面料',             'admin', NOW(), 'admin', NOW(), b'0'),
(3715, 5, '赠品',   'ZENGPIN',       'zc_product_classify', 0, 'success', '', '随单赠送物品',       'admin', NOW(), 'admin', NOW(), b'0'),
(3716, 6, '绑带',   'BANGDAI',       'zc_product_classify', 0, 'default', '', '窗帘绑带配件',       'admin', NOW(), 'admin', NOW(), b'0'),
(3717, 7, '窗帘纱', 'CHUANGLIANSHA', 'zc_product_classify', 0, 'primary', '', '纱帘面料',           'admin', NOW(), 'admin', NOW(), b'0'),
(3718, 8, '成品',   'CHENGPIN',      'zc_product_classify', 0, 'success', '', '已加工完成的成品帘', 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：余额变动业务类型（zc_customer_balance_biz_type） id=2306, data=3719~3725
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2306, '余额变动业务类型', 'zc_customer_balance_biz_type', 0, '客户余额变动来源类型', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3719, 1, '订单确认扣减', 'ORDER_CONFIRM',   'zc_customer_balance_biz_type', 0, 'danger',  '', '订单确认时从余额扣除订单金额', 'admin', NOW(), 'admin', NOW(), b'0'),
(3720, 2, '取消确认回退', 'ORDER_UNCONFIRM', 'zc_customer_balance_biz_type', 0, 'warning', '', '撤销订单确认时退回余额',       'admin', NOW(), 'admin', NOW(), b'0'),
(3721, 3, '订单更新调整', 'ORDER_CHANGE',    'zc_customer_balance_biz_type', 0, 'info',    '', '订单金额变更时补差额',         'admin', NOW(), 'admin', NOW(), b'0'),
(3722, 4, '收款入账',     'COLLECTION',      'zc_customer_balance_biz_type', 0, 'success', '', '收款单创建，客户余额增加',     'admin', NOW(), 'admin', NOW(), b'0'),
(3723, 5, '收款作废冲回', 'COLLECTION_VOID', 'zc_customer_balance_biz_type', 0, 'danger',  '', '收款单删除/作废，冲回余额',    'admin', NOW(), 'admin', NOW(), b'0'),
(3724, 6, '手工调整',     'MANUAL_ADJUST',   'zc_customer_balance_biz_type', 0, 'info',    '', '后台人工直接调整余额',         'admin', NOW(), 'admin', NOW(), b'0'),
(3725, 7, '其他',         'OTHER',           'zc_customer_balance_biz_type', 0, 'default', '', '不属于以上类型的余额变动',     'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：粘贴方向（zc_paste_direction） id=2307, data=3726~3728
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2307, '粘贴方向', 'zc_paste_direction', 0, '窗帘粘贴方向', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3726, 1, '正反贴', 'ZFT', 'zc_paste_direction', 0, 'default', '', '正面与背面均可粘贴', 'admin', NOW(), 'admin', NOW(), b'0'),
(3727, 2, '反贴',   'FT',  'zc_paste_direction', 0, 'default', '', '仅背面粘贴',         'admin', NOW(), 'admin', NOW(), b'0'),
(3728, 3, '正贴',   'ZT',  'zc_paste_direction', 0, 'default', '', '仅正面粘贴',         'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：打开方式（zc_open_method） id=2308, data=3729~3734
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2308, '打开方式', 'zc_open_method', 0, '窗帘打开方式', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3729, 1, '右开', 'RIGHT_OPEN',  'zc_open_method', 0, 'default', '', '窗帘向右侧拉开',         'admin', NOW(), 'admin', NOW(), b'0'),
(3730, 2, '左开', 'LEFT_OPEN',   'zc_open_method', 0, 'default', '', '窗帘向左侧拉开',         'admin', NOW(), 'admin', NOW(), b'0'),
(3731, 3, '四开', 'FOUR_OPEN',   'zc_open_method', 0, 'default', '', '两侧各两幅，共四幅拉开', 'admin', NOW(), 'admin', NOW(), b'0'),
(3732, 4, '三开', 'THREE_OPEN',  'zc_open_method', 0, 'default', '', '三幅窗帘拉开',           'admin', NOW(), 'admin', NOW(), b'0'),
(3733, 5, '双开', 'TWO_OPEN',    'zc_open_method', 0, 'default', '', '两幅从中间向两侧拉开',   'admin', NOW(), 'admin', NOW(), b'0'),
(3734, 6, '单开', 'ONE_OPEN',    'zc_open_method', 0, 'default', '', '单幅窗帘拉开',           'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：加工类型（zc_process_type） id=2309, data=3735~3736
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2309, '加工类型', 'zc_process_type', 0, '窗帘加工计价方式', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3735, 1, '定宽买高', 'DKMG', 'zc_process_type', 0, 'primary', '', '以宽度为基准，按高度计费', 'admin', NOW(), 'admin', NOW(), b'0'),
(3736, 2, '定高买宽', 'DGMK', 'zc_process_type', 0, 'warning', '', '以高度为基准，按宽度计费', 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 字典：关联单据类型（zc_ref_type） id=2310, data=3737~3738
-- ----------------------------
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2310, '关联单据类型', 'zc_ref_type', 0, '客户余额变动关联单据类型', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(3737, 1, '销售单', 'SALES_ORDER',       'zc_ref_type', 0, 'primary', '', '来源于销售订单', 'admin', NOW(), 'admin', NOW(), b'0'),
(3738, 2, '收款单', 'COLLECTION_RECORD', 'zc_ref_type', 0, 'success', '', '来源于收款单',   'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- zc_product_version 新增 specs 字段 / zc_product 删除 spec_id 字段
-- ----------------------------
ALTER TABLE `zc_product_version`
    ADD COLUMN `specs` json DEFAULT NULL COMMENT '规格列表，如 ["12","45","wq"]';

ALTER TABLE `zc_product`
    DROP COLUMN `spec_id`;

-- ----------------------------
-- zc_bill_methods 新增 group 字段（已有库升级）
-- ----------------------------
ALTER TABLE `zc_bill_methods`
    ADD COLUMN `group` int NOT NULL DEFAULT 1 COMMENT '分组：0=系统配置，1=手工配置' AFTER `note`;

-- ----------------------------
-- zc_sales_order 新增 logistic_name 字段（已有库升级）
-- ----------------------------
ALTER TABLE `zc_sales_order`
    ADD COLUMN `logistic_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流名字' AFTER `logistic_id`;
