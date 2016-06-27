package com.michael.spec.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michael.poi.exp.ExportEngine;
import com.michael.spec.bo.CustomerBo;
import com.michael.spec.domain.Customer;
import com.michael.spec.service.CustomerService;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.CustomerVo;
import com.ycrl.base.common.JspAccessType;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.DateStringConverter;
import com.ycrl.utils.gson.GsonUtils;
import com.ycrl.utils.string.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/customer"})
public class CustomerCtrl extends BaseController {
    @Resource
    private CustomerService customerService;
    @Resource
    private RoomService roomService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "house/customer/list/customer_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        // 房屋ID
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("roomId", request.getParameter("roomId"));
        return "house/customer/edit/customer_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Customer customer = GsonUtils.wrapDataToEntity(request, Customer.class);
        customerService.save(customer);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        request.setAttribute("roomId", request.getParameter("roomId"));
        return "house/customer/edit/customer_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Customer customer = GsonUtils.wrapDataToEntity(request, Customer.class);
        // 如果传递了房屋ID，则表示更新客户的信息时，同时添加日志，否则只是单纯的更改客户信息
        String roomId = request.getParameter("roomId");
        if (StringUtils.isNotEmpty(roomId)) {
            roomService.addCustomer(roomId, customer, null);
        } else {
            customerService.update(customer);
        }
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "house/customer/edit/customer_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        CustomerVo vo = customerService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/applyInvalid", params = {"ids"}, method = RequestMethod.POST)
    public void applyInvalid(@RequestParam String ids, HttpServletResponse response) {
        customerService.applyInvalid(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量设置为无效
    @ResponseBody
    @RequestMapping(value = "/batchPassInvalid", params = {"ids"}, method = RequestMethod.POST)
    public void batchPassInvalid(@RequestParam String ids, HttpServletResponse response) {
        customerService.batchPassInvalid(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量申请添加
    @ResponseBody
    @RequestMapping(value = "/batchAdd", params = {"ids"}, method = RequestMethod.POST)
    public void batchAdd(@RequestParam String ids, HttpServletResponse response) {
        customerService.batchAdd(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量申请修改
    @ResponseBody
    @RequestMapping(value = "/batchModify", params = {"ids"}, method = RequestMethod.POST)
    public void batchModify(@RequestParam String ids, HttpServletResponse response) {
        customerService.batchModify(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量同意申请
    @ResponseBody
    @RequestMapping(value = "/batchPass", params = {"ids"}, method = RequestMethod.POST)
    public void batchPass(@RequestParam String ids, HttpServletResponse response) {
        customerService.batchPass(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量拒绝申请
    @ResponseBody
    @RequestMapping(value = "/batchDeny", params = {"ids"}, method = RequestMethod.POST)
    public void batchDeny(@RequestParam String ids, HttpServletResponse response) {
        customerService.batchDeny(ids.split(","));
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        CustomerBo bo = GsonUtils.wrapDataToEntity(request, CustomerBo.class);
        PageVo pageVo = customerService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        customerService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    // 导出数据
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateStringConverter("yyyy-MM-dd HH:mm:ss"))
                .create();
        CustomerBo bo = GsonUtils.wrapDataToEntity(request, CustomerBo.class);
        List<CustomerVo> data = customerService.pageQuery(bo).getData();
        if (data == null) {
            data = new ArrayList<CustomerVo>();
        }
        String json = gson.toJson(data);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("客户数据.xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            new ExportEngine().export(response.getOutputStream(), this.getClass().getClassLoader().getResourceAsStream("export_customer.xlsx"), o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 下载模板
    @ResponseBody
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response) {
        InputStream input = CustomerCtrl.class.getClassLoader().getResourceAsStream("import_customer.xlsx");
        response.setContentType("application/vnd.ms-excel");
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("客户数据导入模板.xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", disposition);
        try {
            IOUtils.copy(input, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/import", params = {"attachmentIds"}, method = RequestMethod.POST)
    public void importData(@RequestParam String attachmentIds, HttpServletResponse response) {
        customerService.importData(attachmentIds.split(","));
        GsonUtils.printSuccess(response);
    }
}
