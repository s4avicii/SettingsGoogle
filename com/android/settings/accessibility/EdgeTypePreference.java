package com.android.settings.accessibility;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.internal.widget.SubtitleView;

public class EdgeTypePreference extends ListDialogPreference {
    public EdgeTypePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = context.getResources();
        setValues(resources.getIntArray(C0444R.array.captioning_edge_type_selector_values));
        setTitles(resources.getStringArray(C0444R.array.captioning_edge_type_selector_titles));
        setDialogLayoutResource(C0444R.C0450layout.grid_picker_dialog);
        setListItemLayoutResource(C0444R.C0450layout.preset_picker_item);
    }

    public boolean shouldDisableDependents() {
        return getValue() == 0 || super.shouldDisableDependents();
    }

    /* access modifiers changed from: protected */
    public void onBindListItem(View view, int i) {
        SubtitleView findViewById = view.findViewById(C0444R.C0448id.preview);
        findViewById.setForegroundColor(-1);
        findViewById.setBackgroundColor(0);
        findViewById.setTextSize(getContext().getResources().getDisplayMetrics().density * 32.0f);
        findViewById.setEdgeType(getValueAt(i));
        findViewById.setEdgeColor(-16777216);
        CharSequence titleAt = getTitleAt(i);
        if (titleAt != null) {
            ((TextView) view.findViewById(C0444R.C0448id.summary)).setText(titleAt);
        }
    }
}
