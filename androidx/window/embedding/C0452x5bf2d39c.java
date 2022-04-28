package androidx.window.embedding;

import androidx.window.embedding.ExtensionEmbeddingBackend;
import java.util.List;

/* renamed from: androidx.window.embedding.ExtensionEmbeddingBackend$SplitListenerWrapper$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0452x5bf2d39c implements Runnable {
    public final /* synthetic */ ExtensionEmbeddingBackend.SplitListenerWrapper f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ C0452x5bf2d39c(ExtensionEmbeddingBackend.SplitListenerWrapper splitListenerWrapper, List list) {
        this.f$0 = splitListenerWrapper;
        this.f$1 = list;
    }

    public final void run() {
        ExtensionEmbeddingBackend.SplitListenerWrapper.m55accept$lambda1(this.f$0, this.f$1);
    }
}
