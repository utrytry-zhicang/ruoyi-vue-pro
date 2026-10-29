package cn.iocoder.yudao.module.zc.service.processnode;

import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordMasterMaterialRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordRevokeReqVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordSaveReqVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordTodayUserMaterialRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.salesorder.vo.ZcSalesOrderProcessRecordDetailRespVO;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单工序记录 Service 接口
 *
 * <p>工序记录采用完成即记录的模式：
 * 创建记录时 status 默认为 1（完成），可通过撤销接口将 status 改为 2（撤销）。
 * 撤销后的记录方可删除，以保留完整审计轨迹。</p>
 *
 * @author 01Coder
 */
public interface ZcOrderProcessRecordService {

    /**
     * 新增工序记录（记录某道工序已完成）
     *
     * <p>前置校验：<br>
     * 1. 订单必须已确认（confirmTime 不为空）；<br>
     * 2. 若指定了车间员工，该员工必须已绑定所选工序节点。</p>
     *
     * @param reqVO 创建信息，含订单、窗帘行、结构行、用料明细及工序节点
     * @return 新记录 ID
     */
    Long createProcessRecord(@Valid ZcOrderProcessRecordSaveReqVO reqVO);

    /**
     * 撤销工序记录
     *
     * <p>仅允许撤销状态为「完成」（status=1）的记录，将 status 更新为 2（撤销）。</p>
     *
     * @param reqVO 包含记录 ID 和撤销原因备注
     */
    void revokeProcessRecord(@Valid ZcOrderProcessRecordRevokeReqVO reqVO);

    /**
     * 删除工序记录
     *
     * <p>仅允许删除状态为「撤销」（status=2）的记录，
     * 已完成的记录必须先撤销方可删除，防止篡改有效生产数据。</p>
     *
     * @param id 记录 ID
     */
    void deleteProcessRecord(Long id);

    /**
     * 获取订单的工序时间线，按创建时间降序排列（含车间员工名称）
     *
     * <p>按 {@code zc_process_node.group} 筛选节点记录，默认仅返回 group=1（手工配置）节点。</p>
     *
     * @param orderId     订单 ID，为 null 时不过滤
     * @param masterId    主操作人员 ID，为 null 时不过滤
     * @param curtainId   窗帘行 ID，为 null 时不过滤
     * @param structureId 结构行 ID，为 null 时不过滤
     * @param materialId  用料明细 ID，为 null 时不过滤
     * @param nodeId      工序节点 ID，为 null 时不过滤
     * @param groups      工序节点分组多选（0=系统配置，1=手工配置）
     * @return 工序记录列表
     */
    List<ZcOrderProcessRecordRespVO> getProcessRecordList(Long orderId, Long masterId,
                                                          Long curtainId, Long structureId,
                                                          Long materialId, Long nodeId,
                                                          List<Integer> groups);

    /**
     * 获得销售订单工序记录详情（完整窗帘结构 + 各层工序记录）
     *
     * <p>先加载销售订单完整详情（窗帘→结构→用料），再将工序记录挂载到对应层级；
     * 无工序记录的窗帘/结构/用料仍会返回。各层 processRecords 按创建时间升序排列。</p>
     *
     * @param orderId 销售订单 ID
     * @return 工序记录详情
     */
    ZcSalesOrderProcessRecordDetailRespVO getSalesOrderProcessRecordDetail(Long orderId);

    /**
     * 按用料组件分组获取某操作员在指定工序节点下的用料统计（工序次数、用料合计）
     *
     * <p>仅统计完成状态（status=1）的工序记录，用料通过节点绑定的组件与订单用料明细匹配后按组件分组累加。</p>
     *
     * @param masterId        主操作人员 ID
     * @param nodeId          工序节点 ID
     * @param beginCreateTime 创建时间范围（开始），可为 null
     * @param endCreateTime   创建时间范围（结束），可为 null
     * @return 按用料组件分组的统计结果列表
     */
    List<ZcOrderProcessRecordMasterMaterialRespVO> getMasterMaterialStat(Long masterId, Long nodeId,
                                                                          LocalDateTime beginCreateTime,
                                                                          LocalDateTime endCreateTime);

    /**
     * 获取指定时间范围内所有员工在各自对应工序节点下的用料统计
     *
     * @param beginCreateTime 创建时间范围（开始），若 null 默认当日零点
     * @param endCreateTime   创建时间范围（结束），若 null 默认当日 23:59:59
     * @return 按员工 -> 节点 -> 组件分组的统计结果列表
     */
    List<ZcOrderProcessRecordTodayUserMaterialRespVO> getTodayUserMaterialStat(LocalDateTime beginCreateTime,
                                                                               LocalDateTime endCreateTime);

}
