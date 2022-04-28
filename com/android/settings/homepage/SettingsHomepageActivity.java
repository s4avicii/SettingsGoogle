package com.android.settings.homepage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.window.C0444R;
import androidx.window.embedding.SplitController;
import com.android.settings.Settings;
import com.android.settings.SettingsApplication;
import com.android.settings.accounts.AvatarViewMixin;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.activityembedding.ActivityEmbeddingUtils;
import com.android.settings.core.CategoryMixin;
import com.android.settings.homepage.contextualcards.ContextualCardsFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.Utils;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;
import java.net.URISyntaxException;
import java.util.Set;

public class SettingsHomepageActivity extends FragmentActivity implements CategoryMixin.CategoryHandler {
    private CategoryMixin mCategoryMixin;
    private View mHomepageView;
    private boolean mIsEmbeddingActivityEnabled;
    private boolean mIsTwoPane;
    private Set<HomepageLoadedListener> mLoadedListeners;
    private TopLevelSettings mMainFragment;
    private SplitController mSplitController;
    private View mSuggestionView;
    private View mTwoPaneSuggestionView;

    private interface FragmentBuilder<T extends Fragment> {
        T build();
    }

    public interface HomepageLoadedListener {
        void onHomepageLoaded();
    }

    public boolean addHomepageLoadedListener(HomepageLoadedListener homepageLoadedListener) {
        if (this.mHomepageView == null) {
            return false;
        }
        if (this.mLoadedListeners.contains(homepageLoadedListener)) {
            return true;
        }
        this.mLoadedListeners.add(homepageLoadedListener);
        return true;
    }

    public void showHomepageWithSuggestion(boolean z) {
        if (this.mHomepageView != null) {
            Log.i("SettingsHomepageActivity", "showHomepageWithSuggestion: " + z);
            View view = this.mHomepageView;
            int i = 8;
            this.mSuggestionView.setVisibility(z ? 0 : 8);
            View view2 = this.mTwoPaneSuggestionView;
            if (z) {
                i = 0;
            }
            view2.setVisibility(i);
            this.mHomepageView = null;
            this.mLoadedListeners.forEach(SettingsHomepageActivity$$ExternalSyntheticLambda4.INSTANCE);
            this.mLoadedListeners.clear();
            view.setVisibility(0);
        }
    }

    public TopLevelSettings getMainFragment() {
        return this.mMainFragment;
    }

