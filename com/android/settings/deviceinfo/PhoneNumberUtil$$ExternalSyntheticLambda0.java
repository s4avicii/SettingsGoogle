package com.android.settings.deviceinfo;

import java.util.function.IntPredicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhoneNumberUtil$$ExternalSyntheticLambda0 implements IntPredicate {
    public static final /* synthetic */ PhoneNumberUtil$$ExternalSyntheticLambda0 INSTANCE = new PhoneNumberUtil$$ExternalSyntheticLambda0();

    private /* synthetic */ PhoneNumberUtil$$ExternalSyntheticLambda0() {
    }

    public final boolean test(int i) {
        return PhoneNumberUtil.isPhoneNumberDigit(i);
    }
}
