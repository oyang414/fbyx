package com.ouyang.fbyx.common.bean;

import com.ouyang.fbyx.common.utils.ArrayUtils;

import java.io.Serializable;



/**
 * @description 排序
 * @author ouyangxingjie
 * @date 2021/6/29 21:19
 */
public class Sort implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 排序字段
	 */
	private String fieldName;
	
	/**
	 * 排序方式
	 */
	private String order;
	
	/**
	 * 正序
	 */
	public static final String ASC = "asc";
	/**
	 * 倒序
	 */
	public static final String DESC = "desc";
	
	public Sort(){
		
	}

	public Sort(String fieldName, String order) {
		this.fieldName = fieldName;
		this.order = order;
	}

	/**  
	 * 获取排序字段  
	 * @return fieldName 排序字段  
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**  
	 * 设置排序字段  
	 * @param fieldName 排序字段  
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**  
	 * 获取排序方式  
	 * @return order 排序方式  
	 */
	public String getOrder() {
		return order;
	}

	/**  
	 * 设置排序方式  
	 * @param order 排序方式  
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	
	/**
	 * 将排序条件拼接成字符串
	 * @param sorts
	 * @return
	 */
	public static String getSortString(Sort...sorts){
		if(ArrayUtils.isNotEmpty(sorts)){
			Sort sort = null;
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < sorts.length; i++) {
				if(i>0){
					buffer.append(", ");
				}
				sort = sorts[i];
				buffer.append(sort.getFieldName());
				buffer.append(" ");
				buffer.append(sort.getOrder());
			}
			return buffer.toString();
		}
		return "";
	}
	
}
