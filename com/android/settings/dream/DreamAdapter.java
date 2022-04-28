package com.android.settings.dream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settingslib.Utils;
import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<IDreamItem> mItemList;
    /* access modifiers changed from: private */
    public int mLastSelectedPos = -1;

    private class DreamViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        private final Button mCustomizeButton;
        private final ImageView mIconView;
        private final ImageView mPreviewPlaceholderView;
        private final ImageView mPreviewView;
        private final TextView mTitleView;

        DreamViewHolder(View view, Context context) {
            super(view);
            this.mContext = context;
            this.mPreviewView = (ImageView) view.findViewById(C0444R.C0448id.preview);
            this.mPreviewPlaceholderView = (ImageView) view.findViewById(C0444R.C0448id.preview_placeholder);
            this.mIconView = (ImageView) view.findViewById(C0444R.C0448id.icon);
            this.mTitleView = (TextView) view.findViewById(C0444R.C0448id.title_text);
            this.mCustomizeButton = (Button) view.findViewById(C0444R.C0448id.customize_button);
        }

        public void bindView(IDreamItem iDreamItem, int i) {
            Drawable drawable;
            this.mTitleView.setText(iDreamItem.getTitle());
            Drawable previewImage = iDreamItem.getPreviewImage();
            int i2 = 8;
            if (previewImage != null) {
                this.mPreviewView.setImageDrawable(previewImage);
                this.mPreviewView.setClipToOutline(true);
                this.mPreviewPlaceholderView.setVisibility(8);
            }
            if (iDreamItem.isActive()) {
                drawable = this.mContext.getDrawable(C0444R.C0447drawable.ic_dream_check_circle);
            } else {
                drawable = iDreamItem.getIcon();
            }
            if (drawable instanceof VectorDrawable) {
                drawable.setTint(Utils.getColorAttrDefaultColor(this.mContext, 17956901));
            }
            this.mIconView.setImageDrawable(drawable);
            if (iDreamItem.isActive()) {
                DreamAdapter.this.mLastSelectedPos = i;
                this.itemView.setSelected(true);
            } else {
                this.itemView.setSelected(false);
            }
            this.mCustomizeButton.setOnClickListener(new DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda1(iDreamItem));
            Button button = this.mCustomizeButton;
            if (iDreamItem.allowCustomization()) {
                i2 = 0;
            }
            button.setVisibility(i2);
            this.itemView.setOnClickListener(new DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda0(this, iDreamItem, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$bindView$1(IDreamItem iDreamItem, int i, View view) {
            iDreamItem.onItemClicked();
            if (DreamAdapter.this.mLastSelectedPos > -1 && DreamAdapter.this.mLastSelectedPos != i) {
                DreamAdapter dreamAdapter = DreamAdapter.this;
                dreamAdapter.notifyItemChanged(dreamAdapter.mLastSelectedPos);
            }
            DreamAdapter.this.notifyItemChanged(i);
        }
    }

    public DreamAdapter(List<IDreamItem> list) {
        this.mItemList = list;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DreamViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0444R.C0450layout.dream_preference_layout, viewGroup, false), viewGroup.getContext());
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((DreamViewHolder) viewHolder).bindView(this.mItemList.get(i), i);
    }

    public int getItemCount() {
        return this.mItemList.size();
    }
}
