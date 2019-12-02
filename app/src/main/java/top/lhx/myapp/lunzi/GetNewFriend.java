package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.FileUtils;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.WechatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * GetNewFriend class
 * 拉新好友
 *
 * @author auser
 * @date 11/2/2019
 */
public class GetNewFriend {
    private static GetNewFriend instance;

    private GetNewFriend() {
    }

    public static GetNewFriend getInstance() {
        if (instance == null) {
            instance = new GetNewFriend();
        }
        return instance;
    }

    private static void downLoad(final String path, final String FileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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

            }

        }).start();

    }

    public void start(AccessibilityService context, String keyValue) {
        //点击通讯录
        try {
            Log.e("aaa", "头部");
            String[] value = keyValue.split("!==!");
            String[] friendNames = value[3].split("!");
            if (value[5].equals("空")) {

            } else {
                downLoad(value[5], "aaa.jpg");

                MyAccessibilityService.getInstance().scanDirAsync(Environment.getExternalStorageDirectory() + "/DCIM/Camera/aaa.jpg");

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

            }
            Thread.sleep(2000);
            for (int i = 0; i < friendNames.length; i++) {
                Log.e("aaa", "需要添加的好友次数：" + friendNames.length);
                Log.e("aaa", "好友名称：" + friendNames[i]);
                Thread.sleep(2000);
                List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
                WechatUtils.performClick(Enter_found.get(1));

                Thread.sleep(1000);//进入新的朋友
                List<AccessibilityNodeInfo> Enter_PYQ = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/nm", "com.tencent.mm:id/k6", "com.tencent.mm:id/bxk");
                WechatUtils.performClick(Enter_PYQ.get(0));
                Log.e("nnnnn-size===", "" + Enter_PYQ.size());
                Thread.sleep(2000);
                for (int ii = 0; ii < 5; ii++) {

                    List<AccessibilityNodeInfo> Enter_new = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/by5", "com.tencent.mm:id/bxx", "com.tencent.mm:id/bxu");

                    Thread.sleep(1000);
                    int j = 0;
                    for (j = 0; j < Enter_new.size(); j++) {
                        Thread.sleep(2000);
                        Log.e("aaa" + j, String.valueOf(Enter_new.get(j).getText()));
                        boolean whetherThereAre = String.valueOf(Enter_new.get(j).getText()).contains(friendNames[i]);
                        if (whetherThereAre) {
                            Log.e("aaa", "添加好友列表中包含此好友！！！");
                            WechatUtils.performClick(Enter_new.get(j));
                            Thread.sleep(1000);
                            List<AccessibilityNodeInfo> jieShou = MyAccessibilityService.getInstance().PengYouQuan("android:id/list", "com.tencent.mm:id/cv", "com.tencent.mm:id/ddj");
                            if (jieShou.get(0).getText().equals("发消息")) {
                                context.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);
                                Thread.sleep(2500);
                                context.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);

                                Thread.sleep(2000);
                                break;
                            }
                            WechatUtils.performClick(jieShou.get(0));
                            Thread.sleep(2000);
                            List<AccessibilityNodeInfo> ok = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/f20", "com.tencent.mm:id/ky", "com.tencent.mm:id/kg");
                            WechatUtils.performClick(ok.get(0));
                            Thread.sleep(2666);
                            List<AccessibilityNodeInfo> sendMessage = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/f20", "com.tencent.mm:id/cv", "com.tencent.mm:id/b7z");

                            WechatUtils.performClick(sendMessage.get(0));

                            Thread.sleep(2500);
                            //输入框
                            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/ao9");
                            Thread.sleep(2500);
                            //输入框输入数据
                            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/ao9", value[4]);
                            Thread.sleep(2500);
                            //com.tencent.mm:id/aof
                            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/aof");
                            Thread.sleep(2500);
                            if (value[5].equals("空")) {

                            } else {
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/aoe");
                                Thread.sleep(1000);
                                //com.tencent.mm:id/yi
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/yi");
                                Thread.sleep(1000);
                                //com.tencent.mm:id/eaq
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/eaq");
                                Thread.sleep(1000);
                                //com.tencent.mm:id/eaw
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/eaw");
                                Thread.sleep(1000);
                                //com.tencent.mm:id/amk
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/amk");
                                Thread.sleep(1000);
                                //com.tencent.mm:id/ky
                                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/ky");
                                Thread.sleep(1000);
                            }
                            context.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);
                            Thread.sleep(2500);

                            break;
                        }
                    }
                    if (j == Enter_new.size()) {
                        if (ii == 4){
                            Thread.sleep(2500);
                            context.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);
                            Thread.sleep(2000);
                            break;
                        }
                        Log.e("aaa", "翻页次数" + ii);
                        Thread.sleep(1000);
                        AccessibilityNodeInfo rootNode = context.getRootInActiveWindow();
                        List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/by5");
                        listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        Thread.sleep(2000);
                    } else {
                        break;
                    }

                }
            }
            //回执修改
            OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId=" + value[value.length - 1], new OkhtttpUtils.OkCallback() {
                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(String json) {
                    System.out.println(json);
                }
            });
        } catch (Exception e) {
            Log.e("aaa","拉新加好友报错"+e);

        }finally {
            Log.e("aaa", "拉新加好友-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
        delete(context, Environment.getExternalStorageDirectory() + "/DCIM/");
//        message = "aaaa";
    }

    private boolean delete(AccessibilityService context, String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(context, delFile);
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

    private boolean deleteDirectory(AccessibilityService context, String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
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
                flag = deleteDirectory(context, file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            Log.e("aaa","删除目录：" + filePath + "失败！");
            return false;
        }
    }
}
