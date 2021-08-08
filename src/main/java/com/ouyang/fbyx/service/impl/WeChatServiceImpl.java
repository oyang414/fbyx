package com.ouyang.fbyx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ouyang.fbyx.common.bean.PaginationResult;
import com.ouyang.fbyx.common.bean.WeChatMsg;
import com.ouyang.fbyx.common.utils.BeanConverter;
import com.ouyang.fbyx.common.utils.CollectionUtil;
import com.ouyang.fbyx.common.utils.DateUtil;
import com.ouyang.fbyx.mapper.RosterMapper;
import com.ouyang.fbyx.mapper.po.RosterPO;
import com.ouyang.fbyx.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * @description: 微信公众号Service实现类
 * @author: ouyangxingjie
 * @create: 2021/7/19 20:08
 **/
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private RosterMapper rosterMapper;

    /**
     * @param weChatMsg
     * @description 微信公众号查询接口
     * @return com.ouyang.fbyx.common.bean.WeChatMsg
     * @author ouyangxingjie
     * @date 2021/7/19 22:44
     */
    @Override
    public WeChatMsg query(WeChatMsg weChatMsg) {
        WeChatMsg result = BeanConverter.convert(weChatMsg,WeChatMsg.class);
        QueryWrapper<RosterPO> queryWrapper = new QueryWrapper<>();
        //如果name值不为空
        Optional.ofNullable(weChatMsg.getContent()).ifPresent(n->{
            //如果name值不为""或者" "，则添加条件查询
            if(StringUtils.hasText(n)) {
                queryWrapper.eq("user_name", n);
                log.info("根据玩家名称：{}，查询...", n);
                List<RosterPO> poList = rosterMapper.selectList(queryWrapper);
                String resultString = buildWeChatMsg(poList);
                result.setContent(resultString);
            }
        });
        return result;
    }

    /**
     * @param poList
     * @description 构造微信自动回复的字符串内容 content
     * @return java.lang.String
     * @author ouyangxingjie
     * @date 2021/7/19 23:13
     */
    private String buildWeChatMsg(List<RosterPO> poList){
        StringBuffer sb = new StringBuffer();
        if(CollectionUtil.isNotEmpty(poList)) {
            sb.append(MessageFormat.format("共找到了 {0} 名伞兵：\n", poList.size()));
            for(RosterPO po : poList){
                sb.append(MessageFormat.format("玩家 {0}#{1} 在 {2} 用 {3} 影响了大家的游戏体验，恶劣行为如下：{4} \n",
                        po.getUserName(),po.getUserId(), DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(po.getCreateTime()),
                        po.getHeroName(),po.getReason()));
            }
        }else {
            sb.append("未找到该玩家");
        }
        return sb.toString();

    }
}
