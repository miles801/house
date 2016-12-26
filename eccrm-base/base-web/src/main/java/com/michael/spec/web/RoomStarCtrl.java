package com.michael.spec.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michael.poi.exp.ExportEngine;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.RoomStarService;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.DateStringConverter;
import com.ycrl.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/roomStar"})
public class RoomStarCtrl extends BaseController {
    @Resource
    private RoomStarService roomStarService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/roomStar/list/roomStar_list";
    }

    /**
     * 添加关注
     *
     * @param roomId 房屋ID
     */
    @RequestMapping(value = "/star", params = "roomId", method = RequestMethod.POST)
    @ResponseBody
    public void save(String roomId, HttpServletRequest request, HttpServletResponse response) {
        roomStarService.star(roomId, SecurityContext.getEmpId());
        GsonUtils.printSuccess(response);
    }

    /**
     * 取消关注
     *
     * @param roomId 房屋ID
     */
    @RequestMapping(value = "/cancel", params = "roomId", method = RequestMethod.POST)
    @ResponseBody
    public void cancel(String roomId, HttpServletRequest request, HttpServletResponse response) {
        roomStarService.cancel(roomId, SecurityContext.getEmpId());
        GsonUtils.printSuccess(response);
    }

    /**
     * 指定的房屋是否已经被关注
     *
     * @param roomId 房屋ID
     */
    @RequestMapping(value = "/isStar", params = "roomId", method = RequestMethod.GET)
    @ResponseBody
    public void isStar(String roomId, HttpServletRequest request, HttpServletResponse response) {
        boolean stared = roomStarService.isStar(roomId, SecurityContext.getEmpId());
        GsonUtils.printData(response, stared);
    }


    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        PageVo pageVo = roomStarService.myStarRoom(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        roomStarService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    // 导出数据
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateStringConverter("yyyy-MM-dd HH:mm:ss"))
                .create();
        RoomBo bo = GsonUtils.wrapDataToEntity(request, RoomBo.class);
        PageVo pageVo = roomStarService.myStarRoom(bo);
        List<RoomView> data = pageVo.getData();
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

}
