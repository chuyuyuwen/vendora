package com.city.vendora.entity;

import java.util.Date;

public class Product {
    private String productId;      // 商品编号
    private String productName;
    private Integer categoryId;    // 外键
    private Date createTime;
    private Integer status;        // 1在用，0停用
    // 扩展显示用字段（非数据库字段）
    private String categoryName;   // 性质名称，查询时连接获取

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
