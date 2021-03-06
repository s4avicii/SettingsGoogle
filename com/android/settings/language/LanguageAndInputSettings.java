package com.android.settings.language;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.inputmethod.PhysicalKeyboardPreferenceController;
import com.android.settings.inputmethod.SpellCheckerPreferenceController;
import com.android.settings.inputmethod.VirtualKeyboardPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.PreferenceCategoryController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageAndInputSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.language_and_input) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return LanguageAndInputSettings.buildPreferenceControllers(context, (Lifecycle) null);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LangAndInputSettings";
    }

    public int getMetricsCategory() {
        return 750;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.language_and_input;
    }

    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle(C0444R.string.language_settings);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("language_and_input_for_work_category", "Settings.WORK_PROFILE_KEYBOARDS_AND_TOOLS", C0444R.string.language_and_input_for_work_category_title);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PhoneLanguagePreferenceController(context));
        VirtualKeyboardPreferenceController virtualKeyboardPreferenceController = new VirtualKeyboardPreferenceController(context);
        PhysicalKeyboardPreferenceController physicalKeyboardPreferenceController = new PhysicalKeyboardPreferenceController(context, lifecycle);
        arrayList.add(virtualKeyboardPreferenceController);
        arrayList.add(physicalKeyboardPreferenceController);
        arrayList.add(new PreferenceCategoryController(context, "keyboards_category").setChildren(Arrays.asList(new AbstractPreferenceController[]{virtualKeyboardPreferenceController, physicalKeyboardPreferenceController})));
        DefaultVoiceInputPreferenceController defaultVoiceInputPreferenceController = new DefaultVoiceInputPreferenceController(context, lifecycle);
        TtsPreferenceController ttsPreferenceController = new TtsPreferenceController(context, "tts_settings_summary");
        arrayList.add(defaultVoiceInputPreferenceController);
        arrayList.add(ttsPreferenceController);
        arrayList.add(new PreferenceCategoryController(context, "speech_category").setChildren(Arrays.asList(new AbstractPreferenceController[]{defaultVoiceInputPreferenceController, ttsPreferenceController})));
        PointerSpeedController pointerSpeedController = new PointerSpeedController(context);
        arrayList.add(pointerSpeedController);
        arrayList.add(new PreferenceCategoryController(context, "pointer_category").setChildren(Arrays.asList(new AbstractPreferenceController[]{pointerSpeedController})));
        arrayList.add(new SpellCheckerPreferenceController(context));
        return arrayList;
    }
}
