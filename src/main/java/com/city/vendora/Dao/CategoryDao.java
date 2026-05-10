package com.city.vendora.Dao;

import com.city.vendora.entity.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    List<Category> findAll() throws SQLException;
    Category findById(Integer id) throws SQLException;
    // 可能需要添加、修改、删除性质（基础数据维护），可选择性实现
}
