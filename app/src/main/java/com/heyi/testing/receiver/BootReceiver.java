//package com.heyi.testing.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import com.heyi.testing.service.MyAccessibilityService;
//
///**
// * BootReceiver class
// *
// * @author auser
// * @date 11/2/2019
// */
//public class BootReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("top.lhx.myaccessibilityservice.destory")) {
//            Intent sevice = new Intent(context, MyAccessibilityService.class);
//            context.startService(sevice);
//        }
//    }
//}
