package com.ouyang.fbyx.common.bean;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 分页类
 * @author ouyangxingjie
 * @date 2021/6/29 21:18
 */
public class Pagination implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int FIRST_PAGE_NUMBER = 1;
	
	public static final String DEFAULT_SORT_COLUMN = "id";
	
	/**
	 * 当前页码
	 */
	private int pageNum;
	
	/**
	 * 每页条数
	 */
	private int pageSize;
	
	/**
	 * 查询条件
	 */
	private Map<String,Object> query;
	
	/**
	 * 排序条件
	 * id desc
	 * name asc
	 */
	private Sort[] sort;
	
	public Pagination(){
		pageNum = 1;
		pageSize = 20;
		initSort();
	}
	
	/**
	 * @param pageNum 开始记录数下标，从0开始
	 * @param pageSize 每页条数
	 */
	public Pagination(int pageNum, int pageSize) {
		this.pageNum = validatePageNum(pageNum);
		this.pageSize = pageSize;
		initSort();
	}
	
	/**
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 * @param query 查询条件
	 */
	public Pagination(int pageNum, int pageSize, Map<String, Object> query) {
		this.pageNum = validatePageNum(pageNum);
		this.pageSize = pageSize;
		this.query = query;
		initSort();
	}
	
	/**
	 * @param query 查询条件
	 * @param pageNum 开始记录数
	 * @param pageSize 每页条数
	 */
	public Pagination(int pageNum, int pageSize, Map<String, Object> query, Sort... sort) {
		this.pageNum = validatePageNum(pageNum);
		this.pageSize = pageSize;
		this.query = query;
		this.sort = sort;
	}
	
	private void initSort(){
		Sort s = new Sort(DEFAULT_SORT_COLUMN, Sort.DESC);
		sort = new Sort[1];
		sort[0] = s;
		if(query==null){
			query = new HashMap<>();
		}
	}
	/***********************************************
	 *  					getter setter
	 * *********************************************
	 */

	/**  
	 * 获取pageSize  
	 * @return pageSize pageSize  
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**  
	 * 获取排序条件  
	 * @return sort 排序条件  
	 */
	public Sort[] getSort() {
		return sort;
	}

	/**  
	 * 设置排序条件  
	 * @param sort 排序条件  
	 */
	public void setSort(Sort... sort) {
		this.sort = sort;
	}

	/**  
	 * 设置pageSize  
	 * @param pageSize pageSize  
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**  
	 * 获取query  
	 * @return query query  
	 */
	public Map<String, Object> getQuery() {
		return query;
	}

	/**  
	 * 设置query  
	 * @param query query  
	 */
	public void setQuery(Map<String, Object> query) {
		this.query = query;
	}

	/**  
	 * 获取当前页码  
	 * @return pageNum 当前页码  
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**  
	 * 设置当前页码  
	 * @param pageNum 当前页码  
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 验证输入的页码正确性
	 * @param pageNum
	 * @return
	 */
	private int validatePageNum(int pageNum){
		if( pageNum < FIRST_PAGE_NUMBER ){ 
			return FIRST_PAGE_NUMBER;
		}
		return pageNum;
	}
	
	/**
	 * 获取开始下标
	 * @return
	 */
	public int getStart(){
		return (this.getPageNum() - FIRST_PAGE_NUMBER) * this.getPageSize();
	}

	/**
	 * 获取结束下标
	 * @return
	 */
	public int getEnd(){
		return (getStart() + getPageSize());
	}
}
