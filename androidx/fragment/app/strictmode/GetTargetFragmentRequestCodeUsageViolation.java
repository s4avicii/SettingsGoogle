package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class GetTargetFragmentRequestCodeUsageViolation extends TargetFragmentUsageViolation {
    GetTargetFragmentRequestCodeUsageViolation(Fragment fragment) {
        super(fragment);
    }

    public String getMessage() {
        return "Attempting to get target request code from fragment " + this.mFragment;
    }
}
