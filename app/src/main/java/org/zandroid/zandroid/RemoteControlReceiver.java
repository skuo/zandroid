package org.zandroid.zandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by skuo on 8/2/16.
 */
public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int code = event.getKeyCode();
            if (KeyEvent.KEYCODE_MEDIA_PLAY == code) {
                // Handle key press
            }
        }
    }
}
