package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class GetRetainInstanceUsageViolation extends RetainInstanceUsageViolation {
    GetRetainInstanceUsageViolation(Fragment fragment) {
        super(fragment);
    }

    public String getMessage() {
        return "Attempting to get retain instance for fragment " + this.mFragment;
    }
}