    public CategoryMixin getCategoryMixin() {
        return this.mCategoryMixin;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0444R.C0450layout.settings_homepage_container);
        this.mIsEmbeddingActivityEnabled = ActivityEmbeddingUtils.isEmbeddingActivityEnabled(this);
        SplitController instance = SplitController.getInstance();
        this.mSplitController = instance;
        this.mIsTwoPane = instance.isActivityEmbedded(this);
        findViewById(C0444R.C0448id.app_bar_container).setMinimumHeight(getSearchBoxHeight());
        initHomepageContainer();
        updateHomepageAppBar();
        updateHomepageBackground();
        this.mLoadedListeners = new ArraySet();
        initSearchBarView();
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
        this.mCategoryMixin = new CategoryMixin(this);
        getLifecycle().addObserver(this.mCategoryMixin);
        String highlightMenuKey = getHighlightMenuKey();
        if (!((ActivityManager) getSystemService(ActivityManager.class)).isLowRamDevice()) {
            initAvatarView();
            showSuggestionFragment(this.mIsEmbeddingActivityEnabled && !TextUtils.equals(getString(C0444R.string.menu_key_network), highlightMenuKey));
            if (FeatureFlagUtils.isEnabled(this, "settings_contextual_home")) {
                showFragment(SettingsHomepageActivity$$ExternalSyntheticLambda2.INSTANCE, C0444R.C0448id.contextual_cards_content);
            }
        }
        this.mMainFragment = (TopLevelSettings) showFragment(new SettingsHomepageActivity$$ExternalSyntheticLambda1(highlightMenuKey), C0444R.C0448id.main_content);
        ((FrameLayout) findViewById(C0444R.C0448id.main_content)).getLayoutTransition().enableTransitionType(4);
        launchDeepLinkIntentToRight();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ ContextualCardsFragment lambda$onCreate$1() {
        return new ContextualCardsFragment();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ TopLevelSettings lambda$onCreate$2(String str) {
        TopLevelSettings topLevelSettings = new TopLevelSettings();
        topLevelSettings.getArguments().putString(":settings:fragment_args_key", str);
        return topLevelSettings;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        ((SettingsApplication) getApplication()).setHomeActivity(this);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        reloadHighlightMenuKey();
        if (!isFinishing()) {
            launchDeepLinkIntentToRight();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean isActivityEmbedded = this.mSplitController.isActivityEmbedded(this);
        if (this.mIsTwoPane != isActivityEmbedded) {
            this.mIsTwoPane = isActivityEmbedded;
            updateHomepageAppBar();
            updateHomepageBackground();
        }
    }

    private void initSearchBarView() {
        FeatureFactory.getFactory(this).getSearchFeatureProvider().initSearchToolbar(this, (Toolbar) findViewById(C0444R.C0448id.search_action_bar), 1502);
        if (this.mIsEmbeddingActivityEnabled) {
            FeatureFactory.getFactory(this).getSearchFeatureProvider().initSearchToolbar(this, (Toolbar) findViewById(C0444R.C0448id.search_action_bar_two_pane), 1502);
        }
    }

    private void initAvatarView() {
        ImageView imageView = (ImageView) findViewById(C0444R.C0448id.account_avatar);
        ImageView imageView2 = (ImageView) findViewById(C0444R.C0448id.account_avatar_two_pane_version);
        if (AvatarViewMixin.isAvatarSupported(this)) {
            imageView.setVisibility(0);
            getLifecycle().addObserver(new AvatarViewMixin(this, imageView));
            if (this.mIsEmbeddingActivityEnabled) {
                imageView2.setVisibility(0);
                getLifecycle().addObserver(new AvatarViewMixin(this, imageView2));
            }
        }
    }

    private void updateHomepageBackground() {
        int i;
        if (this.mIsEmbeddingActivityEnabled) {
            Window window = getWindow();
            if (this.mIsTwoPane) {
                i = Utils.getColorAttrDefaultColor(this, 17956909);
            } else {
                i = Utils.getColorAttrDefaultColor(this, 16842801);
            }
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(i);
            findViewById(C0444R.C0448id.settings_homepage_container).setBackgroundColor(i);
        }
    }

    private void showSuggestionFragment(boolean z) {
        Class<? extends Fragment> contextualSuggestionFragment = FeatureFactory.getFactory(this).getSuggestionFeatureProvider(this).getContextualSuggestionFragment();
        if (contextualSuggestionFragment != null) {
            this.mSuggestionView = findViewById(C0444R.C0448id.suggestion_content);
            this.mTwoPaneSuggestionView = findViewById(C0444R.C0448id.two_pane_suggestion_content);
            View findViewById = findViewById(C0444R.C0448id.settings_homepage_container);
            this.mHomepageView = findViewById;
            findViewById.setVisibility(z ? 4 : 8);
            this.mHomepageView.postDelayed(new SettingsHomepageActivity$$ExternalSyntheticLambda3(this), 300);
            SettingsHomepageActivity$$ExternalSyntheticLambda0 settingsHomepageActivity$$ExternalSyntheticLambda0 = new SettingsHomepageActivity$$ExternalSyntheticLambda0(contextualSuggestionFragment);
            showFragment(settingsHomepageActivity$$ExternalSyntheticLambda0, C0444R.C0448id.suggestion_content);
            if (this.mIsEmbeddingActivityEnabled) {
                showFragment(settingsHomepageActivity$$ExternalSyntheticLambda0, C0444R.C0448id.two_pane_suggestion_content);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestionFragment$3() {
        showHomepageWithSuggestion(false);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Fragment lambda$showSuggestionFragment$4(Class cls) {
        try {
            return (Fragment) cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            Log.w("SettingsHomepageActivity", "Cannot show fragment", e);
            return null;
        }
    }

    private <T extends Fragment> T showFragment(FragmentBuilder<T> fragmentBuilder, int i) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        T findFragmentById = supportFragmentManager.findFragmentById(i);
        if (findFragmentById == null) {
            findFragmentById = fragmentBuilder.build();
            beginTransaction.add(i, (Fragment) findFragmentById);
        } else {
            beginTransaction.show(findFragmentById);
        }
        beginTransaction.commit();
        return findFragmentById;
    }

    private void launchDeepLinkIntentToRight() {
        Intent intent;
        if (!this.mIsEmbeddingActivityEnabled || (intent = getIntent()) == null || !TextUtils.equals(intent.getAction(), "android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY")) {
            return;
        }
        if ((this instanceof DeepLinkHomepageActivity) || (this instanceof SliceDeepLinkHomepageActivity)) {
            String stringExtra = intent.getStringExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI");
            if (TextUtils.isEmpty(stringExtra)) {
                Log.e("SettingsHomepageActivity", "No EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI to deep link");
                finish();
                return;
            }
            try {
                Intent parseUri = Intent.parseUri(stringExtra, 1);
                ComponentName resolveActivity = parseUri.resolveActivity(getPackageManager());
                if (resolveActivity == null) {
                    Log.e("SettingsHomepageActivity", "No valid target for the deep link intent: " + parseUri);
                    finish();
                    return;
                }
                parseUri.setComponent(resolveActivity);
                intent.setAction((String) null);
                parseUri.setFlags(parseUri.getFlags() & -268435457);
                parseUri.addFlags(33554432);
                parseUri.replaceExtras(intent);
                parseUri.putExtra("is_from_settings_homepage", true);
                parseUri.putExtra("is_from_slice", false);
                parseUri.setData((Uri) intent.getParcelableExtra("settings_large_screen_deep_link_intent_data"));
                ComponentName componentName = resolveActivity;
                ActivityEmbeddingRulesController.registerTwoPanePairRule(this, new ComponentName(getApplicationContext(), getClass()), componentName, parseUri.getAction(), 1, 1, true);
                ActivityEmbeddingRulesController.registerTwoPanePairRule(this, new ComponentName(getApplicationContext(), Settings.class), componentName, parseUri.getAction(), 1, 1, true);
                startActivity(parseUri);
            } catch (URISyntaxException e) {
                Log.e("SettingsHomepageActivity", "Failed to parse deep link intent: " + e);
                finish();
            }
        } else {
            Log.e("SettingsHomepageActivity", "Not a deep link component");
            finish();
        }
    }

    private String getHighlightMenuKey() {
        Intent intent = getIntent();
        if (intent != null && TextUtils.equals(intent.getAction(), "android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY")) {
            String stringExtra = intent.getStringExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY");
            if (!TextUtils.isEmpty(stringExtra)) {
                return stringExtra;
            }
        }
        return getString(C0444R.string.menu_key_network);
    }

    private void reloadHighlightMenuKey() {
        this.mMainFragment.getArguments().putString(":settings:fragment_args_key", getHighlightMenuKey());
        this.mMainFragment.reloadHighlightMenuKey();
    }

    private void initHomepageContainer() {
        View findViewById = findViewById(C0444R.C0448id.homepage_container);
        findViewById.setFocusableInTouchMode(true);
        findViewById.requestFocus();
    }

    private void updateHomepageAppBar() {
        if (this.mIsEmbeddingActivityEnabled) {
            if (this.mIsTwoPane) {
                findViewById(C0444R.C0448id.homepage_app_bar_regular_phone_view).setVisibility(8);
                findViewById(C0444R.C0448id.homepage_app_bar_two_pane_view).setVisibility(0);
                return;
            }
            findViewById(C0444R.C0448id.homepage_app_bar_regular_phone_view).setVisibility(0);
            findViewById(C0444R.C0448id.homepage_app_bar_two_pane_view).setVisibility(8);
        }
    }

    private int getSearchBoxHeight() {
        return getResources().getDimensionPixelSize(C0444R.dimen.search_bar_height) + (getResources().getDimensionPixelSize(C0444R.dimen.search_bar_margin) * 2);
    }
}
