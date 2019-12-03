package com.heyi.testing.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.DaoUser;
import com.heyi.testing.greendao.DaoUserDao;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.lunzi.*;
import com.heyi.testing.qutoutiaolunzi.BoringHeadlinesLogin;
import com.heyi.testing.timer.SendFriendMessage;
import com.heyi.testing.utils.*;
import com.heyi.testing.utils.wxmsgroot.WeChatEnDbUtils;
import com.heyi.testing.utils.wxmsgroot.WeChatSnsDbUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    /**
     * 储存当前任务时间戳,初始为0 表示无任务
     */
    private static long currentMsgTimeStamp = 0;
    private static MyAccessibilityService instance;
    private List<String> allNameList = new ArrayList<>();
    private int mRepeatCount;
    //退出循环条件
    private String wxname;

    private List<ViewFile> fileList = new ArrayList<ViewFile>();
    private String[] messgeTextzu;
    private String keyValue;
    private String messgeText;
    private String message;
    private String linkmanname;
    private boolean linkScroll = true;

    public static synchronized MyAccessibilityService getInstance() {
        return instance;
    }

    private static void setInstance(MyAccessibilityService service) {
        instance = service;
    }



    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.e("aaa", "进入了onServiceConnected");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("aaa", "创建了services");
        setInstance(this);
        //注册监听
    }

    @Override
    public void onDestroy() {
        Log.e("aaa", "销毁了services");
        setInstance(null);
//        stopForeground(true);
//        Intent intent = new Intent("top.lhx.myaccessibilityservice.destory");
//        sendBroadcast(intent);
        Intent intent = new Intent(this, MyAccessibilityService.class);
        //this.startService(intent);
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!SpUtil.get("nickname", "").equals("") && !SpUtil.get("mobile", "").equals("") && !SpUtil.get("id", "").equals("")) {
            SendFriendMessage.getInstance().Strat();
        }
        UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
        List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
        if (userDaos.size() == 0) return;
        // 对比储存的任务时间戳与新取出的任务时间戳是否相同
        if (currentMsgTimeStamp == userDaos.get(0).getTimestamp()) {
            return;
        }
        // 如果不同将当前任务时间戳储存
        UserDao userDao = userDaos.get(0);
        currentMsgTimeStamp = userDao.getTimestamp();
        // 打印当前任务与时间戳
        Log.e("DBTest", userDao.getMessage() + userDao.getTimestamp());

        try {
            messgeText = userDao.getMessage();
            Log.e("aaa", "message==2312152=" + messgeText);
            messgeTextzu = messgeText.split("!\\*!");
            message = messgeTextzu[0];
            keyValue = messgeTextzu[1];
            Log.e("aaa", "message===" + message);
        } catch (Exception e) {
            e.printStackTrace();
            userDaoDao.delete(userDao);
            return;
        }
        //startService(new Intent(this, Myservices.class));

        //SpUtil.remove("message");
        //Log.e("aaa", "message===" + message);
        //int eventType = event.getEventType();
        //收红包消息保存
        // 启动微信
        if (!"微信2获取聊天消息!*!0".equals(messgeText) && !"微信4获取添加好友人的信息!*!0".equals(messgeText)) {
            startApp("com.tencent.mm");
        }


        /*Log.e("aaa", "自检-删除数据");
        UserDaoDao userDaoDao1 = AppLication.getInstance().getDaoSession().getUserDaoDao();
        List<UserDao> userDaos1 = userDaoDao1.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
        if (userDaos1.size() > 0) {
            Log.e("aaa", "删除了" + userDaos1.get(0).getMessage());
            userDaoDao.delete(userDaos1.get(0));
        }
        if(1==1){
        return;
        }*/
        if ("收红包".equals(message)) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    new SaveRedPacket().shouRedPacket(this, messgeText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else if ("wx自检".equals(message)) {
            Log.e("aaa", "执行了微信自检");
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WxMyCheck.weChatcheck(this);
            }).start();
            message = "aaaa";
        } else if ("获取".equals(message)) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    delete(Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin");
                    new FriendCircleSaveImage().saveCircleimage(this, event, keyValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            message = "aaaa";
        } else if ("朋友圈".equals(message)) {//3
            new Thread(() -> {
                try {
                    Thread.sleep(2000);

                    delete(Environment.getExternalStorageDirectory() + "/DCIM/Camera");

                    Thread.sleep(2000);
                    for (int i = 1; i < 10; i++) {

                        String filepath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/Img" + i + ".jpg";
                        //删除系统缩略图
                        this.getApplicationContext().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{filepath});
                        //删除手机中图片
                        Log.e("aaa", "删除成功!");

                    }

                    new SendFriendCircle().sendBroadcast(keyValue, event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();

            message = "aaa";
        } else if ("新好友发消息".equals(message)) {//5
            //吴程飞 2019-10-31 17:53:46
            new Thread(() -> {
                message = new NewFriendSendMessage().send(keyValue);
            }).start();
            message = "aaaa";

        } /*else if ("查看点赞".equals(message)) {//6
                new Thread(()->{
                    new CheckPointGood().check(keyValue);
                }).start();
                message = "aaaaa";
            }*/ else if ("拉新加好友".equals(message)) {//7
            new Thread(() -> {
                GetNewFriend.getInstance().start(this, keyValue);
            }).start();

            message = "aaaa";
        } else if ("微信加好友".equals(message)) {//8
            new Thread(() -> {
                WxAddFriend.getInstance().start(this, keyValue);
            }).start();

            message = "aaaaa";
        } else if ("自检".equals(message)) {//9
            new Thread(() -> {

                new ziJianLunZi().ziJian(keyValue, this);
            }).start();

            message = "0000000000000000000000000";
        } else if ("666".equals(message)) {//11
            new Thread(() -> {
                new SixSixSix().Six(keyValue, event);

            }).start();
            message = "aaaaa";
        } else if ("获取聊天图片".equals(message)) {//保存微信聊天的图片 //12
            new Thread(() -> {
                new GetChatPictures().GetChatPic(keyValue, event);
            }).start();

            message = "000000000000000";
        } else if ("自检群成员".equals(message)) {//13
            new Thread(() -> {
                CheckGroupMembers checkGroupMembers = new CheckGroupMembers();
                checkGroupMembers.SelfChecking(linkScroll, keyValue);
            }).start();

            message = "0000000000";
        } else if ("查看点赞".equals(message)) {//14
            new Thread(() -> {
                FriendsCircleLike friendsCircleLike = new FriendsCircleLike();
                friendsCircleLike.GetLike(keyValue);
            }).start();

            message = "0000000000";
        } else if ("微信1好友自检".equals(message)) {
            new Thread(() -> {
                try {
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatEnDbUtils.getInstance().getWxMsg(this, 1, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            message = "aaaa";
        } else if ("微信2获取聊天消息".equals(message)) {

            new Thread(() -> {
                try {
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatEnDbUtils.getInstance().getWxMsg(this, 2, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();


            message = "aaaa";
        } else if ("微信3获取群成员列表".equals(message)) {
            new Thread(() -> {
                try {
                    String[] value = keyValue.split("!==!");
                    SpUtil.put("qunId", value[6]);
                    SpUtil.put("WxQunId", value[4]);
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatEnDbUtils.getInstance().getWxMsg(this, 3, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();

            message = "aaaa";
        } else if ("微信4获取添加好友人的信息".equals(message)) {

            new Thread(() -> {
                try {
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatEnDbUtils.getInstance().getWxMsg(this, 4, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            message = "aaaa";
        } else if ("微信5获取群列表".equals(message)) {

            new Thread(() -> {
                try {
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatEnDbUtils.getInstance().getWxMsg(this, 5, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();


            message = "aaaa";
        } else if ("微信1获取朋友圈消息".equals(message)) {

            new Thread(() -> {
                try {
                    String[] keyValues = keyValue.split("!==!");
                    String taskId = "";
                    if (keyValues.length > 1) {
                        taskId = keyValues[keyValues.length - 1];
                    }
                    WeChatSnsDbUtils.getInstance().getSnsMsg(this, 1, taskId);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();


            message = "aaaa";
        } else if ("拉群赠送次数".equals(message)) {//15
            new Thread(() -> {
                Log.e("aaa", "进入了拉群赠送次数");
                try {
                    String[] keyValues = keyValue.split("!==!");
                    AssetManager assetManager = this.getAssets();
                    new LaQunService(assetManager, keyValues);

                    OkhtttpUtils.getInstance().doGet("YM/PullGroup/addMessage?haoyouName=" + keyValues[3] + "&wxHao=" + SpUtil.get("wx_name", ""), new OkhtttpUtils.OkCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(e);
                        }

                        @Override
                        public void onResponse(String json) {
                            System.out.println(json);
                        }
                    });

                    message = "aaa";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Log.e("aaa", "拉群增次数-删除数据");
                    UserDaoDao userDaoDao1 = AppLication.getInstance().getDaoSession().getUserDaoDao();
                    List<UserDao> userDaos1 = userDaoDao1.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
                    if (userDaos1.size() > 0) {
                        Log.e("aaa", "删除了" + userDaos1.get(0).getMessage());
                        userDaoDao1.delete(userDaos1.get(0));
                    }
                }
            }).start();
        } else if ("发红包".equals(message)) {
            new Thread(() -> {
                try {
                    delete(Environment.getExternalStorageDirectory() + "/MyApp");

                    Thread.sleep(1000);

                    faHongBaoLunZi.getInstance().faHongBao(keyValue, event);

                }catch (Exception e){
                    Log.e("aaa","报错了昂~~");
                }

            }).start();

            message = "aaa";
        } else if ("趣头条Login".equals(message)) {
            new Thread(() -> {
                Log.e("aaa", "ququququququq");
                BoringHeadlinesLogin.quTouTiao();
            }).start();
            message = "aaaaa";

        } else if ("快手".equals(message)) {
            new Thread(() -> {
                Log.e("aaa", "开始快手");
                BoringHeadlinesLogin.quTouTiao();
            }).start();
            message = "aaaaa";

        } else {
            Log.e("aaa", "什么都没匹配到-删除数据");
            UserDaoDao userDaoDao1 = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos1 = userDaoDao1.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos1.size() > 0) {
                Log.e("aaa", "删除了" + userDaos1.get(0).getMessage());
                userDaoDao1.delete(userDaos1.get(0));
            }
        }


    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath();
     *                RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    private boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception ignored) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    //获取群成员
    public Boolean xunhuanliebiao(List<AccessibilityNodeInfo> headerList) {
        try {
            //循环成员提交后台
            xunhuanpost(headerList);
            if (headerList.size() < 30) {//小于30 直接返回
                return true;
            }
            Thread.sleep(2000);
            //向下滑屏幕
            AccessibilityNodeInfo wipeUpNodee = getRootInActiveWindow();
            List<AccessibilityNodeInfo> wipeUpList = wipeUpNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eco");
            wipeUpList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);//向下话
            //获取列表
            Thread.sleep(2000);
            AccessibilityNodeInfo headerNodee = getRootInActiveWindow();
            List<AccessibilityNodeInfo> headerListTwo = headerNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ax0");
            if (headerListTwo.size() < 30) {
                //循环post后台
                xunhuanpost(headerListTwo);
                return true;
            } else {
                xunhuanliebiao(headerListTwo);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    //循环成员发给后台
    public void xunhuanpost(List<AccessibilityNodeInfo> headerList) {
        for (int i = 0; i < headerList.size(); i++) {
            try {
                String wxName = headerList.get(i).getText().toString();
                WechatUtils.performClick(headerList.get(i));
                Thread.sleep(2000);
                AccessibilityNodeInfo imageNodee = getRootInActiveWindow();
                List<AccessibilityNodeInfo> imageList = imageNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jp");
                int sf;
                if (imageList.size() > 0) {
                    Log.e("aaa", "好友 = " + i);
                    sf = 1;
                } else {
                    sf = 0;
                    Log.e("aaa", "非好友 = " + i);
                }
                //上传数据
                OkhtttpUtils.getInstance().doGet("YM/Wx/WxAddQunCyName?CyName=" + wxName + "&qunId=123&sffriend=" + sf + "&uid=123", new OkhtttpUtils.OkCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String json) {
                        System.out.println(json);
                    }
                });
                Thread.sleep(2000);
                WechatUtils.findViewIdAndClick(this, "com.tencent.mm:id/lb");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void scanDirAsync(String dir) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(dir)));
        sendBroadcast(scanIntent);
    }

    /**
     * 从头至尾遍历寻找联系人
     *
     * @return
     */
    public AccessibilityNodeInfo TraversalAndFindContacts(String listid, String wenziid, String tiaomuid, String name) {
        if (allNameList != null) allNameList.clear();

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId(listid);

        //是否滚动到了底部
        boolean scrollToBottom = false;
        if (listview != null && !listview.isEmpty()) {
            while (true) {
                //获取当前屏幕上的联系人信息
                List<AccessibilityNodeInfo> nameList = rootNode.findAccessibilityNodeInfosByViewId(wenziid);
                wxname = nameList.get(0).getText().toString();
                //Log.e("aaa", "naem1" + nameList.get(0).getText());
                List<AccessibilityNodeInfo> itemList = rootNode.findAccessibilityNodeInfosByViewId(tiaomuid);
                //Log.e("aaa", "item1" + itemList.get(0).getText());
                if (nameList != null && !nameList.isEmpty()) {
                    for (int i = 0; i < nameList.size(); i++) {
                        if (i == 0) {
                            //必须在一个循环内，防止翻页的时候名字发生重复
                            mRepeatCount = 0;
                        }
                        AccessibilityNodeInfo itemInfo = itemList.get(i);
                        AccessibilityNodeInfo nodeInfo = nameList.get(i);
                        String nickname = nodeInfo.getText().toString();
                        Log.e("aaa", "nickname = " + nickname);
                        //霸州便生活～1站
                        if (nickname.equals(name)) {
                            return nodeInfo;
                        }
                        if (!allNameList.contains(nickname)) {
                            allNameList.add(nickname);
                        } else if (allNameList.contains(nickname)) {
                            Log.e("aaa", "mRepeatCount = " + mRepeatCount);
                            if (mRepeatCount == 3) {
                                WechatUtils.performClick(itemInfo);
                                //表示已经滑动到顶部了
                                if (scrollToBottom) {
                                    Log.e("aaa", "没有找到联系人");
                                    //此次发消息操作已经完成
                                    return null;
                                }
                                scrollToBottom = true;
                            }
                            mRepeatCount++;
                        }
                    }
                }

                if (!scrollToBottom) {
                    //向下滚动
                    listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

                } else {
                    return null;
                }

                //必须等待，因为需要等待滚动操作完成
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 通过ID获取控件，并进行模拟点击
     *
     * @param clickId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputClick(String clickId) {
        Log.e("aaa", "进入了点击==");
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(clickId);
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    /**
     * Auto-click after finding the node
     *
     * @param nodeInfo AccessibilityNodeInfo
     * @param text     the message of node
     */
    private void setNodeClick(AccessibilityNodeInfo nodeInfo, String text) {
        AccessibilityNodeInfo info = NodeUtils.findNodeByParentClick(nodeInfo, text);
        if (info != null) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void setNodeIdClick(AccessibilityNodeInfo nodeInfo, String id) {
        Log.e("aaa", "执行了setNodeIdClick");
        AccessibilityNodeInfo info = NodeUtils.findNodeByIdClick(nodeInfo, id);
        Log.e("aaa", "setNodeIdClick====" + info);
        if (info != null) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     */
    public List<AccessibilityNodeInfo> findNodesById(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
            }
        }
        return null;
    }

    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                node = nodeInfo;
                // 获得点击View的类型
                Log.e("aaa", "View类型：" + node.getClassName());
                // 进行模拟点击
                if (node.isEnabled()) {
                    return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        return false;
    }

    /**
     * 从头至尾遍历寻找联系人
     *
     * @return
     */
    public List<AccessibilityNodeInfo> PengYouQuan(String listid, String wenziid, String tiaomuid) throws InterruptedException {
        if (allNameList != null) allNameList.clear();

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId(listid);

        //是否滚动到了底部
        boolean scrollToBottom = false;
        if (listview != null && !listview.isEmpty()) {
            while (true) {
                //获取当前屏幕上的联系人信息
                List<AccessibilityNodeInfo> nameList = rootNode.findAccessibilityNodeInfosByViewId(wenziid);
                //Log.e("aaa", "naem1" + nameList.get(0).getText());
                List<AccessibilityNodeInfo> itemList = rootNode.findAccessibilityNodeInfosByViewId(tiaomuid);
                //Log.e("aaa", "item1" + itemList.get(0).getText());
                if (nameList != null && !nameList.isEmpty() && nameList.size() != 0) {
                    return nameList;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public long getFileSize(String prefix, File file, int threshold) {
        long size = file.length();
        if (file.isDirectory()) {
            File[] subs = file.listFiles();
            if (subs != null) {
                for (File sub : subs) {
                    size += getFileSize(prefix, sub, threshold);
                }
            }
        }

        float sizem = (float) (size * 1.0 / 1024 / 1024);
        if (sizem > threshold && !file.isDirectory()) {
            String path = file.getPath();
            if (path.startsWith(prefix)) {
                path = path.substring(prefix.length());
            }

            Log.e("aaa", "File: path=" + path + ", size= " + sizem);

            fileList.add(new ViewFile(path));
        }
        return size;
    }

    public boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            Log.e("aaa", "删除文件失败:" + delFile + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("aaa", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Log.e("aaa", "删除目录失败：" + filePath + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            Log.e("aaa", "删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            Log.e("aaa", "删除目录：" + filePath + "失败！");
            return false;
        }

    }

    public class ViewFile {
        public String path;

        ViewFile(String p) {
            path = p;
        }
    }


    public static void downLoad(final String path, final String FileName) {
        new Thread(() -> {
            try {
                URL url = new URL(path);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {
                    InputStream is = con.getInputStream();//获取输入流
                    FileOutputStream fileOutputStream = null;//文件输出流
                    if (is != null) {
                        FileUtils fileUtils = new FileUtils();
                        fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径，代码看下一步
                        byte[] buf = new byte[1024];
                        int ch;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安全
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private void startApp(String packageName) {
        Log.e("aaa", "打开===startapp");
        PackageManager packageManager = this.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        }
    }

}
