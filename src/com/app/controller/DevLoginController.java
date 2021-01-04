package com.app.controller;

import com.app.pojo.DevUser;
import com.app.service.DevService;
import com.app.util.EmptyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@Controller
@RequestMapping(value = "/dev")
public class DevLoginController {
    @Resource
    private DevService devService;
    //权限处理
    @RequestMapping("/main")
    public String main(HttpServletRequest request) throws NoPermissionException {
        DevUser devUserSession = (DevUser) request.getSession().getAttribute("devUserSession");
        if(devUserSession == null){
            //进行全局异常处理
            throw new NoPermissionException();
        }else {
            return "developer/main";
        }
    }

    @RequestMapping(value = "/login")
    public String devLogin(){
        return "devlogin";
    }
    @RequestMapping(value = "/dologin",method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(required = true) String devCode,
                          @RequestParam(required = true) String devPassword){
        //用户名和密码的判空
        if(EmptyUtils.isNotEmpty(devCode) && EmptyUtils.isNotEmpty(devPassword)){
           //进行查询
            DevUser devUser =  devService.doLogin(devCode,devPassword);
           //判空
            if(EmptyUtils.isNotEmpty(devUser)){
               request.getSession().setAttribute("devUserSession",devUser);
               return "redirect:/dev/main";
           }else {
                request.setAttribute("error","用户名或者密码错误");
            }
        }else {
            request.setAttribute("error","用户名或密码不能为空");
        }
        return "devlogin";
    }
}
