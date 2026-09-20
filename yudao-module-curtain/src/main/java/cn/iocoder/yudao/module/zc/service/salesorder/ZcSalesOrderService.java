package cn.iocoder.yudao.module.zc.service.salesorder;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.zc.controller.admin.salesorder.vo.*;
import cn.iocoder.yudao.module.zc.dal.dataobject.salesorder.ZcSalesOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售订单 Service 接口
 *
 * @author 01Coder
 */
public interface ZcSalesOrderService {

    /**
     * 整单创建销售订单（含嵌套窗帘行、结构行、用料明细）
     *
     * <p>订单号自动生成，格式：ZC + 租户ID + yyyyMMdd + 5位累计序号（如 ZC120260519000001）。
     * 结算状态默认 unpaid，订单状态默认 pending，是否加急默认 false。
     * 所有子表记录在同一事务内级联保存。</p>
     *
     * @param createReqVO 整单创建请求（含窗帘行→结构行→用料明细三层嵌套）
     * @return 新订单 ID
     */
    Long createSalesOrder(@Valid ZcSalesOrderCreateReqVO createReqVO);

    /**
     * 整单创建面单（类型固定为 FABRIC，curtainId / structureId 均可为空）
     *
     * <p>与 {@link #createSalesOrder} 使用相同的三层嵌套插入逻辑，
     * 区别仅在于订单类型写入 FABRIC，且不强制要求绑定具体款式或结构。</p>
     *
     * @param createReqVO 面单创建请求（curtainId / structureId 可为空）
     * @return 新订单 ID
     */
    Long createFabricSalesOrder(@Valid ZcSalesOrderFabricCreateReqVO createReqVO);

    /**
     * 整单更新销售订单（含嵌套窗帘行、结构行、用料明细）
     *
     * <p>已确认订单禁止修改。未确认时先删旧子表再重新插入，全量替换。
     * orderNo、payStatus、status、isExpedited、amountReceived、confirmTime 等系统字段不受此接口影响。</p>
     *
     * @param updateReqVO 整单更新请求（含窗帘行→结构行→用料明细三层嵌套）
     */
    void updateSalesOrder(@Valid ZcSalesOrderUpdateReqVO updateReqVO);

    /**
     * 整单更新面单（类型固定 FABRIC，curtainId/structureId 可为空）
     *
     * <p>与 {@link #updateSalesOrder} 使用相同的校验和替换逻辑，区别仅在于使用面单简化 VO。</p>
     *
     * @param updateReqVO 面单更新请求
     */
    void updateFabricSalesOrder(@Valid ZcSalesOrderFabricUpdateReqVO updateReqVO);

    /**
     * 删除销售订单
     *
     * @param id 编号
     */
    void deleteSalesOrder(Long id);

    /**
    * 批量删除销售订单
    *
    * @param ids 编号
    */
    void deleteSalesOrderListByIds(List<Long> ids);

    /**
     * 获得销售订单
     *
     * @param id 编号
     * @return 销售订单
     */
    ZcSalesOrderDO getSalesOrder(Long id);

    /**
     * 获得销售订单分页
     *
     * @param pageReqVO 分页查询
     * @return 销售订单分页（含关联的客户名称、物流名称、创建人名称）
     */
    PageResult<ZcSalesOrderRespVO> getSalesOrderPage(ZcSalesOrderPageReqVO pageReqVO);

    /**
     * 确认销售订单
     *
     * <p>将订单状态从 unconfirmed 变更为 confirmed，记录确认时间，
     * 并从对应客户的账户余额中扣除订单金额（amount）。</p>
     *
     * @param id 销售订单 ID
     */
    void confirmSalesOrder(Long id);

    /**
     * 取消确认销售订单
     *
     * <p>将订单状态从 confirmed 变更回 unconfirmed，清空确认时间，
     * 并将订单金额（amount）退回客户账户余额。</p>
     *
     * @param id 销售订单 ID
     */
    void cancelConfirmSalesOrder(Long id);

