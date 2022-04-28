package androidx.transition;

import android.graphics.Matrix;
import android.widget.ImageView;

class ImageViewUtils {
    static void animateTransform(ImageView imageView, Matrix matrix) {
        imageView.animateTransform(matrix);
    }
}
