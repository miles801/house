package com.michael.spec.web;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.RoomVo;
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
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/room"})
public class RoomCtrl extends BaseController {
    @Resource
    private RoomService roomService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/room/list/room_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/room/edit/room_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Room room = GsonUtils.wrapDataToEntity(request, Room.class);
        roomService.save(room);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "house/room/list/room_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Room room = GsonUtils.wrapDataToEntity(request, Room.class);
        roomService.update(room);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/room/edit/room_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        RoomVo vo = roomService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        PageVo pageVo = roomService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/customer", params = "roomId", method = RequestMethod.POST)
    public void addCustomer(String roomId, HttpServletRequest request, HttpServletResponse response) {
        Customer customer = GsonUtils.wrapDataToEntity(request, Customer.class);
        roomService.addCustomer(roomId, customer);
    }

    @ResponseBody
    @RequestMapping(value = "/query", params = "unitId", method = RequestMethod.POST)
    public void query(String unitId, HttpServletRequest request, HttpServletResponse response) {
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        if (bo == null) {
            bo = new RoomBo();
        }
        bo.setUnitId(unitId);
        List<RoomView> data = roomService.query(bo);
        GsonUtils.printData(response, data);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        roomService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
