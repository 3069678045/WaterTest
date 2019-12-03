package com.heyi.testing.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import okhttp3.*;
import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * GetChatPictures class
 *
 * @author auser
 * @date 11/2/2019
 */
public class GetChatPictures {
    private static final String TAG = "aaa";
    private static MyAccessibilityService instance;
    private String keyValue;
    private AccessibilityNodeInfo rootNode;
    private String message;
    private List<ViewFile> fileList = new ArrayList<ViewFile>();
    //退出循环条件
    private boolean tuichu = true;

    public void GetChatPic(String v, AccessibilityEvent event) {
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        try {
            String[] value = keyValue.split("!==!");
            //点击搜索按钮
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/c2");

            Thread.sleep(2500);

            //输入框
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lh");
            Thread.sleep(2500);
            //输入框输入数据
            WechatUtils.findViewByIdAndPasteContent(myAccessibilityService, "com.tencent.mm:id/lh", value[3]);
            Thread.sleep(2500);

            //遍历搜索列表   点击搜索出来的名称
//                if (allNameList != null) allNameList.clear();
//                AccessibilityNodeInfo itemInfo = TraversalAndFindContacts("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_", value[3]);
//                Thread.sleep(2500);
//                if (itemInfo != null) {
//                    WechatUtils.performClick(itemInfo);
//                }

            AccessibilityNodeInfo rootNode100 = myAccessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> listview100 = rootNode100.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/rc");
            if (listview100.size() > 0) {
                WechatUtils.performClick(listview100.get(0));
            } else {
                message = "0000";
            }
            Thread.sleep(2500);
            AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ass");
            Thread.sleep(2500);

            for (int i = 0; i < listview.size(); i++) {
                //点击 图片和视频
                WechatUtils.performClick(listview.get(i));
                Thread.sleep(2500);
                Path path1 = new Path();
                path1.moveTo(400, 800);
                final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 2000);
                myAccessibilityService.dispatchGesture(new GestureDescription.Builder().addStroke(sd1).build(), new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                        Log.e(TAG, "点击成功");
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                        Log.e(TAG, "点击失败");
                    }
                }, null);
                Thread.sleep(2500);
                //点击保存图片
                AccessibilityNodeInfo rootNode1 = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview1 = rootNode1.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lp");
                WechatUtils.performClick(listview1.get(2));

                Thread.sleep(2500);
                tuichu = false;
                myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File sdcardDir = new File(Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin");
                    if (sdcardDir.isDirectory()) {
                        File[] subs = sdcardDir.listFiles();
                        if (subs != null) {
                            fileList.clear();
                            int threshold = 0;

                            for (File sub : subs) {
                                MyAccessibilityService.getInstance().getFileSize(sdcardDir.getPath(), sub, threshold);

                            }
                        }
                    }
                }

                Log.e("aaa", "size大小" + fileList.size());

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)//连接超时
                        .writeTimeout(30, TimeUnit.SECONDS)//写入超时
                        .readTimeout(30, TimeUnit.SECONDS)//读取超时
                        .build();
                for (ViewFile viewFile : fileList) {
                    String path = viewFile.path;
                    String filepath = Environment.getExternalStorageDirectory() + "/tencent/MicroMsg/WeiXin" + path;
                    Log.e("aaa", "filepath===" + filepath);
                    File file = new File(filepath);
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    MultipartBody requestBody = builder.build();
                    Request request = new Request.Builder().url("YM/WeiShun/ImageUpload/VueAutoUpload?type=WechatImage").post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("aaa", "上传失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            OkhtttpUtils.getInstance().doGet("YM/diaoDuBusiness/NewPeopleSendController/upImage?imageuRL=TU" + response.body().string() + "&time=" + System.currentTimeMillis() / 1000 + "&haoYouName=" + value[3] + "&udId=" + value[1], new OkhtttpUtils.OkCallback() {
                                @Override
                                public void onFailure(Exception e) {
                                    System.out.println(e);
                                }

                                @Override
                                public void onResponse(String json) {

                                    System.out.println(json);
                                }
                            });
                            File file = new File(filepath);
                            //删除系统缩略图
                            myAccessibilityService.getApplicationContext().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{filepath});
                            //删除手机中图片
                            file.delete();
                            Log.e("aaa", "删除成功!");
                        }
                    });
                    Thread.sleep(2500);
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
            Log.e("aaa","获取聊天图片报错"+e);
            e.printStackTrace();
        }finally {
            Log.e("aaa", "获取聊天-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
        message = "000000000000000";
    }

    class ViewFile {
        private String path;

        ViewFile(String p) {
            path = p;
        }
    }
}
