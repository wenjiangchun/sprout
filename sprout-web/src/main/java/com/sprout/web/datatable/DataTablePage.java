package com.sprout.web.datatable;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DataTable表格分页类
 *
 */
public class DataTablePage {

	private int draw;
	
	private int recordsFiltered;
	
	private int recordTotals;
	
	/**
	 * 分页数据
	 */
	private List<?> data;

	public DataTablePage() {
		
	}
	
	public DataTablePage(int draw, int recordsFiltered,
			int recordTotals) {
		super();
		this.draw = draw;
		this.recordsFiltered = recordsFiltered;
		this.recordTotals = recordTotals;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public int getRecordTotals() {
		return recordTotals;
	}

	public void setRecordTotals(int recordTotals) {
		this.recordTotals = recordTotals;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	/**
	 * 根据Page对象和查询参数对象生成DataTable分页对象
	 * @param page 从数据库查询数据分页对象
	 * @param dataTableParams 分页参数 主要设置前台传递sEcho值
	 * @return DataTablePage
	 */
	public static DataTablePage generateDataTablePage(Page<?> page,DataTableParams dataTableParams) {
		int totalRecords = Integer.parseInt(String.valueOf(page.getTotalElements()));
        DataTablePage dtp = new DataTablePage(dataTableParams.getDraw(),totalRecords,totalRecords);
        dtp.setData(page.getContent());
		return dtp;
	}
}
