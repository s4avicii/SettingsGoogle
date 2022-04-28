package com.android.settings.development.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.window.C0444R;

class BlobInfoViewHolder {
    TextView blobExpiry;
    TextView blobId;
    TextView blobLabel;
    TextView blobSize;
    View rootView;

    BlobInfoViewHolder() {
    }

    static BlobInfoViewHolder createOrRecycle(LayoutInflater layoutInflater, View view) {
        if (view != null) {
            return (BlobInfoViewHolder) view.getTag();
        }
        View inflate = layoutInflater.inflate(C0444R.C0450layout.blob_list_item_view, (ViewGroup) null);
        BlobInfoViewHolder blobInfoViewHolder = new BlobInfoViewHolder();
        blobInfoViewHolder.rootView = inflate;
        blobInfoViewHolder.blobLabel = (TextView) inflate.findViewById(C0444R.C0448id.blob_label);
        blobInfoViewHolder.blobId = (TextView) inflate.findViewById(C0444R.C0448id.blob_id);
        blobInfoViewHolder.blobExpiry = (TextView) inflate.findViewById(C0444R.C0448id.blob_expiry);
        blobInfoViewHolder.blobSize = (TextView) inflate.findViewById(C0444R.C0448id.blob_size);
        inflate.setTag(blobInfoViewHolder);
        return blobInfoViewHolder;
    }
}
