package com.google.android.settings.dream;

import android.content.Intent;
import android.os.Bundle;
import com.android.settings.SettingsActivity;
import com.google.android.setupdesign.util.ThemeHelper;
import com.google.android.setupdesign.util.ThemeResolver;

public class DreamSetupActivity extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return false;
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", DreamSetupFragment.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return DreamSetupFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(ThemeResolver.getDefault().resolve(getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
    }
}
