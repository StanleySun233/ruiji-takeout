package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.exception.CategoryNotZeroException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.model.Category;
import com.itheima.reggie.model.Dish;
import com.itheima.reggie.model.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Override
    public void removeById(String id) {
        // 是否关联菜品
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int countDish = dishService.count(dishQueryWrapper);
        if (countDish > 0) {
            throw new CategoryNotZeroException("当且标签关联菜品，无法直接删除");
        }

        // 是否关联商品

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int countSetmeal = setmealService.count(setmealQueryWrapper);

        if (countSetmeal > 0) {
            throw new CategoryNotZeroException("当且标签关联商品，无法直接删除");
        }

        super.removeById(id);
    }
}
