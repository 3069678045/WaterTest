package com.heyi.testing.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.CoordinatesClick;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.BitmapCut;
import com.heyi.testing.utils.FileUtils;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * faHongBaoLunZi class
 *
 * @author auser
 * @date 11/2/2019
 */
public class faHongBaoLunZi {


    private static faHongBaoLunZi instance;

    private faHongBaoLunZi() {
    }

    public static faHongBaoLunZi getInstance() {
        if (instance == null) {
            instance = new faHongBaoLunZi();
        }
        return instance;
    }



    private int screenWidth;
    private int screenHeight;

    public void faHongBao(String keyValue, AccessibilityEvent event) throws InterruptedException {//10
        String[] value = keyValue.split("!==!");



        Thread.sleep(2000);

        //点击搜索按钮
        WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/c2");
        try {

            Thread.sleep(2500);
            //输入框
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/lh");
            Thread.sleep(2500);
            //输入框输入数据
            WechatUtils.findViewByIdAndPasteContent(MyAccessibilityService.getInstance(), "com.tencent.mm:id/lh", value[3]);
            Thread.sleep(2500);

            AccessibilityNodeInfo clickTx = MyAccessibilityService.getInstance().getRootInActiveWindow();
            List<AccessibilityNodeInfo> clickTxviwe = clickTx.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ra");
            WechatUtils.performClick(clickTxviwe.get(0));
            Thread.sleep(2500);

            //消息页加号
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/aoe");
            Thread.sleep(2500);

            AccessibilityNodeInfo rootNode = MyAccessibilityService.getInstance().getRootInActiveWindow();
            List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/yj");
            WechatUtils.performClick(listview.get(4));

            //输入钱
            Thread.sleep(2500);
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/d1_");
            Thread.sleep(2500);
            //输入框输入数据
            WechatUtils.findViewByIdAndPasteContent(MyAccessibilityService.getInstance(), "com.tencent.mm:id/d1_", value[5]);
            Thread.sleep(2500);

            //输入消息
            Thread.sleep(2500);
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/d4v");
            Thread.sleep(2500);
            //输入框输入数据
            WechatUtils.findViewByIdAndPasteContent(MyAccessibilityService.getInstance(), "com.tencent.mm:id/d4v", value[4]);
            Thread.sleep(2500);

            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/d3x");
            Thread.sleep(3000);

            //获取屏幕宽高
            DisplayMetrics dm = MyAccessibilityService.getInstance().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;

            Log.e("aaa", "当前屏幕宽度：" + screenWidth);
            Log.e("aaa", "当前屏幕高度：" + screenHeight);

            //创建文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获取SDCard指定目录下
                // 判断是否可以对SDcard进行操作
                String sdCardDir = Environment.getExternalStorageDirectory() + "/MyApp/opencv/";
                File dirFile = new File(sdCardDir);  //目录转化成文件夹
                if (!dirFile.exists()) {                //如果不存在，那就建立这个文件夹
                    dirFile.mkdirs();
                }                            //文件夹有啦，就可以保存图片啦
            }

            Thread.sleep(5000);

            // 进入支付页面截屏
            RootCommand("su -c 'screencap /mnt/sdcard/MyApp/opencv/screenaaa.png' && adb pull /sdcard/MyApp/screenaaa.png");
            Thread.sleep(10000);

            // 取截屏图片的下半部分
            try {
                FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/MyApp/opencv/screenaaa.png");
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                Bitmap imageJie = BitmapCut.ImageCrop(bitmap, 1, 1, false);
                Log.e("aaa", "保存路径" + Environment.getExternalStorageDirectory() + "/MyApp/tupian.png");
                BitmapCut.saveBitmap(imageJie, Environment.getExternalStorageDirectory() + "/MyApp/tupian.png");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //调用python脚本
            Log.e("aaa", Environment.getExternalStorageDirectory() + "/opencvimg/000004.jpg");
            new Thread(() -> {
                try {
                    Object li = null;
                    // 获取python脚本返回的数字坐标数据
                    li = Python.getInstance().getModule("hello").callAttr("ginseng", Environment.getExternalStorageDirectory() + "/MyApp/tupian.png");
                    Log.e("aaa", "我是Objet打印：" + li);

                    Thread.sleep(1500);

                    //object 转换 String
                    String strLi2 = li.toString();

                    //截取数据两端的 [   ]  符号
                    String strLi1 = strLi2.split("\\[")[1];
                    String strLi = strLi1.split("]")[0];

                    // 根据数据的逗号分割
                    String[] strs = strLi.split(",");
                    ArrayList<String> arr = new ArrayList<String>();
                    for (int i = 0; i < strs.length - 1; i += 3) {
                        arr.add(strs[i] + "," + strs[i + 1] + "," + strs[i + 2]);
                    }

                    //假设 abcd.. = 1234567890
                    String a = arr.get(1).split(",")[0].toString().trim();
                    String b = arr.get(2).split(",")[0].toString().trim();
                    String c = arr.get(3).split(",")[0].toString().trim();
                    String d = arr.get(4).split(",")[0].toString().trim();
                    String m = arr.get(5).split(",")[0].toString().trim();
                    String f = arr.get(6).split(",")[0].toString().trim();
                    String g = arr.get(7).split(",")[0].toString().trim();
                    String h = arr.get(8).split(",")[0].toString().trim();
                    String i = arr.get(9).split(",")[0].toString().trim();
                    String z = arr.get(0).split(",")[0].toString().trim();

                    Log.e("aaa","a"+a);
                    Log.e("aaa","b"+b);
                    Log.e("aaa","c"+c);
                    Log.e("aaa","d"+d);
                    Log.e("aaa","m"+m);
                    Log.e("aaa","f"+f);
                    Log.e("aaa","g"+g);
                    Log.e("aaa","h"+h);
                    Log.e("aaa","i"+i);
                    Log.e("aaa","z"+z);

                    //遍历密码以单个输出
                    String password = value[6];
                    for (int j = 0; j < password.length(); j++) {
                        Thread.sleep(1000);
                        char pwdchar = password.charAt(j);
                        String zhuanstrpwd = String.valueOf(pwdchar);

                        //判断 字母数字是否等于遍历出来的密码

                        if (a.equals(zhuanstrpwd)) {
                            Thread.sleep(1000);
                            //拆分数字对应的坐标
                            String x = String.valueOf(arr.get(1).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(1).split(",")[2].toString().trim());
                            Log.e("aaa","x坐标："+x);
                            Log.e("aaa","x坐标："+y);

                            Thread.sleep(1000);

                            Path path1 = new Path();
                            // y坐标加上之前截取屏幕高度的一半再加20高度 = 数字对应坐标
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (b.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(2).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(2).split(",")[2].toString().trim());

                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (c.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(3).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(3).split(",")[2].toString().trim());

                            Thread.sleep(1000);

                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (d.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(4).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(4).split(",")[2].toString().trim());

                            Thread.sleep(1000);

                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (m.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(5).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(5).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);

                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if(f.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(6).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(6).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);

                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (g.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(7).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(7).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);

                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (h.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(8).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(8).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);

                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (i.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(9).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(9).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);

                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }else if (z.equals(zhuanstrpwd)){
                            Thread.sleep(1000);
                            String x = String.valueOf(arr.get(0).split(",")[1].toString().trim());
                            String y = String.valueOf(arr.get(0).split(",")[2].toString().trim());
                            Thread.sleep(1000);
                            Path path1 = new Path();
                            path1.moveTo(Float.valueOf(x), Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            Log.e("aaa","x坐标："+Float.valueOf(x));
                            Log.e("aaa","Y坐标："+Float.valueOf(y)+Float.valueOf(screenHeight/2)+20);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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
                            Thread.sleep(1000);
                        }

                    }

                    MyAccessibilityService.getInstance().delete(Environment.getExternalStorageDirectory() + "/MyApp");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Log.e("aaa", "发红包-删除数据");
                    UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                    List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
                    if (userDaos.size() > 0) {
                        Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                        userDaoDao.delete(userDaos.get(0));
                    }
                }

            }).start();

            Thread.sleep(2000);

            Log.e("aaa", "元素==");

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
            Log.e("aaaa", "发红包报错" + e);
            e.printStackTrace();
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
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
}

    /*String password = value[6];

                if (screenWidth ==720 && screenHeight <= 1440 ) {
                        *//*String pwdStr = password;*//*
                        for (int i = 0; i < password.length(); i++) {
        Thread.sleep(1000);
        char pwdchar = password.charAt(i);
        String zhuanstrpwd = String.valueOf(pwdchar);
        switch (zhuanstrpwd) {
        case "1":
        CoordinatesClick.cdaClick_720_1440_1(MyAccessibilityService.getInstance());
        break;
        case "2":
        CoordinatesClick.cdaClick_720_1440_2(MyAccessibilityService.getInstance());
        break;
        case "3":
        CoordinatesClick.cdaClick_720_1440_3(MyAccessibilityService.getInstance());
        break;
        case "4":
        CoordinatesClick.cdaClick_720_1440_4(MyAccessibilityService.getInstance());
        break;
        case "5":
        CoordinatesClick.cdaClick_720_1440_5(MyAccessibilityService.getInstance());
        break;
        case "6":
        CoordinatesClick.cdaClick_720_1440_6(MyAccessibilityService.getInstance());
        break;
        case "7":
        CoordinatesClick.cdaClick_720_1440_7(MyAccessibilityService.getInstance());
        break;
        case "8":
        CoordinatesClick.cdaClick_720_1440_8(MyAccessibilityService.getInstance());
        break;
        case "9":
        CoordinatesClick.cdaClick_720_1440_9(MyAccessibilityService.getInstance());
        break;
        case "0":
        CoordinatesClick.cdaClick_720_1440_0(MyAccessibilityService.getInstance());
        break;
        }
        }
        } else if (screenWidth == 720 && screenHeight >= 1450 && screenHeight <= 1520) {

        for (int i = 0; i < password.length(); i++) {
        Thread.sleep(1000);
        char pwdchar = password.charAt(i);
        String zhuanstrpwd = String.valueOf(pwdchar);
        switch (zhuanstrpwd) {
        case "1":
        CoordinatesClick.cdaClick_720_1520_1(MyAccessibilityService.getInstance());
        break;
        case "2":
        CoordinatesClick.cdaClick_720_1520_2(MyAccessibilityService.getInstance());
        break;
        case "3":
        CoordinatesClick.cdaClick_720_1520_3(MyAccessibilityService.getInstance());
        break;
        case "4":
        CoordinatesClick.cdaClick_720_1520_4(MyAccessibilityService.getInstance());
        break;
        case "5":
        CoordinatesClick.cdaClick_720_1520_5(MyAccessibilityService.getInstance());
        break;
        case "6":
        CoordinatesClick.cdaClick_720_1520_6(MyAccessibilityService.getInstance());
        break;
        case "7":
        CoordinatesClick.cdaClick_720_1520_7(MyAccessibilityService.getInstance());
        break;
        case "8":
        CoordinatesClick.cdaClick_720_1520_8(MyAccessibilityService.getInstance());
        break;
        case "9":
        CoordinatesClick.cdaClick_720_1520_9(MyAccessibilityService.getInstance());
        break;
        case "0":
        CoordinatesClick.cdaClick_720_1520_0(MyAccessibilityService.getInstance());
        break;
        }
        }

        }else if (screenWidth == 1080 && screenHeight >= 1900 && screenHeight <=2100 ){

        for (int i = 0; i < password.length(); i++) {
        Thread.sleep(1000);
        char pwdchar = password.charAt(i);
        String zhuanstrpwd = String.valueOf(pwdchar);
        switch (zhuanstrpwd) {

        case "1":
        CoordinatesClick.cdaClick_1080_1920_1(MyAccessibilityService.getInstance());
        break;
        case "2":
        CoordinatesClick.cdaClick_1080_1920_2(MyAccessibilityService.getInstance());
        break;
        case "3":
        CoordinatesClick.cdaClick_1080_1920_3(MyAccessibilityService.getInstance());
        break;
        case "4":
        CoordinatesClick.cdaClick_1080_1920_4(MyAccessibilityService.getInstance());
        break;
        case "5":
        CoordinatesClick.cdaClick_1080_1920_5(MyAccessibilityService.getInstance());
        break;
        case "6":
        CoordinatesClick.cdaClick_1080_1920_6(MyAccessibilityService.getInstance());
        break;
        case "7":
        CoordinatesClick.cdaClick_1080_1920_7(MyAccessibilityService.getInstance());
        break;
        case "8":
        CoordinatesClick.cdaClick_1080_1920_8(MyAccessibilityService.getInstance());
        break;
        case "9":
        CoordinatesClick.cdaClick_1080_1920_9(MyAccessibilityService.getInstance());
        break;
        case "0":
        CoordinatesClick.cdaClick_1080_1920_0(MyAccessibilityService.getInstance());
        break;
        }
        }

        }else if (screenWidth == 1080 && screenHeight >=2200){

        for (int i = 0; i < password.length(); i++) {
        Thread.sleep(1000);
        char pwdchar = password.charAt(i);
        String zhuanstrpwd = String.valueOf(pwdchar);
        switch (zhuanstrpwd) {

        case "1":
        CoordinatesClick.cdaClick_1080_2340_1(MyAccessibilityService.getInstance());
        break;
        case "2":
        CoordinatesClick.cdaClick_1080_2340_2(MyAccessibilityService.getInstance());
        break;
        case "3":
        CoordinatesClick.cdaClick_1080_2340_3(MyAccessibilityService.getInstance());
        break;
        case "4":
        CoordinatesClick.cdaClick_1080_2340_4(MyAccessibilityService.getInstance());
        break;
        case "5":
        CoordinatesClick.cdaClick_1080_2340_5(MyAccessibilityService.getInstance());
        break;
        case "6":
        CoordinatesClick.cdaClick_1080_2340_6(MyAccessibilityService.getInstance());
        break;
        case "7":
        CoordinatesClick.cdaClick_1080_2340_7(MyAccessibilityService.getInstance());
        break;
        case "8":
        CoordinatesClick.cdaClick_1080_2340_8(MyAccessibilityService.getInstance());
        break;
        case "9":
        CoordinatesClick.cdaClick_1080_2340_9(MyAccessibilityService.getInstance());
        break;
        case "0":
        CoordinatesClick.cdaClick_1080_2340_0(MyAccessibilityService.getInstance());
        break;
        }
        }
        }*/