package com.android.settings.development.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.window.C0444R;

class LeaseInfoViewHolder {
    ImageView appIcon;
    TextView leaseDescription;
    TextView leaseExpiry;
    TextView leasePackageName;
    View rootView;

    LeaseInfoViewHolder() {
    }

    static LeaseInfoViewHolder createOrRecycle(LayoutInflater layoutInflater, View view) {
        if (view != null) {
            return (LeaseInfoViewHolder) view.getTag();
        }
        View inflate = layoutInflater.inflate(C0444R.C0450layout.lease_list_item_view, (ViewGroup) null);
        LeaseInfoViewHolder leaseInfoViewHolder = new LeaseInfoViewHolder();
        leaseInfoViewHolder.rootView = inflate;
        leaseInfoViewHolder.appIcon = (ImageView) inflate.findViewById(C0444R.C0448id.app_icon);
        leaseInfoViewHolder.leasePackageName = (TextView) inflate.findViewById(C0444R.C0448id.lease_package);
        leaseInfoViewHolder.leaseDescription = (TextView) inflate.findViewById(C0444R.C0448id.lease_desc);
        leaseInfoViewHolder.leaseExpiry = (TextView) inflate.findViewById(C0444R.C0448id.lease_expiry);
        inflate.setTag(leaseInfoViewHolder);
        return leaseInfoViewHolder;
    }
}
