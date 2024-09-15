package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.exception.CategoryNotZeroException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.model.Category;
import com.itheima.reggie.model.Dish;
import com.itheima.reggie.model.Setmeal;
import com.itheima.reggie.model.User;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



}
