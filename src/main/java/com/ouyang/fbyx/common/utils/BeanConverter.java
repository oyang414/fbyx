package com.ouyang.fbyx.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ouyang.fbyx.common.bean.PaginationResult;
import com.ouyang.fbyx.common.exception.PaginationResultParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;


/**
 * @description 简单对象转换工具类
 * @author ouyangxingjie
 * @date 2021/6/29 21:19
 */
public  class BeanConverter{
	
	private static final Logger logger = LoggerFactory.getLogger(BeanConverter.class);
	
	public static<T,S> T to(S source,Class<T> t) {
		T target = null;
		if (source != null) {
			try {
				target = t.newInstance();
			} catch (Exception e) {				
				logger.error(String.format("实例化class类出现异常：%s", StringUtil.trace(source)), e);
			}
			BeanUtils.copyProperties(source, target);
		}
		return target;
	}

	
	public static<T,S>  List<T> to(List<S> source,Class<T> t) {

		if (source == null)
			return null;

		List<T> targets = getTargetList(source.size());

		for (S s : source) {
			targets.add(to(s,t));
		}

		return targets;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static<T>  List<T> getTargetList(int size){
		return new ArrayList(size);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static<T extends Serializable> PaginationResult<T> getTargetPaginationResult(int pageNum, int pageSize, int totalNum, List<T> result) throws PaginationResultParamException {
		return new PaginationResult(pageNum, pageSize, totalNum, result);
	}
	

	public static<T extends Serializable,S extends Serializable>  PaginationResult<T> to(PaginationResult<S> source,Class<T> t) {
		  
		if(source == null){
			return null;
		}
		
		List<T> list= to(source.getResult(),t);
		try{
			return getTargetPaginationResult(source.getPageNum(), source.getPageSize(), source.getTotalNum(), list);
		}catch( Exception e ){
			logger.error(String.format("获取page对象出现异常:%s", StringUtil.trace(source)), e);
			return null;
		}

	}
	
	public static <T, S> T convert(S source, Class<T> t) {
		T target = null;
		if (source != null) {
			try {
				JSON json = (JSON) JSON.toJSON(source);
				target = JSON.toJavaObject(json,t);
			} catch (Exception e) {
				logger.error(String.format("获取page对象出现异常:%s", StringUtil.trace(source)), e);
			}
		}
		return target;
	}
	
	public static<T,S>  List<T> convert(List<S> source,Class<T> t) {
		if (source == null)
			return null;

		List<T> targets = getTargetList(source.size());

		for (S s : source) {
			targets.add(convert(s,t));
		}
		return targets;
	}
	
    public static<T extends Serializable,S extends Serializable>  PaginationResult<T> convert(PaginationResult<S> source,Class<T> t) {
		
		if(source == null){
			return null;
		}
		
		List<T> list= convert(source.getResult(),t);
		try{
			return getTargetPaginationResult(source.getPageNum(), source.getPageSize(), source.getTotalNum(), list);
		}catch( Exception e ){
			logger.error(String.format("转换page对象出现异常:%s", StringUtil.trace(source)), e);
			return null;
		}

	}
	

}
