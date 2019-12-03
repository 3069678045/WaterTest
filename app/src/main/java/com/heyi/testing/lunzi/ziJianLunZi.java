package com.heyi.testing.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ziJianLunZi class
 *
 * @author auser
 * @date 11/2/2019
 */
public class ziJianLunZi {
    private List<String> allNameList = new ArrayList<>();

    public void ziJian(String keyValue, AccessibilityService context) {//9
        try {
            //点击通讯录
            String[] value = keyValue.split("!==!");
            List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(1));

            Thread.sleep(1000);//进入群聊
            List<AccessibilityNodeInfo> Enter_PYQ = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/nm", "com.tencent.mm:id/k6", "com.tencent.mm:id/bxk");
            WechatUtils.performClick(Enter_PYQ.get(1));
            Log.e("nnnnn-size===", "" + Enter_PYQ.size());
            Thread.sleep(2000);
            //com.tencent.mm:id/oa

            boolean loop = true;
            while (loop) {
                List<AccessibilityNodeInfo> Enter_new = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/nm", "com.tencent.mm:id/oa", "com.tencent.mm:id/o3");
                Thread.sleep(2000);
                for (int i = 0; i < Enter_new.size(); i++) {
                    Log.e("aaa", "微信群名称:" + Enter_new.get(i).getText());
                    OkhtttpUtils.getInstance().doGet("YM/Wx/WxAddQunName?WxQunName=" + Enter_new.get(i).getText() + "&WxId=" + value[3] + "&uid=" + value[4] + "&whetherobtain=1", new OkhtttpUtils.OkCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(e);
                        }

                        @Override
                        public void onResponse(String json) {
                            System.out.println(json);
                        }
                    });
                }
                try {
                    //com.tencent.mm:id/b3a
                    Thread.sleep(2000);
                    AccessibilityNodeInfo add = context.getRootInActiveWindow();
                    List<AccessibilityNodeInfo> listviewadd = add.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b3a");
                    Thread.sleep(1000);
                    Log.e("aaa", "群聊个数：" + listviewadd.get(0).getText());
                    boolean whetherThereAre = String.valueOf(listviewadd.get(0).getText()).contains("群聊");
                    if (whetherThereAre) {
                        Log.e("aaa", "群聊以获取完毕");
                        loop = false;
                    }
                } catch (Exception e) {
                    Thread.sleep(2000);
                    AccessibilityNodeInfo rootNode = context.getRootInActiveWindow();
                    List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nm");
                    listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
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
            Log.e("aaa","自检报错"+e);
        }finally {
            Log.e("aaa", "自检-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }
}
