package com.city.vendora.util;

import java.util.List;

/**
 * 分页工具类，封装分页查询结果
 * @param <T> 实体类型
 */
public class PageBean<T> {
    private int currentPage;      // 当前页码（从1开始）
    private int pageSize;         // 每页记录数
    private int totalRecords;     // 总记录数
    private int totalPages;       // 总页数
    private List<T> data;         // 当前页数据列表

    // 构造方法，传入当前页、每页大小、总记录数、当前页数据
    public PageBean(int currentPage, int pageSize, int totalRecords, List<T> data) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.data = data;
        // 自动计算总页数（向上取整）
        this.totalPages = (totalRecords + pageSize - 1) / pageSize;
    }

    // Getter 方法
    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getData() {
        return data;
    }

    // 判断是否有上一页/下一页，方便JSP页面控制翻页按钮
    public boolean isHasPrevious() {
        return currentPage > 1;
    }

    public boolean isHasNext() {
        return currentPage < totalPages;
    }

    // 获取上一页/下一页页码
    public int getPreviousPage() {
        return currentPage - 1;
    }

    public int getNextPage() {
        return currentPage + 1;
    }
}