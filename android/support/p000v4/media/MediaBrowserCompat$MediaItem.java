package android.support.p000v4.media;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint({"BanParcelableUsage"})
/* renamed from: android.support.v4.media.MediaBrowserCompat$MediaItem */
public class MediaBrowserCompat$MediaItem implements Parcelable {
    public static final Parcelable.Creator<MediaBrowserCompat$MediaItem> CREATOR = new Parcelable.Creator<MediaBrowserCompat$MediaItem>() {
        public MediaBrowserCompat$MediaItem createFromParcel(Parcel parcel) {
            return new MediaBrowserCompat$MediaItem(parcel);
        }

        public MediaBrowserCompat$MediaItem[] newArray(int i) {
            return new MediaBrowserCompat$MediaItem[i];
        }
    };
    private final MediaDescriptionCompat mDescription;
    private final int mFlags;

    public int describeContents() {
        return 0;
    }

    MediaBrowserCompat$MediaItem(Parcel parcel) {
        this.mFlags = parcel.readInt();
        this.mDescription = MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mFlags);
        this.mDescription.writeToParcel(parcel, i);
    }

    public String toString() {
        return "MediaItem{" + "mFlags=" + this.mFlags + ", mDescription=" + this.mDescription + '}';
    }
}
