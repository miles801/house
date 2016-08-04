package com.michael.spec.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michael.poi.exp.ExportEngine;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomRent;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
import com.michael.spec.vo.RoomVo;
import com.ycrl.base.common.JspAccessType;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.DateStringConverter;
import com.ycrl.utils.gson.GsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
        return "house/room/list/room_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "house/room/list/room_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Room room = GsonUtils.wrapDataToEntity(request, Room.class);
        String id = roomService.save(room);
        GsonUtils.printData(response, id);
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
        return "house/room/list/room_edit";
    }

    /**
     * 通过房屋编号调整到明细页面
     *
     * @param code 房屋编号
     */
    @RequestMapping(value = {"/view"}, params = {"code"}, method = RequestMethod.GET)
    public String toView(@RequestParam String code, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        RoomVo vo = roomService.findByCode(code);
        if (vo != null) {
            request.setAttribute("id", vo.getId());
        }
        return "house/room/list/room_detail";
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
    @RequestMapping(value = "/addRent", method = RequestMethod.POST)
    public void addRent(HttpServletRequest request, HttpServletResponse response) {
        RoomData roomData = GsonUtils.wrapDataToEntity(request, RoomData.class);
        Assert.notNull(roomData, "操作失败!数据不能为空!");
        Assert.notNull(roomData.getCustomer(), "操作失败!租户信息不能为空!");
        Assert.notNull(roomData.getRoomRent(), "操作失败!租赁信息不能为空!");
        roomService.addRent(roomData.getRoomId(), roomData.getCustomer(), roomData.getRoomRent());
    }

    @ResponseBody
    @RequestMapping(value = "/changeRent", method = RequestMethod.POST)
    public void changeRent(HttpServletRequest request, HttpServletResponse response) {
        RoomData roomData = GsonUtils.wrapDataToEntity(request, RoomData.class);
        Assert.notNull(roomData, "操作失败!数据不能为空!");
        Assert.notNull(roomData.getCustomer(), "操作失败!租户信息不能为空!");
        Assert.notNull(roomData.getRoomRent(), "操作失败!租赁信息不能为空!");
        roomService.changeRent(roomData.getRoomId(), roomData.getCustomer(), roomData.getRoomRent());
    }

    @ResponseBody
    @RequestMapping(value = "/updateRent", method = RequestMethod.POST)
    public void updateRent(HttpServletRequest request, HttpServletResponse response) {
        RoomData roomData = GsonUtils.wrapDataToEntity(request, RoomData.class);
        Assert.notNull(roomData, "操作失败!数据不能为空!");
        Assert.notNull(roomData.getCustomer(), "操作失败!租户信息不能为空!");
        Assert.notNull(roomData.getRoomRent(), "操作失败!租赁信息不能为空!");
        roomService.updateRent(roomData.getRoomId(), roomData.getCustomer(), roomData.getRoomRent());
    }

    @ResponseBody
    @RequestMapping(value = "/rent", params = "id", method = RequestMethod.GET)
    public void findCurrentRent(String id, HttpServletResponse response) {
        RoomRent roomRent = roomService.findCurrent(id);
        GsonUtils.printData(response, roomRent);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteRent", params = "rentId", method = RequestMethod.POST)
    public void deleteRent(String rentId, HttpServletResponse response) {
        roomService.deleteRent(rentId);
        GsonUtils.printSuccess(response);
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
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        PageVo data = roomService.pageQuery(bo);
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

    // 批量申请无效
    @ResponseBody
    @RequestMapping(value = "/applyInvalid", params = {"ids"}, method = RequestMethod.POST)
    public void batchInvalid(@RequestParam String ids, HttpServletResponse response) {
        roomService.applyInvalid(ids.split(","));
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


    // 导出数据
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateStringConverter("yyyy-MM-dd HH:mm:ss"))
                .create();
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        List<RoomView> data = roomService.query(bo);
        String json = gson.toJson(data);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("房屋数据" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            new ExportEngine().export(response.getOutputStream(), this.getClass().getClassLoader().getResourceAsStream("export_room.xlsx"), o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 下载模板
    @ResponseBody
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response) {
        InputStream input = RoomCtrl.class.getClassLoader().getResourceAsStream("import_room.xlsx");
        Assert.notNull(input, "模板下载失败!房屋数据导入模板不存在!");
        response.setContentType("application/vnd.ms-excel");
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("房屋数据导入模板.xlsx", "UTF-8");
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
        roomService.importData(attachmentIds.split(","));
        GsonUtils.printSuccess(response);
    }
}
