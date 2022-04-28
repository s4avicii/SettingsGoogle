package com.android.settings.sim;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.settings.network.SubscriptionUtil;
import java.util.ArrayList;
import java.util.List;

public class SimListDialogFragment extends SimDialogFragment {
    protected SelectSubscriptionAdapter mAdapter;
    List<SubscriptionInfo> mSubscriptions;

    public int getMetricsCategory() {
        return 1707;
    }

    public static SimListDialogFragment newInstance(int i, int i2, boolean z, boolean z2) {
        SimListDialogFragment simListDialogFragment = new SimListDialogFragment();
        Bundle initArguments = SimDialogFragment.initArguments(i, i2);
        initArguments.putBoolean("include_ask_every_time", z);
        initArguments.putBoolean("show_cancel_item", z2);
        simListDialogFragment.setArguments(initArguments);
        return simListDialogFragment;
    }

    /* JADX WARNING: type inference failed for: r1v9, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Dialog onCreateDialog(android.os.Bundle r5) {
        /*
            r4 = this;
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r4.mSubscriptions = r5
            androidx.appcompat.app.AlertDialog$Builder r5 = new androidx.appcompat.app.AlertDialog$Builder
            android.content.Context r0 = r4.getContext()
            r5.<init>(r0)
            android.content.Context r0 = r4.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            r1 = 2131100189(0x7f06021d, float:1.7812752E38)
            r2 = 0
            android.view.View r0 = r0.inflate(r1, r2)
            r1 = 2131559929(0x7f0d05f9, float:1.8745216E38)
            android.view.View r0 = r0.findViewById(r1)
            android.widget.TextView r0 = (android.widget.TextView) r0
            android.content.Context r1 = r4.getContext()
            int r3 = r4.getTitleResId()
            java.lang.String r1 = r1.getString(r3)
            r0.setText(r1)
            r5.setCustomTitle(r0)
            com.android.settings.sim.SimListDialogFragment$SelectSubscriptionAdapter r0 = new com.android.settings.sim.SimListDialogFragment$SelectSubscriptionAdapter
            android.content.Context r1 = r5.getContext()
            java.util.List<android.telephony.SubscriptionInfo> r3 = r4.mSubscriptions
            r0.<init>(r1, r3)
            r4.mAdapter = r0
            androidx.appcompat.app.AlertDialog r5 = r5.create()
            android.content.Context r0 = r4.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            r1 = 2131100188(0x7f06021c, float:1.781275E38)
            android.view.View r0 = r0.inflate(r1, r2)
            if (r0 == 0) goto L_0x0067
            r1 = 2131558722(0x7f0d0142, float:1.8742768E38)
            android.view.View r1 = r0.findViewById(r1)
            r2 = r1
            android.widget.ListView r2 = (android.widget.ListView) r2
        L_0x0067:
            if (r2 == 0) goto L_0x0078
            r4.setAdapter(r2)
            r1 = 0
            r2.setVisibility(r1)
            com.android.settings.sim.SimListDialogFragment$1 r1 = new com.android.settings.sim.SimListDialogFragment$1
            r1.<init>()
            r2.setOnItemClickListener(r1)
        L_0x0078:
            r5.setView(r0)
            r4.updateDialog()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.sim.SimListDialogFragment.onCreateDialog(android.os.Bundle):android.app.Dialog");
    }

    public void onClick(int i) {
        if (i >= 0 && i < this.mSubscriptions.size()) {
            int i2 = -1;
            SubscriptionInfo subscriptionInfo = this.mSubscriptions.get(i);
            if (subscriptionInfo != null) {
                i2 = subscriptionInfo.getSubscriptionId();
            }
            ((SimDialogActivity) getActivity()).onSubscriptionSelected(getDialogType(), i2);
        }
    }

    /* access modifiers changed from: protected */
    public List<SubscriptionInfo> getCurrentSubscriptions() {
        return ((SubscriptionManager) getContext().getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoList();
    }

    public void updateDialog() {
        Log.d("SimListDialogFragment", "Dialog updated, dismiss status: " + this.mWasDismissed);
        List<SubscriptionInfo> currentSubscriptions = getCurrentSubscriptions();
        if (currentSubscriptions != null) {
            boolean z = getArguments().getBoolean("include_ask_every_time");
            boolean z2 = getArguments().getBoolean("show_cancel_item");
            if (z || z2) {
                ArrayList arrayList = new ArrayList(currentSubscriptions.size() + (z ? 1 : 0) + (z2 ? 1 : 0));
                if (z) {
                    arrayList.add((Object) null);
                }
                arrayList.addAll(currentSubscriptions);
                if (z2) {
                    arrayList.add((Object) null);
                }
                currentSubscriptions = arrayList;
            }
            if (!currentSubscriptions.equals(this.mSubscriptions)) {
                this.mSubscriptions.clear();
                this.mSubscriptions.addAll(currentSubscriptions);
                this.mAdapter.notifyDataSetChanged();
            }
        } else if (!this.mWasDismissed) {
            dismiss();
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdapter(ListView listView) {
        listView.setAdapter(this.mAdapter);
    }

    private static class SelectSubscriptionAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        List<SubscriptionInfo> mSubscriptions;

        public SelectSubscriptionAdapter(Context context, List<SubscriptionInfo> list) {
            this.mSubscriptions = list;
            this.mContext = context;
        }

        public int getCount() {
            return this.mSubscriptions.size();
        }

        public SubscriptionInfo getItem(int i) {
            return this.mSubscriptions.get(i);
        }

        public long getItemId(int i) {
            SubscriptionInfo subscriptionInfo = this.mSubscriptions.get(i);
            if (subscriptionInfo == null) {
                return -1;
            }
            return (long) subscriptionInfo.getSubscriptionId();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                if (this.mInflater == null) {
                    this.mInflater = LayoutInflater.from(viewGroup.getContext());
                }
                view = this.mInflater.inflate(C0444R.C0450layout.select_account_list_item, viewGroup, false);
            }
            SubscriptionInfo item = getItem(i);
            TextView textView = (TextView) view.findViewById(C0444R.C0448id.title);
            TextView textView2 = (TextView) view.findViewById(C0444R.C0448id.summary);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
            if (marginLayoutParams != null) {
                marginLayoutParams.setMargins(0, this.mContext.getResources().getDimensionPixelSize(C0444R.dimen.sims_select_margin_top), 0, this.mContext.getResources().getDimensionPixelSize(C0444R.dimen.sims_select_margin_bottom));
                view.setLayoutParams(marginLayoutParams);
            }
            if (item == null) {
                if (i == 0) {
                    textView.setText(C0444R.string.sim_calls_ask_first_prefs_title);
                } else {
                    textView.setText(C0444R.string.sim_action_cancel);
                }
                textView2.setVisibility(8);
            } else {
                textView.setText(SubscriptionUtil.getUniqueSubscriptionDisplayName(item, this.mContext));
                String number = isMdnProvisioned(item.getNumber()) ? item.getNumber() : "";
                if (!TextUtils.isEmpty(number)) {
                    textView2.setVisibility(0);
                    textView2.setText(number);
                } else {
                    textView2.setVisibility(8);
                }
            }
            return view;
        }

        private boolean isMdnProvisioned(String str) {
            return !TextUtils.isEmpty(str) && !str.matches("[\\D0]+");
        }
    }
}
