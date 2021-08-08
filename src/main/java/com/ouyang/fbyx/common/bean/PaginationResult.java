package com.ouyang.fbyx.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ouyang.fbyx.common.exception.PaginationResultParamException;
import com.ouyang.fbyx.common.utils.CollectionUtil;
import lombok.Data;


/**
 * @description 分页结果类
 * @return
 * @author ouyangxingjie
 * @date 2021/6/29 21:18
 */
@Data
public class PaginationResult<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	//当前页码
	@JsonProperty("page_num")
	private Integer pageNum;
	
	//每条页数
	@JsonProperty("page_size")
	private Integer pageSize;
	
	//总页数
	@JsonProperty("total_page")
	private Integer totalPage;
	
	//总记录数
	@JsonProperty("total_num")
	private Integer totalNum;
	
	//数据结果集
	private List<T> result = new ArrayList<T>();

	public PaginationResult(){
		this.pageNum = 1;
		this.pageSize = 10;
	}

	public PaginationResult(List<T> result){
		this.pageNum = 1;
		this.pageSize = 10;
		this.totalNum = result.size();
		this.result = result;
		calculatePage();
	}
	public PaginationResult(int pageNum, int pageSize, int totalNum, List<T> result) throws PaginationResultParamException {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
		this.result = result;
		validateParam();
		calculatePage();
	}

	public PaginationResult(Pagination pagination,int totalNum,List<T> result) throws PaginationResultParamException  {
		this.pageNum = pagination.getPageNum();
		this.pageSize = pagination.getPageSize();
		this.totalNum = totalNum;
		this.result = result;
		validateParam();
		calculatePage();
	}

	/**
	 * @param
	 * @description 验证pageNum和pageSize参数有效性
	 * @return void
	 * @author ouyangxingjie
	 * @date 2021/7/7 21:34
	 */
	private void validateParam() throws PaginationResultParamException {
		if(pageNum<0){
			throw new PaginationResultParamException("pageNum is less than zero!");
		}
		if(pageSize<0 || pageSize>10000){
			throw new PaginationResultParamException("pageSize is less than zero or pageSize greater than ten thousand");
		}
	}

	/**
	 * @param
	 * @description 校验是否有结果集
	 * @return boolean
	 * @author ouyangxingjie
	 * @date 2021/7/7 21:35
	 */
	public boolean hasResult(){
		return CollectionUtil.isEmpty(result)?false:true;
	}
	/**
	 * @param
	 * @description 计算总页数
	 * @return void
	 * @author ouyangxingjie
	 * @date 2021/7/7 21:35
	 */
	private void calculatePage(){
		if(pageSize==0){
			this.totalPage = 0;
			return;
		}
		if( totalNum%pageSize == 0 ){
			this.totalPage = totalNum/pageSize;
		}else{
			this.totalPage = totalNum/pageSize+1;
		}
	}
}
