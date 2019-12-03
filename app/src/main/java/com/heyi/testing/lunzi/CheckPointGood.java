package com.heyi.testing.lunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * CheckPointGood class
 *
 * @author auser
 * @date 11/2/2019
 */
public class CheckPointGood {
    public String check(String keyValue) {
        Log.e("aaa", "=================这是message5=================");

        String[] value = keyValue.split("!==!");
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        try {
            Thread.sleep(2000);
            //点击发现
            List<AccessibilityNodeInfo> Enter_found = myAccessibilityService.PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(2));

            Thread.sleep(2000);

            //进入朋友圈
            List<AccessibilityNodeInfo> Enter_PYQ = myAccessibilityService.PengYouQuan("com.tencent.mm:id/der", "android:id/title", "com.tencent.mm:id/k6");
            WechatUtils.performClick(Enter_PYQ.get(0));

            Thread.sleep(2000);

            //点击朋友圈头像
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/ra");

            Thread.sleep(2000);

            //点击自己的朋友圈
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/ddu");

            Thread.sleep(2000);

            //点击朋友圈消息
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/kz");

            Thread.sleep(2000);

            for (int i = 0; i <= 10; i++) {
                Thread.sleep(1500);
                AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqh");
                listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            Thread.sleep(2000);

            for (int i = 0; i <= 10; i++) {
                Thread.sleep(1500);
                AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqh");
                listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }

            Thread.sleep(2000);

            //获取时间
            AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqb");
            Log.e("aaa", "==========准备获取时间==========");
            int timeNum = listview.size();
            Log.e("aaa", "当前页time元素个数：" + timeNum);
            Thread.sleep(1500);

            int wh = 0;
            while (wh < 1) {
                Thread.sleep(1500);

                try {

                    for (int i = 0; i < timeNum; i++) {
                        Thread.sleep(1500);
                        String timeNumStr = listview.get(i).getText().toString();

                        Thread.sleep(1500);

                        String dateOf = "";
                        String Hours_minutes = "";
                        String[] brr = timeNumStr.split(" ");
                        dateOf = brr[0];
                        Hours_minutes = brr[1];

                        Log.e("aaa", "获取的时间：" + timeNumStr);
                        Thread.sleep(1500);
                        boolean judge_Today = timeNumStr.contains("今天");
                        Thread.sleep(1500);
                        boolean judgeYesterday = timeNumStr.contains("昨天");
                        Thread.sleep(1500);
                        boolean judgeMonthday = timeNumStr.contains("月");
                        Thread.sleep(1500);

                        String dianZanNameStr = "";
                        String dianZanWenBenStr = "";

                        if (!judge_Today == false && !judgeYesterday == false && !judgeMonthday == false) {
                            //获取当前日期
                            Thread.sleep(1500);
                            DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String DateTime = dateF.format(date);
                            Thread.sleep(1500);
                            String completeTime = DateTime + " " + timeNumStr;
                            Log.e("aaa", "拼接今天的完整时间：" + completeTime);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Thread.sleep(1500);
                            if (dateFormat.parse(completeTime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                Thread.sleep(1500);
                                if (dateFormat.parse(completeTime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                    Thread.sleep(1500);

                                    Log.e("aaa", "当前时间正确");

                                    AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                    WechatUtils.performClick(listviewPyq.get(i));
                                    Thread.sleep(1500);
                                    AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                    dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                    Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                    Thread.sleep(1500);
                                    //点击 返回
                                    WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                    Thread.sleep(1500);
                                    //获取name
                                    AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                    dianZanNameStr = listviewitName.get(0).getText().toString();
                                    Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                    Thread.sleep(1500);

                                } else {

                                    OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + completeTime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println(e);
                                        }

                                        @Override
                                        public void onResponse(String json) {
                                            System.out.println(json);
                                        }
                                    });

                                    Thread.sleep(1000);

                                    System.exit(0);

                                }
                            } else {
                                Log.e("aaa", "时间不对");
                            }
                        } else if (judge_Today == true) {
                            Log.e("aaa", "包含今天");

                            DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String DateTime = dateF.format(date);

                            String TodayTime = DateTime + " " + Hours_minutes;
                            System.out.println("今天的完整时间：" + TodayTime);

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                            if (dateFormat.parse(TodayTime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                Thread.sleep(1500);
                                if (dateFormat.parse(TodayTime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                    Thread.sleep(1500);

                                    Log.e("aaa", "当前时间正确");

                                    AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                    WechatUtils.performClick(listviewPyq.get(i));
                                    Thread.sleep(1500);
                                    AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                    dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                    Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                    Thread.sleep(1500);
                                    //点击 返回
                                    WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                    Thread.sleep(1500);
                                    //获取name
                                    AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                    dianZanNameStr = listviewitName.get(0).getText().toString();
                                    Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                    Thread.sleep(1500);

                                } else {
                                    OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + TodayTime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println(e);
                                        }

                                        @Override
                                        public void onResponse(String json) {
                                            System.out.println(json);
                                        }
                                    });

                                    Thread.sleep(1000);

                                    System.exit(0);
                                }
                            } else {
                                Log.e("aaa", "时间不对");
                            }

                        } else if (judgeYesterday == true) {
                            //获取昨天的时间
                            Thread.sleep(1500);
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -1);
                            Date d = cal.getTime();
                            Thread.sleep(1000);
                            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
                            String zuotianTime = sp.format(d);
                            Log.e("aaa", "昨天的Time：" + zuotianTime);
                            Thread.sleep(1500);
                            String zuoCompleteTime = zuotianTime + " " + Hours_minutes;
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Thread.sleep(1500);
                            if (dateFormat.parse(zuoCompleteTime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                Thread.sleep(1500);
                                if (dateFormat.parse(zuoCompleteTime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                    Thread.sleep(1500);
                                    Log.e("aaa", "当前获取的日期包含昨天");

                                    AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                    WechatUtils.performClick(listviewPyq.get(i));
                                    Thread.sleep(1500);
                                    AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                    dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                    Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                    Thread.sleep(1500);
                                    //点击 返回
                                    WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                    Thread.sleep(1500);
                                    //获取name
                                    AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                    dianZanNameStr = listviewitName.get(0).getText().toString();
                                    Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                    Thread.sleep(1500);
                                } else {
                                    OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + zuoCompleteTime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println(e);
                                        }

                                        @Override
                                        public void onResponse(String json) {
                                            System.out.println(json);
                                        }
                                    });

                                    Thread.sleep(1000);

                                    System.exit(0);
                                }
                            } else {
                                Log.e("aaa", "时间不对");
                            }
                        } else if (judgeMonthday == true) {
                            //准备截取日月时间

                            String jiayueNum = "";
                            String danNum = "0";
                            String jiariNum = "";

                            //开始截取月前面的数字
                            String[] yueNum = dateOf.split("月");
                            String yueNumStr = yueNum[0];
                            Log.e("aaa", "月份：" + yueNumStr);
                            int yuechangdu = yueNumStr.length();
                            Thread.sleep(1500);
                            if (yuechangdu == 1) {
                                System.out.println("当前月份是单数字");
                                jiayueNum = danNum + yueNumStr;
                                System.out.println(jiayueNum);
                            } else {
                                System.out.println("当前月份是双数字");
                            }

                            Thread.sleep(1500);

                            //开始截取日后面的数字
                            String[] timebrr = dateOf.split("日");
                            String riQianStr = timebrr[0];
                            Log.e("aaa", "获取日数字：" + riQianStr);
                            Thread.sleep(1500);
                            String[] riNum = timebrr[0].split("月");
                            String riNumStr = riNum[1];
                            System.out.println("日前面的数字：" + riNumStr);
                            int richangdu = riNumStr.length();
                            Thread.sleep(1500);
                            if (richangdu == 1) {
                                System.out.println("当前日期是单数字");
                                jiariNum = danNum + riNumStr;
                                System.out.println(jiariNum);
                            } else {
                                System.out.println("档期日期是双数字");
                            }
                            Thread.sleep(1500);
                            DateFormat dateF = new SimpleDateFormat("yyyy");
                            Date date = new Date();
                            String DateTime = dateF.format(date);

                            Thread.sleep(1500);
                            if (richangdu != 1 && yuechangdu != 1) {
                                Thread.sleep(1500);
                                System.out.println("年月日:" + DateTime + "-" + yueNumStr + "-" + riNumStr + " " + Hours_minutes);
                                String wanzhengtime = DateTime + "-" + yueNumStr + "-" + riNumStr + " " + Hours_minutes;
                                System.out.println("变量年月日:" + wanzhengtime);

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Thread.sleep(1500);
                                if (dateFormat.parse(wanzhengtime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                    Thread.sleep(1500);
                                    if (dateFormat.parse(wanzhengtime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                        Thread.sleep(1500);
                                        System.out.println("正确");

                                        AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                        WechatUtils.performClick(listviewPyq.get(i));
                                        Thread.sleep(1500);
                                        AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                        dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                        Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                        Thread.sleep(1500);
                                        //点击 返回
                                        WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                        Thread.sleep(1500);
                                        //获取name
                                        AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                        dianZanNameStr = listviewitName.get(0).getText().toString();
                                        Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                        Thread.sleep(1500);
                                    } else {
                                        OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + wanzhengtime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                System.out.println(e);
                                            }

                                            @Override
                                            public void onResponse(String json) {
                                                System.out.println(json);
                                            }
                                        });

                                        Thread.sleep(1000);

                                        System.exit(0);

                                        Thread.sleep(2000);

                                        System.out.println("时间达到最大限制，程序即将结束");

                                        System.exit(0);
                                    }
                                } else {
                                    System.out.println("时间不对");
                                }

                            } else if (yuechangdu == 1 && richangdu == 1) {
                                System.out.println("年月日:" + DateTime + "-" + jiayueNum + "-" + jiariNum + " " + Hours_minutes);
                                String wanzhengtime = DateTime + "-" + yueNumStr + "-" + riNumStr + " " + Hours_minutes;
                                System.out.println("变量年月日:" + wanzhengtime);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Thread.sleep(1500);
                                if (dateFormat.parse(wanzhengtime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                    Thread.sleep(1500);
                                    if (dateFormat.parse(wanzhengtime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                        Thread.sleep(1500);
                                        System.out.println("正确");
                                        Thread.sleep(1000);
                                        AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                        WechatUtils.performClick(listviewPyq.get(i));
                                        Thread.sleep(1500);
                                        AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                        dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                        Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                        Thread.sleep(1500);
                                        //点击 返回
                                        WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                        Thread.sleep(1500);
                                        //获取name
                                        AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                        dianZanNameStr = listviewitName.get(0).getText().toString();
                                        Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                        Thread.sleep(1500);
                                    } else {
                                        OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + wanzhengtime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                System.out.println(e);
                                            }

                                            @Override
                                            public void onResponse(String json) {
                                                System.out.println(json);
                                            }
                                        });


                                        Thread.sleep(2000);

                                        System.out.println("时间达到最大限制，程序即将结束");

                                        System.exit(0);
                                    }
                                } else {
                                    System.out.println("时间不对");
                                }

                            } else if (yuechangdu != 1 && richangdu == 1) {
                                System.out.println("年月日:" + DateTime + "-" + yueNumStr + "-" + jiariNum + " " + Hours_minutes);
                                String wanzhengtime = DateTime + "-" + yueNumStr + "-" + riNumStr + " " + Hours_minutes;
                                System.out.println("变量年月日:" + wanzhengtime);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Thread.sleep(1000);
                                if (dateFormat.parse(wanzhengtime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                    Thread.sleep(1500);
                                    if (dateFormat.parse(wanzhengtime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                        Thread.sleep(1500);
                                        System.out.println("正确");
                                        Thread.sleep(1000);

                                        AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                        WechatUtils.performClick(listviewPyq.get(i));
                                        Thread.sleep(1500);
                                        AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                        dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                        Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                        Thread.sleep(1500);
                                        //点击 返回
                                        WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                        Thread.sleep(1500);
                                        //获取name
                                        AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                        dianZanNameStr = listviewitName.get(0).getText().toString();
                                        Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                        Thread.sleep(1500);
                                    } else {

                                        OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + wanzhengtime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                System.out.println(e);
                                            }

                                            @Override
                                            public void onResponse(String json) {
                                                System.out.println(json);
                                            }
                                        });

                                        Thread.sleep(1000);

                                        System.exit(0);

                                    }
                                } else {
                                    System.out.println("时间不对");
                                }

                            } else if (yuechangdu == 1 && richangdu != 1) {
                                System.out.println("年月日:" + DateTime + "-" + jiayueNum + "-" + riNumStr + " " + Hours_minutes);

                                String wanzhengtime = DateTime + "-" + jiayueNum + "-" + riNumStr + " " + Hours_minutes;
                                System.out.println("变量年月日:" + wanzhengtime);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Thread.sleep(1000);
                                if (dateFormat.parse(wanzhengtime).getTime() <= dateFormat.parse(value[4]).getTime()) {
                                    Thread.sleep(1500);
                                    if (dateFormat.parse(wanzhengtime).getTime() >= dateFormat.parse(value[3]).getTime()) {
                                        Thread.sleep(1500);

                                        AccessibilityNodeInfo dianzanPyq = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewPyq = dianzanPyq.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqa");
                                        WechatUtils.performClick(listviewPyq.get(i));
                                        Thread.sleep(1500);
                                        AccessibilityNodeInfo dianzanitWenben = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitWenben = dianzanitWenben.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lt");
                                        dianZanWenBenStr = listviewitWenben.get(0).getText().toString();
                                        Log.e("aaa", "打印点赞的文本：" + dianZanWenBenStr);
                                        Thread.sleep(1500);
                                        //点击 返回
                                        WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/lb");
                                        Thread.sleep(1500);
                                        //获取name
                                        AccessibilityNodeInfo dZHyName = myAccessibilityService.getRootInActiveWindow();
                                        List<AccessibilityNodeInfo> listviewitName = dZHyName.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eq9");
                                        dianZanNameStr = listviewitName.get(0).getText().toString();
                                        Log.e("aaa", "这是点赞的好友name：" + dianZanNameStr);
                                        Thread.sleep(1500);

                                    } else {
                                        //调用结束接口

                                        OkhtttpUtils.getInstance().doGet("YM/FriendSpriaise/Insert?friendName=" + dianZanNameStr + "&udid=" + value[0] + "&strategy_id=0&stopFabulouscontent=" + dianZanWenBenStr + "&uid=" + value[6] + "&stopfabulous=" + wanzhengtime + "&zu=" + value[5] + "", new OkhtttpUtils.OkCallback() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                System.out.println(e);
                                            }

                                            @Override
                                            public void onResponse(String json) {
                                                System.out.println(json);
                                            }
                                        });

                                        Thread.sleep(1000);

                                        System.exit(0);
                                    }
                                } else {
                                    System.out.println("时间不对");
                                }
                            }

                        }

                        //判断条数是否获取完毕
                        if (i == timeNum) {
                            Log.e("aaa", "当前页以获取完毕是，即将翻页");

                            Thread.sleep(1500);

                            AccessibilityNodeInfo UnderSlide = myAccessibilityService.getRootInActiveWindow();
                            List<AccessibilityNodeInfo> UnderSlideList = UnderSlide.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eqh");
                            UnderSlideList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);

                            break;

                        }
                    }

                    //******************
                } catch (Exception e) {

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
            System.out.println("程序报错");
        }

        return null;
    }
}
