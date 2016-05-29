package eccrm.base.log.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * 操作记录
 *
 * @author Michael
 */
public class OperateLog extends CommonDomain {
    /**
     * 操作类型
     *
     * @see eccrm.base.log.OperateType
     */
    @NotNull
    @Column(length = 40)
    private String operateType;
    // 操作内容
    @NotNull
    @Column(length = 1000)
    private String content;
    // 描述
    @Column(length = 1000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}
