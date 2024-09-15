package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.model.Setmeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveSetmealDto(SetmealDto setmealDto);

    @Transactional
    SetmealDto getSetmealDtoById(String id);

    @Transactional
    List<SetmealDto> getSetmealDtoByName(String name);

    @Transactional
    List<SetmealDto> getSetmealListWithDishesByIds(String[] id);

    Page<SetmealDto> getPagesByName(int page, int pageSize, String name);

    void deleteSetmealDtoById(String id);
}
