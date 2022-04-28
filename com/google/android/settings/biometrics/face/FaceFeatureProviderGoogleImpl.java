package com.google.android.settings.biometrics.face;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.biometrics.face.FaceFeatureProvider;

public class FaceFeatureProviderGoogleImpl implements FaceFeatureProvider {
    public boolean isAttentionSupported(Context context) {
        return context.getResources().getBoolean(C0444R.bool.config_face_settings_attention_supported);
    }
}
