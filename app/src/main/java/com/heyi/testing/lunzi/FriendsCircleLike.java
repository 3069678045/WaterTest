package com.heyi.testing.lunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.SpUtil;
import com.heyi.testing.utils.WechatUtils;
import com.heyi.testing.utils.wxmsgroot.WeChatSnsDbUtils;

import java.util.List;

/**
 * FriendsCircleLike class
 *
 * @author auser
 * @date 11/2/2019
 */
public class FriendsCircleLike {
    public void GetLike(String keyValue) {
        try {
            String[] value = keyValue.split("!==!");

            SpUtil.put("zu",value[5]);

            /*AccessibilityNodeInfo rootNodee = MyAccessibilityService.getInstance().getRootInActiveWindow();
            List<AccessibilityNodeInfo> linkman = rootNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/sg");
            WechatUtils.performClick(linkman.get(2));
            Thread.sleep(2000);*/

            List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(2));
            Thread.sleep(2000);
            AccessibilityNodeInfo MailListNodee = MyAccessibilityService.getInstance().getRootInActiveWindow();
            List<AccessibilityNodeInfo> MailList = MailListNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ddj");
            WechatUtils.performClick(MailList.get(0));
            Thread.sleep(2000);
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/ra");
            Thread.sleep(2000);
            AccessibilityNodeInfo myPengYouQuanNodee = MyAccessibilityService.getInstance().getRootInActiveWindow();
            List<AccessibilityNodeInfo> myPengYouQuanList = myPengYouQuanNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ddj");
            WechatUtils.performClick(myPengYouQuanList.get(0));
            Thread.sleep(2000);
            WechatUtils.findViewIdAndClick(MyAccessibilityService.getInstance(), "com.tencent.mm:id/kz");
            Thread.sleep(2000);
            //获取朋友圈点赞列表信息
            WeChatSnsDbUtils.getInstance().getSnsMsg(MyAccessibilityService.getInstance(), 1, value[value.length - 1]);

            Thread.sleep(2000);
            //YM/SendRedEncelopes/Breanch?zu=" + zuID + "&uid=" + uid + "&strategy_Id=123
            OkhtttpUtils.getInstance().doGet("YM/SendRedEncelopes/Breanch?zu=" + value[5]+"&uid="+value[6]+"&strategy_Id=123", new OkhtttpUtils.OkCallback() {
                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(String json) {
                    System.out.println(json);
                }
            });


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
            Log.e("aaa", "查看点赞报错"+e);
            e.printStackTrace();
        }finally {
            Log.e("aaa", "查看点赞报错-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }
}
