package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class SetUserVisibleHintViolation extends Violation {
    private final boolean mIsVisibleToUser;

    SetUserVisibleHintViolation(Fragment fragment, boolean z) {
        super(fragment);
        this.mIsVisibleToUser = z;
    }

    public String getMessage() {
        return "Attempting to set user visible hint to " + this.mIsVisibleToUser + " for fragment " + this.mFragment;
    }
}
