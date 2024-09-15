package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.exception.CategoryNotZeroException;
import com.itheima.reggie.model.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize, @Nullable String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Category::getName, name)
                .orderByDesc(Category::getSort);

        Page<Category> pages = new Page<>(page, pageSize);
        categoryService.page(pages, queryWrapper);

        return R.success(pages);
    }

    @DeleteMapping
    public R<String> deleteById(String ids) {
        try {
            categoryService.removeById(ids);
        } catch (CategoryNotZeroException e) {
            return R.error(e.getMessage());
        }

        return R.success("删除成功");
    }

    @PutMapping
    public R<String> updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category.getType().toString()), Category::getType, category.getType()).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
