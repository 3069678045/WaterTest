package com.heyi.testing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.heyi.testing.app.AppLication;
import com.heyi.testing.bean.LoginBean;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.timer.SendFriendMessage;
import com.heyi.testing.utils.*;
import com.heyi.testing.utils.wxmsgroot.DecryptUtils;

import java.security.MessageDigest;
import java.util.Date;

/**
 * @author auser
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_login_phoneNumber;
    private EditText et_login_password;
    private RelativeLayout btn_login;
    private ImageView imagqk;
    private String uname;
    private String passwd;
    private long t1;
    private String url;
    private String loginbean;
    private LoginBean Loginresult;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtil.showShort("连接服务器失败");
                    break;
                case 2:
                    SpUtil.put("nickname", Loginresult.getNickname());
                    SpUtil.put("mobile", Loginresult.getMobile());
                    SpUtil.put("id", Loginresult.getId());
                    ToastUtil.showShort("登录成功!");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                    UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                    userDaoDao.deleteAll();
                    UserDao userDao = new UserDao(null,"wx自检!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(userDao);
                    UserDao wx1 = new UserDao(null,"微信1好友自检!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(wx1);
                    UserDao wx2 = new UserDao(null,"微信2获取聊天消息!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(wx2);
                    UserDao wx4 = new UserDao(null,"微信4获取添加好友人的信息!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(wx4);
                    UserDao wx5 = new UserDao(null,"微信5获取群列表!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(wx5);
                    UserDao wxcir1 = new UserDao(null,"微信1获取朋友圈消息!*!0", System.currentTimeMillis());
                    userDaoDao.insertOrReplace(wxcir1);
                    SendFriendMessage.getInstance().Strat();
                    break;
                case 3:
                    ToastUtil.showShort("登录失败!账号或密码错误!");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (!SpUtil.get("nickname","").equals("")&&!SpUtil.get("mobile","").equals("")&&!SpUtil.get("id","").equals("")){
        setContentView(R.layout.activity_login);
        //}else {
        //    startActivity(new Intent(LoginActivity.this,MainActivity.class));
        //    finish();
        //}


        initView();
        DecryptUtils.execRootCmd("ls");

        requestStoragePermission();
    }

    private void initView() {
        et_login_phoneNumber = (EditText) findViewById(R.id.et_login_phoneNumber);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (RelativeLayout) findViewById(R.id.btn_login);
        imagqk = (ImageView) findViewById(R.id.imagqk);
        btn_login.setOnClickListener(this);
        imagqk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登录
            case R.id.btn_login:
                if (t1 == 0) {//第一次单击，初始化为本次单击的时间
                    t1 = (new Date()).getTime();
                    uname = et_login_phoneNumber.getText().toString().trim();
                    passwd = et_login_password.getText().toString().trim();
                    loginin();
                } else {
                    long curTime = (new Date()).getTime();//本地单击的时间
                    System.out.println("两次单击间隔时间：" + (curTime - t1));//计算本地和上次的时间差
                    if (curTime - t1 > 1 * 1000) {
                        //间隔5秒允许点击，可以根据需要修改间隔时间
                        t1 = curTime;//当前单击事件变为上次时间
                        uname = et_login_phoneNumber.getText().toString().trim();
                        passwd = et_login_password.getText().toString().trim();
                        loginin();
                    }
                }
                break;
            //清空密码
            case R.id.imagqk:
                et_login_password.setText("");
                break;
        }
    }

    private void loginin() {
        if (CheckUtil.isNetworkConnected(this) == false) {

            Toast.makeText(this, "网络连接失败，请检查网络...", Toast.LENGTH_SHORT).show();

        } else {

            final String name = uname;
            if (!CheckUtil.checkNameAndPwd(uname, passwd)) {
                return;
            }
            if (passwd.length() < 6) {
                ToastUtil.showShort("密码长度不正确");
                return;
            }
        }

        url = "YM/login/loginVerification?" + "mobile=" + uname + "&password=" + passwd + "&autograph=" + MD5(passwd) + "&username=" + uname;

        OkhtttpUtils.getInstance().doGet(url, new OkhtttpUtils.OkCallback() {
            @Override
            public void onFailure(Exception e) {
                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
                Log.e("aaa", "登录失败!==原因" + e);
            }

            @Override
            public void onResponse(String json) {
                loginbean = json;
                try {
                    JSONObject jDat = new JSONObject(loginbean);
                    Log.e("aaa", "登录成功!" + loginbean);

                    if (jDat.getString("code").equals("10000") && jDat.getString("message").equals("成功")) {
                        Loginresult = JsonUtil.parseJsonToBean(jDat.getString("resultCode"), LoginBean.class);
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {
                        Message message = handler.obtainMessage();
                        message.what = 3;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    /**
     * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安全
     *
     * @param str
     * @return
     */
    public String MD5(String str) {
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

    private void requestStoragePermission() {
        String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"};
        int requestCode = 200;
        int readpermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readnumberpermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, requestCode);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, requestCode);
        }
        if (readnumberpermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, requestCode);
        }
    }


    /**
     * 開始正常流程
     */
    private void startOpenFriendCircle() {
        if (!Utils.isAppAvilible(LoginActivity.this, "com.tencent.mm")) { //没有安装微信
            Toast.makeText(this, "未安装微信", Toast.LENGTH_LONG).show();
        } else {
            //检测用户是否开启辅助功能权限，如果沒有則進入設置界面開啟輔助
            if (Utils.isNeededPermissionsGranted(LoginActivity.this)) {
                //開始正常流程。。。
                startApp("com.tencent.mm");
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
}
