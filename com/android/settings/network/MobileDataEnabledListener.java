package com.android.settings.network;

import android.content.Context;

public class MobileDataEnabledListener {
    /* access modifiers changed from: private */
    public Client mClient;
    private Context mContext;
    private GlobalSettingsChangeListener mListener;
    private GlobalSettingsChangeListener mListenerForSubId;
    private int mSubId = -1;

    public interface Client {
        void onMobileDataEnabledChange();
    }

    public MobileDataEnabledListener(Context context, Client client) {
        this.mContext = context;
        this.mClient = client;
    }

    public void start(int i) {
        this.mSubId = i;
        if (this.mListener == null) {
            this.mListener = new GlobalSettingsChangeListener(this.mContext, "mobile_data") {
                public void onChanged(String str) {
                    MobileDataEnabledListener.this.mClient.onMobileDataEnabledChange();
                }
            };
        }
        stopMonitorSubIdSpecific();
        if (this.mSubId != -1) {
            Context context = this.mContext;
            this.mListenerForSubId = new GlobalSettingsChangeListener(context, "mobile_data" + this.mSubId) {
                public void onChanged(String str) {
                    MobileDataEnabledListener.this.stopMonitor();
                    MobileDataEnabledListener.this.mClient.onMobileDataEnabledChange();
                }
            };
        }
    }

    public int getSubId() {
        return this.mSubId;
    }

    public void stop() {
        stopMonitor();
        stopMonitorSubIdSpecific();
    }

    /* access modifiers changed from: private */
    public void stopMonitor() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListener;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListener = null;
        }
    }

    private void stopMonitorSubIdSpecific() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListenerForSubId;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListenerForSubId = null;
        }
    }
}
