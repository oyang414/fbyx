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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;

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
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        // 构造一个10000个元素的集合
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        // 统计并行执行list的线程
        Set<Thread> threadSet = new CopyOnWriteArraySet<>();
        // 并行执行
        list.parallelStream().forEach(integer -> {
            Thread thread = Thread.currentThread();
            // System.out.println(thread);
            // 统计并行执行list的线程
            threadSet.add(thread);
        });
        System.out.println("threadSet一共有" + threadSet.size() + "个线程");
        System.out.println("系统一个有"+Runtime.getRuntime().availableProcessors()+"个cpu");
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list1.add(i);
            list2.add(i);
        }
        Set<Thread> threadSetTwo = new CopyOnWriteArraySet<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread threadA = new Thread(() -> {
            list1.parallelStream().forEach(integer -> {
                Thread thread = Thread.currentThread();
                // System.out.println("list1" + thread);
                threadSetTwo.add(thread);
            });
            countDownLatch.countDown();
        });
        Thread threadB = new Thread(() -> {
            list2.parallelStream().forEach(integer -> {
                Thread thread = Thread.currentThread();
                // System.out.println("list2" + thread);
                threadSetTwo.add(thread);
            });
            countDownLatch.countDown();
        });

        threadA.start();
        threadB.start();
        countDownLatch.await();
        System.out.print("threadSetTwo一共有" + threadSetTwo.size() + "个线程");

        System.out.println("---------------------------");
        System.out.println(threadSet);
        System.out.println(threadSetTwo);
        System.out.println("---------------------------");
        threadSetTwo.addAll(threadSet);
        System.out.println(threadSetTwo);
        System.out.println("threadSetTwo一共有" + threadSetTwo.size() + "个线程");
        System.out.println("系统一个有"+Runtime.getRuntime().availableProcessors()+"个cpu");
    }

}