    /**
     * 完成销售订单
     *
     * <p>将订单状态从 fahuo（已发货）变更为 complete（完成）。</p>
     *
     * @param id 销售订单 ID
     */
    void completeSalesOrder(Long id);

    /**
     * 废弃销售订单
     *
     * <p>将订单状态变更为 DISCARDED（废弃），终止履约且禁止查看。</p>
     *
     * @param id 销售订单 ID
     */
    void discardSalesOrder(Long id);

    /**
     * 标记销售订单为加急
     *
     * <p>将订单的 is_expedited 设置为 true，不限当前订单状态。</p>
     *
     * @param orderId 销售订单 ID
     */
    void markExpedited(Long orderId);

    /**
     * 取消销售订单加急
     *
     * <p>将订单的 is_expedited 设置为 false，不限当前订单状态。</p>
     *
     * @param orderId 销售订单 ID
     */
    void cancelExpedited(Long orderId);

    /**
     * 生成销售订单 PDF 字节流
     *
     * <p>包含订单主信息、全量窗帘行→结构行→用料明细，使用 OpenPDF 输出 A4 横向 PDF。</p>
     *
     * @param orderId 销售订单 ID
     * @return PDF 文件字节数组
     */
    byte[] generateSalesOrderPdf(Long orderId);

    /**
     * 获得销售订单完整详情（主表信息 + 三层嵌套明细）
     *
     * <p>返回订单主表全部字段，并在 curtains 节点下包含该订单的所有窗帘行，
     * 每行含若干结构行，每个结构行含若干用料明细，并冗余关联表名称字段，避免前端多次请求。</p>
     *
     * @param orderId 销售订单 ID
     * @return 销售订单完整详情（含 curtains 嵌套节点）
     */
    ZcSalesOrderDetailRespVO getSalesOrderDetail(Long orderId);

    /**
     * 按订单号获得销售订单完整详情（主表信息 + 三层嵌套明细）
     *
     * <p>先按 orderNo 精确匹配查出订单 ID，再复用 {@link #getSalesOrderDetail(Long)} 逻辑返回完整详情。</p>
     *
     * @param orderNo 销售订单编号，如 ZC1020240101000001
     * @return 销售订单完整详情（含 curtains 嵌套节点）
     * @throws cn.iocoder.yudao.framework.common.exception.ServiceException 订单不存在时抛出
     */
    ZcSalesOrderDetailRespVO getSalesOrderDetailByOrderNo(String orderNo);

    /**
     * 按确认时间范围统计各客户的已确认订单数与订单金额合计
     *
     * <p>筛选 confirm_time 落在指定范围内且 confirm_time 不为空的订单（即已确认订单），
     * 按 customer_id 分组，汇总订单数、amount 合计、amount_received 合计及未收金额（amount - amount_received）。</p>
     *
     * @param reqVO 确认时间范围
     * @return 按客户分组的统计列表，按订单金额合计降序
     */
    List<ZcSalesOrderCustomerStatisticsRespVO> getCustomerStatistics(
            @Valid ZcSalesOrderCustomerStatisticsReqVO reqVO);

    /**
     * 统计已确认订单中，用料明细（product_id 不为空）按产品+规格汇总的数量
     *
     * <p>筛选 confirm_time 落在指定范围内且 confirm_time 不为空的订单，
     * 关联 zc_sales_order_material，按 product_id + spec 分组汇总 quantity。</p>
     *
     * @param reqVO 确认时间范围
     * @return 按产品与规格分组的用料数量列表
     */
    List<ZcSalesOrderMaterialProductStatisticsRespVO> getMaterialProductStatistics(
            @Valid ZcSalesOrderCustomerStatisticsReqVO reqVO);

    /**
     * 获得今日需要发货的客户数（交付日期等于今天且未废弃订单的去重客户总数）
     *
     * @return 客户数
     */
    Long getTodayDeliveryCustomerCount();

}