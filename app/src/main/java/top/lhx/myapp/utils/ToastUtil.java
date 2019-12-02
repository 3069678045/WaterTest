package top.lhx.myapp.utils;

import android.widget.Toast;
import top.lhx.myapp.app.AppLication;

/**
 * ToastUtil class
 * 单例吐司工具类
 *
 * @author auser
 * @date 11/2/2019
 */
public class ToastUtil {
    private static Toast toast;

    public static void showShort(String text) {
        if (toast == null) {
            toast = Toast.makeText(AppLication.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void showLong(String text) {
        if (toast == null) {
            toast = Toast.makeText(AppLication.getContext(), text, Toast.LENGTH_LONG);
        }
        toast.setText(text);
        toast.show();
    }
}
