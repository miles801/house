package eccrm.base.im.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公告接收者
 * 只是保存公告与接收者的关系，这里的接收者并不一定是实际的接收者
 *
 * @author Michael
 */
@Entity
@Table(name = "msg_receiver")
public class NewsReceiver extends CommonDomain {

    /**
     * 公告接收者类型：系统参数
     */
    public static final String TYPE = "NEWS_RECEIVER_TYPE";
    /**
     * 接收者类型：组织机构
     */
    public static final String TYPE_ORG = "ORG";
    /**
     * 接收者类型：机构岗位
     */
    public static final String TYPE_POSITION = "ORG_POSITION";
    /**
     * 接收者类型：全部
     */
    public static final String TYPE_ALL = "ALL";
    /**
     * 接收者类型：业态
     */
    public static final String TYPE_PARAM = "PARAM";

    // 接收者类型：系统参数
    @Column(length = 40)
    private String receiverType;
    @Column(length = 40)
    private String receiverId;      // 接收者ID
    @Column(length = 40)
    private String receiverName;    // 接收者名称
    @Column(length = 40)
    private String receiverId2;     // 辅助接收者ID
    @Column(length = 40)
    private String receiverName2;   // 辅助接收者名称
    @Column(length = 40)
    private String newsId;          // 新闻公告ID
    @Column(length = 40)
    private String newsTitle;       // 新闻公告标题

    @Column(length = 40)
    private String status;

    @Column(length = 1000)
    private String description;

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
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

    public String getReceiverId2() {
        return receiverId2;
    }

    public void setReceiverId2(String receiverId2) {
        this.receiverId2 = receiverId2;
    }

    public String getReceiverName2() {
        return receiverName2;
    }

    public void setReceiverName2(String receiverName2) {
        this.receiverName2 = receiverName2;
    }

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
