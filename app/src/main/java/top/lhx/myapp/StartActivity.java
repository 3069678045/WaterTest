package top.lhx.myapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.Myservices;
import top.lhx.myapp.utils.SpUtil;
import top.lhx.myapp.utils.Utils;
import top.lhx.myapp.utils.wxmsgroot.DecryptUtils;

import java.lang.reflect.Method;

/**
 * @author auser
 */
public class StartActivity extends Activity {

    private static final String enabledServicesBuilder ="com.smart.autoinstaller/com.smart.autoinstaller.service.InstallAccessibilityService";


    int mTime = 2;
    int start = 0;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (mTime <= 0) {
                    Log.e("aaa", "nickname" + SpUtil.get("nickname", ""));
                    if (!SpUtil.get("nickname", "").equals("") && !SpUtil.get("mobile", "").equals("") && !SpUtil.get("id", "").equals("")) {
                        DecryptUtils.execRootCmd("ls");
                        UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                        userDaoDao.deleteAll();
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        DecryptUtils.execRootCmd("ls");
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    return;
                }
                mTime--;
                mHandler.sendEmptyMessageDelayed(1, 1000);

            }
        }
    };

    private TextView number;

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                //8.0+
                serial = Build.SERIAL;
            } else {
                //8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("aaa", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        /*boolean accessibilityEnabled = true;
        Settings.Secure.putString(getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                enabledServicesBuilder.toString());
Settings.Secure.putInt(getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, accessibilityEnabled ? 1 : 0);*/





        String serialNumber = getSerialNumber();
        //if (serialNumber != null && !serialNumber.equals("") && !serialNumber.equals("null")) {
            Log.e("aaa", "唯一序列号==" + serialNumber);
            SpUtil.put("SerialNumber", serialNumber);
        //}

        number = findViewById(R.id.number);
        this.startService(new Intent(this, Myservices.class));
        UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
        userDaoDao.deleteAll();
        Utils.isNeededPermissionsGranted(this);
        if (Utils.isNeededPermissionsGranted(this)){
            new Thread(() -> StartActivity.this.runOnUiThread(()-> {

                mHandler.sendEmptyMessage(1);

            })).start();
        }









    }

    @Override
    protected void onResume() {
        super.onResume();
        start++;
        Log.e("aaa","走了onResume");
        if (start == 2){
            new Thread(() -> StartActivity.this.runOnUiThread(()-> {

                    mHandler.sendEmptyMessage(1);

            })).start();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("aaa","进入了onRause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("aaa","进入了onStop");

    }
}
