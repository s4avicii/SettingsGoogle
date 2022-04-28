package com.android.settings.applications.specialaccess.notificationaccess;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class ScaryWarningDialogFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 557;
    }

    public ScaryWarningDialogFragment setServiceInfo(ComponentName componentName, CharSequence charSequence, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("c", componentName.flattenToString());
        bundle.putCharSequence("l", charSequence);
        setArguments(bundle);
        setTargetFragment(fragment, 0);
        return this;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        CharSequence charSequence = arguments.getCharSequence("l");
        ComponentName unflattenFromString = ComponentName.unflattenFromString(arguments.getString("c"));
        return new AlertDialog.Builder(getContext()).setView(getDialogView(getContext(), charSequence, (NotificationAccessDetails) getTargetFragment(), unflattenFromString)).setCancelable(true).create();
    }

    private View getDialogView(Context context, CharSequence charSequence, NotificationAccessDetails notificationAccessDetails, ComponentName componentName) {
        Drawable drawable = null;
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0444R.C0450layout.enable_nls_dialog_content, (ViewGroup) null);
        try {
            drawable = context.getPackageManager().getApplicationIcon(componentName.getPackageName());
        } catch (PackageManager.NameNotFoundException unused) {
        }
        ImageView imageView = (ImageView) inflate.findViewById(C0444R.C0448id.app_icon);
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(8);
        }
        ((TextView) inflate.findViewById(C0444R.C0448id.title)).setText(context.getResources().getString(C0444R.string.notification_listener_security_warning_title, new Object[]{charSequence}));
        ((TextView) inflate.findViewById(C0444R.C0448id.prompt)).setText(context.getResources().getString(C0444R.string.nls_warning_prompt, new Object[]{charSequence}));
        ((Button) inflate.findViewById(C0444R.C0448id.allow_button)).setOnClickListener(new ScaryWarningDialogFragment$$ExternalSyntheticLambda1(this, notificationAccessDetails, componentName));
        ((Button) inflate.findViewById(C0444R.C0448id.deny_button)).setOnClickListener(new ScaryWarningDialogFragment$$ExternalSyntheticLambda0(this));
        return inflate;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getDialogView$0(NotificationAccessDetails notificationAccessDetails, ComponentName componentName, View view) {
        notificationAccessDetails.enable(componentName);
        dismiss();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getDialogView$1(View view) {
        dismiss();
    }
}
