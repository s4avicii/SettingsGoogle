package com.android.settings.sim.smartForwarding;

import com.android.settings.sim.smartForwarding.EnableSmartForwardingTask;
import java.util.function.Consumer;

/* renamed from: com.android.settings.sim.smartForwarding.EnableSmartForwardingTask$UpdateCallWaitingCommand$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1270xf4424410 implements Consumer {
    public final /* synthetic */ EnableSmartForwardingTask.UpdateCallWaitingCommand f$0;

    public /* synthetic */ C1270xf4424410(EnableSmartForwardingTask.UpdateCallWaitingCommand updateCallWaitingCommand) {
        this.f$0 = updateCallWaitingCommand;
    }

    public final void accept(Object obj) {
        this.f$0.updateStatusCallBack(((Integer) obj).intValue());
    }
}
