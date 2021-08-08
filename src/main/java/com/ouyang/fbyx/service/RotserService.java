package com.ouyang.fbyx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ouyang.fbyx.common.bean.PaginationResult;
import com.ouyang.fbyx.controller.param.PageParams;
import com.ouyang.fbyx.controller.vo.RosterVO;
import com.ouyang.fbyx.mapper.po.RosterPO;

import java.util.List;

/**
 * @description: 花名册Service接口
 * @author: ouyangxingjie
 * @create: 2021/6/27 14:00
 **/
public interface RotserService extends IService<RosterPO> {
    /**
     * @param name	玩家名称
     * @param pageParams 分页参数
     * @description 根据玩家名称查询roster记录(分页)
     * @return com.ouyang.fbyx.common.bean.PaginationResult<com.ouyang.fbyx.controller.vo.RosterVO>
     * @author ouyangxingjie
     * @date 2021/7/7 21:17
     */
    PaginationResult<RosterVO> query(String name, PageParams pageParams);
}
