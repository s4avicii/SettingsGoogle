package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

class CanvasUtils {
    @SuppressLint({"SoonBlockedPrivateApi"})
    static void enableZ(Canvas canvas, boolean z) {
        if (z) {
            canvas.enableZ();
        } else {
            canvas.disableZ();
        }
    }
}
