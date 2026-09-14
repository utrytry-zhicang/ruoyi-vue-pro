package cn.iocoder.yudao.module.zc.controller.admin.salesorder;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.zc.controller.admin.salesorder.vo.*;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZcSalesOrderDO;
import cn.iocoder.yudao.module.zc.enums.ZcSalesOrderStatusEnum;
import cn.iocoder.yudao.module.zc.service.salesorder.ZCSalesOrderMaterialService;
import cn.iocoder.yudao.module.zc.service.salesorder.ZcSalesOrderService;
import cn.iocoder.yudao.module.zc.service.processnode.ZcOrderProcessRecordService;

@Tag(name = "管理后台 - 销售订单")
@RestController
@RequestMapping("/zc/sales-order")
@Validated
public class ZcSalesOrderController {

    @Resource
    private ZcSalesOrderService salesOrderService;
    @Resource
    private ZCSalesOrderMaterialService salesOrderMaterialService;
    @Resource
    private ZcOrderProcessRecordService processRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建销售订单（整单，含窗帘行→结构行→用料明细）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:create')")
    public CommonResult<Long> createSalesOrder(@Valid @RequestBody ZcSalesOrderCreateReqVO createReqVO) {
        return success(salesOrderService.createSalesOrder(createReqVO));
    }

    @PostMapping("/fabric/create")
    @Operation(summary = "创建面单（类型固定 FABRIC，curtainId/structureId 可为空，含窗帘行→结构行→用料明细）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:create')")
    public CommonResult<Long> createFabricSalesOrder(@Valid @RequestBody ZcSalesOrderFabricCreateReqVO createReqVO) {
        return success(salesOrderService.createFabricSalesOrder(createReqVO));
    }

