package androidx.window.core;

import androidx.window.core.SpecificationComputer;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SpecificationComputer.kt */
final class ValidSpecification<T> extends SpecificationComputer<T> {
    @NotNull
    private final Logger logger;
    @NotNull
    private final String tag;
    @NotNull
    private final T value;
    @NotNull
    private final SpecificationComputer.VerificationMode verificationMode;

    @NotNull
    public final T getValue() {
        return this.value;
    }

    @NotNull
    public final String getTag() {
        return this.tag;
    }

    @NotNull
    public final SpecificationComputer.VerificationMode getVerificationMode() {
        return this.verificationMode;
    }

    @NotNull
    public final Logger getLogger() {
        return this.logger;
    }

    public ValidSpecification(@NotNull T t, @NotNull String str, @NotNull SpecificationComputer.VerificationMode verificationMode2, @NotNull Logger logger2) {
        Intrinsics.checkNotNullParameter(t, "value");
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(verificationMode2, "verificationMode");
        Intrinsics.checkNotNullParameter(logger2, "logger");
        this.value = t;
        this.tag = str;
        this.verificationMode = verificationMode2;
        this.logger = logger2;
    }

    @NotNull
    public SpecificationComputer<T> require(@NotNull String str, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(str, "message");
        Intrinsics.checkNotNullParameter(function1, "condition");
        if (function1.invoke(this.value).booleanValue()) {
            return this;
        }
        return new FailedSpecification(this.value, this.tag, str, this.logger, this.verificationMode);
    }

    @NotNull
    public T compute() {
        return this.value;
    }
}
