package top.lhx.myapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import top.lhx.myapp.app.AppLication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SpUtil class
 * SharedPreferences工具类
 *
 * @author auser
 * @date 11/2/2019
 */
public class SpUtil {
    private static SharedPreferences sp;

    /**
     * @param key
     * @param defValue 只能为String，Long，int（Integer），Float，Boolean五种类型
     * @param <T>
     * @return null：出现异常
     */
    public static <T> T get(String key, T defValue) {
        String tType = defValue.getClass().getSimpleName();
        if ("Integer".equals(tType)) {
            tType = "Int";
        }
        Class clazz = getSp().getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("get") && !name.equals("getClass")
                    && !name.equals("getStringSet") && !name.equals("getAll")) {
                if (name.substring(3).equals(tType)) {
                    try {
                        T result = (T) m.invoke(sp, new Object[]{key, defValue});
                        return result;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param
     * @param key
     * @param value 只能为String，Long，int（Integer），Float，Boolean五种类型
     * @param <T>
     * @return
     */
    public static <T> void put(String key, T value) {
        String tType = value.getClass().getSimpleName();
        if ("Integer".equals(tType)) {
            tType = "Int";
        }
        SharedPreferences.Editor editor = getSp().edit();
        Class clazz = editor.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("put") && !name.equals("putStringSet")) {
                if (name.substring(3).equals(tType)) {
                    try {
                        editor = (SharedPreferences.Editor) m.invoke(editor,
                                new Object[]{key, value});
                        editor.commit();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param key 要删除的键
     */
    public static void remove(String key) {
        getSp().edit().remove(key).commit();
    }

    /**
     * @param
     * @return 返回SharedPreferences的单例
     */
    private static SharedPreferences getSp() {
        if (sp == null) {
            sp = AppLication.getContext().getSharedPreferences("settings",
                    Context.MODE_PRIVATE);
        }
        return sp;
    }
}
