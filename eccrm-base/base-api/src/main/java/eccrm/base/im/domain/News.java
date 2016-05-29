package eccrm.base.im.domain;

import com.ycrl.base.common.CommonDomain;
import eccrm.base.attachment.AttachmentSymbol;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 新闻公告
 *
 * @author Michael
 */
@Entity
@Table(name = "news")
public class News extends CommonDomain implements AttachmentSymbol {
    /**
     * 公告分类：业务参数
     */
    public static final String CATEGORY = "GG_TYPE";
    /**
     * 公告发布状态：系统参数
     */
    public static final String PUBLISH_STATE = "SP_NEWS_STATE";
    /**
     * 未发布
     */
    public static final String PUBLISH_STATE_UNPUBLISHED = "UNPUBLISHED";
    /**
     * 已发布
     */
    public static final String PUBLISH_STATE_PUBLISHED = "PUBLISHED";
    /**
     * 已注销
     */
    public static final String PUBLISH_STATE_CANCELED = "CANCELED";

    @Column(length = 40, nullable = false)
    private String title;       // 标题
    @Column(length = 100)
    private String summary;     // 摘要
    @Column(length = 2000)
    private String content;     // 内容
    @Column(length = 40)
    private String category;    // 业务参数（公告分类）

    @Column
    private Date startTime;     // 生效时间
    @Column
    private Date endTime;       // 失效时间
    @Column
    private Boolean isTop;      // 是否顶置
    @Column
    private Boolean recordRead; // 是否记录阅读
    @Column
    private String publisherId;     // 发布人ID
    @Column
    private String publisherName;   // 发布人名称
    @Column
    private Date publishTime;       // 发布时间
    @Column
    private String publishState;    // 系统参数（发布状态）

    @Column
    private String receiverType;    // 接收者类型


    private String status;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getRecordRead() {
        return recordRead;
    }

    public void setRecordRead(Boolean recordRead) {
        this.recordRead = recordRead;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublishState() {
        return publishState;
    }

    public void setPublishState(String publishState) {
        this.publishState = publishState;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String businessId() {
        return getId();
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
}
