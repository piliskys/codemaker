package com.piliskys.db;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * ========================================================
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 *          Date: 2009-2-16
 *          Time: 10:54:21
 *          功能说明：
 *          ========================================================
 */
public class Page  implements Serializable {
    private int pages;   //总页数
    private int currentPage=1;
    private List results=new ArrayList();
    private int totalSize;   //总条数
    private int pageRecords=20 ;//每页显示多少条
    
    public int getPageRecords() {
		return pageRecords;
	}

	public void setPageRecords(int pageRecords) {
		this.pageRecords = pageRecords;
	}

	public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
