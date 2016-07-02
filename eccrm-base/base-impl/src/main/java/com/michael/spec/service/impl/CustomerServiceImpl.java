package com.michael.spec.service.impl;

import com.michael.base.common.BaseParameter;
import com.michael.poi.adapter.AnnotationCfgAdapter;
import com.michael.poi.core.Context;
import com.michael.poi.core.Handler;
import com.michael.poi.core.ImportEngine;
import com.michael.poi.core.RuntimeContext;
import com.michael.poi.imp.cfg.Configuration;
import com.michael.spec.bo.CustomerBo;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.CustomerDTO;
import com.michael.spec.domain.Room;
import com.michael.spec.service.CustomerService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.vo.CustomerVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.number.IntegerUtils;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.attachment.AttachmentProvider;
import eccrm.base.attachment.utils.AttachmentHolder;
import eccrm.base.attachment.vo.AttachmentVo;
import eccrm.base.parameter.service.ParameterContainer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService, BeanWrapCallback<Customer, CustomerVo> {
    @Resource
    private CustomerDao customerDao;

    @Resource
    private RoomDao roomDao;

    @Override
    public String save(Customer customer) {
        // 设置编号
        String code = customerDao.maxCode();
        int newNo = 1;
        if (StringUtils.isNotEmpty(code)) {
            newNo = Integer.parseInt(code.substring(1)) + 1;
        }
        DecimalFormat decimalFormat = new DecimalFormat("C000000");
        customer.setCode(decimalFormat.format(newNo));

        // 设置状态
        customer.setStatus(Room.STATUS_APPLY_ADD);
        ValidatorUtils.validate(customer);
        String id = customerDao.save(customer);
        return id;
    }

    @Override
    public void update(Customer customer) {
        ValidatorUtils.validate(customer);
        customerDao.update(customer);
    }

    @Override
    public PageVo pageQuery(CustomerBo bo) {
        PageVo vo = new PageVo();
        Long total = customerDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Customer> customerList = customerDao.query(bo);
        List<CustomerVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(customerList, CustomerVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public CustomerVo findById(String id) {
        Customer customer = customerDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrap(customer, CustomerVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            // 判断该客户是否具有房产
            List<String> rooms = roomDao.findCodeByCustomer(id);
            Assert.isTrue(rooms == null || rooms.isEmpty(), "删除客户失败!该客户具有房产信息，请先变更房产的客户再行删除!");

            // 删除客户信息
            customerDao.deleteById(id);
        }
    }

    @Override
    public void applyInvalid(String[] ids) {
        Assert.notEmpty(ids, "申请失败!客户ID不能为空!");
        customerDao.batchSetStatus(ids, Room.STATUS_APPLY_INVALID);
    }

    @Override
    public void batchPassInvalid(String[] ids) {
        Assert.notEmpty(ids, "操作失败!客户ID不能为空!");
        customerDao.batchSetStatus(ids, Room.STATUS_INVALID);
    }

    @Override
    public void batchPass(String[] customerIds) {
        Assert.notEmpty(customerIds, "操作失败!ID不能为空!");
        for (String id : customerIds) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            String status = customer.getStatus();
            if (Room.STATUS_APPLY_ADD.equals(status) || Room.STATUS_APPLY_MODIFY.equals(status)) {
                customer.setStatus(Room.STATUS_ACTIVE);
            } else if (Room.STATUS_APPLY_INVALID.equals(status)) {
                customer.setStatus(Room.STATUS_INVALID);
            }
        }
    }

    @Override
    public void batchAdd(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Customer customer = customerDao.findById(id);
            // 只有“未录入”可以申请为新增
            if (customer != null && (Room.STATUS_INACTIVE.equals(customer.getStatus()) || Room.STATUS_INVALID.equals(customer.getStatus()))) {
                customer.setStatus(Room.STATUS_APPLY_ADD);
            }
        }
    }

    @Override
    public void batchModify(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            customer.setStatus(Room.STATUS_APPLY_MODIFY);
        }
    }

    @Override
    public void batchDeny(String[] customerIds) {
        Assert.notEmpty(customerIds, "操作失败!ID不能为空!");
        for (String id : customerIds) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            String status = customer.getStatus();
            if (Room.STATUS_APPLY_ADD.equals(status)) {
                customer.setStatus(Room.STATUS_INVALID);
            } else if (Room.STATUS_APPLY_MODIFY.equals(status) || Room.STATUS_APPLY_INVALID.equals(status)) {
                customer.setStatus(Room.STATUS_ACTIVE);
            }
        }
    }

    @Override
    public Integer addRoom(String customerId, String roomId) {
        Assert.hasText(customerId, "房产添加失败!客户ID不能为空!");
        Assert.hasText(roomId, "房产添加失败!房屋ID不能为空!");
        Customer customer = customerDao.findById(customerId);
        Assert.notNull(customer, "房产添加失败!客户不存在!");
        customer.setRoomCounts(IntegerUtils.add(customer.getRoomCounts(), 1));
        return customer.getRoomCounts();
    }


    @Override
    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(CustomerServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据文件不能为空，请重试!");

        final ParameterContainer parameterContainer = ParameterContainer.getInstance();
        for (String id : attachmentIds) {
            AttachmentVo vo = AttachmentProvider.getInfo(id);
            Assert.notNull(vo, "附件已经不存在，请刷新后重试!");
            File file = AttachmentHolder.newInstance().getTempFile(id);
            logger.info("准备导入数据：" + file.getAbsolutePath());
            logger.info("初始化导入引擎....");
            long start = System.currentTimeMillis();

            // 初始化引擎
            Configuration configuration = new AnnotationCfgAdapter(CustomerDTO.class).parse();
            configuration.setStartRow(2);
            String newFilePath = file.getAbsolutePath() + vo.getFileName().substring(vo.getFileName().lastIndexOf(".")); //获取路径
            try {
                FileUtils.copyFile(file, new File(newFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Map<String, String> params = new HashMap<String, String>();
            configuration.setPath(newFilePath);
            configuration.setHandler(new Handler<CustomerDTO>() {
                @Override
                public void execute(CustomerDTO dto) {
                    Context context = RuntimeContext.get();
                    Customer customer = new Customer();
                    BeanUtils.copyProperties(dto, customer);
                    // 设置参数
                    String sex = dto.getSex();
                    if (StringUtils.isNotEmpty(sex)) {
                        customer.setSex(parameterContainer.getBusinessValue(BaseParameter.SEX, sex));
                    }
                    String marriage = dto.getMarriage();
                    if (StringUtils.isNotEmpty(marriage)) {
                        customer.setMarriage(parameterContainer.getBusinessValue(BaseParameter.MARRIAGE, marriage));
                    }
                    String education = dto.getEducation();
                    if (StringUtils.isNotEmpty(education)) {
                        customer.setEducation(parameterContainer.getBusinessValue(BaseParameter.EDU, education));
                    }
                    String age = dto.getAge();
                    if (StringUtils.isNotEmpty(age)) {
                        customer.setAge(parameterContainer.getBusinessValue(Customer.AGE_STAGE, age));
                    }
                    String money = dto.getMoney();
                    if (StringUtils.isNotEmpty(money)) {
                        customer.setMoney(parameterContainer.getBusinessValue(Customer.MONEY_STAGE, money));
                    }
                    customer.setStatus(Room.STATUS_APPLY_ADD);
                    try {
                        save(customer);
                    } catch (Exception e) {
                        Assert.isTrue(false, String.format("数据异常!发生在第%d行!原因:%s", context.getRowIndex(), e.getMessage()));
                    }
                }
            });
            logger.info("开始导入数据....");
            ImportEngine engine = new ImportEngine(configuration);
            engine.execute();
            logger.info(String.format("导入数据成功,用时(%d)s....", (System.currentTimeMillis() - start) / 1000));
            new File(newFilePath).delete();
        }
    }

    @Override
    public void doCallback(Customer customer, CustomerVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setAgeName(container.getBusinessName(Customer.AGE_STAGE, customer.getAge()));
        vo.setMoneyName(container.getBusinessName(Customer.MONEY_STAGE, customer.getMoney()));
        vo.setSexName(container.getBusinessName(BaseParameter.SEX, customer.getSex()));
        vo.setEducationName(container.getBusinessName(BaseParameter.EDU, customer.getEducation()));
        vo.setMarriageName(container.getBusinessName(BaseParameter.MARRIAGE, customer.getMarriage()));
        vo.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, customer.getStatus()));

        // 名下房产
        List<String> roomKeys = roomDao.findCodeByCustomer(customer.getId());
        if (roomKeys != null && !roomKeys.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String s : roomKeys) {
                builder.append(",").append(s);
            }
            vo.setRoomCounts(roomKeys.size());
            vo.setRoomKeys(builder.substring(1));
        }
    }
}
