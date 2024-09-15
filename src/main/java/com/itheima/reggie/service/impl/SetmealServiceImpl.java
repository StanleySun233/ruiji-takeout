package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.controller.FileController;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.exception.SetmealNotDiscardException;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.model.Setmeal;
import com.itheima.reggie.model.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileController fileController;


    @Override
    @Transactional
    public void saveSetmealDto(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = Long.parseLong(setmealDto.getId());
        List<SetmealDish> setmealDishList = setmealDto
                .getSetmealDishes().stream()
                .map((item) -> {
                    item.setSetmealId(setmealId);
                    return item;
                }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishList);
    }

    @Transactional
    public SetmealDto setmealToSetmealDto(Setmeal setmeal) {
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        setmealDto.setSetmealDishes(setmealDishService.list(setmealDishLambdaQueryWrapper));
        setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
        return setmealDto;
    }

    @Transactional
    @Override
    public SetmealDto getSetmealDtoById(String id) {
        Setmeal setmeal = setmealService.getById(id);
        return this.setmealToSetmealDto(setmeal);
    }

    @Transactional
    @Override
    public List<SetmealDto> getSetmealDtoByName(String name) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name).orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);

        return setmealList.stream()
                .map(this::setmealToSetmealDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SetmealDto> getSetmealListWithDishesByIds(String[] ids) {
        return Arrays.stream(ids)
                .map(this::getSetmealDtoById)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Page<SetmealDto> getPagesByName(int page, int pageSize, String name) {
        Page<Setmeal> pages = new Page<>(page, pageSize);
        Page<SetmealDto> pageDtos = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pages, setmealLambdaQueryWrapper);
        BeanUtils.copyProperties(page, pageDtos, "records");

        pageDtos.setRecords(pages.getRecords().stream().map(this::setmealToSetmealDto).collect(Collectors.toList()));


        return pageDtos;
    }

    @Transactional
    @Override
    public void deleteSetmealDtoById(String id) {
        Setmeal setmeal = setmealService.getById(id);
        if (setmeal == null)
            throw new SetmealNotDiscardException("id:" + id + "不存在");

        if (setmeal.getStatus() == 1)
            throw new SetmealNotDiscardException(setmeal.getName() + "处于启用状态，无法删除");

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(StringUtils.isNotEmpty(setmeal.getId()), SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);
        for (SetmealDish setmealDish : setmealDishList) {
            setmealDishService.removeById(setmealDish.getId());
        }
        setmealService.removeById(setmeal.getId());
        fileController.deleteFile(setmeal.getImage());
    }
}
