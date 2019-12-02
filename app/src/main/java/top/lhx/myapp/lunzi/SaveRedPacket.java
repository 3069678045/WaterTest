package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.WechatUtils;

public class SaveRedPacket {

    public void shouRedPacket(AccessibilityService context, String messgeText){
        try {
            Log.e("aaa", "message==2312152=" + messgeText);
            String[] messgeTextzu = messgeText.split("!\\*!");
            String keyValue = messgeTextzu[1];
            String[] value = keyValue.split("!==!");
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/c2");
            Thread.sleep(2000);
            //com.tencent.mm:id/lh
            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/lh", value[3]);
            Thread.sleep(2000);
            //com.tencent.mm:id/r_
            //遍历搜索列表   点击搜索出来的名称
            //AccessibilityNodeInfo itemInfo = MyAccessibilityService.getInstance().TraversalAndFindContacts("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_", value[3]);
            List<AccessibilityNodeInfo> itemInfolist = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_");
            WechatUtils.performClick(itemInfolist.get(0));
            Thread.sleep(2500);
            try {
                //com.tencent.mm:id/as9
                List<AccessibilityNodeInfo> Enter_new = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/ae", "com.tencent.mm:id/as9", "com.tencent.mm:id/as8");
                WechatUtils.performClick(Enter_new.get(Enter_new.size() - 1));
                Thread.sleep(1000);
                //com.tencent.mm:id/d4h
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/d4h");
                //com.tencent.mm:id/lb
                Thread.sleep(1000);
                WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/lb");

            } catch (Exception e) {
                    /*Thread.sleep(1000);
                    performGlobalAction(GLOBAL_ACTION_BACK);*/
            }
            Thread.sleep(2000);
            List<AccessibilityNodeInfo> Enter_new = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/ae", "com.tencent.mm:id/asw", "com.tencent.mm:id/ar0");
            WechatUtils.performClick(Enter_new.get(Enter_new.size() - 1));
            Thread.sleep(1000);
            //com.tencent.mm:id/d4h
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/e6y");
            //com.tencent.mm:id/lb
            Thread.sleep(1000);
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/lb");

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
            //daoUserDao.deleteAll();
        }finally {
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }
}
