package top.lhx.myapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * CheckUtil class
 * 格式校验工具类
 *
 * @author auser
 * @date 11/2/2019
 */
public class CheckUtil {
    // 两次点击按钮之间的点击间隔不能少于500毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /**
     * 拦截暴击
     *
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @return true:验证通过
     */
    public static boolean checkNameAndPwd(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showShort("手机号码不能为空");
            return false;
        } else if (username.length() != 11) {
            ToastUtil.showShort("手机号码长度不正确");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort("密码不能为空");
            return false;
        }
        return true;
    }

    //判断是否有网
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
