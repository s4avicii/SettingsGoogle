package androidx.emoji2.text;

import android.graphics.Typeface;
import android.util.SparseArray;
import androidx.core.p002os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class MetadataRepo {
    private final char[] mEmojiCharArray;
    private final MetadataList mMetadataList;
    private final Node mRootNode = new Node(1024);
    private final Typeface mTypeface;

    private MetadataRepo(Typeface typeface, MetadataList metadataList) {
        this.mTypeface = typeface;
        this.mMetadataList = metadataList;
        this.mEmojiCharArray = new char[(metadataList.listLength() * 2)];
        constructIndex(metadataList);
    }

    public static MetadataRepo create(Typeface typeface, ByteBuffer byteBuffer) throws IOException {
        try {
            TraceCompat.beginSection("EmojiCompat.MetadataRepo.create");
            return new MetadataRepo(typeface, MetadataListReader.read(byteBuffer));
        } finally {
            TraceCompat.endSection();
        }
    }

    private void constructIndex(MetadataList metadataList) {
        int listLength = metadataList.listLength();
        for (int i = 0; i < listLength; i++) {
            EmojiMetadata emojiMetadata = new EmojiMetadata(this, i);
            Character.toChars(emojiMetadata.getId(), this.mEmojiCharArray, i * 2);
            put(emojiMetadata);
        }
    }

    /* access modifiers changed from: package-private */
    public Typeface getTypeface() {
        return this.mTypeface;
    }

    /* access modifiers changed from: package-private */
    public int getMetadataVersion() {
        return this.mMetadataList.version();
    }

    /* access modifiers changed from: package-private */
    public Node getRootNode() {
        return this.mRootNode;
    }

    public char[] getEmojiCharArray() {
        return this.mEmojiCharArray;
    }

    public MetadataList getMetadataList() {
        return this.mMetadataList;
    }

    /* access modifiers changed from: package-private */
    public void put(EmojiMetadata emojiMetadata) {
        Preconditions.checkNotNull(emojiMetadata, "emoji metadata cannot be null");
        Preconditions.checkArgument(emojiMetadata.getCodepointsLength() > 0, "invalid metadata codepoint length");
        this.mRootNode.put(emojiMetadata, 0, emojiMetadata.getCodepointsLength() - 1);
    }

    static class Node {
        private final SparseArray<Node> mChildren;
        private EmojiMetadata mData;

        private Node() {
            this(1);
        }

        Node(int i) {
            this.mChildren = new SparseArray<>(i);
        }

        /* access modifiers changed from: package-private */
        public Node get(int i) {
            SparseArray<Node> sparseArray = this.mChildren;
            if (sparseArray == null) {
                return null;
            }
            return sparseArray.get(i);
        }

        /* access modifiers changed from: package-private */
        public final EmojiMetadata getData() {
            return this.mData;
        }

        /* access modifiers changed from: package-private */
        public void put(EmojiMetadata emojiMetadata, int i, int i2) {
            Node node = get(emojiMetadata.getCodepointAt(i));
            if (node == null) {
                node = new Node();
                this.mChildren.put(emojiMetadata.getCodepointAt(i), node);
            }
            if (i2 > i) {
                node.put(emojiMetadata, i + 1, i2);
            } else {
                node.mData = emojiMetadata;
            }
        }
    }
}
