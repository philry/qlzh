package com.sy.vo;

import org.springframework.data.domain.Page;

public class PageJsonResult extends JsonResult{

    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totleCounts;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotleCounts() {
        return totleCounts;
    }

    public void setTotleCounts(Integer totleCounts) {
        this.totleCounts = totleCounts;
    }

    public static PageJsonResult buildSuccessPage(Integer code, Page page){

        PageJsonResult pageJsonResult = new PageJsonResult();

        pageJsonResult.setCode(code);
        pageJsonResult.setPageNumber(page.getNumber());
        pageJsonResult.setPageSize(page.getSize());
        pageJsonResult.setTotalPages(page.getTotalPages());
        pageJsonResult.setTotleCounts((int)page.getTotalElements());
        pageJsonResult.setData(page.getContent());

        return pageJsonResult;
    }

    public static PageJsonResult buildFailurePage(int code, String errMsg) {
        PageJsonResult pageJsonResult = new PageJsonResult();
        pageJsonResult.setCode(code);
        pageJsonResult.setError(errMsg);
        return pageJsonResult;
    }
}
