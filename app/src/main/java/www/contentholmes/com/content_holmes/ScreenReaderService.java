package www.contentholmes.com.content_holmes;

/**
 * Created by rogu3 on 16/7/17.
 */

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;

public class ScreenReaderService extends AccessibilityService{
    String AppName = "Content-Holmes";
    AccessibilityServiceInfo info;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
//        Log.v("Content-Holmes", "start event");
//        Log.v("Content-Holmes", "asdasdasd");
        if (source == null) {
            return;
        }
        if(source.getPackageName().toString().equals("com.android.systemui")){
            return;
        }
        if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            getNotificationFromView(event);
        }else{
            getTextFromView(source);
        }
        Log.v(AppName, source.getPackageName().toString());
        Log.v(AppName, new Date(event.getEventTime()).toString());
//        Log.v(AppName, source.getPackageName().toString());
//        Log.v("Content-Holmes", event.toString()+"-event");
//        Log.v("Content-Holmes", source.toString()+"-source");
//        Log.v("Content-Holmes", source.getChildCount()+"-children");
    }

    public void getNotificationFromView(AccessibilityEvent event){
        if(event == null){
            return;
        }
        Parcelable data = event.getParcelableData();
        if(data instanceof Notification){
            Notification n = (Notification) data;
            Log.v(AppName, "Notification-"+n.tickerText.toString());
            Log.v(AppName, "Notification text-"+n.toString());
        }

    }

    public void getTextFromView(AccessibilityNodeInfo source){
        if (source == null){
            return;
        }
        //Log.d(AppName, "source is " + source.toString());
        if (source.getChildCount() > 0){
            for(int i=0; i<source.getChildCount(); i++){
                getTextFromView(source.getChild(i));
            }
        } else {
            if(source.getClassName().equals("android.widget.TextView") && source.getText() != null && !source.getText().toString().isEmpty()){
                Log.v(AppName, "text-" + source.getText().toString());
            } else if(source.getClassName().equals("android.widget.EditText") && source.getText() != null && !source.getText().toString().isEmpty()){
                Log.v(AppName, "input text-" + source.getText().toString());
            } else if(source.getClassName().equals("android.widget.ImageView")) {
                Log.v(AppName, "image");
            } else if(source.getClassName().equals("android.widget.Button") && source.getText() != null && !source.getText().toString().isEmpty()){
                Log.v(AppName, "button-" + source.getText().toString());
            } else if(source.getText() != null && !source.getText().toString().isEmpty()){
                Log.v(AppName, "other-" + source.getText().toString());
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.notificationTimeout = 0;
        this.setServiceInfo(info);
        Log.v("Content-Holmes", "Connected");
    }


}
