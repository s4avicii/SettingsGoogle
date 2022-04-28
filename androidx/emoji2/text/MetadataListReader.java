package androidx.emoji2.text;

import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class MetadataListReader {

    private interface OpenTypeReader {
        long getPosition();

        int readTag() throws IOException;

        long readUnsignedInt() throws IOException;

        int readUnsignedShort() throws IOException;

        void skip(int i) throws IOException;
    }

    static long toUnsignedInt(int i) {
        return ((long) i) & 4294967295L;
    }

    static int toUnsignedShort(short s) {
        return s & 65535;
    }

    static MetadataList read(ByteBuffer byteBuffer) throws IOException {
        ByteBuffer duplicate = byteBuffer.duplicate();
        duplicate.position((int) findOffsetInfo(new ByteBufferReader(duplicate)).getStartOffset());
        return MetadataList.getRootAsMetadataList(duplicate);
    }

    private static OffsetInfo findOffsetInfo(OpenTypeReader openTypeReader) throws IOException {
        long j;
        openTypeReader.skip(4);
        int readUnsignedShort = openTypeReader.readUnsignedShort();
        if (readUnsignedShort <= 100) {
            openTypeReader.skip(6);
            int i = 0;
            while (true) {
                if (i >= readUnsignedShort) {
                    j = -1;
                    break;
                }
                int readTag = openTypeReader.readTag();
                openTypeReader.skip(4);
                j = openTypeReader.readUnsignedInt();
                openTypeReader.skip(4);
                if (1835365473 == readTag) {
                    break;
                }
                i++;
            }
            if (j != -1) {
                openTypeReader.skip((int) (j - openTypeReader.getPosition()));
                openTypeReader.skip(12);
                long readUnsignedInt = openTypeReader.readUnsignedInt();
                for (int i2 = 0; ((long) i2) < readUnsignedInt; i2++) {
                    int readTag2 = openTypeReader.readTag();
                    long readUnsignedInt2 = openTypeReader.readUnsignedInt();
                    long readUnsignedInt3 = openTypeReader.readUnsignedInt();
                    if (1164798569 == readTag2 || 1701669481 == readTag2) {
                        return new OffsetInfo(readUnsignedInt2 + j, readUnsignedInt3);
                    }
                }
            }
            throw new IOException("Cannot read metadata.");
        }
        throw new IOException("Cannot read metadata.");
    }

    private static class OffsetInfo {
        private final long mLength;
        private final long mStartOffset;

        OffsetInfo(long j, long j2) {
            this.mStartOffset = j;
            this.mLength = j2;
        }

        /* access modifiers changed from: package-private */
        public long getStartOffset() {
            return this.mStartOffset;
        }
    }

    private static class ByteBufferReader implements OpenTypeReader {
        private final ByteBuffer mByteBuffer;

        ByteBufferReader(ByteBuffer byteBuffer) {
            this.mByteBuffer = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        public int readUnsignedShort() throws IOException {
            return MetadataListReader.toUnsignedShort(this.mByteBuffer.getShort());
        }

        public long readUnsignedInt() throws IOException {
            return MetadataListReader.toUnsignedInt(this.mByteBuffer.getInt());
        }

        public int readTag() throws IOException {
            return this.mByteBuffer.getInt();
        }

        public void skip(int i) throws IOException {
            ByteBuffer byteBuffer = this.mByteBuffer;
            byteBuffer.position(byteBuffer.position() + i);
        }

        public long getPosition() {
            return (long) this.mByteBuffer.position();
        }
    }
}
