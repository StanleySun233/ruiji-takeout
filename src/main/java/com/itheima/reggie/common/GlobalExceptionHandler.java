package com.itheima.reggie.common;

import com.sun.org.apache.bcel.internal.generic.RET;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        if (e.getMessage().contains("Duplicate entry")) {
            String[] split = e.getMessage().split(" ");
            StringBuffer sb = new StringBuffer();
            sb.append("账号").append(split[2], 1, split[2].length()-1).append("已存在");
            String msg = sb.toString();
            log.error(msg);
            return R.error(msg);
        }
        log.error(e.getMessage());
        return R.error("失败了");
    }
}
