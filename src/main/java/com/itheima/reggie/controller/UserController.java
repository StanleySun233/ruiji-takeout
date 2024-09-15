package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.model.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.EmailUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) throws MessagingException {
        String id = user.getPhone();
        if(StringUtils.isNotEmpty(id)){
            String validCode = ValidateCodeUtils.generateValidateCode(6).toString();

            EmailUtils.sendEmail(id,"您的验证码",validCode);
            log.info(validCode);
            httpSession.setAttribute("phone",validCode);
            return R.success("验证码发送成功");
        }


        return R.error("验证码发送失败");
    }

}
