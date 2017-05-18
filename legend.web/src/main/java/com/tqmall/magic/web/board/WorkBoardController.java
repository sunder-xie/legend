package com.tqmall.magic.web.board;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.enums.ProductionLineTypeEnum;
import com.tqmall.legend.facade.magic.LaodPlateFacade;
import com.tqmall.legend.facade.magic.vo.ProductionLineVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.result.board.BoardProcessDTO;
import com.tqmall.magic.object.result.workshop.ProductionLineDTO;
import com.tqmall.magic.service.board.RpcBoardProcessService;
import com.tqmall.magic.service.workshop.RpcProductionLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by litan on 16/7/13.
 */
@Controller
@Slf4j
@RequestMapping("board")
public class WorkBoardController extends BaseController {

    @Autowired
    private RpcBoardProcessService rpcBoardProcessService;
    @Autowired
    private LaodPlateFacade laodPlateFacade;
    @Autowired
    private RpcProductionLineService rpcProductionLineService;
    @Value("${bp.socket.url}")
    private String socketUrl;

    /**
     * 钣喷中心快修看板
     *
     * @return
     */
    @RequestMapping("/process-kx")
    public String showProcessKx(Model model, Long lineId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        List<ProductionLineVO> productionLines = null;
        List<ProductionLineVO> productionLineVOList = new ArrayList<>();
        try {
            productionLines = laodPlateFacade.getProductionLine(shopId);
        } catch (Exception e) {
            log.error("[工序看板] 获取看板生产线失败！，e={}", e);
        }
        // add:钣喷四期增加小钣金事故线。入口先关闭
        if (!CollectionUtils.isEmpty(productionLines)) {
            for (ProductionLineVO productionLineVO : productionLines) {
                if (productionLineVO.getType() != null && !productionLineVO.getType().equals(ProductionLineTypeEnum.XSGX.getType())) {
                    productionLineVOList.add(productionLineVO);
                }
            }
        }
        model.addAttribute("productionLines", productionLineVOList);
        model.addAttribute("socketUrl", socketUrl);
        model.addAttribute("lineId", lineId);
        Result<ProductionLineDTO> result = rpcProductionLineService.selectProductionLine(lineId);
        if (result.isSuccess()) {
            ProductionLineDTO productionLineDTO = result.getData();
            model.addAttribute("lineName", productionLineDTO.getName());
        }
        return "yqx/page/magic/board/process_board_kx";
    }

    /**
     * 钣喷中心快喷看板
     *
     * @return
     */
    @RequestMapping("/process-kp")
    public String showProcessKp(Model model) {
        return "yqx/page/magic/board/process_board_kp";
    }

    /**
     * 钣喷中心事故看板
     *
     * @return
     */
    @RequestMapping("/process-sg")
    public String showProcessSg(Model model) {
        return "yqx/page/magic/board/process_board_sg";
    }

    /**
     * 工序看板数据加载
     *
     * @return
     */
    @RequestMapping("/queryBoardProcess/{lineId}")
    @ResponseBody
    public Result queryBoardProcess(@PathVariable("lineId") Long lineId) {
        log.info("[工序看板] 初始化数据加载 lineId={}", lineId);
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            Result<BoardProcessDTO> result = rpcBoardProcessService.queryBoardProcess(shopId, lineId);
            return result;
        } catch (Exception e) {
            log.error("[工序看板] 初始化数据加载异常,e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.BOARD_PROCESS_LOAD.getCode(), LegendErrorCode.BOARD_PROCESS_LOAD.getErrorMessage());
    }

    /**
     * 测试消息发送
     * @param workOrderId
     * @return
     */
    /*@RequestMapping("/sendMessage/{workOrderId}")
    @ResponseBody
    public Result sendMessage(@PathVariable("workOrderId") Long workOrderId){
        Long shopId = UserUtils.getShopIdForSession(request);
        boardFacade.sendMessage(shopId,null,workOrderId);
        return Result.wrapSuccessfulResult("发送成功");
    }*/

    /**
     * 清空缓存(测试使用)之后删除
     *
     * @return
     */
    @RequestMapping("/clearCache/{lineId}/{processId}")
    @ResponseBody
    public Result sendMessage(@PathVariable("lineId") Long lineId, @PathVariable("processId") Long processId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        String key = "BP_BOARD_PROCESS:S" + shopId;
        if (lineId != null && lineId > 0) {
            key = key + ":L" + lineId;
        }
        if (lineId != null && processId != null && lineId > 0 && processId > 0) {
            key = key + ":P" + processId;
        }
        long count = 0;
        try {
            Set<String> keys = jedis.keys(key + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                count += jedis.del(keys.toArray(new String[keys.size()]));
            }
        } catch (Exception e) {
            log.error("[看板] 清除缓存 e={}", e);
            return Result.wrapErrorResult("", "清除缓存失败");
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return Result.wrapSuccessfulResult("清除成功:" + count);
    }
}
