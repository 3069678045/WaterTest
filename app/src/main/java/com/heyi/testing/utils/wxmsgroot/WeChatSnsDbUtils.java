package com.heyi.testing.utils.wxmsgroot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.threekilogram.objectbus.bus.ObjectBus;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.SpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WeChatSnsDbUtils class
 *
 * @author auser
 * @date 10/30/2019
 */
public class WeChatSnsDbUtils {
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
     * 朋友圈数据库文件名
     */
    private static final String WX_DB_FILE_NAME = "SnsMicroMsg.db";
    /**
     * 拷贝的朋友圈数据库文件名
     */
    private static final String COPY_WX_SNS_DATA_DB = "wx_sns_data.db";
    /**
     * SD卡根路径
     */
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    /**
     * 拷贝的数据库路径
     */
    private static final String copyFilePath = SDCARD_PATH + COPY_WX_SNS_DATA_DB;
    private static WeChatSnsDbUtils instance;
    public boolean isDebug = false;
    private String UID;
    private String UDID;
    private String PHONE_WXID;

    /**
     * 私有化构造函数
     */
    private WeChatSnsDbUtils() {
        this.UID = SpUtil.get("id", "");
        this.UDID = SpUtil.get("SerialNumber", "");
        this.PHONE_WXID = SpUtil.get("wx_name", "");
    }

    /**
     * 单例模式
     *
     * @return 当前类实例
     */
    public static WeChatSnsDbUtils getInstance() {
        if (instance == null) {
            instance = new WeChatSnsDbUtils();
        }
        return instance;
    }

    /**
     * 获取朋友圈消息
     *
     * @param context 上下文
     * @param type    类型
     */
    public void getSnsMsg(Context context, int type, String taskId) {
        if (!DecryptUtils.isRootSystem()) {
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
            String uid = DecryptUtils.initCurrWxUin();
            try {
                String path = WX_DB_DIR_PATH + "/" + Md5Utils.md5Encode("mm" + uid) + "/" + WX_DB_FILE_NAME;
                Log.e("path", copyFilePath);
                Log.e("path", path);
                //微信原始数据库的地址
                File wxDataDir = new File(path);
                //将微信数据库拷贝出来，因为直接连接微信的db，会导致微信崩溃
                boolean isCopied = copyFile(wxDataDir.getAbsolutePath(), copyFilePath);
                if (!isCopied) return;
                //将微信数据库导出到sd卡操作sd卡上数据库
                android.database.sqlite.SQLiteDatabase db = openWxDb(new File(copyFilePath));
                switch (type) {
                    case 1:
                        runPointGoods(context, db, taskId);
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

    private void runPointGoods(final Context context, final android.database.sqlite.SQLiteDatabase db, String taskId) {
        task.toPool(() -> getPointGoodsData(db, taskId)).toMain(() -> Log.d("WeChatSnsDbUtils", "获取点赞数据完成")).run();
    }

    private void getPointGoodsData(android.database.sqlite.SQLiteDatabase db, String taskId) {
        String zu = SpUtil.get("zu", "");
        Cursor c1 = null;
        try {
            c1 = db.rawQuery("select * from SnsComment where type = 1",
                    null);

            while (c1.moveToNext()) {
                // 点赞人微信号
                String talker = c1.getString(c1.getColumnIndex("talker"));
                String snsID = c1.getString(c1.getColumnIndex("snsID"));
                String createTime = c1.getString(c1.getColumnIndex("createTime"));
                Log.e("openWxDb", "点赞方微信号====" + talker);
                // 采集微信好友接口地址
                Map<String, String> params = new HashMap<>();
                params.put("uid", UID);
                params.put("udid", UDID);
                params.put("benjiWxHao", PHONE_WXID);
                params.put("haoyouWxHao", talker);
                params.put("snsId", snsID);
                params.put("createTime", createTime);
                params.put("zu", zu);
                // todo 改线上接口
                OkhtttpUtils.getInstance().doPost("YM/FriendSpriaise/AddHyName", params, new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("添加微信好友点赞消息", "请求失败" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.i("添加微信好友点赞消息", "请求成功");
                    }
                });
                if (isDebug) {
                    break;
                }
            }
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
                OkhtttpUtils.getInstance().doPost("YM/UpTaskSch/StatusAndCompletion", params, new OkhtttpUtils.OkCallback() {
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
            Log.e("aaa", "添加微信通讯录记录-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    private android.database.sqlite.SQLiteDatabase openWxDb(File file) {
        //打开数据库连接
        return android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(file, null);
    }

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
}
