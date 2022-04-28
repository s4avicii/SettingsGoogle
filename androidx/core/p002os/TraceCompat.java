package androidx.core.p002os;

import android.os.Trace;

@Deprecated
/* renamed from: androidx.core.os.TraceCompat */
public final class TraceCompat {
    public static void beginSection(String str) {
        Trace.beginSection(str);
    }

    public static void endSection() {
        Trace.endSection();
    }
}
