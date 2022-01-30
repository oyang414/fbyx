package com.ouyang.fbyx.test;


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
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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
public class TestServiceImpl implements TestService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ImageService imageService;


    @Override
    @Transactional
    public void addGoods() {
        System.out.println("===添加商品===");
        GoodsPO goodsPO = new GoodsPO();
        goodsPO.setName("洗面奶");
        goodsService.save(goodsPO);
        try {
            ((TestService)AopContext.currentProxy()).addImage();
            //addImage();
        }catch (Exception e){

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
     public void addImage() {
        System.out.println("===添加图片===");
        ImagePO imagePO1 = new ImagePO();
        imagePO1.setName("111");
        imageService.save(imagePO1);
        throw new RuntimeException();
    }
}
