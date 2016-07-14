package com.michael.spec.web;

import com.michael.spec.bo.CustomerNewsBo;
import com.michael.spec.domain.CustomerNews;
import com.michael.spec.service.CustomerNewsService;
import com.michael.spec.vo.CustomerNewsVo;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/customerNews"})
public class CustomerNewsCtrl extends BaseController {
    @Resource
    private CustomerNewsService customerNewsService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/customerNews/list/customerNews_list";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        CustomerNews customerNews = GsonUtils.wrapDataToEntity(request, CustomerNews.class);
        customerNewsService.save(customerNews);
        GsonUtils.printSuccess(response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        CustomerNews customerNews = GsonUtils.wrapDataToEntity(request, CustomerNews.class);
        customerNewsService.update(customerNews);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        CustomerNewsVo vo = customerNewsService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        CustomerNewsBo bo = GsonUtils.wrapDataToEntity(request, CustomerNewsBo.class);
        PageVo pageVo = customerNewsService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        customerNewsService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
