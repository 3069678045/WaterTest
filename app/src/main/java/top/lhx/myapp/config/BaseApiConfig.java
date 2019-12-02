package top.lhx.myapp.config;

/**
 * BaseApiConfig class
 *
 * @author auser
 * @date 11/2/2019
 */
public class BaseApiConfig {
    /**
     * 调度中心接口前缀
     */
    private static final String API_PREFIX = "YM";
//    private static final String API_PREFIX = "http://192.168.1.90:9961";
    /**
     * 使用root权限发送好友接口
     */
    public static final String ROOT_INSERT_FRIEND = API_PREFIX + "/Self/insertFriend";
    /**
     * 使用root权限获取好友信息接口
     */
    public static final String ROOT_INSERT_FRIEND_MESSAGE = API_PREFIX + "/diaoDuBusiness/NewPeopleSendController/xiaoxiruku";
    /**
     * 统计好友推荐人接口
     */
    public static final String ROOT_NEW_FRIEND_RECOMMENDER = API_PREFIX + "/Recommender/Add/Recommender";
    /**
     * 使用root权限获取群列表
     */
    public static final String ROOT_WX_GROUP_LIST = API_PREFIX + "/Wx/insertWxQunId";
    /**
     * 使用root权限发送群成员列表接口
     */
    public static final String ROOT_WX_GROUP_MEMBER_LIST = API_PREFIX + "/Wx/WxAddQunCyNameList";
    /**
     * 任务回执接口
     */
    public static final String TASK_RECEIPT = API_PREFIX + "/UpTaskSch/StatusAndCompletion";

}