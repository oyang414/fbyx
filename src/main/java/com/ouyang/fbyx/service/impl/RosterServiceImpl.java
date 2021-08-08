package com.ouyang.fbyx.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ouyang.fbyx.common.bean.PaginationResult;
import com.ouyang.fbyx.common.utils.BeanConverter;
import com.ouyang.fbyx.common.utils.CollectionUtil;
import com.ouyang.fbyx.common.utils.DateUtil;
import com.ouyang.fbyx.controller.param.PageParams;
import com.ouyang.fbyx.controller.vo.RosterVO;
import com.ouyang.fbyx.mapper.RosterMapper;
import com.ouyang.fbyx.mapper.po.RosterPO;
import com.ouyang.fbyx.service.RotserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @description: 花名册Service实现类
 * @author: ouyangxingjie
 * @create: 2021/6/27 14:01
 **/
@Service
@Slf4j
public class RosterServiceImpl extends ServiceImpl<RosterMapper,RosterPO> implements RotserService {
    @Autowired
    private RosterMapper rosterMapper;
   /**
    * @param name	玩家名称
    * @param pageParams	 分页参数
    * @description 根据玩家名称查询roster记录(分页)
    * @return com.ouyang.fbyx.common.bean.PaginationResult<com.ouyang.fbyx.controller.vo.RosterVO>
    * @author ouyangxingjie
    * @date 2021/7/7 21:17
    */
    @Override
    public PaginationResult<RosterVO> query(String name, PageParams pageParams) {
        QueryWrapper<RosterPO> queryWrapper = new QueryWrapper<>();
        //如果name值不为空
        Optional.ofNullable(name).ifPresent(n->{
            //如果name值不为""或者" "，则添加条件查询
            if(StringUtils.hasText(n)) {
                queryWrapper.eq("user_name", n);
                log.info("根据玩家名称：{}，查询...", n);
            }
        });
        //如果分页不为空
        if (Optional.ofNullable(pageParams)
                .map(p->pageParams.getPageSize())
                .map(p->pageParams.getPageNum()).isPresent()) {
            //分页查询
            return findPage(queryWrapper,pageParams);
        } else {
            //条件查询
            return find(queryWrapper);
        }
    }

    /**
     * @param poList
     * @description 将花名册po结果集转换成vo结果集
     * @return java.util.List<com.ouyang.fbyx.controller.vo.RosterVO>
     * @author ouyangxingjie
     * @date 2021/7/7 22:26
     */
    private  List<RosterVO> buildRosterVOList(List<RosterPO> poList){
        List<RosterVO> voList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(poList)){
            DateTimeFormatter df = DateTimeFormatter.ofPattern(DateUtil.SHORTDATEFORMATER);
            for(RosterPO po : poList){
                RosterVO vo = BeanConverter.convert(po,RosterVO.class);
                vo.setCreateTime(df.format(po.getCreateTime()));
                voList.add(vo);
            }
        }
        return voList;
    }
    /**
     * @param queryWrapper	查询条件
     * @param pageParams	分页参数
     * @description 分页查询
     * @return com.ouyang.fbyx.common.bean.PaginationResult<com.ouyang.fbyx.controller.vo.RosterVO>
     * @author ouyangxingjie
     * @date 2021/7/7 22:26
     */
    private PaginationResult<RosterVO> findPage(QueryWrapper queryWrapper,PageParams pageParams){
        log.info("进行分页查询,当前页：{},每页条数：{}",pageParams.getPageNum(),pageParams.getPageSize());
        Page<RosterPO> page = new Page<>(pageParams.getPageNum(), pageParams.getPageSize());
        IPage<RosterPO> pageResult = rosterMapper.selectPage(page,queryWrapper);
        return new PaginationResult<>((int)page.getCurrent(),(int)page.getSize(),(int)page.getTotal(),
                buildRosterVOList(pageResult.getRecords()));
    }

   /**
    * @param queryWrapper
    * @description 条件查询
    * @return com.ouyang.fbyx.common.bean.PaginationResult<com.ouyang.fbyx.controller.vo.RosterVO>
    * @author ouyangxingjie
    * @date 2021/7/7 22:27
    */
    private PaginationResult<RosterVO> find(QueryWrapper queryWrapper){
        log.info("进行条件查询...");
        List<RosterPO> poList = rosterMapper.selectList(queryWrapper);
        return new PaginationResult<>(buildRosterVOList(poList));
    }


}
