package androidx.core.app;

import android.app.PendingIntent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NotificationCompat$Action {
    public PendingIntent actionIntent;
    @Deprecated
    public int icon;
    private boolean mAllowGeneratedReplies;
    private final RemoteInput[] mDataOnlyRemoteInputs;
    final Bundle mExtras;
    private IconCompat mIcon;
    private final boolean mIsContextual;
    private final RemoteInput[] mRemoteInputs;
    private final int mSemanticAction;
    boolean mShowsUserInterface = true;
    public CharSequence title;

    NotificationCompat$Action(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr, RemoteInput[] remoteInputArr2, boolean z, int i, boolean z2, boolean z3) {
        this.mIcon = iconCompat;
        if (iconCompat != null && iconCompat.getType() == 2) {
            this.icon = iconCompat.getResId();
        }
        this.title = NotificationCompat$Builder.limitCharSequenceLength(charSequence);
        this.actionIntent = pendingIntent;
        this.mExtras = bundle == null ? new Bundle() : bundle;
        this.mRemoteInputs = remoteInputArr;
        this.mDataOnlyRemoteInputs = remoteInputArr2;
        this.mAllowGeneratedReplies = z;
        this.mSemanticAction = i;
        this.mShowsUserInterface = z2;
        this.mIsContextual = z3;
    }

    public IconCompat getIconCompat() {
        int i;
        if (this.mIcon == null && (i = this.icon) != 0) {
            this.mIcon = IconCompat.createWithResource((Resources) null, "", i);
        }
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public PendingIntent getActionIntent() {
        return this.actionIntent;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public boolean getAllowGeneratedReplies() {
        return this.mAllowGeneratedReplies;
    }

    public RemoteInput[] getRemoteInputs() {
        return this.mRemoteInputs;
    }

    public int getSemanticAction() {
        return this.mSemanticAction;
    }

    public boolean isContextual() {
        return this.mIsContextual;
    }

    public boolean getShowsUserInterface() {
        return this.mShowsUserInterface;
    }

    public static final class Builder {
        private boolean mAllowGeneratedReplies;
        private final Bundle mExtras;
        private final IconCompat mIcon;
        private final PendingIntent mIntent;
        private boolean mIsContextual;
        private ArrayList<RemoteInput> mRemoteInputs;
        private int mSemanticAction;
        private boolean mShowsUserInterface;
        private final CharSequence mTitle;

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public Builder(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            this(i != 0 ? IconCompat.createWithResource((Resources) null, "", i) : null, charSequence, pendingIntent, new Bundle(), (RemoteInput[]) null, true, 0, true, false);
        }

        private Builder(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr, boolean z, int i, boolean z2, boolean z3) {
            ArrayList<RemoteInput> arrayList;
            this.mAllowGeneratedReplies = true;
            this.mShowsUserInterface = true;
            this.mIcon = iconCompat;
            this.mTitle = NotificationCompat$Builder.limitCharSequenceLength(charSequence);
            this.mIntent = pendingIntent;
            this.mExtras = bundle;
            if (remoteInputArr == null) {
                arrayList = null;
            } else {
                arrayList = new ArrayList<>(Arrays.asList(remoteInputArr));
            }
            this.mRemoteInputs = arrayList;
            this.mAllowGeneratedReplies = z;
            this.mSemanticAction = i;
            this.mShowsUserInterface = z2;
            this.mIsContextual = z3;
        }

        private void checkContextualActionNullFields() {
            if (this.mIsContextual) {
                Objects.requireNonNull(this.mIntent, "Contextual Actions must contain a valid PendingIntent");
            }
        }

        /* JADX WARNING: type inference failed for: r0v5, types: [java.lang.Object[]] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public androidx.core.app.NotificationCompat$Action build() {
            /*
                r15 = this;
                r15.checkContextualActionNullFields()
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                java.util.ArrayList<androidx.core.app.RemoteInput> r2 = r15.mRemoteInputs
                if (r2 == 0) goto L_0x002f
                java.util.Iterator r2 = r2.iterator()
            L_0x0015:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x002f
                java.lang.Object r3 = r2.next()
                androidx.core.app.RemoteInput r3 = (androidx.core.app.RemoteInput) r3
                boolean r4 = r3.isDataOnly()
                if (r4 == 0) goto L_0x002b
                r0.add(r3)
                goto L_0x0015
            L_0x002b:
                r1.add(r3)
                goto L_0x0015
            L_0x002f:
                boolean r2 = r0.isEmpty()
                r3 = 0
                if (r2 == 0) goto L_0x0038
                r10 = r3
                goto L_0x0045
            L_0x0038:
                int r2 = r0.size()
                androidx.core.app.RemoteInput[] r2 = new androidx.core.app.RemoteInput[r2]
                java.lang.Object[] r0 = r0.toArray(r2)
                androidx.core.app.RemoteInput[] r0 = (androidx.core.app.RemoteInput[]) r0
                r10 = r0
            L_0x0045:
                boolean r0 = r1.isEmpty()
                if (r0 == 0) goto L_0x004c
                goto L_0x0059
            L_0x004c:
                int r0 = r1.size()
                androidx.core.app.RemoteInput[] r0 = new androidx.core.app.RemoteInput[r0]
                java.lang.Object[] r0 = r1.toArray(r0)
                r3 = r0
                androidx.core.app.RemoteInput[] r3 = (androidx.core.app.RemoteInput[]) r3
            L_0x0059:
                r9 = r3
                androidx.core.app.NotificationCompat$Action r0 = new androidx.core.app.NotificationCompat$Action
                androidx.core.graphics.drawable.IconCompat r5 = r15.mIcon
                java.lang.CharSequence r6 = r15.mTitle
                android.app.PendingIntent r7 = r15.mIntent
                android.os.Bundle r8 = r15.mExtras
                boolean r11 = r15.mAllowGeneratedReplies
                int r12 = r15.mSemanticAction
                boolean r13 = r15.mShowsUserInterface
                boolean r14 = r15.mIsContextual
                r4 = r0
                r4.<init>(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat$Action.Builder.build():androidx.core.app.NotificationCompat$Action");
        }
    }
}
