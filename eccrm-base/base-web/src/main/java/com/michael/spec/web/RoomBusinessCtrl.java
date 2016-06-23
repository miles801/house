package com.michael.spec.web;

import com.michael.spec.bo.RoomBusinessBo;
import com.michael.spec.domain.RoomBusiness;
import com.michael.spec.service.RoomBusinessService;
import com.michael.spec.vo.RoomBusinessVo;
import com.ycrl.base.common.JspAccessType;
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
@RequestMapping(value = {"/house/roomBusiness"})
public class RoomBusinessCtrl extends BaseController {
    @Resource
    private RoomBusinessService roomBusinessService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "house/roomBusiness/list/roomBusiness_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "house/roomBusiness/edit/roomBusiness_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        RoomBusiness roomBusiness = GsonUtils.wrapDataToEntity(request, RoomBusiness.class);
        roomBusinessService.save(roomBusiness);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "house/roomBusiness/edit/roomBusiness_edit";
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "house/roomBusiness/edit/roomBusiness_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        RoomBusinessVo vo = roomBusinessService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        RoomBusinessBo bo = GsonUtils.wrapDataToEntity(request, RoomBusinessBo.class);
        PageVo pageVo = roomBusinessService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }


}
