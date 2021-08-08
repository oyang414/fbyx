package com.ouyang.fbyx.controller;

import com.ouyang.fbyx.common.bean.PaginationResult;
import com.ouyang.fbyx.common.exception.BusinessException;
import com.ouyang.fbyx.common.utils.BeanConverter;
import com.ouyang.fbyx.controller.param.PageParams;
import com.ouyang.fbyx.controller.param.RosterParams;
import com.ouyang.fbyx.controller.vo.RosterVO;
import com.ouyang.fbyx.mapper.po.RosterPO;
import com.ouyang.fbyx.service.RotserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @description: 花名册Controller
 * @author: ouyangxingjie
 * @create: 2021/6/27 14:12
 **/
@RestController
@RequestMapping(value = "/rosters",produces="application/json")
@Validated
@Api(tags = "风暴英雄傻逼花名册相关接口")
@Slf4j
public class RosterController {

    @Autowired
    private RotserService rotserService;

    /**
     * @param name	
     * @description 根据玩家名称查询roster记录
     * @return java.util.List<com.ouyang.fbyx.controller.vo.RosterVO>
     * @author ouyangxingjie
     * @date 2021/6/27 14:20
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "根据玩家名称在花名册查询", tags = "风暴英雄傻逼花名册相关接口",notes = "根据玩家名称在花名册查询")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = PaginationResult.class),
            @ApiResponse(code = 404, message = "查询失败", response = BusinessException.class) })
    public PaginationResult<RosterVO> query(@RequestParam(value = "name",required = false)
                                              @ApiParam(name = "name", value = "玩家名称",example = "Misaka") String name,
                                                      @Valid PageParams pageParams){
        return rotserService.query(name,pageParams);
    }

    /**
     * @param params
     * @description 保存roster
     * @return void
     * @author ouyangxingjie
     * @date 2021/6/28 19:59
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "录入傻逼到花名册", tags = "风暴英雄傻逼花名册相关接口",notes = "录入傻逼到花名册")
    @ApiResponses({
            @ApiResponse(code = 200, message = "录入成功", response = Void.class),
            @ApiResponse(code = 404, message = "录入失败", response = BusinessException.class) })
    public void save(@RequestBody  @Valid RosterParams params){
        RosterPO po = BeanConverter.to(params,RosterPO.class);
        po.setCreateTime(LocalDateTime.now());
        log.debug("保存数据：{}",po);
        rotserService.save(po);
    }
}
