package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.itemize.EmployeeStatusEnum;
import com.itheima.reggie.model.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee queryEmployee = employeeService.getOne(queryWrapper);

        if (queryEmployee == null) return R.error("账号不存在");

        if (!password.equals(queryEmployee.getPassword())) return R.error("账号或密码错误");

        if (queryEmployee.getStatus() == 0) return R.error("账号已禁用");

        request.getSession().setAttribute("employee", queryEmployee.getId());

        return R.success(queryEmployee);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
//        处理方法之一
//        LambdaQueryWrapper<Employee> queryWrapper= new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getUsername,employee.getUsername());
//        Employee queryEmployee =employeeService.getOne(queryWrapper);
//        if (queryEmployee != null)
//            return R.error("用户名已存在");

//        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        String currentEmployeeId = (String) httpServletRequest.getSession().getAttribute("employee");
//        employee.setCreateUser(currentEmployeeId);
//        employee.setUpdateUser(currentEmployeeId);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);


        log.info("新增员工: {}", employee.toString());
        return R.success("添加成功");
    }


    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, @Nullable String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        Page<Employee> pages = new Page<>(page, pageSize);
        employeeService.page(pages, queryWrapper);

        return R.success(pages);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id) {
        log.info("Select id = ", id);
        return R.success(employeeService.getById(id));
    }

    @PutMapping("/status")
    public R<String> status(HttpServletRequest request, @RequestBody Employee employee) {
        String id = (String) request.getSession().getAttribute("employee");
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, id);
        Employee currentEmployee = employeeService.getOne(queryWrapper);

        Employee updateEmployee = employeeService.getById(employee.getId());

        if (!currentEmployee.getUsername().equals("admin")) {
            return R.error("非管理员无法操作");
        }

        if (currentEmployee.getUsername().equals(updateEmployee.getUsername())) {
            return R.error("无法禁用管理员");
        }

        updateEmployee.setStatus(employee.getStatus());
//        updateEmployee.setUpdateTime(LocalDateTime.now());
//        updateEmployee.setUpdateUser(currentEmployee.getUpdateUser());
        employeeService.updateById(updateEmployee);
        return R.success("账号已" + EmployeeStatusEnum.fromCode(updateEmployee.getStatus()));
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        String id = (String) request.getSession().getAttribute("employee");
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, id);
        Employee currentEmployee = employeeService.getOne(queryWrapper);

        if (!currentEmployee.getUsername().equals("admin")) {
            return R.error("非管理员无法操作");
        }
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(currentEmployee.getId());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

}
