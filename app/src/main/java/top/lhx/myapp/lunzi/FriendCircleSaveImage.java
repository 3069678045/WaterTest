package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import okhttp3.*;
import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.SpUtil;
import top.lhx.myapp.utils.WechatUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static top.lhx.myapp.service.MyAccessibilityService.getScreenHeight;

/**
 * FriendCircleSaveImage class
 *
 * @author auser
 * @date 11/2/2019
 */
public class FriendCircleSaveImage {
    String wenzi = "";
    private List<ViewFile> fileList = new ArrayList<>();
    private AccessibilityNodeInfo rootNode;
    private boolean aa;
    private boolean aBoolean;
    private boolean tuichu;
    private List<String> allNameList = new ArrayList<>();

    public void saveCircleimage(AccessibilityService context, AccessibilityEvent event, String keyValue) {
        String[] value = keyValue.split("!==!");
            //点击搜索按钮
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/c2");
            try {
                Thread.sleep(2500);
                //输入框
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/lh");
                Thread.sleep(2500);
                //输入框输入数据
                WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/lh", value[3]);
                Thread.sleep(2500);

                //遍历搜索列表   点击搜索出来的名称
                if (allNameList != null) allNameList.clear();
                //AccessibilityNodeInfo itemInfo = MyAccessibilityService.getInstance().TraversalAndFindContacts("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_", value[3]);
                List<AccessibilityNodeInfo> itemInfolist = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_");
                WechatUtils.performClick(itemInfolist.get(0));
                Thread.sleep(2500);
               /* if (itemInfo != null) {
                    WechatUtils.performClick(itemInfo);
                }*/
                Thread.sleep(2500);

                //点击更多
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/kz");
                Thread.sleep(2500);

                //进入个人信息页
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/e9y");
                Thread.sleep(2500);

                //com.tencent.mm:id/ddu   进入朋友圈
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/ddu");

                Thread.sleep(2500);
                int screenHeight = getScreenHeight(context.getApplicationContext());
                Path pengzhua = new Path();
                pengzhua.moveTo(400, screenHeight / 2);
//                        penghua.moveTo(400, 800);
//                        penghua.lineTo(400, 800-540);
                pengzhua.lineTo(400, 0);
                final GestureDescription.StrokeDescription sdzhua = new GestureDescription.StrokeDescription(pengzhua, 0, 500);
                for (int i = 0; i < 5; i++) {
                    context.dispatchGesture(new GestureDescription.Builder().addStroke(sdzhua).build(), new AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);

                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            super.onCancelled(gestureDescription);

                        }
                    }, null);
                    Thread.sleep(2500);
                }

                Path pengfhua = new Path();
                pengfhua.moveTo(400, 300);
