package androidx.appcompat.widget;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

final class AppCompatReceiveContentHelper {
    static boolean maybeHandleDragEventViaPerformReceiveContent(View view, DragEvent dragEvent) {
        return false;
    }

    static boolean maybeHandleMenuActionViaPerformReceiveContent(TextView textView, int i) {
        return false;
    }
}
