package androidx.window.embedding;

import androidx.window.embedding.EmbeddingInterfaceCompat;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EmbeddingCompat$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ EmbeddingInterfaceCompat.EmbeddingCallbackInterface f$0;
    public final /* synthetic */ EmbeddingCompat f$1;

    public /* synthetic */ EmbeddingCompat$$ExternalSyntheticLambda0(EmbeddingInterfaceCompat.EmbeddingCallbackInterface embeddingCallbackInterface, EmbeddingCompat embeddingCompat) {
        this.f$0 = embeddingCallbackInterface;
        this.f$1 = embeddingCompat;
    }

    public final void accept(Object obj) {
        EmbeddingCompat.m54setEmbeddingCallback$lambda0(this.f$0, this.f$1, (List) obj);
    }
}
