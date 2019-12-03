package com.heyi.testing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.app.MyReceiverBh;
import com.heyi.testing.greendao.DaoUser;
import com.heyi.testing.greendao.DaoUserDao;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.Myservices;
import com.heyi.testing.utils.BitmapCut;
import com.heyi.testing.utils.SpUtil;
import com.heyi.testing.utils.Utils;
import com.heyi.testing.utils.wxmsgroot.DecryptUtils;
import com.heyi.testing.utils.wxmsgroot.WeChatEnDbUtils;
import com.heyi.testing.utils.wxmsgroot.WeChatSnsDbUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author auser
 */
public class MainActivity extends Activity {
    public static String packageName = "com.tencent.mm";
    private List<ViewFile> fileList = new ArrayList<ViewFile>();
    private MyReceiverBh myReceiver;
    private Bitmap mBitmap;
    private ImageView imageq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //注册广播Home监听
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        //==============================app存活================================
        /* 注册广播，监听手机屏幕事件 */
        myReceiver = new MyReceiverBh();
        Log.e("aaa","=== rst ===="+myReceiver);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON); //亮屏
        filter.addAction(Intent.ACTION_SCREEN_OFF); //锁屏、黑屏
        registerReceiver(myReceiver, filter);

        boolean ls = DecryptUtils.execRootCmd("ls");
        imageq = findViewById(R.id.imageq);

        Button btn_getRoot = findViewById(R.id.btn_getRoot);
        btn_getRoot.setOnClickListener((view) -> DecryptUtils.execRootCmd("ls"));

        Button btn_getFriendMsg = findViewById(R.id.btn_getFriendMsg);
        btn_getFriendMsg.setOnClickListener((view) -> {
            WeChatEnDbUtils.getInstance().isDebug = true;
            WeChatEnDbUtils.getInstance().getWxMsg(this, 1, "");
        });

        Button btn_getMessage = findViewById(R.id.btn_getMessage);
        btn_getMessage.setOnClickListener((view) -> {
            WeChatEnDbUtils.getInstance().isDebug = true;
            WeChatEnDbUtils.getInstance().getWxMsg(this, 2, "");
        });

        Button btn_getNewFriend = findViewById(R.id.btn_getNewFriend);
        btn_getNewFriend.setOnClickListener((view) -> {
            WeChatEnDbUtils.getInstance().isDebug = true;
            WeChatEnDbUtils.getInstance().getWxMsg(this, 4, "");
        });

        Button btn_getChatRoomList = findViewById(R.id.btn_getChatRoomList);
        btn_getChatRoomList.setOnClickListener((view) -> {
            WeChatEnDbUtils.getInstance().isDebug = true;
            WeChatEnDbUtils.getInstance().getWxMsg(this, 5, "");
        });

        Button btn_getChatRoom = findViewById(R.id.btn_getChatRoom);
        btn_getChatRoom.setOnClickListener((view) -> {
            WeChatEnDbUtils.getInstance().isDebug = true;
            WeChatEnDbUtils.getInstance().getWxMsg(this, 3, "");
        });

        Button btn_getMsg = findViewById(R.id.btn_getSnsMsg);
        btn_getMsg.setOnClickListener((view) -> {
            WeChatSnsDbUtils.getInstance().isDebug = true;
            WeChatSnsDbUtils.getInstance().getSnsMsg(this, 1, "");
        });

        Button qutoutiaoLogin = findViewById(R.id.qutoutiaoLogin);
        qutoutiaoLogin.setOnClickListener((view) -> {
//            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
//            //userDaoDao.insertOrReplace(new UserDao(null,"趣头条Login!*!",System.currentTimeMillis()));
//            userDaoDao.insertOrReplace(new UserDao(null,"拉群赠送次数!*!0",System.currentTimeMillis()));
//            //startQuTouTiao();
            //Python.getInstance().getModule("hello").callAttr("aaa");

            try {
                FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/MyApp/screenaaa.png");
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                Bitmap imageJie = BitmapCut.ImageCrop(bitmap,1,1,false);
                imageq.setImageBitmap(imageJie);
                Log.e("aaa","保存路径"+Environment.getExternalStorageDirectory() + "/MyApp/tupian.png");
                BitmapCut.saveBitmap(imageJie, Environment.getExternalStorageDirectory() + "/MyApp/tupian.png");
            } catch (Exception e) {
                e.printStackTrace();
            }




        });


        this.startService(new Intent(this, Myservices.class));



        TextView nickname = findViewById(R.id.nickname);
        TextView mobile = findViewById(R.id.mobile);
        TextView ser = findViewById(R.id.ser);
        nickname.setText(SpUtil.get("nickname",""));
        mobile.setText(SpUtil.get("mobile",""));
        ser.setText(SpUtil.get("SerialNumber",""));
        Button button = findViewById(R.id.diji);
        Button zijian = findViewById(R.id.zijian);

        button.setOnClickListener((view) -> {
            DaoUserDao daoUserDao = AppLication.getInstance().getDaoSession().getDaoUserDao();
            List<DaoUser> daoUsers = daoUserDao.loadAll();
            for (int i = 0; i < daoUsers.size(); i++) {
                Log.e("aaa", "meaaaaaaa=" + daoUsers.get(i).getMessage() + "第" + i + "个");
            }
            startOpenFriendCircle();
        });

        //自检微信
        zijian.setOnClickListener((v) -> {
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            userDaoDao.insertOrReplace(new UserDao(null,"wx自检!*!0",System.currentTimeMillis()));
            startOpenFriendCircle();
        });
    }

    private void startQuTouTiao() {
        Log.e("aaa", "打开===");
        if (!Utils.isAppAvilible(this, "com.jifen.qukan")) {
            Toast.makeText(this, "未安装趣头条", Toast.LENGTH_SHORT).show();
        } else if (Utils.isNeededPermissionsGranted(this)) {
            this.startApp("com.jifen.qukan");
        }

    }

    /**
     * 開始正常流程
     */
    private void startOpenFriendCircle() {
        if (!Utils.isAppAvilible(MainActivity.this, packageName)) { //没有安装微信
            Toast.makeText(this, "未安装微信", Toast.LENGTH_LONG).show();
        } else {
            //检测用户是否开启辅助功能权限，如果沒有則進入設置界面開啟輔助
            if (Utils.isNeededPermissionsGranted(MainActivity.this)) {
                //開始正常流程。。。
                startApp(packageName);
            }
        }
    }

    /**
     * 進入APP
     *
     * @param packageName 包名
     */
    private void startApp(String packageName) {

        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return;
        }
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private long getFileSize(String prefix, File file, int threshold) {
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
            fileList.add(new ViewFile(path, sizem));
        }
        return size;
    }

    class ViewFile {
        private String path;
        private float size;

        ViewFile(String p, float s) {
            path = p;
            size = s;
        }
    }


    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
//                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();

                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("aaa","App竟然走了OnDestory  ~~~(0.0)~~~");
    }
}
