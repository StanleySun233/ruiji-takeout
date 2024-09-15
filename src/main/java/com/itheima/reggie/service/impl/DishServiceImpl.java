package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.model.Dish;
import com.itheima.reggie.model.DishFlavor;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void updateWithFlavorById(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = Long.parseLong(dishDto.getId());
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }

    @Override
    public DishDto getDishDtoById(String dishId) {
        Dish dish = this.getById(dishId);
        DishDto dto = new DishDto();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        BeanUtils.copyProperties(dish, dto);
        dto.setFlavors(flavors);

        return dto;
    }

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = Long.parseLong(dishDto.getId());
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }

    @Override
    @Transactional
    public void deleteWithFlavorById(String dishId) {
        Dish dish = this.getById(dishId);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);

        File file = new File(dish.getImage());
        file.delete();

        this.removeById(dishId);
    }
}
