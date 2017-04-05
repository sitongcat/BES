package org.bes.common;

public class Page {

	private int currentPage = 1;
	private int pageSize = 15;
	private int dataCount;
	private int pageCount;

	public Page() {}

	public Page(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}
	
	public int getCurrentPage() {
		if (this.currentPage < 1) {
			this.currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		if (currentPage == null) {
			currentPage = 1;
		}
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null) {
			return;
		}
		this.pageSize = pageSize;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
		if (this.dataCount % this.pageSize == 0) {
			this.pageCount = this.dataCount / this.pageSize;
		} else {
			this.pageCount = this.dataCount / this.pageSize + 1;
		}
	}

	public int getPageCount() {
		return pageCount;
	}

}
