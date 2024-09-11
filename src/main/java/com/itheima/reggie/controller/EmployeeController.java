package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.model.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee)
    {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee queryEmployee =employeeService.getOne(queryWrapper);

        if(queryEmployee==null) return R.error("账号不存在");

        if(!password.equals(queryEmployee.getPassword())) return R.error("账号或密码错误");

        if(queryEmployee.getStatus() == 0) return R.error("账号已禁用");

        request.getSession().setAttribute("employee",queryEmployee.getId());

        return R.success(queryEmployee);
    }

    @PostMapping("/logout")
    public R<String>  logout(HttpServletRequest request)
    {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

//    @PostMapping("/page")
//    public R<Page<Employee>> page(HttpServletRequest request)
//    {
//        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
//         employees = queryWrapper.orderByAsc(Employee::getId);
//
//
//        return R.success();
//    }
}
