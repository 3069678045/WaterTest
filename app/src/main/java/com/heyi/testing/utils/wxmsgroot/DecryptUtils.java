package com.heyi.testing.utils.wxmsgroot;

import android.text.TextUtils;
import android.util.Log;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * DecryptUtils class
 *
 * @author auser
 * @date 10/28/2019
 */
public class DecryptUtils {
    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    private static final String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";
    private static final String WX_SP_IMEI_PATH = WX_ROOT_PATH + "shared_prefs/DENGTA_META.xml";

    /**
     * execRootCmd("chmod 777 -R " + WX_ROOT_PATH);
     * <p>
     * 执行linux指令
     */
    public static boolean execRootCmd(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream((OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            localObject = localProcess.exitValue();
            return true;
        } catch (Exception localException) {
            localException.printStackTrace();
            return false;
        }
    }

    /**
     * 根据imei和uin生成的md5码，获取数据库的密码（去前七位的小写字母）
     *
     * @return
     */
    public static String initDbPassword() {
        String imei = initPhoneIMEI();
        String uin = initCurrWxUin();
        Log.e("initDbPassword", "imei===" + imei);
        Log.e("initDbPassword", "uin===" + uin);
        try {
            if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(uin)) {
                Log.e("initDbPassword", "初始化数据库密码失败：imei或uid为空");
                return "";
            }
            String md5 = Md5Utils.md5Encode(imei + uin);
            String password = md5.substring(0, 7).toLowerCase();
            Log.e("initDbPassword", password);
            return password;
        } catch (Exception e) {
            Log.e("initDbPassword", e.getMessage());
        }
        return "";
    }

    /**
     * 获取手机的imei码
     *
     * @return
     */
    private static String initPhoneIMEI() {
        String mCurrWxImei = null;
        File file = new File(WX_SP_IMEI_PATH);
        try {
            FileInputStream in = new FileInputStream(file);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements) {
                if ("IMEI_DENGTA".equals(element.attributeValue("name"))) {
                    mCurrWxImei = element.getText();
                }
            }
            return mCurrWxImei;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("initCurrWxUin", "获取微信uid失败，请检查auth_info_key_prefs文件权限");
        }
        return "";
    }

    /**
     * 获取微信的uid
     * 微信的uid存储在SharedPreferences里面
     * 存储位置\data\data\com.tencent.mm\shared_prefs\auth_info_key_prefs.xml
     */
    public static String initCurrWxUin() {
        String mCurrWxUin = null;
        File file = new File(WX_SP_UIN_PATH);
        try {
            FileInputStream in = new FileInputStream(file);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements) {
                if ("_auth_uin".equals(element.attributeValue("name"))) {
                    mCurrWxUin = element.attributeValue("value");
                }
            }
            return mCurrWxUin;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("initCurrWxUin", "获取微信uid失败，请检查auth_info_key_prefs文件权限");
        }
        return "";
    }


    public static boolean isRootSystem() {
        if (isRootSystem1() || isRootSystem2()) {
            //TODO 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
            //多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
            //市场上常用的的权限管理app的包名   com.qihoo.permmgr  com.noshufou.android.su  eu.chainfire.supersu   com.kingroot.kinguser  com.kingouser.com  com.koushikdutta.superuser
            //com.dianxinos.superuser  com.lbe.security.shuame com.geohot.towelroot 。。。。。。
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }
}
