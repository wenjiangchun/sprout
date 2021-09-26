package com.sprout.web.datatable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Datable参数绑定对象
 */
public class DataTableParams {

	private int draw ;

    /// 每页显示的数量
	private int length ;

    /// 分页时每页跨度数量
	private int start ;

	private Map<String, Object> queryParams = new HashMap<>();

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	private String orderColumn = "id";

	private String orderDirection = "asc";

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * 根据参数对象获取查询分页对象
	 * @return PageRequest
	 */
	public PageRequest getPageRequest() {
		/*Sort sort = Sort.by(Sort.Direction.fromString(sSortDir_0), sColumns[iSortCol_0]);
		return PageRequest.of(iDisplayStart/iDisplayLength, iDisplayLength, sort);*/
		Sort sort = Sort.by(Sort.Direction.fromString(orderDirection), orderColumn);
		return PageRequest.of(start/length, length, sort);
	}
}
