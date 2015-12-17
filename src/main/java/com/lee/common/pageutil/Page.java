package com.lee.common.pageutil;

import java.io.Serializable;
import java.util.List;

public class Page<M> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total;
	private int pageNumber;
	private int pageIndex;
	private List<M> elements;
	private int first;
	private int pageSize = 10;
	private String sysTime;

	/**
	 * <p>
	 * The default constrctor ,after new Page() You should use 'setXXX()'methods
	 * to give the value of this object
	 * </p>
	 */
	public Page() {
	}
	
	public Page(int pageIndex, int total) {
		this.total = total;
		if (total == 0)
			return;
		pageNumber = total / pageSize;
		if (total % pageSize > 0)
			pageNumber++;
		if (pageIndex < 1)
			pageIndex = 1;
		if (pageIndex > pageNumber) {
			pageIndex = pageNumber;
		}
		this.pageIndex = pageIndex;
		this.first = (pageIndex - 1) * pageSize;
	}

	public Page(int pageIndex, int total, int pageSize) {
		this.total = total;
		this.pageSize = pageSize;
		if (total == 0)
			return;
		pageNumber = total / pageSize;
		if (total % pageSize > 0)
			pageNumber++;
		if (pageIndex < 1)
			pageIndex = 1;
		if (pageIndex > pageNumber) {
			pageIndex = pageNumber;
		}
		this.pageIndex = pageIndex;
		this.first = (pageIndex - 1) * pageSize;
	}

	public int getTotal() {
		return total;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public List<M> getElements() {
		return elements;
	}

	public void setElements(List<M> elements) {
		this.elements = elements;
	}

	public int getFirst() {
		return first;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotal(int total) {
		this.total = total;		
		if (total == 0)
			return;
		pageNumber = total / pageSize;
		if (total % pageSize > 0)
			pageNumber++;
		if (pageIndex < 1)
			pageIndex = 1;
		if (pageIndex > pageNumber) {
			pageIndex = pageNumber;
		}		
		first = (pageIndex - 1) * pageSize;		
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
}