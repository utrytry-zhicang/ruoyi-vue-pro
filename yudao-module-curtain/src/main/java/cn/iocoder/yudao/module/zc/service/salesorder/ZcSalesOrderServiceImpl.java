package cn.iocoder.yudao.module.zc.service.salesorder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.zc.dal.mysql.curtain.ZcCurtainMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.zc.controller.admin.salesorder.vo.*;
import cn.iocoder.yudao.module.zc.dal.dataobject.curtain.ZcCurtainDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.curtaininstallprocess.ZcCurtainInstallProcessDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.curtainstructure.ZcCurtainStructureDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.curtainstructureelement.ZcCurtainStructureElementDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.product.ZcProductDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.productbatch.ZcProductBatchDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZcSalesOrderCurtainDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZcSalesOrderDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZcSalesOrderStructureDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZCSalesOrderMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.zc.dal.redis.ZcNoGeneratorRedisDAO;
import cn.iocoder.yudao.module.zc.dal.dataobject.customer.ZcCustomerDO;
import cn.iocoder.yudao.module.zc.dal.dataobject.customerbalancelog.ZcCustomerBalanceLogDO;
import cn.iocoder.yudao.module.zc.service.customer.ZcCustomerService;
import cn.iocoder.yudao.module.zc.service.customerbalancelog.ZcCustomerBalanceLogService;
import cn.iocoder.yudao.module.zc.service.logistics.ZcLogisticsService;
import cn.iocoder.yudao.module.zc.dal.mysql.curtaininstallprocess.ZcCurtainInstallProcessMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.curtainstructure.ZcCurtainStructureMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.curtainstructureelement.ZcCurtainStructureElementMapper;
import cn.iocoder.yudao.module.zc.dal.dataobject.productspec.ZcProductSpecDO;
import cn.iocoder.yudao.module.zc.dal.mysql.product.ZcProductMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.productbatch.ZcProductBatchMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.productspec.ZcProductSpecMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.salesorder.ZCSalesOrderMaterialMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.salesorder.ZcSalesOrderCurtainMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.salesorder.ZcSalesOrderMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.salesorder.ZcSalesOrderProductMapper;
import cn.iocoder.yudao.module.zc.dal.mysql.salesorder.ZcSalesOrderStructureMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;

import cn.iocoder.yudao.module.zc.enums.ZcCustomerBalanceBizTypeEnum;
import cn.iocoder.yudao.module.zc.enums.ZcOrderTypeEnum;
import cn.iocoder.yudao.module.zc.enums.ZcRefTypeEnum;
import cn.iocoder.yudao.module.zc.enums.ZcSalesOrderMaterialStatusEnum;
import cn.iocoder.yudao.module.zc.enums.ZcSalesOrderPayStatusEnum;
import cn.iocoder.yudao.module.zc.enums.ZcSalesOrderStatusEnum;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.zc.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.zc.enums.LogRecordConstants.*;

/**
 * 销售订单 Service 实现类
 *
 * @author 01Coder
 */
@Service
@Validated
public class ZcSalesOrderServiceImpl implements ZcSalesOrderService {

    @Resource
    private ZcCustomerService customerService;
    @Resource
    private ZcCustomerBalanceLogService customerBalanceLogService;
    @Resource
    private ZcNoGeneratorRedisDAO noGeneratorRedisDAO;

    @Resource
    private ZcSalesOrderMapper salesOrderMapper;
    @Resource
    private ZcSalesOrderCurtainMapper salesOrderCurtainMapper;
    @Resource
    private ZcSalesOrderProductMapper salesOrderProductMapper;
    @Resource
    private ZcSalesOrderStructureMapper salesOrderStructureMapper;
    @Resource
    private ZCSalesOrderMaterialMapper salesOrderMaterialMapper;

