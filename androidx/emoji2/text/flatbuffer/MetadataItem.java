package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class MetadataItem extends Table {
    public void __init(int i, ByteBuffer byteBuffer) {
        __reset(i, byteBuffer);
    }

    public MetadataItem __assign(int i, ByteBuffer byteBuffer) {
        __init(i, byteBuffer);
        return this;
    }

    /* renamed from: id */
    public int mo4318id() {
        int __offset = __offset(4);
        if (__offset != 0) {
            return this.f40bb.getInt(__offset + this.bb_pos);
        }
        return 0;
    }

    public boolean emojiStyle() {
        int __offset = __offset(6);
        return (__offset == 0 || this.f40bb.get(__offset + this.bb_pos) == 0) ? false : true;
    }

    public short sdkAdded() {
        int __offset = __offset(8);
        if (__offset != 0) {
            return this.f40bb.getShort(__offset + this.bb_pos);
        }
        return 0;
    }

    public short width() {
        int __offset = __offset(12);
        if (__offset != 0) {
            return this.f40bb.getShort(__offset + this.bb_pos);
        }
        return 0;
    }

    public short height() {
        int __offset = __offset(14);
        if (__offset != 0) {
            return this.f40bb.getShort(__offset + this.bb_pos);
        }
        return 0;
    }

    public int codepoints(int i) {
        int __offset = __offset(16);
        if (__offset != 0) {
            return this.f40bb.getInt(__vector(__offset) + (i * 4));
        }
        return 0;
    }

    public int codepointsLength() {
        int __offset = __offset(16);
        if (__offset != 0) {
            return __vector_len(__offset);
        }
        return 0;
    }
}
