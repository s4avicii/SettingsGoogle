package com.android.settings.sim.smartForwarding;

import com.android.settings.sim.smartForwarding.EnableSmartForwardingTask;
import java.util.function.Consumer;

/* renamed from: com.android.settings.sim.smartForwarding.EnableSmartForwardingTask$UpdateCallForwardingCommand$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1269x438b917a implements Consumer {
    public final /* synthetic */ EnableSmartForwardingTask.UpdateCallForwardingCommand f$0;

    public /* synthetic */ C1269x438b917a(EnableSmartForwardingTask.UpdateCallForwardingCommand updateCallForwardingCommand) {
        this.f$0 = updateCallForwardingCommand;
    }

    public final void accept(Object obj) {
        this.f$0.updateStatusCallBack(((Integer) obj).intValue());
    }
}
