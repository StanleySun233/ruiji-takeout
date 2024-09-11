package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.model.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest,@RequestBody Employee employee)
    {
//        处理方法之一
//        LambdaQueryWrapper<Employee> queryWrapper= new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getUsername,employee.getUsername());
//        Employee queryEmployee =employeeService.getOne(queryWrapper);
//        if (queryEmployee != null)
//            return R.error("用户名已存在");

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long currentEmployeeId = (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setCreateUser(currentEmployeeId);
        employee.setUpdateUser(currentEmployeeId);
        employeeService.save(employee);


        log.info("新增员工: {}",employee.toString());
        return R.success("添加成功");
    }


    @GetMapping("/page")
    public R<Page<Employee>> page(HttpServletRequest request)
    {
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
//        <Employee> employeers = queryWrapper.orderByAsc(Employee::getId);


        return R.success(new Page<Employee>());
    }

}
