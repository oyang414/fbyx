package com.ouyang.fbyx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ouyang.fbyx.mapper.po.RosterPO;
import com.ouyang.fbyx.test.GoodsPO;
import org.springframework.stereotype.Component;

/**
 * @description: 花名册
 * @author: ouyangxingjie
 * @create: 2021/6/27 13:50
 **/

@Component("goodsMapper")
public interface GoodsMapper extends BaseMapper<GoodsPO>{
}
