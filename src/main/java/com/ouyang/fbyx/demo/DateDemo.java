package com.ouyang.fbyx.demo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 时间相关类Demo
 * @author: ouyangxingjie
 * @create: 2021/7/13 21:02
 **/
public class DateDemo {

    public static void main(String [] args){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateFormat.format(calendar.getTime()));
        //=========================================================================//
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        firstDay = LocalDateTime.of(firstDay.getYear(),firstDay.getMonth(),firstDay.getDayOfMonth(),0,0,0);
        lastDay = LocalDateTime.of(lastDay.getYear(),lastDay.getMonth(),lastDay.getDayOfMonth(),23,59,59);
        System.out.println("firstDay:" + firstDay);
        System.out.println("lastDay:" + lastDay);
        Date dd = Date.from( firstDay.atZone( ZoneId.systemDefault()).toInstant());
        System.out.println(dd);
    }
}
