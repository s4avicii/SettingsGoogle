package com.android.settings.accessibility;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Preconditions;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.window.C0444R;
import com.airbnb.lottie.LottieAnimationView;
import java.util.ArrayList;
import java.util.List;

public final class AccessibilityGestureNavigationTutorial {
    private static final DialogInterface.OnClickListener mOnClickListener = AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda0.INSTANCE;

    public static void showGestureNavigationTutorialDialog(Context context, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog create = new AlertDialog.Builder(context).setView(createTutorialDialogContentView(context, 2)).setNegativeButton((int) C0444R.string.accessibility_tutorial_dialog_button, mOnClickListener).setOnDismissListener(onDismissListener).create();
        create.requestWindowFeature(1);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    static AlertDialog showAccessibilityGestureTutorialDialog(Context context) {
        return createDialog(context, 1);
    }

    static AlertDialog createAccessibilityTutorialDialog(Context context, int i, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setView(createShortcutNavigationContentView(context, i)).setNegativeButton((int) C0444R.string.accessibility_tutorial_dialog_button, onClickListener).create();
    }

    private static View createTutorialDialogContentView(Context context, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        if (i == 0) {
            return layoutInflater.inflate(C0444R.C0450layout.tutorial_dialog_launch_service_by_accessibility_button, (ViewGroup) null);
        }
        if (i == 1) {
            View inflate = layoutInflater.inflate(C0444R.C0450layout.tutorial_dialog_launch_service_by_gesture_navigation, (ViewGroup) null);
            setupGestureNavigationTextWithImage(context, inflate);
            return inflate;
        } else if (i != 2) {
            return null;
        } else {
            View inflate2 = layoutInflater.inflate(C0444R.C0450layout.tutorial_dialog_launch_by_gesture_navigation_settings, (ViewGroup) null);
            setupGestureNavigationTextWithImage(context, inflate2);
            return inflate2;
        }
    }

    private static void setupGestureNavigationTextWithImage(Context context, View view) {
        boolean isTouchExploreEnabled = AccessibilityUtil.isTouchExploreEnabled(context);
        ((ImageView) view.findViewById(C0444R.C0448id.image)).setImageResource(isTouchExploreEnabled ? C0444R.C0447drawable.illustration_accessibility_gesture_three_finger : C0444R.C0447drawable.illustration_accessibility_gesture_two_finger);
        ((TextView) view.findViewById(C0444R.C0448id.gesture_tutorial_message)).setText(isTouchExploreEnabled ? C0444R.string.accessibility_tutorial_dialog_message_gesture_settings_talkback : C0444R.string.accessibility_tutorial_dialog_message_gesture_settings);
    }

    private static AlertDialog createDialog(Context context, int i) {
        AlertDialog create = new AlertDialog.Builder(context).setView(createTutorialDialogContentView(context, i)).setNegativeButton((int) C0444R.string.accessibility_tutorial_dialog_button, mOnClickListener).create();
        create.requestWindowFeature(1);
        create.setCanceledOnTouchOutside(false);
        create.show();
        return create;
    }

    private static class TutorialPagerAdapter extends PagerAdapter {
        private final List<TutorialPage> mTutorialPages;

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        private TutorialPagerAdapter(List<TutorialPage> list) {
            this.mTutorialPages = list;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View illustrationView = this.mTutorialPages.get(i).getIllustrationView();
            viewGroup.addView(illustrationView);
            return illustrationView;
        }

        public int getCount() {
            return this.mTutorialPages.size();
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView(this.mTutorialPages.get(i).getIllustrationView());
        }
    }

    private static ImageView createImageView(Context context, int i) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(i);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    private static View createIllustrationView(Context context, int i) {
        View inflateAndInitIllustrationFrame = inflateAndInitIllustrationFrame(context);
        ((LottieAnimationView) inflateAndInitIllustrationFrame.findViewById(C0444R.C0448id.image)).setImageResource(i);
        return inflateAndInitIllustrationFrame;
    }

    private static View createIllustrationViewWithImageRawResource(Context context, int i) {
        View inflateAndInitIllustrationFrame = inflateAndInitIllustrationFrame(context);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) inflateAndInitIllustrationFrame.findViewById(C0444R.C0448id.image);
        lottieAnimationView.setFailureListener(new AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda3(i));
        lottieAnimationView.setAnimation(i);
        lottieAnimationView.setRepeatCount(-1);
        lottieAnimationView.playAnimation();
        return inflateAndInitIllustrationFrame;
    }

    private static View inflateAndInitIllustrationFrame(Context context) {
        return ((LayoutInflater) context.getSystemService(LayoutInflater.class)).inflate(C0444R.C0450layout.accessibility_lottie_animation_view, (ViewGroup) null);
    }

