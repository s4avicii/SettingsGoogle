package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public abstract class TargetFragmentUsageViolation extends Violation {
    TargetFragmentUsageViolation(Fragment fragment) {
        super(fragment);
    }
}
