package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.WechatUtils;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static top.lhx.myapp.service.MyAccessibilityService.MD5;
import static top.lhx.myapp.service.MyAccessibilityService.downLoad;

/**
 * SendFriendCircle class
 *
 * @author auser
 * @date 11/2/2019
 */
public class SendFriendCircle {
    public void sendBroadcast(String v, AccessibilityEvent event) {
        Log.e("aaa", "这是发送朋友圈");
        String[] value = v.split("!==!");
        //图片地址
        String imgurl = value[4];
        //朋友圈文字
        String Text = value[3];
        //回执id
        String TaskId = value[value.length - 1];
        MyAccessibilityService messagesss = MyAccessibilityService.getInstance();
        if (!"空".equals(imgurl)) {

            //String currentActivity = event.getClassName().toString();

                Log.e("aaa", "准备进入朋友圈");

                try {
                    int imgNum = 0;
                    String[] receiveLists = imgurl.split("!");
                    for (String it : receiveLists) {
                        Thread.sleep(2000);
                        imgNum++;
                        downLoad(it, "Img" + imgNum + ".jpg");
                        Thread.sleep(2000);

                        MyAccessibilityService.getInstance().scanDirAsync(Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + "Img"+imgNum + ".jpg");

                        messagesss.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                    }

                    Thread.sleep(3000);
                    //点击发现
                    List<AccessibilityNodeInfo> Enter_found = messagesss.PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
                    WechatUtils.performClick(Enter_found.get(2));

                    Thread.sleep(1500);

                    //进入朋友圈
                    List<AccessibilityNodeInfo> Enter_PYQ = messagesss.PengYouQuan("com.tencent.mm:id/der", "android:id/title", "com.tencent.mm:id/k6");
                    WechatUtils.performClick(Enter_PYQ.get(0));

                    Thread.sleep(1000);
                    try {
                        Thread.sleep(1000);
                        WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/b1v");
                    }catch (Exception e){
                        Thread.sleep(1000);
                        Log.e("aaa","未出现我发现了按钮");
                    }

                    Thread.sleep(1500);

                    //点击相机
                    WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/kz");

                    Thread.sleep(1500);

                    //弹框点击从相册选择
                    List<AccessibilityNodeInfo> from_PhotoAlbum_Choose = messagesss.PengYouQuan("com.tencent.mm:id/lo", "com.tencent.mm:id/cz", "com.tencent.mm:id/cz");
                    WechatUtils.performClick(from_PhotoAlbum_Choose.get(1));

                    Thread.sleep(1500);

                    //点击 图片和视频

                    WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/eaq");

                    Thread.sleep(1500);

                    //点击 系统相册
                    List<AccessibilityNodeInfo> Click_System_PhotoAlbum = messagesss.PengYouQuan("com.tencent.mm:id/eat", "com.tencent.mm:id/eaw", "com.tencent.mm:id/eav");
                    WechatUtils.performClick(Click_System_PhotoAlbum.get(1));

                    Thread.sleep(1500);


                    List<AccessibilityNodeInfo> bianliImg = messagesss.PengYouQuan("com.tencent.mm:id/eal", "com.tencent.mm:id/dt1", "com.tencent.mm:id/iv");
                    int imgsize = bianliImg.size();

                    Log.e("aaa", "这是wxint++++++++++++++++++++++++++++++++" + imgsize);

                    //选择指定目录
                    List<AccessibilityNodeInfo> cycle_Click_Img = messagesss.PengYouQuan("com.tencent.mm:id/bsz", "com.tencent.mm:id/bsz", "com.tencent.mm:id/bsz");
                    WechatUtils.performClick(cycle_Click_Img.get(0));

                    Thread.sleep(1500);

                    try {

                        for (int i = 0; i < imgsize; i++) {
                            Thread.sleep(1000);

                            WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/clu");
                            Thread.sleep(2000);

                            DisplayMetrics dm1 = messagesss.getResources().getDisplayMetrics();
                            int height1 = dm1.heightPixels;
                            int width1 = dm1.widthPixels;
                            Log.e("aaa", "kuan===" + width1);

                            Path path = new Path();
                            path.moveTo(width1 - 100, 800);
                            path.lineTo(10, 800);
                            final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 500);
                            messagesss.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    super.onCompleted(gestureDescription);
                                    Log.e("aaaa", "滑动成功");
                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    super.onCancelled(gestureDescription);
                                    Log.e("aaaa", "取消了滑动");
                                }
                            }, null);

                            Thread.sleep(2000);

                        }

                    } catch (Exception e) {
                        Log.e("aaa", "相册未到九张图片");
                    }
                    Thread.sleep(1500);

                    //点击完成
                    WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/ky");

                    Thread.sleep(1500);

                    //点击输入朋友圈内容
                    WechatUtils.findViewByIdAndPasteContent(messagesss, "com.tencent.mm:id/cwv", Text);

                    Thread.sleep(1500);

                    //点击发表
                    WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/ky");

                    Thread.sleep(1500);


                    messagesss.performGlobalAction(GLOBAL_ACTION_BACK);


                    //回执修改
                    OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId=" + TaskId, new OkhtttpUtils.OkCallback() {
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
                    Log.e("aaa","发送图文朋友圈报错"+e);
                    e.printStackTrace();
                }finally {
                    Log.e("aaa", "朋友圈-删除数据");
                    UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                    List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
                    if (userDaos.size() > 0) {
                        Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                        userDaoDao.delete(userDaos.get(0));
                    }
                }
            //}
        } else {
            //imgurl为空 走纯文本方法v
            Log.e("aaa", "开始发送存文本模式");
            String[] value1 = v.split("!==!");
            //回执任务id
            String TaskId1 = value1[value.length - 1];
            //文字
            String content = value[3];
            try {
                List<AccessibilityNodeInfo> Enter_found = messagesss.PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
                WechatUtils.performClick(Enter_found.get(2));

                Thread.sleep(1500);

                //进入朋友圈
                List<AccessibilityNodeInfo> Enter_PYQ = messagesss.PengYouQuan("com.tencent.mm:id/der", "android:id/title", "com.tencent.mm:id/k6");
                WechatUtils.performClick(Enter_PYQ.get(0));

                Thread.sleep(2000);

                DisplayMetrics dm1 = messagesss.getResources().getDisplayMetrics();
                int height1 = dm1.heightPixels;
                int width1 = dm1.widthPixels;

                Resources resources = messagesss.getApplicationContext().getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                int height = resources.getDimensionPixelSize(resourceId);
                Log.e("aaa", "这是通知栏高度：" + height);

                Path path1 = new Path();
                path1.moveTo(width1 - 10, height + 15);
                final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 3000);
                messagesss.dispatchGesture(new GestureDescription.Builder().addStroke(sd1).build(), new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                        Log.e("aaa", "点击成功");
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                        Log.e("aaaa", "点击失败");
                    }
                }, null);

                Thread.sleep(1000);

                //预判点击我知道了
                try {
                    Thread.sleep(1000);
                    WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/f1s");
                }catch (Exception e){
                    Thread.sleep(1000);
                    Log.e("aaa","未出现我发现了按钮");
                }

                Thread.sleep(2000);

                WechatUtils.findViewByIdAndPasteContent(messagesss, "com.tencent.mm:id/cwv", content);

                Thread.sleep(2000);

                //点击发表
                WechatUtils.findViewIdAndClick(messagesss, "com.tencent.mm:id/ky");


                //回执修改
                OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId=" + TaskId1, new OkhtttpUtils.OkCallback() {
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
                Log.e("aaa","发送纯文本朋友圈报错"+e);
                System.out.println("程序中间报错");
            }finally {
                Log.e("aaa", "朋友圈-删除数据");
                UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
                if (userDaos.size() > 0) {
                    Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                    userDaoDao.delete(userDaos.get(0));
                }
            }
        }
    }
}
