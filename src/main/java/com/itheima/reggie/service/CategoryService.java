package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.model.Category;

public interface CategoryService extends IService<Category> {
    public void removeById(String id);
}