    private static View createShortcutNavigationContentView(Context context, int i) {
        View inflate = ((LayoutInflater) context.getSystemService(LayoutInflater.class)).inflate(C0444R.C0450layout.accessibility_shortcut_tutorial_dialog, (ViewGroup) null);
        List<TutorialPage> createShortcutTutorialPages = createShortcutTutorialPages(context, i);
        int i2 = 1;
        Preconditions.checkArgument(!createShortcutTutorialPages.isEmpty(), "Unexpected tutorial pages size");
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C0444R.C0448id.indicator_container);
        linearLayout.setVisibility(createShortcutTutorialPages.size() > 1 ? 0 : 8);
        for (TutorialPage indicatorIcon : createShortcutTutorialPages) {
            linearLayout.addView(indicatorIcon.getIndicatorIcon());
        }
        createShortcutTutorialPages.get(0).getIndicatorIcon().setEnabled(true);
        TextSwitcher textSwitcher = (TextSwitcher) inflate.findViewById(C0444R.C0448id.title);
        textSwitcher.setFactory(new AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda2(context));
        textSwitcher.setText(createShortcutTutorialPages.get(0).getTitle());
        TextSwitcher textSwitcher2 = (TextSwitcher) inflate.findViewById(C0444R.C0448id.instruction);
        textSwitcher2.setFactory(new AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda1(context));
        textSwitcher2.setText(createShortcutTutorialPages.get(0).getInstruction());
        ViewPager viewPager = (ViewPager) inflate.findViewById(C0444R.C0448id.view_pager);
        viewPager.setAdapter(new TutorialPagerAdapter(createShortcutTutorialPages));
        viewPager.setContentDescription(context.getString(C0444R.string.accessibility_tutorial_pager, new Object[]{1, Integer.valueOf(createShortcutTutorialPages.size())}));
        if (createShortcutTutorialPages.size() <= 1) {
            i2 = 4;
        }
        viewPager.setImportantForAccessibility(i2);
        viewPager.addOnPageChangeListener(new TutorialPageChangeListener(context, viewPager, textSwitcher, textSwitcher2, createShortcutTutorialPages));
        return inflate;
    }

    /* access modifiers changed from: private */
    public static View makeTitleView(Context context) {
        TextView textView = new TextView(context);
        TextViewCompat.setTextAppearance(textView, C0444R.style.AccessibilityDialogTitle);
        textView.setGravity(17);
        return textView;
    }

    /* access modifiers changed from: private */
    public static View makeInstructionView(Context context) {
        TextView textView = new TextView(context);
        TextViewCompat.setTextAppearance(textView, C0444R.style.AccessibilityDialogDescription);
        return textView;
    }

    private static TutorialPage createSoftwareTutorialPage(Context context) {
        CharSequence softwareTitle = getSoftwareTitle(context);
        View createSoftwareImage = createSoftwareImage(context);
        CharSequence softwareInstruction = getSoftwareInstruction(context);
        ImageView createImageView = createImageView(context, C0444R.C0447drawable.ic_accessibility_page_indicator);
        createImageView.setEnabled(false);
        return new TutorialPage(softwareTitle, createSoftwareImage, createImageView, softwareInstruction);
    }

    private static TutorialPage createHardwareTutorialPage(Context context) {
        CharSequence text = context.getText(C0444R.string.accessibility_tutorial_dialog_title_volume);
        View createIllustrationView = createIllustrationView(context, C0444R.C0447drawable.accessibility_shortcut_type_hardware);
        ImageView createImageView = createImageView(context, C0444R.C0447drawable.ic_accessibility_page_indicator);
        CharSequence text2 = context.getText(C0444R.string.accessibility_tutorial_dialog_message_volume);
        createImageView.setEnabled(false);
        return new TutorialPage(text, createIllustrationView, createImageView, text2);
    }

    private static TutorialPage createTripleTapTutorialPage(Context context) {
        CharSequence text = context.getText(C0444R.string.accessibility_tutorial_dialog_title_triple);
        View createIllustrationViewWithImageRawResource = createIllustrationViewWithImageRawResource(context, C0444R.raw.accessibility_shortcut_type_triple_tap);
        CharSequence text2 = context.getText(C0444R.string.accessibility_tutorial_dialog_message_triple);
        ImageView createImageView = createImageView(context, C0444R.C0447drawable.ic_accessibility_page_indicator);
        createImageView.setEnabled(false);
        return new TutorialPage(text, createIllustrationViewWithImageRawResource, createImageView, text2);
    }

    static List<TutorialPage> createShortcutTutorialPages(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        if ((i & 1) == 1) {
            arrayList.add(createSoftwareTutorialPage(context));
        }
        if ((i & 2) == 2) {
            arrayList.add(createHardwareTutorialPage(context));
        }
        if ((i & 4) == 4) {
            arrayList.add(createTripleTapTutorialPage(context));
        }
        return arrayList;
    }

    private static View createSoftwareImage(Context context) {
        int i;
        if (AccessibilityUtil.isFloatingMenuEnabled(context)) {
            i = C0444R.C0447drawable.accessibility_shortcut_type_software_floating;
        } else {
            i = AccessibilityUtil.isGestureNavigateEnabled(context) ? AccessibilityUtil.isTouchExploreEnabled(context) ? C0444R.C0447drawable.accessibility_shortcut_type_software_gesture_talkback : C0444R.C0447drawable.accessibility_shortcut_type_software_gesture : C0444R.C0447drawable.accessibility_shortcut_type_software;
        }
        return createIllustrationView(context, i);
    }

    private static CharSequence getSoftwareTitle(Context context) {
        boolean isFloatingMenuEnabled = AccessibilityUtil.isFloatingMenuEnabled(context);
        int i = C0444R.string.accessibility_tutorial_dialog_title_button;
        if (!isFloatingMenuEnabled && AccessibilityUtil.isGestureNavigateEnabled(context)) {
            i = C0444R.string.accessibility_tutorial_dialog_title_gesture;
        }
        return context.getText(i);
    }

    private static CharSequence getSoftwareInstruction(Context context) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (AccessibilityUtil.isFloatingMenuEnabled(context)) {
            spannableStringBuilder.append(context.getText(C0444R.string.accessibility_tutorial_dialog_message_floating_button));
        } else if (AccessibilityUtil.isGestureNavigateEnabled(context)) {
            spannableStringBuilder.append(context.getText(AccessibilityUtil.isTouchExploreEnabled(context) ? C0444R.string.accessibility_tutorial_dialog_message_gesture_talkback : C0444R.string.accessibility_tutorial_dialog_message_gesture));
        } else {
            spannableStringBuilder.append(getSoftwareInstructionWithIcon(context, context.getText(C0444R.string.accessibility_tutorial_dialog_message_button)));
        }
        return spannableStringBuilder;
    }

    private static CharSequence getSoftwareInstructionWithIcon(Context context, CharSequence charSequence) {
        String charSequence2 = charSequence.toString();
        SpannableString valueOf = SpannableString.valueOf(charSequence2);
        int indexOf = charSequence2.indexOf("%s");
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getDrawable(C0444R.C0447drawable.ic_accessibility_new));
        Drawable mutate = imageView.getDrawable().mutate();
        ImageSpan imageSpan = new ImageSpan(mutate);
        imageSpan.setContentDescription("");
        mutate.setBounds(0, 0, mutate.getIntrinsicWidth(), mutate.getIntrinsicHeight());
        valueOf.setSpan(imageSpan, indexOf, indexOf + 2, 33);
        return valueOf;
    }

    private static class TutorialPage {
        private final View mIllustrationView;
        private final ImageView mIndicatorIcon;
        private final CharSequence mInstruction;
        private final CharSequence mTitle;

        TutorialPage(CharSequence charSequence, View view, ImageView imageView, CharSequence charSequence2) {
            this.mTitle = charSequence;
            this.mIllustrationView = view;
            this.mIndicatorIcon = imageView;
            this.mInstruction = charSequence2;
            setupIllustrationChildViewsGravity();
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public View getIllustrationView() {
            return this.mIllustrationView;
        }

        public ImageView getIndicatorIcon() {
            return this.mIndicatorIcon;
        }

        public CharSequence getInstruction() {
            return this.mInstruction;
        }

        private void setupIllustrationChildViewsGravity() {
            initViewGravity(this.mIllustrationView.findViewById(C0444R.C0448id.image_background));
            initViewGravity(this.mIllustrationView.findViewById(C0444R.C0448id.image));
        }

        private void initViewGravity(View view) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 17;
            view.setLayoutParams(layoutParams);
        }
    }

    private static class TutorialPageChangeListener implements ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TextSwitcher mInstruction;
        private int mLastTutorialPagePosition = 0;
        private final TextSwitcher mTitle;
        private final List<TutorialPage> mTutorialPages;
        private final ViewPager mViewPager;

        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        TutorialPageChangeListener(Context context, ViewPager viewPager, ViewGroup viewGroup, ViewGroup viewGroup2, List<TutorialPage> list) {
            this.mContext = context;
            this.mViewPager = viewPager;
            this.mTitle = (TextSwitcher) viewGroup;
            this.mInstruction = (TextSwitcher) viewGroup2;
            this.mTutorialPages = list;
        }

        public void onPageSelected(int i) {
            boolean z = this.mLastTutorialPagePosition > i;
            int i2 = z ? 17432578 : 17432745;
            int i3 = z ? 17432579 : 17432748;
            this.mTitle.setInAnimation(this.mContext, i2);
            this.mTitle.setOutAnimation(this.mContext, i3);
            this.mTitle.setText(this.mTutorialPages.get(i).getTitle());
            this.mInstruction.setInAnimation(this.mContext, i2);
            this.mInstruction.setOutAnimation(this.mContext, i3);
            this.mInstruction.setText(this.mTutorialPages.get(i).getInstruction());
            for (TutorialPage indicatorIcon : this.mTutorialPages) {
                indicatorIcon.getIndicatorIcon().setEnabled(false);
            }
            this.mTutorialPages.get(i).getIndicatorIcon().setEnabled(true);
            this.mLastTutorialPagePosition = i;
            this.mViewPager.setContentDescription(this.mContext.getString(C0444R.string.accessibility_tutorial_pager, new Object[]{Integer.valueOf(i + 1), Integer.valueOf(this.mTutorialPages.size())}));
        }
    }
}
