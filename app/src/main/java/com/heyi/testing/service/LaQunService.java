package com.heyi.testing.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import com.heyi.testing.utils.OpencvUtils;
import com.heyi.testing.utils.WechatUtils;

import java.io.*;
import java.util.List;

public class LaQunService {

    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private static String DB_NAME = "matching.png";
    private static String ASSETS_NAME = "matching.png";
    private AssetManager assetManager;

    public LaQunService(AssetManager assetManager,String[] msg) throws InterruptedException, IOException {
        this.assetManager = assetManager;
        laQun(msg);
    }

    public void laQun(String[] msg) throws InterruptedException, IOException {

            // 搜索联系人
            Thread.sleep(2000);
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/c2");
            Thread.sleep(2000);
            WechatUtils.findViewByIdAndPasteContent(MyAccessibilityService.getInstance(), "com.tencent.mm:id/lh", msg[3]);
            Thread.sleep(2000);

            //进入联系人
            List<AccessibilityNodeInfo> itemInfo = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_");

            if (itemInfo != null && itemInfo.size() > 0) {
                WechatUtils.performClick(itemInfo.get(0));

                Thread.sleep(2500);

                // 进去加群页面
                List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/ae", "com.tencent.mm:id/are", "com.tencent.mm:id/ard");
                if (Enter_found != null && Enter_found.size() > 0) {
                    for (int i = 0; i < Enter_found.size(); i++) {
                        Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/ae", "com.tencent.mm:id/are", "com.tencent.mm:id/ard");
                        // 点击群名片
                        WechatUtils.performClick(Enter_found.get(Enter_found.size() - 1-i));
                        Thread.sleep(10000);

                        // 截屏
                        RootCommand("su -c 'screencap /mnt/sdcard/screenaaa.png' && adb pull /sdcard/screenaaa.png");
                        Thread.sleep(10000);

                        // 把特征图存储到sd卡
                        copyBigDataBase();
                        String path = DB_PATH + DB_NAME;

                        // 初始化opencv
                        System.loadLibrary("opencv_java4");

                        //原图
                        Mat g_src = Imgcodecs.imread(Environment.getExternalStorageDirectory() + "/screenaaa.png",Imgcodecs.IMREAD_COLOR);//原图，也是大图
                        // 特征图
                        Mat g_tem = Imgcodecs.imread(path,Imgcodecs.IMREAD_COLOR);

                        // 匹配
                        OpencvUtils imageRecognition = new OpencvUtils();
                        Point point = imageRecognition.surfMatch(g_tem, g_src);

                        // 检查是否匹配成功
                        if (point != null){
                            float x = (float) point.x;
                            float y = (float) point.y;

                            // 模拟点击事件
                            Path path1 = new Path();
                            path1.moveTo(x, y);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 200);
                            MyAccessibilityService.getInstance().dispatchGesture(new GestureDescription.Builder().addStroke(sd1).build(), new AccessibilityService.GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    super.onCompleted(gestureDescription);
                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    super.onCancelled(gestureDescription);
                                }
                            }, null);
                            Thread.sleep(2000);
                            // 点击后退
                            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/l3");
                            Thread.sleep(2000);
                            // 搜索联系人
                            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/c2");
                            Thread.sleep(2000);
                            WechatUtils.findViewByIdAndPasteContent(MyAccessibilityService.getInstance(), "com.tencent.mm:id/lh", msg[3]);
                            Thread.sleep(2000);
                            //进入联系人
                            List<AccessibilityNodeInfo> itemInfo1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_");
                            WechatUtils.performClick(itemInfo1.get(0));
                            Thread.sleep(2000);

                        } else {
                            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/lb");
                            Thread.sleep(1000);
                        }
                    }
                }
            }
    }




    private void copyBigDataBase() throws IOException {
        InputStream myInput;
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        myInput = this.assetManager.open(ASSETS_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myInput.close();

        myOutput.close();
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
}
