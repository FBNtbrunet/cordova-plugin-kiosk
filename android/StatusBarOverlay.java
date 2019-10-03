package jk.cordova.plugin.kiosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import java.lang.reflect.Method;

// from https://github.com/ngocdaothanh/cordova-plugin-unswipable-android-status-bar
// http://stackoverflow.com/questions/25284233/prevent-status-bar-for-appearing-android-modified
public class StatusBarOverlay extends ViewGroup {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 4545;

    public StatusBarOverlay(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    static StatusBarOverlay create(Activity activity) {
         WindowManager manager = ((WindowManager) activity.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;
        StatusBarOverlay view = new StatusBarOverlay(activity);

        manager.addView(view, localLayoutParams);

        return view;
    }
    
    void destroy(Activity activity) {
        WindowManager manager = ((WindowManager) activity
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        manager.removeView(this);
        System.out.println("Removing StatusBarOverlay");
    }
}
