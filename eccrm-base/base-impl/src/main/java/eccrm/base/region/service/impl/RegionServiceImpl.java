package eccrm.base.region.service.impl;

import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.exception.NullParamException;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.uuid.UUIDGenerator;
import eccrm.base.region.bo.RegionBo;
import eccrm.base.region.dao.RegionDao;
import eccrm.base.region.domain.Region;
import eccrm.base.region.service.RegionService;
import eccrm.base.region.vo.RegionVo;
import eccrm.core.VoHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: miles
 * @datetime: 2014-03-25
 */
@Service("regionService")
public class RegionServiceImpl implements RegionService, BeanWrapCallback<Region, RegionVo> {
    @Resource
    private RegionDao regionDao;

    @Override
    public String save(Region region) {
        region.setId(UUIDGenerator.generate());
        region.setDeleted(false);
        String id = regionDao.save(region);
        return id;
    }

    @Override
    public void update(Region region) {
        regionDao.update(region);
    }

    @Override
    public PageVo query(RegionBo bo) {
        PageVo vo = new PageVo();
        Long total = regionDao.getTotal(bo);
        if (total == null || total == 0) return vo;
        vo.setTotal(total);
        List<Region> regions = regionDao.query(bo);
        vo.setData(VoHelper.wrapVos(regions, this));
        return vo;
    }

    @Override
    public RegionVo findById(String id) {
        return wrap(regionDao.findById(id));
    }


    @Override
    public void deleteByIds(String... ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            RegionBo bo = new RegionBo();
            bo.setParentId(id);
            Long total = regionDao.getTotal(bo);
            Assert.isTrue(total == null || total == 0, "删除失败!行政区域下包含子的行政区域，不允许删除!");
            regionDao.deleteById(id);
        }
    }

    @Override
    public List<RegionVo> tree(RegionBo bo) {
        //查询数据
        List<Region> regions = regionDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(regions, RegionVo.class);
    }

    @Override
    public RegionVo queryByCode(String code) {
        RegionVo vo = new RegionVo();
        if (StringUtils.isBlank(code)) {
            throw new NullParamException("区号不能为空!");
        }
        RegionBo regionBo = new RegionBo();
        regionBo.setCode(code);
        List<Region> regions = regionDao.query(regionBo);
        if (regions != null && regions.size() > 0) {
            Region region = regions.get(0);
            vo.setId(region.getId());
            vo.setName(region.getName());
        }
        return vo;
    }

    @Override
    public RegionVo wrap(Region region) {
        if (region == null) return null;
        RegionVo vo = new RegionVo();
        BeanUtils.copyProperties(region, vo);
        return vo;
    }

    @Override
    public void doCallback(Region region, RegionVo vo) {
    }

    @Override
    public RegionVo getBelongProvence(String city) {
        Region region = regionDao.getBelongProvence(city);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name"})
                .wrap(region, RegionVo.class);
    }
}
