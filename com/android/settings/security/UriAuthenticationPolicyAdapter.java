package com.android.settings.security;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import java.util.List;

public class UriAuthenticationPolicyAdapter extends RecyclerView.Adapter<UriViewHolder> {
    private final List<Uri> mUris;

    public class UriViewHolder extends RecyclerView.ViewHolder {
        TextView mUriNameView = ((TextView) this.itemView.findViewById(C0444R.C0448id.uri_name));

        public UriViewHolder(View view) {
            super(view);
        }
    }

    UriAuthenticationPolicyAdapter(List<Uri> list) {
        this.mUris = list;
    }

    public UriViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new UriViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0444R.C0450layout.app_authentication_uri_item, viewGroup, false));
    }

    public void onBindViewHolder(UriViewHolder uriViewHolder, int i) {
        uriViewHolder.mUriNameView.setText(Uri.decode(this.mUris.get(i).toString()));
    }

    public int getItemCount() {
        return this.mUris.size();
    }
}