    @Resource
    private ZcLogisticsService logisticsService;
    @Resource
    private ZcCurtainMapper curtainMapper;
    @Resource
    private ZcCurtainStructureMapper curtainStructureMapper;
    @Resource
    private ZcCurtainInstallProcessMapper curtainInstallProcessMapper;
    @Resource
    private ZcCurtainStructureElementMapper curtainStructureElementMapper;
    @Resource
    private ZcProductMapper productMapper;
    @Resource
    private ZcProductBatchMapper productBatchMapper;
    @Resource
    private ZcProductSpecMapper productSpecMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_CREATE_SUB_TYPE, bizNo = "{{#salesOrder.id}}",
            success = ZC_SALES_ORDER_CREATE_SUCCESS)
    public Long createSalesOrder(ZcSalesOrderCreateReqVO createReqVO) {
        ZcSalesOrderDO salesOrder = BeanUtils.toBean(createReqVO, ZcSalesOrderDO.class);
        logisticsService.resolveLogisticsForOrder(salesOrder);
        salesOrder.setOrderNo(generateOrderNo("CP"));
        applyOrderDefaults(salesOrder, ZcOrderTypeEnum.CURTAIN.name(), CollUtil.size(createReqVO.getCurtains()));
        salesOrderMapper.insert(salesOrder);
        LogRecordContext.putVariable("salesOrder", salesOrder);
        saveCurtainSubRows(salesOrder.getId(), createReqVO.getCurtains());
        return salesOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_FABRIC_CREATE_SUB_TYPE, bizNo = "{{#salesOrder.id}}",
            success = ZC_SALES_ORDER_FABRIC_CREATE_SUCCESS)
    public Long createFabricSalesOrder(ZcSalesOrderFabricCreateReqVO createReqVO) {
        ZcSalesOrderDO salesOrder = BeanUtils.toBean(createReqVO, ZcSalesOrderDO.class);
        logisticsService.resolveLogisticsForOrder(salesOrder);
        salesOrder.setOrderNo(generateOrderNo("ML"));
        applyOrderDefaults(salesOrder, ZcOrderTypeEnum.FABRIC.name(), CollUtil.size(createReqVO.getCurtains()));
        salesOrderMapper.insert(salesOrder);
        LogRecordContext.putVariable("salesOrder", salesOrder);
        // 面单简化 VO → 标准窗帘 VO，复用同一套三层嵌套保存逻辑
        saveCurtainSubRows(salesOrder.getId(), toStandardCurtainVOs(createReqVO.getCurtains()));
        return salesOrder.getId();
    }

    /** 生成订单号：{prefix}{租户ID}{yyyyMMdd}{5位序号}，Redis INCR 保证并发唯一 */
    private String generateOrderNo(String prefix) {
        Long tenantId = TenantContextHolder.getRequiredTenantId();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = noGeneratorRedisDAO.nextOrderSeq(tenantId, date);
        return String.format("%s%d%s%05d", prefix, tenantId, date, seq);
    }

    /** 设置订单主表的系统默认字段（类型、状态、运费/总金额兜底为 0） */
    private void applyOrderDefaults(ZcSalesOrderDO salesOrder, String types, int sets) {
        salesOrder.setTypes(types);
        salesOrder.setPayStatus(ZcSalesOrderPayStatusEnum.UNPAID.name());
        salesOrder.setStatus(ZcSalesOrderStatusEnum.UNCONFIRMED.name());
        salesOrder.setIsExpedited(false);
        salesOrder.setSets(sets);
        if (salesOrder.getFreight() == null) salesOrder.setFreight(BigDecimal.ZERO);
        if (salesOrder.getTotalAmount() == null) salesOrder.setTotalAmount(BigDecimal.ZERO);
        if (salesOrder.getAmount() == null) salesOrder.setAmount(BigDecimal.ZERO);
        if (salesOrder.getDiscountAmount() == null) salesOrder.setDiscountAmount(BigDecimal.ZERO);
        if (salesOrder.getRounding() == null) salesOrder.setRounding(BigDecimal.ZERO);
        if (salesOrder.getDeliveryAddress() == null) salesOrder.setDeliveryAddress("");
    }

    /** 整单更新时，将主表 NOT NULL 字段的 null 归一为数据库默认值 */
    private void normalizeOrderUpdateFields(ZcSalesOrderDO salesOrder) {
        if (salesOrder.getAmount() == null) {
            salesOrder.setAmount(BigDecimal.ZERO);
        }
        if (salesOrder.getTotalAmount() == null) {
            salesOrder.setTotalAmount(BigDecimal.ZERO);
        }
        if (salesOrder.getDiscountAmount() == null) {
            salesOrder.setDiscountAmount(BigDecimal.ZERO);
        }
        if (salesOrder.getRounding() == null) {
            salesOrder.setRounding(BigDecimal.ZERO);
        }
        if (salesOrder.getDeliveryAddress() == null) {
            salesOrder.setDeliveryAddress("");
        }
    }

    /** 窗帘行 NOT NULL 字段兜底：amount 默认 0，quantity 默认 1 */
    private void normalizeCurtainDO(ZcSalesOrderCurtainDO curtain) {
        if (curtain.getAmount() == null) {
            curtain.setAmount(BigDecimal.ZERO);
        }
        if (curtain.getQuantity() == null) {
            curtain.setQuantity(1L);
        }
    }

    /** 结构行 NOT NULL 字段兜底：isShaping 默认 false */
    private void normalizeStructureDO(ZcSalesOrderStructureDO structure) {
        if (structure.getIsShaping() == null) {
            structure.setIsShaping(false);
        }
    }

    /** 三层嵌套批量保存：窗帘行 → 结构行 → 用料明细 */
    private void saveCurtainSubRows(Long orderId, List<ZcSalesOrderCurtainCreateVO> curtains) {
        if (CollUtil.isEmpty(curtains)) return;
        int curtainIndex = 1;
        for (ZcSalesOrderCurtainCreateVO curtainVO : curtains) {
            ZcSalesOrderCurtainDO curtainDO = BeanUtils.toBean(curtainVO, ZcSalesOrderCurtainDO.class);
            curtainDO.setOrderId(orderId);
            curtainDO.setStatus(ZcSalesOrderStatusEnum.UNCONFIRMED.name());
            curtainDO.setIndex(curtainIndex++);
            normalizeCurtainDO(curtainDO);
            if (CollUtil.isNotEmpty(curtainVO.getMountings())) {
                curtainDO.setMountings(JSONUtil.toJsonStr(curtainVO.getMountings()));
            }
            salesOrderCurtainMapper.insert(curtainDO);
            Long orderCurtainId = curtainDO.getId();

            if (CollUtil.isEmpty(curtainVO.getStructures())) continue;
            for (ZcSalesOrderStructureCreateVO structureVO : curtainVO.getStructures()) {
                ZcSalesOrderStructureDO structureDO = BeanUtils.toBean(structureVO, ZcSalesOrderStructureDO.class);
                structureDO.setOrderId(orderId);
                structureDO.setOrderCurtainId(orderCurtainId);
                normalizeStructureDO(structureDO);
                salesOrderStructureMapper.insert(structureDO);
                Long orderStructureId = structureDO.getId();

                if (CollUtil.isEmpty(structureVO.getMaterials())) continue;
                for (ZCSalesOrderMaterialCreateVO materialVO : structureVO.getMaterials()) {
                    ZCSalesOrderMaterialDO materialDO = BeanUtils.toBean(materialVO, ZCSalesOrderMaterialDO.class);
                    materialDO.setOrderId(orderId);
                    materialDO.setOrderStructureId(orderStructureId);
                    salesOrderMaterialMapper.insert(materialDO);
                }
            }
        }
    }

    /**
     * 三路 merge 更新三层子表：UPDATE 有 id 的行 / INSERT 无 id 的行 / DELETE 不在请求中的行。
     *
     * <p>保护规则：
     * <ul>
     *   <li>窗帘行：status / packTime / shipTime 不覆盖（由确认/打包/发货流程驱动）</li>
     *   <li>用料明细：status / cutQuantity 不覆盖；已裁剪行（HAVE_PEILIAO）的 batchId 保留原值</li>
     *   <li>前置拦截：若待删除的用料明细中存在 HAVE_PEILIAO 行，直接抛异常，防止库存状态不一致</li>
     *   <li>业务字段置 null 可落库：可空列已标注 {@code FieldStrategy.ALWAYS}；NOT NULL 列在 Service 层归一默认值</li>
     * </ul>
     * </p>
     */
    private void mergeCurtainSubRows(Long orderId, List<ZcSalesOrderCurtainCreateVO> curtains) {
        if (CollUtil.isEmpty(curtains)) return;

        // ① 一次性加载当前三层子表，避免后续 N+1
        List<ZcSalesOrderCurtainDO> existingCurtains = salesOrderCurtainMapper.selectListByOrderId(orderId);
        List<ZcSalesOrderStructureDO> existingStructures = salesOrderStructureMapper.selectListByOrderId(orderId);
        List<ZCSalesOrderMaterialDO> existingMaterials = salesOrderMaterialMapper.selectListByOrderId(orderId);

        Map<Long, ZcSalesOrderCurtainDO> existingCurtainMap = convertMap(existingCurtains, ZcSalesOrderCurtainDO::getId);
        Map<Long, ZcSalesOrderStructureDO> existingStructureMap = convertMap(existingStructures, ZcSalesOrderStructureDO::getId);
        Map<Long, ZCSalesOrderMaterialDO> existingMaterialMap = convertMap(existingMaterials, ZCSalesOrderMaterialDO::getId);

        // ② 收集请求中所有 ID，用于确定哪些行需要被删除
        Set<Long> requestCurtainIds = new HashSet<>();
        Set<Long> requestStructureIds = new HashSet<>();
        Set<Long> requestMaterialIds = new HashSet<>();
        for (ZcSalesOrderCurtainCreateVO curtainVO : curtains) {
            if (curtainVO.getId() != null) requestCurtainIds.add(curtainVO.getId());
            if (CollUtil.isNotEmpty(curtainVO.getStructures())) {
                for (ZcSalesOrderStructureCreateVO structureVO : curtainVO.getStructures()) {
                    if (structureVO.getId() != null) requestStructureIds.add(structureVO.getId());
                    if (CollUtil.isNotEmpty(structureVO.getMaterials())) {
                        structureVO.getMaterials().stream()
                                .filter(m -> m.getId() != null)
                                .forEach(m -> requestMaterialIds.add(m.getId()));
                    }
                }
            }
        }

        // ③ 前置校验：待删除的用料明细中不允许有 HAVE_PEILIAO 行（库存已出库，须先撤销裁剪）
        existingMaterials.stream()
                .filter(m -> !requestMaterialIds.contains(m.getId()))
                .filter(m -> ZcSalesOrderMaterialStatusEnum.HAVE_PEILIAO.name().equals(m.getStatus()))
                .findFirst()
                .ifPresent(m -> { throw exception(SALES_ORDER_MATERIAL_CANNOT_DELETE_WHEN_CUT); });

        // ④ 逐层 upsert
        int curtainIndex = 1;
        for (ZcSalesOrderCurtainCreateVO curtainVO : curtains) {
            String mountingsJson = CollUtil.isNotEmpty(curtainVO.getMountings())
                    ? JSONUtil.toJsonStr(curtainVO.getMountings()) : null;
            Long orderCurtainId;

            if (curtainVO.getId() != null && existingCurtainMap.containsKey(curtainVO.getId())) {
                // UPDATE 已有窗帘行
                ZcSalesOrderCurtainDO updateDO = BeanUtils.toBean(curtainVO, ZcSalesOrderCurtainDO.class);
                updateDO.setMountings(mountingsJson); // 手动赋值（VO/DO 类型不同，toBean 不会复制）
                updateDO.setIndex(curtainIndex);
                // status/packTime/shipTime 置 null → updateById 不覆盖
                updateDO.setStatus(null);
                updateDO.setPackTime(null);
                updateDO.setShipTime(null);
                normalizeCurtainDO(updateDO);
                salesOrderCurtainMapper.updateById(updateDO);
                orderCurtainId = curtainVO.getId();
            } else {
                // INSERT 新窗帘行
                ZcSalesOrderCurtainDO newDO = BeanUtils.toBean(curtainVO, ZcSalesOrderCurtainDO.class);
                newDO.setId(null); // 防止前端传入无效 ID 被当作主键
                newDO.setOrderId(orderId);
                newDO.setMountings(mountingsJson);
                newDO.setStatus(ZcSalesOrderStatusEnum.UNCONFIRMED.name());
                newDO.setIndex(curtainIndex);
                normalizeCurtainDO(newDO);
                salesOrderCurtainMapper.insert(newDO);
                orderCurtainId = newDO.getId();
            }
            curtainIndex++;

            if (CollUtil.isEmpty(curtainVO.getStructures())) continue;
            for (ZcSalesOrderStructureCreateVO structureVO : curtainVO.getStructures()) {
                Long orderStructureId;

                if (structureVO.getId() != null && existingStructureMap.containsKey(structureVO.getId())) {
                    // UPDATE 已有结构行
                    ZcSalesOrderStructureDO updateDO = BeanUtils.toBean(structureVO, ZcSalesOrderStructureDO.class);
                    updateDO.setOrderCurtainId(orderCurtainId); // 允许行跨窗帘挪动
                    normalizeStructureDO(updateDO);
                    salesOrderStructureMapper.updateById(updateDO);
                    orderStructureId = structureVO.getId();
                } else {
                    // INSERT 新结构行
                    ZcSalesOrderStructureDO newDO = BeanUtils.toBean(structureVO, ZcSalesOrderStructureDO.class);
                    newDO.setId(null);
                    newDO.setOrderId(orderId);
                    newDO.setOrderCurtainId(orderCurtainId);
                    normalizeStructureDO(newDO);
                    salesOrderStructureMapper.insert(newDO);
                    orderStructureId = newDO.getId();
                }

                if (CollUtil.isEmpty(structureVO.getMaterials())) continue;
                for (ZCSalesOrderMaterialCreateVO materialVO : structureVO.getMaterials()) {
                    if (materialVO.getId() != null && existingMaterialMap.containsKey(materialVO.getId())) {
                        // UPDATE 已有用料明细，保护系统字段
                        ZCSalesOrderMaterialDO existing = existingMaterialMap.get(materialVO.getId());
                        ZCSalesOrderMaterialDO updateDO = BeanUtils.toBean(materialVO, ZCSalesOrderMaterialDO.class);
                        updateDO.setOrderStructureId(orderStructureId);
                        // status / cutQuantity 由裁剪流程维护，置 null 确保 updateById 不覆盖
                        updateDO.setStatus(null);
                        updateDO.setCutQuantity(null);
                        // 已裁剪行的 batchId 与库存扣减绑定，不允许修改（保留原值，避免 ALWAYS 策略将其清空）
                        if (ZcSalesOrderMaterialStatusEnum.HAVE_PEILIAO.name().equals(existing.getStatus())) {
                            updateDO.setBatchId(existing.getBatchId());
                        }
                        salesOrderMaterialMapper.updateById(updateDO);
                    } else {
                        // INSERT 新用料明细
                        ZCSalesOrderMaterialDO newDO = BeanUtils.toBean(materialVO, ZCSalesOrderMaterialDO.class);
                        newDO.setId(null);
                        newDO.setOrderId(orderId);
                        newDO.setOrderStructureId(orderStructureId);
                        salesOrderMaterialMapper.insert(newDO);
                    }
                }
            }
        }

        // ⑤ 删除不在请求中的行（由内向外：先用料明细 → 结构行 → 窗帘行）
        Set<Long> materialsToDelete = existingMaterials.stream()
                .map(ZCSalesOrderMaterialDO::getId)
                .filter(id -> !requestMaterialIds.contains(id))
                .collect(Collectors.toSet());
        Set<Long> structuresToDelete = existingStructures.stream()
                .map(ZcSalesOrderStructureDO::getId)
                .filter(id -> !requestStructureIds.contains(id))
                .collect(Collectors.toSet());
        Set<Long> curtainsToDelete = existingCurtains.stream()
                .map(ZcSalesOrderCurtainDO::getId)
                .filter(id -> !requestCurtainIds.contains(id))
                .collect(Collectors.toSet());

        if (CollUtil.isNotEmpty(materialsToDelete)) salesOrderMaterialMapper.deleteBatchIds(materialsToDelete);
        if (CollUtil.isNotEmpty(structuresToDelete)) salesOrderStructureMapper.deleteBatchIds(structuresToDelete);
        if (CollUtil.isNotEmpty(curtainsToDelete)) salesOrderCurtainMapper.deleteBatchIds(curtainsToDelete);
    }

    /**
     * 将面单简化窗帘 VO 转为标准窗帘 VO，以便复用 mergeCurtainSubRows。
     *
     * <p>透传各层的 id 字段，使 merge 逻辑能正确识别更新/新增行。</p>
     */
    private List<ZcSalesOrderCurtainCreateVO> toStandardCurtainVOs(
            List<ZcSalesOrderFabricCurtainCreateVO> fabricCurtains) {
        if (CollUtil.isEmpty(fabricCurtains)) return Collections.emptyList();
        return fabricCurtains.stream().map(fc -> {
            ZcSalesOrderCurtainCreateVO c = new ZcSalesOrderCurtainCreateVO();
            c.setId(fc.getId()); // 透传 id，供 merge 区分更新/新增
            c.setAmount(fc.getAmount());
            c.setNote(fc.getNote());
            c.setQuantity(fc.getQuantity());
            if (CollUtil.isNotEmpty(fc.getStructures())) {
                c.setStructures(fc.getStructures().stream().map(fs -> {
                    ZcSalesOrderStructureCreateVO s = new ZcSalesOrderStructureCreateVO();
                    s.setId(fs.getId()); // 透传 id，供 merge 区分更新/新增
                    s.setMaterials(fs.getMaterials());
                    return s;
                }).collect(Collectors.toList()));
            }
            return c;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = ZC_SALES_ORDER_UPDATE_SUCCESS)
    public void updateSalesOrder(ZcSalesOrderUpdateReqVO updateReqVO) {
        ZcSalesOrderDO existing = prepareOrderUpdate(updateReqVO.getId());
        ZcSalesOrderDO updateDO = BeanUtils.toBean(updateReqVO, ZcSalesOrderDO.class);
        clearProtectedFields(updateDO);
        normalizeOrderUpdateFields(updateDO);
        logisticsService.resolveLogisticsForOrder(updateDO);
        updateDO.setSets(CollUtil.size(updateReqVO.getCurtains()));
        salesOrderMapper.updateById(updateDO);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(existing, ZcSalesOrderUpdateReqVO.class));
        LogRecordContext.putVariable("orderNo", existing.getOrderNo());
        mergeCurtainSubRows(updateReqVO.getId(), updateReqVO.getCurtains());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_FABRIC_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = ZC_SALES_ORDER_FABRIC_UPDATE_SUCCESS)
    public void updateFabricSalesOrder(ZcSalesOrderFabricUpdateReqVO updateReqVO) {
        ZcSalesOrderDO existing = prepareOrderUpdate(updateReqVO.getId());
        ZcSalesOrderDO updateDO = BeanUtils.toBean(updateReqVO, ZcSalesOrderDO.class);
        clearProtectedFields(updateDO);
        normalizeOrderUpdateFields(updateDO);
        logisticsService.resolveLogisticsForOrder(updateDO);
        updateDO.setSets(CollUtil.size(updateReqVO.getCurtains()));
        salesOrderMapper.updateById(updateDO);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(existing, ZcSalesOrderFabricUpdateReqVO.class));
        LogRecordContext.putVariable("orderNo", existing.getOrderNo());
        mergeCurtainSubRows(updateReqVO.getId(), toStandardCurtainVOs(updateReqVO.getCurtains()));
    }

    /**
     * 更新前置校验：订单必须存在且未确认（confirmTime 为 null）
     *
     * <p>不再提前删除子表，子表的增删改由 {@link #mergeCurtainSubRows} 完成。</p>
     *
     * @return 旧订单记录（供日志 diff 使用）
     */
    private ZcSalesOrderDO prepareOrderUpdate(Long orderId) {
        ZcSalesOrderDO existing = validateSalesOrderExists(orderId);
//        if (existing.getConfirmTime() != null) {
//            throw exception(SALES_ORDER_CONFIRMED_CANNOT_UPDATE);
//        }
        return existing;
    }

    /** 清空系统管理字段，防止更新接口覆写不应修改的列 */
    private void clearProtectedFields(ZcSalesOrderDO updateDO) {
        updateDO.setOrderNo(null);
        updateDO.setTypes(null);
        updateDO.setPayStatus(null);
        updateDO.setStatus(null);
        updateDO.setIsExpedited(null);
        updateDO.setAmountReceived(null);
        updateDO.setConfirmTime(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = ZC_SALES_ORDER_DELETE_SUCCESS)
    public void deleteSalesOrder(Long id) {
        // 校验订单存在
        ZcSalesOrderDO order = validateSalesOrderExists(id);
        // confirm_time 不为空表示已确认，禁止删除
        if (order.getConfirmTime() != null) {
            throw exception(SALES_ORDER_CONFIRMED_CANNOT_DELETE);
        }
        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
        // 级联删除三层子表，再删主记录，防止孤立数据
        salesOrderCurtainMapper.deleteByOrderId(id);
        salesOrderStructureMapper.deleteByOrderId(id);
        salesOrderMaterialMapper.deleteByOrderId(id);
        salesOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSalesOrderListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        salesOrderCurtainMapper.deleteByOrderIds(ids);
        salesOrderStructureMapper.deleteByOrderIds(ids);
        salesOrderMaterialMapper.deleteByOrderIds(ids);
        salesOrderMapper.deleteByIds(ids);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_CONFIRM_SUB_TYPE, bizNo = "{{#id}}",
            success = ZC_SALES_ORDER_CONFIRM_SUCCESS)
    public void confirmSalesOrder(Long id) {
        // 1. 校验订单存在且当前状态为待确认
        ZcSalesOrderDO order = validateSalesOrderExists(id);
        if (!ZcSalesOrderStatusEnum.UNCONFIRMED.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_STATUS_NOT_UNCONFIRMED);
        }

        // 2. 更新状态为已确认，记录确认时间
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getStatus, ZcSalesOrderStatusEnum.CONFIRMED.name())
                .set(ZcSalesOrderDO::getConfirmTime, LocalDateTime.now())
                .eq(ZcSalesOrderDO::getId, id));

        // 根据订单类型同步更新子行状态：面料单更新产品行，成品单更新窗帘行
