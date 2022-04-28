package com.android.settings.applications.manageapplications;

import com.android.settings.applications.manageapplications.ManageApplications;
import com.android.settingslib.applications.ApplicationsState;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ManageApplications.ApplicationsAdapter f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;
    public final /* synthetic */ ApplicationViewHolder f$2;

    public /* synthetic */ ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda1(ManageApplications.ApplicationsAdapter applicationsAdapter, ApplicationsState.AppEntry appEntry, ApplicationViewHolder applicationViewHolder) {
        this.f$0 = applicationsAdapter;
        this.f$1 = appEntry;
        this.f$2 = applicationViewHolder;
    }

    public final void run() {
        this.f$0.lambda$updateIcon$2(this.f$1, this.f$2);
    }
}
