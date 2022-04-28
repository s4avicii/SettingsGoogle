package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class SetTargetFragmentUsageViolation extends TargetFragmentUsageViolation {
    private final int mRequestCode;
    private final Fragment mTargetFragment;

    SetTargetFragmentUsageViolation(Fragment fragment, Fragment fragment2, int i) {
        super(fragment);
        this.mTargetFragment = fragment2;
        this.mRequestCode = i;
    }

    public String getMessage() {
        return "Attempting to set target fragment " + this.mTargetFragment + " with request code " + this.mRequestCode + " for fragment " + this.mFragment;
    }
}
