package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public abstract class Violation extends RuntimeException {
    final Fragment mFragment;

    Violation(Fragment fragment) {
        this.mFragment = fragment;
    }

    public Fragment getFragment() {
        return this.mFragment;
    }
}
