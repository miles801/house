package eccrm.base.im.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 公告实际接收者
 *
 * @author Michael
 */
@Entity
@Table(name = "msg_real_receiver")
public class NewsRealReceiver extends CommonDomain {

    @NotNull
    @Column(length = 40, nullable = false)
    private String newsId;          // 公告ID
    @Column(length = 40)
    private String newsTitle;       // （冗余）公告标题
    @Column(length = 40)
    private String category;        // （冗余）公告类型
    @Column
    private Date publishTime;       // （冗余）发布时间
    @NotNull
    @Column(length = 40, nullable = false)
    private String receiverId;      // 接收者ID

    @NotNull
    @Column(length = 40, nullable = false)
    private String receiverName;    // 接收者名称

    @Column
    private Boolean hasRead;        // 是否已读
    @Column
    private Date readTime;          // 阅读时间
    @Column
    private Boolean hasReply;       // 是否回复
    @Column
    private Date replyTime;         // 回复时间
    @Column
    private Boolean hasStar;          // 是否收藏
    @Column
    private Date starTime;          // 加入收藏的时间
    @Column(length = 40)
    private String status;

    @Column(length = 1000)
    private String description;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Boolean getHasReply() {
        return hasReply;
    }

    public void setHasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Boolean getHasStar() {
        return hasStar;
    }

    public void setHasStar(Boolean hasStar) {
        this.hasStar = hasStar;
    }

    public Date getStarTime() {
        return starTime;
    }

    public void setStarTime(Date starTime) {
        this.starTime = starTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
