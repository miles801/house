package com.michael.spec.web;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
import com.ycrl.base.common.JspAccessType;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
        RoomView vo = roomService.findById(id);
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
        RoomData roomData = GsonUtils.wrapDataToEntity(request, RoomData.class);
        Assert.notNull(roomData, "操作失败!数据不能为空!");
        Assert.notNull(roomData.getCustomer(), "操作失败!客户信息不能为空!");
        roomService.addCustomer(roomId, roomData.getCustomer(), roomData.getRoomBusiness());
    }

    @ResponseBody
    @RequestMapping(value = "/customer", params = "roomId", method = RequestMethod.GET)
    public void getCustomer(String roomId, HttpServletRequest request, HttpServletResponse response) {
        CustomerVo customerVo = roomService.getCustomer(roomId);
        GsonUtils.printData(response, customerVo);
    }

    @ResponseBody
    @RequestMapping(value = "/building", params = "roomId", method = RequestMethod.GET)
    public void getBuilding(String roomId, HttpServletRequest request, HttpServletResponse response) {
        BuildingVo vo = roomService.getBuilding(roomId);
        GsonUtils.printData(response, vo);
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


    // 批量申请新增
    @ResponseBody
    @RequestMapping(value = "/batchAdd", params = {"ids"}, method = RequestMethod.POST)
    public void batchAdd(@RequestParam String ids, HttpServletResponse response) {
        roomService.batchAdd(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量申请修改
    @ResponseBody
    @RequestMapping(value = "/batchModify", params = {"ids"}, method = RequestMethod.POST)
    public void batchModify(@RequestParam String ids, HttpServletResponse response) {
        roomService.batchPass(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量同意
    @ResponseBody
    @RequestMapping(value = "/batchPass", params = {"ids"}, method = RequestMethod.POST)
    public void batchPass(@RequestParam String ids, HttpServletResponse response) {
        roomService.batchPass(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    // 批量拒绝
    @ResponseBody
    @RequestMapping(value = "/batchDeny", params = {"ids"}, method = RequestMethod.POST)
    public void batchDeny(@RequestParam String ids, HttpServletResponse response) {
        roomService.batchDeny(ids.split(","));
        GsonUtils.printSuccess(response);
    }
}
