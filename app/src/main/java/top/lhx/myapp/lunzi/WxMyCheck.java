package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.SpUtil;
import top.lhx.myapp.utils.WechatUtils;
import top.lhx.myapp.utils.wxmsgroot.WeChatEnDbUtils;
import top.lhx.myapp.utils.wxmsgroot.WeChatSnsDbUtils;

import java.util.ArrayList;
import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static top.lhx.myapp.StartActivity.getSerialNumber;
import static top.lhx.myapp.service.MyAccessibilityService.getScreenHeight;

/**
 * WxMyCheck class
 *
 * @author auser
 * @date 11/2/2019
 */
public class WxMyCheck {
    public static void weChatcheck(AccessibilityService context) {
        try {
            Thread.sleep(2000);
            MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
            List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(3));
            Thread.sleep(2000);
            //com.tencent.mm:id/dhb
            AccessibilityNodeInfo rootNode = context.getRootInActiveWindow();
            List<AccessibilityNodeInfo> listviewa = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dhb");

            Thread.sleep(2000);
            String[] wxHao = listviewa.get(0).getText().toString().split("：");
            Log.e("aaa", "微信号aaaa" + wxHao[1]);
            SpUtil.put("wx_name", wxHao[1]);
            String serialNumber = getSerialNumber();
            //if (serialNumber != null && !serialNumber.equals("") && !serialNumber.equals("null")) {
            //Log.e("aaa", "唯一序列号==" + serialNumber);
            //SpUtil.put("SerialNumber", Md5Utils.md5(wxHao[1]));
            //}
            //SpUtil.get("id","")
            OkhtttpUtils.getInstance().doGet("YM/equipment/insert/Equipmentlisst?udid=" + SpUtil.get("SerialNumber", "") + "&model=" + 0 + "&state=1&port=" + 0 + "&uid=" + SpUtil.get("id", ""), new OkhtttpUtils.OkCallback() {
                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(String json) {
                    Log.e("aaa", "" + json);
//                        JSONObject jsonObject = JSONObject.parseObject(json);
//                        String json1=jsonObject.toString();
//                        Log.e("aaa",""+json1);
                    try {
                        Thread.sleep(2000);
                        Log.e("aaa", "YM/Wx/WxAddNow?WxHao=" + wxHao[1] + "&equipmentid=" + json + "&uid=" + SpUtil.get("id", ""));

                        OkhtttpUtils.getInstance().doGet("YM/Wx/WxAddNow?WxHao=" + wxHao[1] + "&equipmentid=" + json + "&uid=" + SpUtil.get("id", ""), new OkhtttpUtils.OkCallback() {
                            @Override
                            public void onFailure(Exception e) {
                                System.out.println(e);
                            }

                            @Override
                            public void onResponse(String json) {
                                System.out.println("--------------:" + json);
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            });
            int page = 0;
            String lastQunName = "";

            Thread.sleep(2000);
            WechatUtils.performClick(Enter_found.get(1));
            Thread.sleep(2000);
            List<AccessibilityNodeInfo> add = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/dbq", "com.tencent.mm:id/jp", "com.tencent.mm:id/kg");
            WechatUtils.performClick(add.get(1));
            Thread.sleep(2000);
            List<AccessibilityNodeInfo> sendFriend = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/jp", "com.tencent.mm:id/cz", "com.tencent.mm:id/lp");
            WechatUtils.performClick(sendFriend.get(0));
            Thread.sleep(2000);
            //com.tencent.mm:id/b2z
            List<AccessibilityNodeInfo> qun = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/j8", "com.tencent.mm:id/b2z", "com.tencent.mm:id/b2z");
            WechatUtils.performClick(qun.get(0));
            Thread.sleep(2000);
            //com.tencent.mm:id/b2z
            Boolean be = true;
            while (be) {
                try {
                    List<AccessibilityNodeInfo> qunList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/cdr", "com.tencent.mm:id/cdu", "com.tencent.mm:id/cdt");

                for (int i = 0; i < qunList.size(); i++) {
                    int screenHeight1 = getScreenHeight(context.getApplicationContext());
                    Path pengzhua1 = new Path();
                    pengzhua1.moveTo(400, screenHeight1 / 2);
//                        penghua.moveTo(400, 800);
                    //   pengzhua.lineTo(400, 800-540);
                    pengzhua1.lineTo(400, 0);
                    final GestureDescription.StrokeDescription sdzhua1 = new GestureDescription.StrokeDescription(pengzhua1, 0, 500);
                    if (i != 0) {
                        for (int j = 0; j < page; j++) {
                            Log.e("Aaa", "" + j);
                            context.dispatchGesture(new GestureDescription.Builder().addStroke(sdzhua1).build(), new AccessibilityService.GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    super.onCompleted(gestureDescription);

                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    super.onCancelled(gestureDescription);

                                }
                            }, null);
                            Thread.sleep(1500);
                        }
                        qunList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/cdr", "com.tencent.mm:id/cdu", "com.tencent.mm:id/cdt");
                    }
                    lastQunName = qunList.get(i).getText().toString();
                    Thread.sleep(500);
                    WechatUtils.performClick(qunList.get(i));
                    Thread.sleep(1000);
                    AccessibilityNodeInfo accessibilityNodeInfo = context.getRootInActiveWindow();
                    List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
                    if (accessibilityNodeInfo != null) {
                        nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/kz");
                    }
                    if(nodeInfoList.size() == 0){
                        qunList = twoBack(1);
                        continue;
                    }
                    Thread.sleep(1000);
                    WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/kz");
                    Thread.sleep(1000);
                    try {
                        int screenHeight = getScreenHeight(context.getApplicationContext());
                        Path pengzhua = new Path();
                        pengzhua.moveTo(400, screenHeight / 2);
//                        penghua.moveTo(400, 800);
                        //   pengzhua.lineTo(400, 800-540);
                        pengzhua.lineTo(400, 0);
                        final GestureDescription.StrokeDescription sdzhua = new GestureDescription.StrokeDescription(pengzhua, 0, 500);
                        Thread.sleep(2000);
                        for (int j = 0; j < 5; j++) {
                            Log.e("Aaa", "" + j);
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
                            Thread.sleep(1500);
                        }

                        Thread.sleep(1000);
                        //com.tencent.mm:id/k_
                        List<AccessibilityNodeInfo> open = MyAccessibilityService.getInstance().PengYouQuan("android:id/list", "com.tencent.mm:id/k_", "com.tencent.mm:id/lj");
                        Thread.sleep(1000);
                        Log.e("aaaa", "123456798" + open.get(2).getContentDescription().toString());
                        //if (open.get(2).getContentDescription().toString() == "已关闭") {
                        if (open.get(2).getContentDescription().equals("已关闭")) {
                            Thread.sleep(1000);
                            //AccessibilityNodeInfo rootNode1 = context.getRootInActiveWindow();
                            //List<AccessibilityNodeInfo> listview = rootNode1.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k_");
                            //647,508
                            String open2Str = open.get(2).toString();
                            String[] open2Strs = open2Str.split(";");
                            String[] bounds = open2Strs[2].split(":");
                            String bound = bounds[1];
                            bound = bound.substring(6, bound.length() - 1);
                            String[] xy1 = bound.split("-")[0].split(",");
                            String[] xy2 = bound.split("-")[1].split(",");
                            Integer x1 = new Integer(xy1[0].trim()).intValue();
                            Integer x2 = new Integer(xy2[0].trim()).intValue();
                            Integer y1 = Integer.valueOf(xy1[1].trim());
                            Integer y2 = Integer.valueOf(xy2[1].trim());
                            Integer zjb1 = Math.abs(x1 - x2);
                            Integer zjb2 = Math.abs(y1 - y2);
                            if (zjb1 > zjb2) {
                                x1 = x1 + (zjb2 / 2);
                                y1 = y1 + (zjb1 / 2);
                            } else {
                                x1 = x1 + (zjb1 / 2);
                                y1 = y1 + (zjb2 / 2);
                            }
                            Path path1 = new Path();
                            path1.moveTo(x1, y1);
                            final GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(path1, 0, 100);
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


                            //WechatUtils.performClick(listview.get(2));


                            Thread.sleep(1000);
                            myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                            Thread.sleep(1000);
                            myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                            Thread.sleep(1000);
                            List<AccessibilityNodeInfo> add1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/dbq", "com.tencent.mm:id/jp", "com.tencent.mm:id/kg");
                            WechatUtils.performClick(add1.get(1));
                            Thread.sleep(1000);
                            List<AccessibilityNodeInfo> sendFriend1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/jp", "com.tencent.mm:id/cz", "com.tencent.mm:id/lp");
                            WechatUtils.performClick(sendFriend1.get(0));
                            Thread.sleep(1000);
                            //com.tencent.mm:id/b2z
                            List<AccessibilityNodeInfo> qun1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/j8", "com.tencent.mm:id/b2z", "com.tencent.mm:id/b2z");
                            WechatUtils.performClick(qun1.get(0));
                            Thread.sleep(1000);
                            qunList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/cdr", "com.tencent.mm:id/cdu", "com.tencent.mm:id/cdt");
                        } else {
                            qunList = twoBack(2);
                        }

                    } catch (InterruptedException e) {

                    }
                }

                page += 1;
                int screenHeight = getScreenHeight(context.getApplicationContext());
                Path pengzhua = new Path();
                pengzhua.moveTo(400, screenHeight / 2);
//                        penghua.moveTo(400, 800);
                //   pengzhua.lineTo(400, 800-540);
                pengzhua.lineTo(400, 0);
                final GestureDescription.StrokeDescription sdzhua = new GestureDescription.StrokeDescription(pengzhua, 0, 500);
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

                qunList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/cdr", "com.tencent.mm:id/cdu", "com.tencent.mm:id/cdt");
                String lastQunName1 = qunList.get(qunList.size() - 1).getText().toString();
                if (lastQunName.equals(lastQunName1)) {

                    Log.e("Aaa", "群列表检查完成");
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    break;
                }else if(page > 5){
                    Log.e("Aaa", "群列表检查完成");
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    break;
                }
                }catch (Exception e){
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    break;
                }
            }
            Thread.sleep(5000);
            WeChatEnDbUtils.getInstance().isDebug = false;
            WeChatEnDbUtils.getInstance().getWxMsg(context, 1, "");


        } catch (Exception e) {
            Log.e("aaa","Wx自检报错"+e);

            e.printStackTrace();
        } finally {
            Log.e("aaa", "自检-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }

    public static List<AccessibilityNodeInfo> twoBack(Integer step) throws InterruptedException {
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        for (int i = 1; i <= step; i++) {
            Thread.sleep(2000);
            myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
        }
        Thread.sleep(2000);
        List<AccessibilityNodeInfo> add1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/dbq", "com.tencent.mm:id/jp", "com.tencent.mm:id/kg");
        WechatUtils.performClick(add1.get(1));
        Thread.sleep(2000);
        List<AccessibilityNodeInfo> sendFriend1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/jp", "com.tencent.mm:id/cz", "com.tencent.mm:id/lp");
        WechatUtils.performClick(sendFriend1.get(0));
        Thread.sleep(2000);
        //com.tencent.mm:id/b2z
        List<AccessibilityNodeInfo> qun1 = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/j8", "com.tencent.mm:id/b2z", "com.tencent.mm:id/b2z");
        WechatUtils.performClick(qun1.get(0));
        Thread.sleep(2000);
        List<AccessibilityNodeInfo> qunList = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/cdr", "com.tencent.mm:id/cdu", "com.tencent.mm:id/cdt");
        return qunList;
    }
}