//        if (ZcOrderTypeEnum.FABRIC.name().equals(order.getTypes())) {
//            salesOrderProductMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.NOT_PEILIAO.name());
//        } else {
//            salesOrderCurtainMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.NOT_PEILIAO.name());
//        }
        salesOrderCurtainMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.NOT_PEILIAO.name());

        // 3. 从客户账户余额中扣除订单金额，并记录余额变动流水
        if (order.getCustomerId() != null && order.getAmount() != null) {
            BigDecimal delta = order.getAmount().negate();
            ZcCustomerDO customer = customerService.getCustomer(order.getCustomerId());
            BigDecimal balanceBefore = customer != null && customer.getBalance() != null
                    ? customer.getBalance() : BigDecimal.ZERO;
            BigDecimal balanceAfter = balanceBefore.add(delta);

            customerService.adjustBalance(order.getCustomerId(), delta);

            customerBalanceLogService.createLog(ZcCustomerBalanceLogDO.builder()
                    .customerId(order.getCustomerId())
                    .changeAmount(delta)
                    .balanceBefore(balanceBefore)
                    .balanceAfter(balanceAfter)
                    .bizType(ZcCustomerBalanceBizTypeEnum.ORDER_CONFIRM.name())
                    .refType(ZcRefTypeEnum.SALES_ORDER.name())
                    .refId(order.getId())
                    .refNo(order.getOrderNo())
                    .build());
        }
        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_CANCEL_CONFIRM_SUB_TYPE, bizNo = "{{#id}}",
            success = ZC_SALES_ORDER_CANCEL_CONFIRM_SUCCESS)
    public void cancelConfirmSalesOrder(Long id) {
        // 1. 校验订单存在且当前状态为已确认
        ZcSalesOrderDO order = validateSalesOrderExists(id);
        if (!ZcSalesOrderStatusEnum.CONFIRMED.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_STATUS_NOT_CONFIRMED);
        }

        // 2. 禁止有收款记录时取消确认：取消确认会将订单金额退回余额，若已收款则余额会虚增
        if (order.getAmountReceived() != null && order.getAmountReceived().compareTo(BigDecimal.ZERO) > 0) {
            throw exception(SALES_ORDER_HAS_RECEIVED_AMOUNT);
        }

        // 3. 禁止存在已裁剪用料时取消确认：库存已扣减，须先逐条撤销裁剪归还库存
        long cutCount = salesOrderMaterialMapper.countByOrderIdAndStatus(
                id, ZcSalesOrderMaterialStatusEnum.HAVE_PEILIAO.name());
        if (cutCount > 0) {
            throw exception(SALES_ORDER_HAS_CUT_MATERIAL);
        }

        // 4. 更新状态回未确认，清空确认时间
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getStatus, ZcSalesOrderStatusEnum.UNCONFIRMED.name())
                .set(ZcSalesOrderDO::getConfirmTime, null)
                .eq(ZcSalesOrderDO::getId, id));

        // 根据订单类型同步更新子行状态回未确认：面料单更新产品行，成品单更新窗帘行
