package cn.iocoder.yudao.module.zc.controller.admin.processnode;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordMasterMaterialRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordTodayUserMaterialRespVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordRevokeReqVO;
import cn.iocoder.yudao.module.zc.controller.admin.processnode.vo.ZcOrderProcessRecordSaveReqVO;
import cn.iocoder.yudao.module.zc.service.processnode.ZcOrderProcessRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 订单工序记录 Controller
 *
 * <p>工厂员工通过此接口记录订单各工序的完成情况（完成即记录），
 * 支持撤销和删除（仅允许删除已撤销的记录），
 * 管理员和客户通过 {@code /list} 接口查看订单工序时间线。</p>
 *
 * @author 01Coder
 */
@Tag(name = "管理后台 - 订单工序记录")
@RestController
@RequestMapping("/zc/order-process-record")
@Validated
public class ZcOrderProcessRecordController {

    @Resource
    private ZcOrderProcessRecordService processRecordService;

    @PostMapping("/create")
    @Operation(summary = "新增工序记录（记录某道工序已完成）",
            description = "记录一经创建即表示工序完成（status=1）；若指定车间员工，该员工须已绑定所选节点")
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:create')")
    public CommonResult<Long> createProcessRecord(@Valid @RequestBody ZcOrderProcessRecordSaveReqVO reqVO) {
        return success(processRecordService.createProcessRecord(reqVO));
    }

    @PutMapping("/revoke")
    @Operation(summary = "撤销工序记录", description = "将已完成（status=1）的记录撤销为 status=2，撤销后可删除")
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:update')")
    public CommonResult<Boolean> revokeProcessRecord(@Valid @RequestBody ZcOrderProcessRecordRevokeReqVO reqVO) {
        processRecordService.revokeProcessRecord(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工序记录（仅允许删除已撤销的记录）")
    @Parameter(name = "id", description = "记录 ID", required = true)
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:delete')")
    public CommonResult<Boolean> deleteProcessRecord(@RequestParam("id") Long id) {
        processRecordService.deleteProcessRecord(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取订单工序时间线（按时间降序）",
            description = "关联 zc_process_node，仅返回手工配置（group=1）的节点记录")
    @Parameters({
            @Parameter(name = "orderId", description = "订单 ID，不传则返回全部"),
            @Parameter(name = "masterId", description = "主操作人员 ID，不传则返回全部"),
            @Parameter(name = "curtainId", description = "窗帘行 ID，不传则返回全部"),
            @Parameter(name = "structureId", description = "结构行 ID，不传则返回全部"),
            @Parameter(name = "materialId", description = "用料明细 ID，不传则返回全部"),
            @Parameter(name = "nodeId", description = "工序节点 ID，不传则返回全部")
    })
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:query')")
    public CommonResult<List<ZcOrderProcessRecordRespVO>> getProcessRecordList(
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "masterId", required = false) Long masterId,
            @RequestParam(value = "curtainId", required = false) Long curtainId,
            @RequestParam(value = "structureId", required = false) Long structureId,
            @RequestParam(value = "materialId", required = false) Long materialId,
            @RequestParam(value = "nodeId", required = false) Long nodeId) {
        return success(processRecordService.getProcessRecordList(orderId, masterId, curtainId, structureId, materialId, nodeId, Arrays.asList(1)));
    }

    @GetMapping("/master-material-stat")
    @Operation(summary = "获取某操作员当前节点的用料统计",
            description = "统计完成状态（status=1）的工序记录，按用料组件分组返回工序次数与用料合计")
    @Parameters({
            @Parameter(name = "masterId", description = "主操作人员 ID", required = true),
            @Parameter(name = "nodeId", description = "工序节点 ID", required = true),
            @Parameter(name = "beginCreateTime", description = "创建时间范围（开始），不传则不限制"),
            @Parameter(name = "endCreateTime", description = "创建时间范围（结束），不传则不限制")
    })
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:query')")
    public CommonResult<List<ZcOrderProcessRecordMasterMaterialRespVO>> getMasterMaterialStat(
            @RequestParam("masterId") Long masterId,
            @RequestParam("nodeId") Long nodeId,
            @RequestParam(value = "beginCreateTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginCreateTime,
            @RequestParam(value = "endCreateTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endCreateTime) {
        return success(processRecordService.getMasterMaterialStat(masterId, nodeId, beginCreateTime, endCreateTime));
    }

    @GetMapping("/today-user-material-stat")
    @Operation(summary = "获取指定时间范围内所有员工对应节点的用料统计",
            description = "统计指定时间范围内完成状态（status=1）的所有员工工序记录，按员工与节点分组返回用料统计")
    @Parameters({
            @Parameter(name = "beginCreateTime", description = "创建时间范围（开始），不传默认当日零点"),
            @Parameter(name = "endCreateTime", description = "创建时间范围（结束），不传默认当日23:59:59")
    })
    @PreAuthorize("@ss.hasPermission('zc:order-process-record:query')")
    public CommonResult<List<ZcOrderProcessRecordTodayUserMaterialRespVO>> getTodayUserMaterialStat(
            @RequestParam(value = "beginCreateTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginCreateTime,
            @RequestParam(value = "endCreateTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endCreateTime) {
        return success(processRecordService.getTodayUserMaterialStat(beginCreateTime, endCreateTime));
    }

}