    @PutMapping("/fabric/update")
    @Operation(summary = "更新面单（类型固定 FABRIC，已确认订单禁止修改）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> updateFabricSalesOrder(@Valid @RequestBody ZcSalesOrderFabricUpdateReqVO updateReqVO) {
        salesOrderService.updateFabricSalesOrder(updateReqVO);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "整单更新销售订单（含窗帘行→结构行→用料明细，已确认订单禁止修改）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> updateSalesOrder(@Valid @RequestBody ZcSalesOrderUpdateReqVO updateReqVO) {
        salesOrderService.updateSalesOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售订单（已确认的订单禁止删除）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:delete')")
    public CommonResult<Boolean> deleteSalesOrder(@RequestParam("id") Long id) {
        salesOrderService.deleteSalesOrder(id);
        return success(true);
    }

//    @DeleteMapping("/delete-list")
//    @Parameter(name = "ids", description = "编号", required = true)
//    @Operation(summary = "批量删除销售订单")
//                @PreAuthorize("@ss.hasPermission('zc:sales-order:delete')")
//    public CommonResult<Boolean> deleteSalesOrderList(@RequestParam("ids") List<Long> ids) {
//        salesOrderService.deleteSalesOrderListByIds(ids);
//        return success(true);
//    }

    @GetMapping("/get")
    @Operation(summary = "获得销售订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<ZcSalesOrderRespVO> getSalesOrder(@RequestParam("id") Long id) {
        ZcSalesOrderDO salesOrder = salesOrderService.getSalesOrder(id);
        return success(BeanUtils.toBean(salesOrder, ZcSalesOrderRespVO.class));
    }

    @GetMapping("/detail")
    @Operation(summary = "获得销售订单完整详情（主表信息 + 窗帘→结构→用料 三层嵌套）")
    @Parameters({
            @Parameter(name = "id",      description = "销售订单 ID，与 orderNo 二选一", example = "1024"),
            @Parameter(name = "orderNo", description = "销售订单编号，与 id 二选一，优先级高于 id", example = "ZC1020240101000001")
    })
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<ZcSalesOrderDetailRespVO> getSalesOrderDetail(
            @RequestParam(value = "id",      required = false) Long id,
            @RequestParam(value = "orderNo", required = false) String orderNo) {
        if (StrUtil.isNotBlank(orderNo)) {
            return success(salesOrderService.getSalesOrderDetailByOrderNo(orderNo));
        }
        return success(salesOrderService.getSalesOrderDetail(id));
    }

    @GetMapping("/process-record")
    @Operation(summary = "获得销售订单工序记录详情（完整窗帘结构 + 各层工序记录）",
            description = "先加载订单完整窗帘结构（同 /detail），在各层级挂载全部工序节点的 processRecords（按时间升序）；"
                    + "无工序的窗帘/结构/用料仍会展示")
    @Parameter(name = "id", description = "销售订单 ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<ZcSalesOrderProcessRecordDetailRespVO> getSalesOrderProcessRecordDetail(
            @RequestParam("id") Long id) {
        return success(processRecordService.getSalesOrderProcessRecordDetail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售订单分页")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<PageResult<ZcSalesOrderRespVO>> getSalesOrderPage(@Valid ZcSalesOrderPageReqVO pageReqVO) {
        return success(salesOrderService.getSalesOrderPage(pageReqVO));
    }

    @GetMapping("/app/page")
    @Operation(summary = "获得销售订单分页（支持订单状态多选筛选，默认排除未确认和已完成状态）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<PageResult<ZcSalesOrderRespVO>> getSalesOrderAppPage(@Valid ZcSalesOrderPageReqVO pageReqVO) {
        // 前端未传 status 时，默认排除"未确认"和"完成"状态，仅展示生产中的订单
        if (CollUtil.isEmpty(pageReqVO.getStatus())) {
            pageReqVO.setStatus(Arrays.stream(ZcSalesOrderStatusEnum.values())
                    .filter(s -> s != ZcSalesOrderStatusEnum.UNCONFIRMED && s != ZcSalesOrderStatusEnum.COMPLETE)
                    .map(ZcSalesOrderStatusEnum::name)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return success(salesOrderService.getSalesOrderPage(pageReqVO));
    }

    @PutMapping("/expedited")
    @Operation(summary = "标记销售订单为加急")
    @Parameter(name = "orderId", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> markExpedited(@RequestParam("orderId") Long orderId) {
        salesOrderService.markExpedited(orderId);
        return success(true);
    }

    @PutMapping("/cancel-expedited")
    @Operation(summary = "取消销售订单加急")
    @Parameter(name = "orderId", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> cancelExpedited(@RequestParam("orderId") Long orderId) {
        salesOrderService.cancelExpedited(orderId);
        return success(true);
    }

    @PutMapping("/confirm")
    @Operation(summary = "确认销售订单（状态 unconfirmed → confirmed，扣减客户余额）")
    @Parameter(name = "id", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> confirmSalesOrder(@RequestParam("id") Long id) {
        salesOrderService.confirmSalesOrder(id);
        return success(true);
    }

    @PutMapping("/cancel-confirm")
    @Operation(summary = "取消确认销售订单（状态 confirmed → unconfirmed，退回客户余额）")
    @Parameter(name = "id", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> cancelConfirmSalesOrder(@RequestParam("id") Long id) {
        salesOrderService.cancelConfirmSalesOrder(id);
        return success(true);
    }

    @PutMapping("/complete")
    @Operation(summary = "完成销售订单（只要订单不是UNCONFIRMED（未确认），均可调用 /complete")
    @Parameter(name = "id", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> completeSalesOrder(@RequestParam("id") Long id) {
        salesOrderService.completeSalesOrder(id);
        return success(true);
    }

    @PutMapping("/discard")
    @Operation(summary = "废弃销售订单")
    @Parameter(name = "id", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> discardSalesOrder(@RequestParam("id") Long id) {
        salesOrderService.discardSalesOrder(id);
        return success(true);
    }

    @GetMapping("/statistics/customer")
    @Operation(summary = "按客户统计已确认订单（指定确认时间范围内，汇总各客户订单数、订单金额、已收金额、未收金额）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<List<ZcSalesOrderCustomerStatisticsRespVO>> getCustomerStatistics(
            @Valid ZcSalesOrderCustomerStatisticsReqVO reqVO) {
        return success(salesOrderService.getCustomerStatistics(reqVO));
    }

    @GetMapping("/statistics/material-product")
    @Operation(summary = "按产品规格统计已确认订单用料（product_id 不为空的用料明细，按 product_id + spec 汇总 quantity）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:query')")
    public CommonResult<List<ZcSalesOrderMaterialProductStatisticsRespVO>> getMaterialProductStatistics(
            @Valid ZcSalesOrderCustomerStatisticsReqVO reqVO) {
        return success(salesOrderService.getMaterialProductStatistics(reqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售订单 Excel")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSalesOrderExcel(@Valid ZcSalesOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ZcSalesOrderRespVO> list = salesOrderService.getSalesOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售订单.xls", "数据", ZcSalesOrderRespVO.class, list);
    }

    @PutMapping("/cut")
    @Operation(summary = "成品订单裁剪（绑定批次、扣减库存；联动更新订单状态为已配料/部分配料）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> cutMaterial(@RequestBody @Valid ZcCutMaterialReqVO reqVO) {
        salesOrderMaterialService.cutMaterial(reqVO);
        return success(true);
    }

    @PutMapping("/cancel-cut")
    @Operation(summary = "撤销裁剪（回退批次库存；联动更新订单状态为已确认/部分配料）")
    @PreAuthorize("@ss.hasPermission('zc:sales-order:update')")
    public CommonResult<Boolean> cancelCutMaterial(@RequestBody @Valid ZcCancelCutMaterialReqVO reqVO) {
        salesOrderMaterialService.cancelCutMaterial(reqVO);
        return success(true);
    }

    @GetMapping("/export-pdf")
    @Operation(summary = "导出销售订单 PDF（含全量明细：窗帘行→结构行→用料明细）")
    @Parameter(name = "id", description = "销售订单 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:sales-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSalesOrderPdf(@RequestParam("id") Long id,
                                    HttpServletResponse response) throws IOException {
        byte[] pdfBytes = salesOrderService.generateSalesOrderPdf(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"sales-order-" + id + ".pdf\"; filename*=UTF-8''sales-order-" + id + ".pdf\"");
        response.setContentLength(pdfBytes.length);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

}