package com.michael.spec.service;

import com.michael.spec.bo.RoomBusinessBo;
import com.michael.spec.domain.RoomBusiness;
import com.michael.spec.vo.RoomBusinessVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface RoomBusinessService {

    /**
     * 保存
     */
    String save(RoomBusiness roomBusiness);

    /**
     * 分页查询
     */
    PageVo pageQuery(RoomBusinessBo bo);

    /**
     * 根据ID查询对象的信息
     */
    RoomBusinessVo findById(String id);

}
