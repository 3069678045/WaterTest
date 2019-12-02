package top.lhx.myapp.lunzi;

import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.WechatUtils;

/**
 * PullGroupGiveNumber class
 *
 * @author auser
 * @date 11/2/2019
 */
public class PullGroupGiveNumber {
    public void PullGroup(boolean linkScroll, String keyValue) {
        try {
            MyAccessibilityService myAccessibilityService = new MyAccessibilityService();
            String[] value = keyValue.split("!==!");
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/c2");
            Thread.sleep(1000);
            //com.tencent.mm:id/lh
            WechatUtils.findViewByIdAndPasteContent(myAccessibilityService, "com.tencent.mm:id/lh", "。。。");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
