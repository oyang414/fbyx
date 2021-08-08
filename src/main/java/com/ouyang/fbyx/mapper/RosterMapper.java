package com.ouyang.fbyx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ouyang.fbyx.mapper.po.RosterPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @description: 花名册
 * @author: ouyangxingjie
 * @create: 2021/6/27 13:50
 **/

@Component("rosterMapper")
public interface RosterMapper extends BaseMapper<RosterPO>{
}