//        if (ZcOrderTypeEnum.FABRIC.name().equals(order.getTypes())) {
//            salesOrderProductMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.UNCONFIRMED.name());
//        } else {
//            salesOrderCurtainMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.UNCONFIRMED.name());
//        }
        salesOrderCurtainMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.UNCONFIRMED.name());

        // 4. 将订单金额退回客户账户余额，并记录余额变动流水
        if (order.getCustomerId() != null && order.getAmount() != null) {
            BigDecimal delta = order.getAmount();
            ZcCustomerDO customer = customerService.getCustomer(order.getCustomerId());
            BigDecimal balanceBefore = customer != null && customer.getBalance() != null
                    ? customer.getBalance() : BigDecimal.ZERO;
            BigDecimal balanceAfter = balanceBefore.add(delta);

            customerService.adjustBalance(order.getCustomerId(), delta);

            customerBalanceLogService.createLog(ZcCustomerBalanceLogDO.builder()
                    .customerId(order.getCustomerId())
                    .changeAmount(delta)
                    .balanceBefore(balanceBefore)
                    .balanceAfter(balanceAfter)
                    .bizType(ZcCustomerBalanceBizTypeEnum.ORDER_UNCONFIRM.name())
                    .refType(ZcRefTypeEnum.SALES_ORDER.name())
                    .refId(order.getId())
                    .refNo(order.getOrderNo())
                    .build());
        }
        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_COMPLETE_SUB_TYPE, bizNo = "{{#id}}",
            success = ZC_SALES_ORDER_COMPLETE_SUCCESS)
    public void completeSalesOrder(Long id) {
        // 1. 校验订单存在，且状态不是未确认（未确认订单禁止直接完成），且未重复完成
        ZcSalesOrderDO order = validateSalesOrderExists(id);
        if (ZcSalesOrderStatusEnum.UNCONFIRMED.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_UNCONFIRMED_CANNOT_COMPLETE);
        }
        if (ZcSalesOrderStatusEnum.COMPLETE.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_ALREADY_COMPLETE);
        }

        // 2. 更新订单状态为完成
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getStatus, ZcSalesOrderStatusEnum.COMPLETE.name())
                .eq(ZcSalesOrderDO::getId, id));

        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_DISCARD_SUB_TYPE, bizNo = "{{#id}}",
            success = ZC_SALES_ORDER_DISCARD_SUCCESS)
    public void discardSalesOrder(Long id) {
        // 1. 校验订单存在，且未重复废弃
        ZcSalesOrderDO order = validateSalesOrderExists(id);
        if (ZcSalesOrderStatusEnum.DISCARDED.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_ALREADY_DISCARDED);
        }

        // 2. 更新订单状态为废弃
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getStatus, ZcSalesOrderStatusEnum.DISCARDED.name())
                .eq(ZcSalesOrderDO::getId, id));

        // 3. 同步更新子行状态
        salesOrderCurtainMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.DISCARDED.name());
        salesOrderProductMapper.updateStatusByOrderId(id, ZcSalesOrderStatusEnum.DISCARDED.name());

        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    @Override
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_MARK_EXPEDITED_SUB_TYPE, bizNo = "{{#orderId}}",
            success = ZC_SALES_ORDER_MARK_EXPEDITED_SUCCESS)
    public void markExpedited(Long orderId) {
        // 校验订单存在
        ZcSalesOrderDO order = validateSalesOrderExists(orderId);
        // 将加急标志设置为 true
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getIsExpedited, true)
                .eq(ZcSalesOrderDO::getId, orderId));
        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    @Override
    @LogRecord(type = ZC_SALES_ORDER_TYPE, subType = ZC_SALES_ORDER_CANCEL_EXPEDITED_SUB_TYPE, bizNo = "{{#orderId}}",
            success = ZC_SALES_ORDER_CANCEL_EXPEDITED_SUCCESS)
    public void cancelExpedited(Long orderId) {
        // 校验订单存在
        ZcSalesOrderDO order = validateSalesOrderExists(orderId);
        // 将加急标志设置为 false
        salesOrderMapper.update(null, Wrappers.<ZcSalesOrderDO>lambdaUpdate()
                .set(ZcSalesOrderDO::getIsExpedited, false)
                .eq(ZcSalesOrderDO::getId, orderId));
        // 记录操作日志上下文
        LogRecordContext.putVariable("orderNo", order.getOrderNo());
    }

    private ZcSalesOrderDO validateSalesOrderExists(Long id) {
        ZcSalesOrderDO order = salesOrderMapper.selectById(id);
        if (order == null) {
            throw exception(SALES_ORDER_NOT_EXISTS);
        }
        return order;
    }

    @Override
    public ZcSalesOrderDO getSalesOrder(Long id) {
        ZcSalesOrderDO order = salesOrderMapper.selectById(id);
        if (order != null && ZcSalesOrderStatusEnum.DISCARDED.name().equals(order.getStatus())) {
            throw exception(SALES_ORDER_DISCARDED_CANNOT_VIEW);
        }
        return order;
    }

    @Override
    public PageResult<ZcSalesOrderRespVO> getSalesOrderPage(ZcSalesOrderPageReqVO pageReqVO) {
        return salesOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ZcSalesOrderCustomerStatisticsRespVO> getCustomerStatistics(
            ZcSalesOrderCustomerStatisticsReqVO reqVO) {
        return salesOrderMapper.selectCustomerStatistics(reqVO);
    }

    @Override
    public List<ZcSalesOrderMaterialProductStatisticsRespVO> getMaterialProductStatistics(
            ZcSalesOrderCustomerStatisticsReqVO reqVO) {
        return salesOrderMaterialMapper.selectProductSpecStatistics(reqVO);
    }

    @Override
    public ZcSalesOrderDetailRespVO getSalesOrderDetail(Long orderId) {
        // 1. 查询订单主表信息（含客户名、物流名、创建人名等关联字段）
        ZcSalesOrderRespVO orderVO = salesOrderMapper.selectVOById(orderId);
        if (orderVO == null) {
            throw exception(SALES_ORDER_NOT_EXISTS);
        }
        if (ZcSalesOrderStatusEnum.DISCARDED.name().equals(orderVO.getStatus())) {
            throw exception(SALES_ORDER_DISCARDED_CANNOT_VIEW);
        }

        // 3. 查询该订单下所有窗帘行
        List<ZcSalesOrderCurtainDO> curtainList = salesOrderCurtainMapper.selectListByOrderId(orderId);
        if (CollUtil.isEmpty(curtainList)) {
            ZcSalesOrderDetailRespVO emptyResult = BeanUtils.toBean(orderVO, ZcSalesOrderDetailRespVO.class);
            emptyResult.setCurtains(Collections.emptyList());
            return emptyResult;
        }

        // 4. 查询该订单下所有结构行与用料明细（一次性批量取出，避免 N+1）
        List<ZcSalesOrderStructureDO> structureList = salesOrderStructureMapper.selectListByOrderId(orderId);
        List<ZCSalesOrderMaterialDO> materialList = salesOrderMaterialMapper.selectListByOrderId(orderId);

        // 5. 构建窗帘款式名称 Map
        Set<Long> curtainIds = curtainList.stream()
                .map(ZcSalesOrderCurtainDO::getCurtainId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> curtainNameMap = convertMap(curtainMapper.selectList(ZcCurtainDO::getId, curtainIds), ZcCurtainDO::getId, ZcCurtainDO::getName);

        // 6. 构建结构名称与安装工艺名称 Map
        Set<Long> structureIds = structureList.stream()
                .map(ZcSalesOrderStructureDO::getStructureId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> structureNameMap = convertMap(curtainStructureMapper.selectList(ZcCurtainStructureDO::getId, structureIds), ZcCurtainStructureDO::getId, ZcCurtainStructureDO::getName);
        Set<Long> installProcessIds = structureList.stream()
                .map(ZcSalesOrderStructureDO::getInstallProcessId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> installProcessNameMap = convertMap(curtainInstallProcessMapper.selectList(ZcCurtainInstallProcessDO::getId, installProcessIds), ZcCurtainInstallProcessDO::getId, ZcCurtainInstallProcessDO::getName);

        // 7. 构建组件类型名称、产品名称、批次号 Map
        Set<Long> elementIds = materialList.stream()
                .map(ZCSalesOrderMaterialDO::getElementId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<ZcCurtainStructureElementDO> elementList = curtainStructureElementMapper.selectList(ZcCurtainStructureElementDO::getId, elementIds);
        Map<Long, String> elementNameMap = convertMap(elementList, ZcCurtainStructureElementDO::getId, ZcCurtainStructureElementDO::getName);
        Map<Long, Boolean> elementIsPrintMap = toMapSkipNullValues(elementList, ZcCurtainStructureElementDO::getId, ZcCurtainStructureElementDO::getIsPrint);
        Map<Long, Boolean> elementIsCalMaterialMap = toMapSkipNullValues(elementList, ZcCurtainStructureElementDO::getId, ZcCurtainStructureElementDO::getIsCalMaterial);
        Set<Long> productIds = materialList.stream()
                .map(ZCSalesOrderMaterialDO::getProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<ZcProductDO> productList = productMapper.selectList(ZcProductDO::getId, productIds);
        Map<Long, String> productNameMap = convertMap(productList, ZcProductDO::getId, ZcProductDO::getName);
        Set<Long> batchIds = materialList.stream()
                .map(ZCSalesOrderMaterialDO::getBatchId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<ZcProductBatchDO> batchList = productBatchMapper.selectList(ZcProductBatchDO::getId, batchIds);
        Map<Long, String> batchNoMap = toMapSkipNullValues(batchList, ZcProductBatchDO::getId, ZcProductBatchDO::getBatchNo);
        // 复用已查出的批次列表，避免重复查库（barcode 可为空，须跳过 null 避免 toMap NPE）
        Map<Long, String> batchBarcodeMap = toMapSkipNullValues(batchList, ZcProductBatchDO::getId, ZcProductBatchDO::getBarcode);

        // 8. 按结构行 ID 分组用料明细
        Map<Long, List<ZCSalesOrderMaterialDetailRespVO>> materialsByStructureId = materialList.stream()
                .collect(Collectors.groupingBy(
                        ZCSalesOrderMaterialDO::getOrderStructureId,
                        Collectors.mapping(m -> {
                            ZCSalesOrderMaterialDetailRespVO vo = BeanUtils.toBean(m, ZCSalesOrderMaterialDetailRespVO.class);
                            vo.setElementName(elementNameMap.get(m.getElementId()));
                            vo.setElementIsPrint(elementIsPrintMap.get(m.getElementId()));
                            vo.setElementIsCalMaterial(elementIsCalMaterialMap.get(m.getElementId()));
                            vo.setProductName(productNameMap.get(m.getProductId()));
                            vo.setBatchNo(batchNoMap.get(m.getBatchId()));
                            vo.setBarcode(batchBarcodeMap.get(m.getBatchId()));
                            return vo;
                        }, Collectors.toList())
                ));

        // 9. 按窗帘行 ID 分组结构行
        Map<Long, List<ZcSalesOrderStructureDetailRespVO>> structuresByCurtainId = structureList.stream()
                .collect(Collectors.groupingBy(
                        ZcSalesOrderStructureDO::getOrderCurtainId,
                        Collectors.mapping(s -> {
                            ZcSalesOrderStructureDetailRespVO vo = BeanUtils.toBean(s, ZcSalesOrderStructureDetailRespVO.class);
                            vo.setStructureName(structureNameMap.get(s.getStructureId()));
                            vo.setInstallProcessName(installProcessNameMap.get(s.getInstallProcessId()));
                            vo.setMaterials(materialsByStructureId.getOrDefault(s.getId(), Collections.emptyList()));
                            return vo;
                        }, Collectors.toList())
                ));

        // 10. 组装窗帘行列表
        List<ZcSalesOrderCurtainDetailRespVO> curtains = curtainList.stream().map(curtain -> {
            ZcSalesOrderCurtainDetailRespVO vo = BeanUtils.toBean(curtain, ZcSalesOrderCurtainDetailRespVO.class);
            vo.setCurtainName(curtainNameMap.get(curtain.getCurtainId()));
            vo.setStructures(structuresByCurtainId.getOrDefault(curtain.getId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());

        // 11. 将订单主表信息与窗帘明细合并为完整详情 VO
        ZcSalesOrderDetailRespVO result = BeanUtils.toBean(orderVO, ZcSalesOrderDetailRespVO.class);
        result.setCurtains(curtains);
        return result;
    }

    @Override
    public ZcSalesOrderDetailRespVO getSalesOrderDetailByOrderNo(String orderNo) {
        // 按订单号精确查出主键，再复用已有的详情组装逻辑
        ZcSalesOrderDO order = salesOrderMapper.selectOne(ZcSalesOrderDO::getOrderNo, orderNo);
        if (order == null) {
            throw exception(SALES_ORDER_NOT_EXISTS);
        }
        return getSalesOrderDetail(order.getId());
    }

    // ======================== PDF 生成 ========================

    @Override
    public byte[] generateSalesOrderPdf(Long orderId) {
        // 复用 getSalesOrderDetail，一次获取订单主表信息及三层嵌套明细
        ZcSalesOrderDetailRespVO detail = getSalesOrderDetail(orderId);
        List<ZcSalesOrderCurtainDetailRespVO> curtains = detail.getCurtains();
        try {
            return buildSalesOrderPdf(detail, curtains);
        } catch (Exception e) {
            throw new RuntimeException("销售订单 PDF 生成失败", e);
        }
    }

    /**
     * 使用 OpenPDF 构建 A4 横向销售订单 PDF。
     *
     * <p>布局：标题 → 订单基本信息区（6列网格）→ 窗帘明细区（逐层展开：窗帘行→结构行→用料明细）</p>
     */
    private byte[] buildSalesOrderPdf(ZcSalesOrderRespVO order,
                                       List<ZcSalesOrderCurtainDetailRespVO> curtains) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // A4 横向，左右上下边距各 36pt
        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 40, 36);
        PdfWriter.getInstance(doc, baos);
        doc.open();

        // 中文字体：STSong-Light 是 PDF 标准 CJK 字体，无需嵌入字体文件
        BaseFont bfCn = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font titleFont  = new Font(bfCn, 16, Font.BOLD);
        Font headFont   = new Font(bfCn, 9,  Font.BOLD);
        Font headWhite  = new Font(bfCn, 9,  Font.BOLD, Color.WHITE);
        Font normalFont = new Font(bfCn, 9);
        Font smallBold  = new Font(bfCn, 8,  Font.BOLD);
        Font smallFont  = new Font(bfCn, 8);
        Font tinyFont   = new Font(bfCn, 7);
        Font tinyBold   = new Font(bfCn, 7,  Font.BOLD);

        // ---- 标题区 ----
        Paragraph titlePara = new Paragraph("销   售   订   单", titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        titlePara.setSpacingAfter(4);
        doc.add(titlePara);

        String printTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Paragraph subTitle = new Paragraph(
            "订单号：" + pdfStr(order.getOrderNo()) + "          打印日期：" + printTime, headFont);
        subTitle.setAlignment(Element.ALIGN_CENTER);
        subTitle.setSpacingAfter(10);
        doc.add(subTitle);

        // ---- 订单基本信息（6 列：标签-值 × 3 组） ----
        PdfPTable infoTable = new PdfPTable(new float[]{1.6f, 3f, 1.6f, 3f, 1.6f, 3f});
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(10);

        addInfoRow(infoTable, headFont, normalFont,
            "客户",    pdfStr(order.getCustomerName()),
            "下单日期", pdfDate(order.getOrderDate()),
            "交付日期", pdfDate(order.getDeliveryDate()));
        addInfoRow(infoTable, headFont, normalFont,
            "手机",    pdfStr(order.getMobile()),
            "物流",    pdfStr(order.getLogisticName()),
            "收货人",  pdfStr(order.getReceiver()));
        addInfoRow(infoTable, headFont, normalFont,
            "订单状态", pdfStatus(order.getStatus()),
            "结算状态", pdfPayStatus(order.getPayStatus()),
            "是否加急", Boolean.TRUE.equals(order.getIsExpedited()) ? "是" : "否");
        addInfoRow(infoTable, headFont, normalFont,
            "总金额",   pdfMoney(order.getTotalAmount()),
            "订单金额", pdfMoney(order.getAmount()),
            "已收金额", pdfMoney(order.getAmountReceived()));
        addInfoRow(infoTable, headFont, normalFont,
            "运费",    pdfMoney(order.getFreight()),
            "创建人",  pdfStr(order.getCreatorName()),
            "送货地址", pdfStr(order.getDeliveryAddress()));

        // 备注：标签 + 值跨 5 列
        infoTable.addCell(pdfLabelCell("备注", headFont));
        PdfPCell noteCell = pdfValueCell(pdfStr(order.getNote()), normalFont);
        noteCell.setColspan(5);
        infoTable.addCell(noteCell);

        doc.add(infoTable);

        // ---- 窗帘明细区 ----
        if (CollUtil.isNotEmpty(curtains)) {
            // 区块标题栏
            PdfPTable sectionBar = new PdfPTable(1);
            sectionBar.setWidthPercentage(100);
            sectionBar.setSpacingAfter(6);
            PdfPCell sectionCell = new PdfPCell(new Phrase("窗   帘   明   细", headWhite));
            sectionCell.setBackgroundColor(new Color(41, 98, 142));
            sectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sectionCell.setPadding(5);
            sectionBar.addCell(sectionCell);
            doc.add(sectionBar);

            int curtainNo = 1;
            for (ZcSalesOrderCurtainDetailRespVO curtain : curtains) {
                // -- 窗帘行标题（蓝色背景，白色字体） --
                String curtainTitle = String.format(
                    "【%d】 房间：%s    款式：%s    褶倍：%s    折扣率：%s    金额：%s    配件：%s",
                    curtainNo++,
                    pdfStr(curtain.getRoom()), pdfStr(curtain.getCurtainName()),
                    pdfStr(curtain.getPleatRatioValue()), pdfStr(curtain.getDiscountRate()),
                    pdfMoney(curtain.getAmount()), pdfStr(curtain.getMountings()));

                PdfPTable curtainBar = new PdfPTable(1);
                curtainBar.setWidthPercentage(100);
                curtainBar.setSpacingBefore(4);
                curtainBar.setSpacingAfter(1);
                PdfPCell curtainCell = new PdfPCell(
                    new Phrase(curtainTitle, new Font(bfCn, 9, Font.BOLD, Color.WHITE)));
                curtainCell.setBackgroundColor(new Color(70, 130, 180));
                curtainCell.setPadding(4);
                curtainBar.addCell(curtainCell);
                doc.add(curtainBar);

                if (CollUtil.isEmpty(curtain.getStructures())) {
                    continue;
                }

                for (ZcSalesOrderStructureDetailRespVO structure : curtain.getStructures()) {
                    // -- 结构行（灰色背景，98% 宽度右对齐） --
                    PdfPTable structTable = new PdfPTable(
                        new float[]{2f, 1.2f, 1.2f, 2f, 1.5f, 1.5f, 1f, 1.2f, 1.2f, 2f});
                    structTable.setWidthPercentage(98);
                    structTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    structTable.setSpacingAfter(1);

                    // 结构行表头
                    String[] structHeaders = {"结构", "高(cm)", "宽(cm)", "安装工艺", "打开方式",
                                              "加工类型", "定型", "褶数", "褶距", "备注"};
                    for (String h : structHeaders) {
                        PdfPCell hc = new PdfPCell(new Phrase(h, smallBold));
                        hc.setBackgroundColor(new Color(215, 215, 215));
                        hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        hc.setPadding(3);
                        structTable.addCell(hc);
                    }
                    // 结构行数据
                    pdfAddCell(structTable, pdfStr(structure.getStructureName()), smallFont, Element.ALIGN_LEFT);
                    pdfAddCell(structTable, pdfStr(structure.getHeight()),        smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getWidth()),         smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getInstallProcessName()), smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getOpenMethod()),    smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getProcessType()),   smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, Boolean.TRUE.equals(structure.getIsShaping()) ? "是" : "否",
                                           smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getPleatsNum()),     smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getPleatsDistance()), smallFont, Element.ALIGN_CENTER);
                    pdfAddCell(structTable, pdfStr(structure.getNote()),           smallFont, Element.ALIGN_LEFT);
                    doc.add(structTable);

                    // -- 用料明细（94% 宽度，进一步右对齐） --
                    if (CollUtil.isEmpty(structure.getMaterials())) {
                        continue;
                    }
                    PdfPTable matTable = new PdfPTable(
                        new float[]{2f, 3f, 2f, 1.5f, 1.5f, 1f, 1.2f, 1.5f});
                    matTable.setWidthPercentage(94);
                    matTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    matTable.setSpacingAfter(4);

                    // 用料表头
                    String[] matHeaders = {"组件类型", "产品名称", "批次号", "单价", "用量", "单位", "折扣率", "小计"};
                    for (String h : matHeaders) {
                        PdfPCell hc = new PdfPCell(new Phrase(h, tinyBold));
                        hc.setBackgroundColor(new Color(235, 235, 235));
                        hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        hc.setPadding(2);
                        matTable.addCell(hc);
                    }
                    // 用料数据行
                    for (ZCSalesOrderMaterialDetailRespVO mat : structure.getMaterials()) {
                        pdfAddCell(matTable, pdfStr(mat.getElementName()),  tinyFont, Element.ALIGN_LEFT);
                        pdfAddCell(matTable, pdfStr(mat.getProductName()),  tinyFont, Element.ALIGN_LEFT);
                        pdfAddCell(matTable, pdfStr(mat.getBatchNo()),      tinyFont, Element.ALIGN_CENTER);
                        pdfAddCell(matTable, pdfMoney(mat.getPrice()),      tinyFont, Element.ALIGN_RIGHT);
                        pdfAddCell(matTable, pdfStr(mat.getQuantity()),     tinyFont, Element.ALIGN_CENTER);
                        pdfAddCell(matTable, pdfStr(mat.getUnitValue()),    tinyFont, Element.ALIGN_CENTER);
                        pdfAddCell(matTable, pdfStr(mat.getDiscountRate()), tinyFont, Element.ALIGN_CENTER);
                        pdfAddCell(matTable, pdfMoney(mat.getAmount()),     tinyFont, Element.ALIGN_RIGHT);
                    }
                    doc.add(matTable);
                }

                // 窗帘行备注（若有）
                if (curtain.getNote() != null && !curtain.getNote().isEmpty()) {
                    Paragraph noteP = new Paragraph("    备注：" + curtain.getNote(), smallFont);
                    noteP.setSpacingAfter(2);
                    doc.add(noteP);
                }
            }

            // ---- 合计行 ----
            Paragraph totalPara = new Paragraph(
                "订单合计：总金额 " + pdfMoney(order.getTotalAmount()) +
                "    订单金额 " + pdfMoney(order.getAmount()) +
                "    已收款 " + pdfMoney(order.getAmountReceived()) +
                "    运费 " + pdfMoney(order.getFreight()), headFont);
            totalPara.setSpacingBefore(10);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalPara);
        }

        doc.close();
        return baos.toByteArray();
    }

    // ---- PDF 辅助方法 ----

    /** 向信息网格表添加一行（3组：标签 + 值） */
    private static void addInfoRow(PdfPTable table, Font labelFont, Font valueFont,
                                   String l1, String v1, String l2, String v2, String l3, String v3) {
        table.addCell(pdfLabelCell(l1, labelFont));
        table.addCell(pdfValueCell(v1, valueFont));
        table.addCell(pdfLabelCell(l2, labelFont));
        table.addCell(pdfValueCell(v2, valueFont));
        table.addCell(pdfLabelCell(l3, labelFont));
        table.addCell(pdfValueCell(v3, valueFont));
    }

    /** 创建标签单元格（浅灰背景，右对齐） */
    private static PdfPCell pdfLabelCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(240, 240, 240));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4);
        return cell;
    }

    /** 创建值单元格（白色背景，左对齐） */
    private static PdfPCell pdfValueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4);
        return cell;
    }

    /** 向普通数据表添加一个单元格 */
    private static void pdfAddCell(PdfPTable table, String text, Font font, int hAlign) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setHorizontalAlignment(hAlign);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);
        cell.setMinimumHeight(16f);
        table.addCell(cell);
    }

    /** null → 空字符串 */
    private static String pdfStr(Object val) {
        return val == null ? "" : val.toString();
    }

    /** BigDecimal → ¥x.xx 格式，null 时返回 "0.00" */
    private static String pdfMoney(BigDecimal val) {
        if (val == null) {
            return "0.00";
        }
        return "¥" + val.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /** LocalDate → yyyy-MM-dd，null 时返回空字符串 */
    private static String pdfDate(LocalDate date) {
        return date == null ? "" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /** 订单状态 code → 中文描述 */
    private static String pdfStatus(String status) {
        if (status == null) {
            return "";
        }
        try {
            return ZcSalesOrderStatusEnum.valueOf(status).getLabel();
        } catch (IllegalArgumentException e) {
            return status;
        }
    }

    /** 支付状态 code → 中文描述 */
    private static String pdfPayStatus(String payStatus) {
        if (payStatus == null) {
            return "";
        }
        try {
            return ZcSalesOrderPayStatusEnum.valueOf(payStatus).getLabel();
        } catch (IllegalArgumentException e) {
            return payStatus;
        }
    }

    /**
     * 构建 Map，跳过 key 或 value 为 null 的项。
     * {@link Collectors#toMap} 遇到 null value 会 NPE，批次 barcode 等字段可为空时需用此方法。
     */
    private static <T, K, V> Map<K, V> toMapSkipNullValues(Collection<T> list, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>(list.size());
        for (T item : list) {
            K key = keyFunc.apply(item);
            V value = valueFunc.apply(item);
            if (key != null && value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

}