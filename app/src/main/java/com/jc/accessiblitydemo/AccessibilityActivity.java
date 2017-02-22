package com.jc.accessiblitydemo;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AccessibilityActivity extends Activity {

    private static final String TAG = "AccessibilityActivity";
    private Dialog mOpenDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);
    }

    public void autoClick(View view){
        if(!isServiceRunning()){
            if(null == mOpenDialog){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("打开" + getString(R.string.automation) );
                builder.setMessage("选择[AccessibilityDemo]并打开");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAccessibilityServiceSettings();
                        mOpenDialog.dismiss();
                    }
                });
                mOpenDialog = builder.show();
            }
            if(!mOpenDialog.isShowing()) {
                mOpenDialog.show();
            }
        }
        else{
            AutomationService.getService().automation();
        }
    }

    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isServiceRunning(){
        boolean running = false;
        String currentServiceName = getPackageName() + "/.AutomationService";
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for(AccessibilityServiceInfo accessibilityServiceInfo: list){
            if(accessibilityServiceInfo.getId().equals(currentServiceName)){
                running = true;
                break;
            }
        }
        return running;
    }
}
