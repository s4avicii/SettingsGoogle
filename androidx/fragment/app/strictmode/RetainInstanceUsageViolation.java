package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public abstract class RetainInstanceUsageViolation extends Violation {
    RetainInstanceUsageViolation(Fragment fragment) {
        super(fragment);
    }
}
