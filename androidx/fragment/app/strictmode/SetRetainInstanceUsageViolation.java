package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class SetRetainInstanceUsageViolation extends RetainInstanceUsageViolation {
    SetRetainInstanceUsageViolation(Fragment fragment) {
        super(fragment);
    }

    public String getMessage() {
        return "Attempting to set retain instance for fragment " + this.mFragment;
    }
}
