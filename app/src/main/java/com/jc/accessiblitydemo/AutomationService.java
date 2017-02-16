package com.jc.accessiblitydemo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */

public class AutomationService extends AccessibilityService{

    private static final String TAG = "AutomationService";
    private static AutomationService mService = null;

    public static AutomationService getService(){
        return mService;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "eventType:" + accessibilityEvent.getEventType() + " eventTime:" + accessibilityEvent.getEventTime());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        automation();
    }

    public void automation(){
        backToHome();
        findWx();
    }

    /**
     * 返回主桌面
     */
    private void backToHome(){
        if(null == mService){
            return;
        }
        //连续两次点击Home键保证到第一个Home页
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }


    private void findWx(){
        if(null == mService){
            return;
        }
        AccessibilityNodeInfo accessibilityNodeInfo = null;
        do {
            accessibilityNodeInfo = findNodeByText("微信");
            mService.performGlobalAction(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_DOWN);
            Log.d(TAG, "finding wx");
        }
        while(null == accessibilityNodeInfo);
        if(null != accessibilityNodeInfo) {
            Log.d(TAG, "find wx");
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    //执行返回
    private void performBack() {
        Log.i(TAG, "performBack");
        if(null == mService){
            return;
        }
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 通过文本查找节点
     * @param text
     * @return
     */
    private AccessibilityNodeInfo findNodeByText(String text){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
