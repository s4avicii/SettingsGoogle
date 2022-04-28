package androidx.window.layout;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: FoldingFeature.kt */
public interface FoldingFeature extends DisplayFeature {
    @NotNull
    OcclusionType getOcclusionType();

    @NotNull
    Orientation getOrientation();

    @NotNull
    State getState();

    boolean isSeparating();

    /* compiled from: FoldingFeature.kt */
    public static final class OcclusionType {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        @NotNull
        public static final OcclusionType FULL = new OcclusionType("FULL");
        @NotNull
        public static final OcclusionType NONE = new OcclusionType("NONE");
        @NotNull
        private final String description;

        private OcclusionType(String str) {
            this.description = str;
        }

        @NotNull
        public String toString() {
            return this.description;
        }

        /* compiled from: FoldingFeature.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }

    /* compiled from: FoldingFeature.kt */
    public static final class Orientation {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        @NotNull
        public static final Orientation HORIZONTAL = new Orientation("HORIZONTAL");
        @NotNull
        public static final Orientation VERTICAL = new Orientation("VERTICAL");
        @NotNull
        private final String description;

        private Orientation(String str) {
            this.description = str;
        }

        @NotNull
        public String toString() {
            return this.description;
        }

        /* compiled from: FoldingFeature.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }

    /* compiled from: FoldingFeature.kt */
    public static final class State {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        @NotNull
        public static final State FLAT = new State("FLAT");
        @NotNull
        public static final State HALF_OPENED = new State("HALF_OPENED");
        @NotNull
        private final String description;

        private State(String str) {
            this.description = str;
        }

        @NotNull
        public String toString() {
            return this.description;
        }

        /* compiled from: FoldingFeature.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }
}
