package com.android.settings.network.telephony;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.network.SubscriptionUtil;
import com.android.settingslib.DeviceInfoUtils;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RenameMobileNetworkDialogFragment extends InstrumentedDialogFragment {
    private Spinner mColorSpinner;
    private Color[] mColors;
    private Map<Integer, Integer> mLightDarkMap;
    private EditText mNameView;
    private int mSubId;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;

    public int getMetricsCategory() {
        return 1642;
    }

    public static RenameMobileNetworkDialogFragment newInstance(int i) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("subscription_id", i);
        RenameMobileNetworkDialogFragment renameMobileNetworkDialogFragment = new RenameMobileNetworkDialogFragment();
        renameMobileNetworkDialogFragment.setArguments(bundle);
        return renameMobileNetworkDialogFragment;
    }

    /* access modifiers changed from: protected */
    public TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    /* access modifiers changed from: protected */
    public SubscriptionManager getSubscriptionManager(Context context) {
        return (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    /* access modifiers changed from: protected */
    public EditText getNameView() {
        return this.mNameView;
    }

    /* access modifiers changed from: protected */
    public Spinner getColorSpinnerView() {
        return this.mColorSpinner;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mTelephonyManager = getTelephonyManager(context);
        this.mSubscriptionManager = getSubscriptionManager(context);
        this.mSubId = getArguments().getInt("subscription_id");
        Resources resources = context.getResources();
        this.mLightDarkMap = ImmutableMap.builder().put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_cyan)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_cyan))).put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_blue800)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_blue))).put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_green800)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_green))).put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_purple800)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_purple))).put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_pink800)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_pink))).put(Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_color_orange)), Integer.valueOf(resources.getInteger(C0444R.C0446color.SIM_dark_mode_color_orange))).build();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        this.mColors = getColors();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = ((LayoutInflater) builder.getContext().getSystemService(LayoutInflater.class)).inflate(C0444R.C0450layout.dialog_mobile_network_rename, (ViewGroup) null);
        populateView(inflate);
        builder.setTitle((int) C0444R.string.mobile_network_sim_name).setView(inflate).setPositiveButton((int) C0444R.string.mobile_network_sim_name_rename, (DialogInterface.OnClickListener) new RenameMobileNetworkDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
        return builder.create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        Color color;
        this.mSubscriptionManager.setDisplayName(this.mNameView.getText().toString(), this.mSubId, 2);
        Spinner spinner = this.mColorSpinner;
        if (spinner == null) {
            color = this.mColors[0];
        } else {
            color = this.mColors[spinner.getSelectedItemPosition()];
        }
        this.mSubscriptionManager.setIconTint(color.getColor(), this.mSubId);
    }

    /* access modifiers changed from: protected */
    public void populateView(View view) {
        SubscriptionInfo subscriptionInfo;
        this.mNameView = (EditText) view.findViewById(C0444R.C0448id.name_edittext);
        List availableSubscriptionInfoList = this.mSubscriptionManager.getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList != null) {
            Iterator it = availableSubscriptionInfoList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                subscriptionInfo = (SubscriptionInfo) it.next();
                if (subscriptionInfo.getSubscriptionId() == this.mSubId) {
                    break;
                }
            }
        }
        subscriptionInfo = null;
        if (subscriptionInfo == null) {
            Log.w("RenameMobileNetwork", "got null SubscriptionInfo for mSubId:" + this.mSubId);
            return;
        }
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, getContext());
        this.mNameView.setText(uniqueSubscriptionDisplayName);
        if (!TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            this.mNameView.setSelection(uniqueSubscriptionDisplayName.length());
        }
        this.mColorSpinner = (Spinner) view.findViewById(C0444R.C0448id.color_spinner);
        this.mColorSpinner.setAdapter(new ColorAdapter(getContext(), C0444R.C0450layout.dialog_mobile_network_color_picker_item, this.mColors));
        this.mColorSpinner.setSelection(getSimColorIndex(subscriptionInfo.getIconTint()));
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        ((TextView) view.findViewById(C0444R.C0448id.operator_name_value)).setText(subscriptionInfo.getCarrierName());
        ((TextView) view.findViewById(C0444R.C0448id.number_label)).setVisibility(subscriptionInfo.isOpportunistic() ? 8 : 0);
        ((TextView) view.findViewById(C0444R.C0448id.number_value)).setText(DeviceInfoUtils.getBidiFormattedPhoneNumber(getContext(), subscriptionInfo));
    }

    private class ColorAdapter extends ArrayAdapter<Color> {
        private Context mContext;
        private int mItemResId;

        public ColorAdapter(Context context, int i, Color[] colorArr) {
            super(context, i, colorArr);
            this.mContext = context;
            this.mItemResId = i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            if (view == null) {
                view = layoutInflater.inflate(this.mItemResId, (ViewGroup) null);
            }
            boolean z = false;
            if ((RenameMobileNetworkDialogFragment.this.getResources().getConfiguration().uiMode & 48) == 32) {
                z = true;
            }
            ((ImageView) view.findViewById(C0444R.C0448id.color_icon)).setImageDrawable(((Color) getItem(i)).getDrawable(z));
            ((TextView) view.findViewById(C0444R.C0448id.color_label)).setText(((Color) getItem(i)).getLabel());
            return view;
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            return getView(i, view, viewGroup);
        }
    }

    private Color[] getColors() {
        Resources resources = getContext().getResources();
        int[] intArray = resources.getIntArray(C0444R.array.sim_color_light);
        String[] stringArray = resources.getStringArray(C0444R.array.color_picker);
        int dimensionPixelSize = resources.getDimensionPixelSize(C0444R.dimen.color_swatch_size);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(C0444R.dimen.color_swatch_stroke_width);
        int length = intArray.length;
        Color[] colorArr = new Color[length];
        for (int i = 0; i < length; i++) {
            colorArr[i] = new Color(stringArray[i], intArray[i], dimensionPixelSize, dimensionPixelSize2);
        }
        return colorArr;
    }

    private class Color {
        private int mColor;
        private ShapeDrawable mDrawable;
        private String mLabel;

        private Color(String str, int i, int i2, int i3) {
            this.mLabel = str;
            this.mColor = i;
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            this.mDrawable = shapeDrawable;
            shapeDrawable.setIntrinsicHeight(i2);
            this.mDrawable.setIntrinsicWidth(i2);
            this.mDrawable.getPaint().setStrokeWidth((float) i3);
            this.mDrawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            this.mDrawable.getPaint().setColor(i);
        }

        /* access modifiers changed from: private */
        public String getLabel() {
            return this.mLabel;
        }

        /* access modifiers changed from: private */
        public int getColor() {
            return this.mColor;
        }

        /* access modifiers changed from: private */
        public ShapeDrawable getDrawable(boolean z) {
            if (z) {
                this.mDrawable.getPaint().setColor(RenameMobileNetworkDialogFragment.this.getDarkColor(this.mColor));
            }
            return this.mDrawable;
        }
    }

    /* access modifiers changed from: private */
    public int getDarkColor(int i) {
        return this.mLightDarkMap.getOrDefault(Integer.valueOf(i), Integer.valueOf(i)).intValue();
    }

    private int getSimColorIndex(int i) {
        int[] intArray = getContext().getResources().getIntArray(17236174);
        int i2 = -1;
        for (int i3 = 0; i3 < intArray.length; i3++) {
            if (intArray[i3] == i) {
                i2 = i3;
            }
        }
        if (i2 == -1) {
            int i4 = 0;
            while (true) {
                Color[] colorArr = this.mColors;
                if (i4 >= colorArr.length) {
                    break;
                }
                if (colorArr[i4].getColor() == i) {
                    i2 = i4;
                }
                i4++;
            }
        }
        if (i2 == -1) {
            return 0;
        }
        return i2;
    }
}
