package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.model.Setmeal;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<SetmealDto> pages = setmealService.getPagesByName(page, pageSize, name);

        return R.success(pages);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveSetmealDto(setmealDto);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> deleteByIds(@RequestParam String ids) {
        String[] idList = ids.split(",");
        for (String s : idList) {
            setmealService.deleteSetmealDtoById(s);
        }
        return R.success("删除成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable String id) {
        return R.success(setmealService.getSetmealDtoById(id));
    }

    @PostMapping("/status/{status}")
    public R<String> updateSetmealStatusByIds(@PathVariable int status, @RequestParam String ids) {
        String[] idList = ids.split(",");
        for (String s : idList) {
            Setmeal setmeal = setmealService.getById(s);
            if (setmeal != null) {
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
            }
        }
        return R.success("禁用成功");
    }

}