//                        penghua.moveTo(400, 800);
//                        penghua.lineTo(400, 800-540);
                pengfhua.lineTo(400, Integer.valueOf(screenHeight / 2) + 300);
                final GestureDescription.StrokeDescription sdfhua = new GestureDescription.StrokeDescription(pengfhua, 0, 500);
                for (int i = 0; i < 5; i++) {
                    context.dispatchGesture(new GestureDescription.Builder().addStroke(sdfhua).build(), new AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);
                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            super.onCancelled(gestureDescription);
                        //tytt
                        }
                    }, null);

                    context.dispatchGesture(new GestureDescription.Builder().addStroke(sdfhua).build(), new AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);
                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            super.onCancelled(gestureDescription);

                        }
                    }, null);

                    Thread.sleep(2500);
                }

                //list控件节点

                rootNode = context.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eok");

                Thread.sleep(2500);
                //遍历pengyouquan列表
                //循环点击朋友圈内容
                for (int t = 0; t <= 4; t++) {

                    //开始滑动个点击
                    if (!aa) {
                        Path penghua = new Path();
                        penghua.moveTo(400, screenHeight / 2);
//                        penghua.moveTo(400, 800);
//                        penghua.lineTo(400, 800-540);
                        penghua.lineTo(400, 0);

                        final GestureDescription.StrokeDescription sdhua = new GestureDescription.StrokeDescription(penghua, 0, 500);
                        context.dispatchGesture(new GestureDescription.Builder().addStroke(sdhua).build(), new AccessibilityService.GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                super.onCompleted(gestureDescription);
                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {
                                super.onCancelled(gestureDescription);

                            }
                        }, null);
                        aa = true;
                        Thread.sleep(2000);
                    } else {
                            /*listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            Thread.sleep(2000);*/
                        Path penghua = new Path();
                        penghua.moveTo(400, screenHeight / 2);
//                          penghua.moveTo(400, 800);
//                          penghua.lineTo(400, 800-540);
                        penghua.lineTo(400, 0);

                        final GestureDescription.StrokeDescription sdhua = new GestureDescription.StrokeDescription(penghua, 0, 500);
                        for (int i = 0; i < 2; i++) {
                            context.dispatchGesture(new GestureDescription.Builder().addStroke(sdhua).build(), new AccessibilityService.GestureResultCallback() {
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
                        }


                    }


                    if (allNameList != null) allNameList.clear();
                    List<AccessibilityNodeInfo> accessibilityNodeInfos = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/eok", "com.tencent.mm:id/oq", "com.tencent.mm:id/k6");



                    for (int i = 0; i < accessibilityNodeInfos.size(); i++) {

                        Log.e("aaa", "循环size=" + i + "====" + accessibilityNodeInfos.size());
                        AccessibilityNodeInfo itemInfoa = accessibilityNodeInfos.get(i);
                        aBoolean = false;
                        tuichu = true;
                        Thread.sleep(2000);

                        if (itemInfoa != null) {

                            WechatUtils.performClick(itemInfoa);
                            Thread.sleep(2000);

                            int iHuaDong = 0;
                            while (tuichu) {

                                Log.e("aaa", "这是图片个数" + String.valueOf(iHuaDong));
                                //查找com.tencent.mm:id/eov
                                AccessibilityNodeInfo rootNodee = context.getRootInActiveWindow();
                                List<AccessibilityNodeInfo> eovtextlist = rootNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eov");
                                AccessibilityNodeInfo eovtext = eovtextlist.get(0);

                                if (!aBoolean) {
                                    wenzi = eovtext.getText().toString();
                                    aBoolean = true;
                                }
                                String item = eovtext.getText().toString();
                                Thread.sleep(2500);
                                iHuaDong++;
                                if (iHuaDong!=10&&wenzi.equals(item)) {
                                    //可以长按
                                    Path path1 = new Path();
                                    path1.moveTo(400, 800);
                                    final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 2000);
                                    context.dispatchGesture(new GestureDescription.Builder().addStroke(sd1).build(), new AccessibilityService.GestureResultCallback() {
                                        @Override
                                        public void onCompleted(GestureDescription gestureDescription) {
                                            super.onCompleted(gestureDescription);
                                        }

                                        @Override
                                        public void onCancelled(GestureDescription gestureDescription) {
                                            super.onCancelled(gestureDescription);
                                        }
                                    }, null);
                                    Thread.sleep(2500);

                                    List<AccessibilityNodeInfo> recyClerList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/dcp", "com.tencent.mm:id/cz", "com.tencent.mm:id/lp");
                                    WechatUtils.performClick(recyClerList.get(2));

                                    Thread.sleep(2500);



                                    Path path = new Path();
                                    path.moveTo(700, 800);
                                    path.lineTo(10, 800);
                                    final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 500);
                                    context.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
                                        @Override
                                        public void onCompleted(GestureDescription gestureDescription) {
                                            super.onCompleted(gestureDescription);
                                        }

                                        @Override
                                        public void onCancelled(GestureDescription gestureDescription) {
                                            super.onCancelled(gestureDescription);
                                        }
                                    }, null);
                                    Thread.sleep(2500);
                                } else {
                                    iHuaDong = 1;
                                    tuichu = false;
                                    context.performGlobalAction(GLOBAL_ACTION_BACK);

                                    String state = Environment.getExternalStorageState();
                                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                                        File sdcardDir = new File(Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin");
                                        if (sdcardDir.isDirectory()) {
                                            File[] subs = sdcardDir.listFiles();
                                            if (subs != null) {
                                                fileList.clear();
                                                int threshold = 0;

                                                for (int r = 0; r < subs.length; r++) {
                                                    getFileSize(sdcardDir.getPath(), subs[r], threshold);

                                                }
                                            }
                                        }
                                    }

                                    Log.e("aaa", "圖片樟樹==" + fileList.size());

                                    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date();
                                    String sj = dateF.format(date);
                                    Log.e("aaa", "文字上传画===" + wenzi);

                                    OkhtttpUtils.getInstance().doGet("YM/FoewardResources/Add/Circleoffriends?forwardtext=" + wenzi + "&stringtext=" + MyAccessibilityService.MD5(wenzi) + "&iconurlNum=" + fileList.size() + "&subordinates=" + value[3] + "&forwardtime=" + sj + "&whethersend=0&uid=" + SpUtil.get("id", ""), new OkhtttpUtils.OkCallback() {
                                        @Override
                                        public void onFailure(Exception e) {

                                        }

                                        @Override
                                        public void onResponse(String json) {
                                            Log.e("aaa", "上传结果===" + json);

                                        }
                                    });

                                    Thread.sleep(2500);


                                    Log.e("aaa", "size大小" + fileList.size());


                                    Log.e("aaa", "size大小" + fileList.size());

                                    OkHttpClient client = new OkHttpClient.Builder()
                                            .connectTimeout(30, TimeUnit.SECONDS)//连接超时
                                            .writeTimeout(30, TimeUnit.SECONDS)//写入超时
                                            .readTimeout(30, TimeUnit.SECONDS)//读取超时
                                            .build();
                                    for (int j = 0; j < fileList.size(); j++) {
                                        String path = fileList.get(j).path;
                                        String filepath = Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin" + path;
                                        Log.e("aaa", "filepath===" + filepath);
                                        File file = new File(filepath);
                                        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                                        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                                        MultipartBody requestBody = builder.build();
                                        Request request = new Request.Builder().url("YM/WeiShun/ImageUpload/VueAutoUpload?type=imgService").post(requestBody).build();
                                        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Log.e("aaa", "上传失败");
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String string = response.body().string();
                                                Log.e("aaa", "图片上传結果"+string);
                                                OkhtttpUtils.getInstance().doGet("YM/Foeward/StringIconurl?iconurl=TU" + string + "&stringtext=" + MyAccessibilityService.MD5(wenzi) + "&uid=" + SpUtil.get("id", ""), new OkhtttpUtils.OkCallback() {
                                                    @Override
                                                    public void onFailure(Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onResponse(String json) {
                                                        Log.e("aaa","二次上传鏈接结果="+json);
                                                        File file = new File(filepath);
                                                        //删除系统缩略图
                                                        context.getApplicationContext().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{filepath});
                                                        //删除手机中图片
                                                        file.delete();
                                                        Log.e("aaa", "删除成功!");
                                                    }
                                                });




                                            }
                                        });


                                    }

                                }
                            }
                        } else {
                        }
                    }
                    Thread.sleep(2000);
                        /*listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        Thread.sleep(2000);*/
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
                Log.e("aaa","获取朋友圈报错"+e);
                e.printStackTrace();
            }finally {
                UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
                List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
                if (userDaos.size() > 0) {
                    Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                    userDaoDao.delete(userDaos.get(0));
                }
            }

    }

    public long getFileSize(String prefix, File file, int threshold) {
        long size = file.length();
        if (file.isDirectory()) {
            File[] subs = file.listFiles();
            if (subs != null) {
                for (File sub : subs) {
                    size += getFileSize(prefix, sub, threshold);
                }
            }
        }

        float sizem = (float) (size * 1.0 / 1024 / 1024);
        if (sizem > threshold && !file.isDirectory()) {
            String path = file.getPath();
            if (path.startsWith(prefix)) {
                path = path.substring(prefix.length());
            }

            Log.e("aaa", "File: path=" + path + ", size= " + sizem);

            fileList.add(new ViewFile(path));
        }
        return size;
    }

    public class ViewFile {
        public String path;

        ViewFile(String p) {
            path = p;
        }
    }
}
