package androidx.window.layout;

import androidx.window.sidecar.SidecarDisplayFeature;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SidecarAdapter.kt */
final class SidecarAdapter$translate$checkedFeature$3 extends Lambda implements Function1<SidecarDisplayFeature, Boolean> {
    public static final SidecarAdapter$translate$checkedFeature$3 INSTANCE = new SidecarAdapter$translate$checkedFeature$3();

    SidecarAdapter$translate$checkedFeature$3() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@NotNull SidecarDisplayFeature sidecarDisplayFeature) {
        Intrinsics.checkNotNullParameter(sidecarDisplayFeature, "$this$require");
        boolean z = true;
        if (!(sidecarDisplayFeature.getType() != 1 || sidecarDisplayFeature.getRect().width() == 0 || sidecarDisplayFeature.getRect().height() == 0)) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
