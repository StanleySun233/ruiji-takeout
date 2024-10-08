package com.itheima.reggie.dto;

import com.itheima.reggie.model.Dish;
import com.itheima.reggie.model.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors;

    private String categoryName;

    private Integer copies;
}
