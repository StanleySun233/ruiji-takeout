package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.model.Dish;

public interface DishService extends IService<Dish> {
    public void updateWithFlavorById(DishDto dishDto);

    public DishDto getDishDtoById(String dishId);

    public void saveWithFlavor(DishDto dishDto);

    public void deleteWithFlavorById(String dishId);
}
