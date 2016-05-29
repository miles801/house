package com.michael.base.emp.web;

import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.GsonUtils;
import com.ycrl.utils.string.StringUtils;
import eccrm.core.security.LoginInfo;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author miles
 * @datetime 2014/3/20 11:06
 */
@Controller
@Scope("prototype")
public class LoginCtrl extends BaseController{
    private Logger logger = Logger.getLogger(LoginCtrl.class);
    @Resource
    private EmpService empService;


    /**
     * 用户登录
     * 必须传入username和password属性
     * 默认为admin/eccrm
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public void login(HttpServletResponse response, HttpServletRequest request) {
        // 获得登录信息
        EmpTemp emp = GsonUtils.wrapDataToEntity(request, EmpTemp.class);
        Assert.notNull(emp, "登录失败!未获取到登录信息!");
        EmpVo vo = empService.login(emp.getLoginName(), emp.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute(LoginInfo.HAS_LOGIN, true);
        session.setAttribute(LoginInfo.USERNAME, vo.getLoginName());
        session.setAttribute(LoginInfo.EMPLOYEE, vo.getId());
        session.setAttribute(LoginInfo.EMPLOYEE_NAME, vo.getName());
        session.setAttribute(LoginInfo.LOGIN_DATETIME, new Date().getTime());
        session.setAttribute(LoginInfo.ORG, vo.getOrgId());
        session.setAttribute(LoginInfo.ORG_NAME, vo.getOrgName());

        //写入Cookie
        try {
            response.addCookie(new Cookie("eccrmContext.id", vo.getId()));
            response.addCookie(new Cookie("eccrmContext.employeeName", URLEncoder.encode(URLEncoder.encode(vo.getName(), "utf-8"), "utf-8")));
            if (StringUtils.isNotEmpty(vo.getOrgId())) {
                response.addCookie(new Cookie("eccrmContext.orgId", vo.getOrgId()));
            }
            if (StringUtils.isNotEmpty(vo.getOrgName())) {
                response.addCookie(new Cookie("eccrmContext.orgName", URLEncoder.encode(URLEncoder.encode(vo.getOrgName(), "utf-8"), "utf-8")));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GsonUtils.printJson(response, "success", true);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "redirect:/index.html";
    }
}
