package androidx.window.embedding;

import android.app.Activity;
import androidx.window.extensions.embedding.ActivityEmbeddingComponent;
import androidx.window.extensions.embedding.EmbeddingRule;
import androidx.window.extensions.embedding.SplitInfo;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmbeddingCompat.kt */
final class EmptyEmbeddingComponent implements ActivityEmbeddingComponent {
    public boolean isActivityEmbedded(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        return false;
    }

    public void setEmbeddingRules(@NotNull Set<EmbeddingRule> set) {
        Intrinsics.checkNotNullParameter(set, "splitRules");
    }

    public void setSplitInfoCallback(@NotNull Consumer<List<SplitInfo>> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "consumer");
    }
}
