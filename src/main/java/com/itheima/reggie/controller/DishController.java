package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.model.Category;
import com.itheima.reggie.model.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByDesc(Dish::getUpdateTime);

        Page<Dish> dishPage = new Page<>(page, pageSize);
        dishService.page(dishPage,dishQueryWrapper);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtos = records.stream().map( (item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                if (StringUtils.isNotEmpty(categoryName)) {
                    dishDto.setCategoryName(categoryName);
                }
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavorById(dishDto);
        return R.success("菜品修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateDishStatusById(@PathVariable int status, @RequestParam String ids) {
        String[] idList = ids.split(",");
        for (String s : idList) {
            Dish dish = dishService.getById(s);
            if (dish != null) {
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("禁用成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable String id) {
        return R.success(dishService.getDishDtoById(id));
    }

    @DeleteMapping
    public R<String> deleteByIds(String ids) {
        String[] idList = ids.split(",");
        for (String s : idList)
            dishService.deleteWithFlavorById(s);

        return R.success("禁用成功");
    }

}
