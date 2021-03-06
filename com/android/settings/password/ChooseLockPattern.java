package com.android.settings.password;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.notification.RedactionInterstitial;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.password.SaveChosenLockWorkerBase;
import com.google.android.collect.Lists;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.template.IconMixin;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.Collections;
import java.util.List;

public class ChooseLockPattern extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return false;
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", getFragmentClass().getName());
        return intent;
    }

    public static class IntentBuilder {
        private final Intent mIntent;

        public IntentBuilder(Context context) {
            Intent intent = new Intent(context, ChooseLockPattern.class);
            this.mIntent = intent;
            intent.putExtra("extra_require_password", false);
            intent.putExtra("confirm_credentials", false);
        }

        public IntentBuilder setUserId(int i) {
            this.mIntent.putExtra("android.intent.extra.USER_ID", i);
            return this;
        }

        public IntentBuilder setRequestGatekeeperPasswordHandle(boolean z) {
            this.mIntent.putExtra("request_gk_pw_handle", z);
            return this;
        }

        public IntentBuilder setPattern(LockscreenCredential lockscreenCredential) {
            this.mIntent.putExtra("password", lockscreenCredential);
            return this;
        }

        public IntentBuilder setForFingerprint(boolean z) {
            this.mIntent.putExtra("for_fingerprint", z);
            return this;
        }

        public IntentBuilder setForFace(boolean z) {
            this.mIntent.putExtra("for_face", z);
            return this;
        }

        public IntentBuilder setForBiometrics(boolean z) {
            this.mIntent.putExtra("for_biometrics", z);
            return this;
        }

        public IntentBuilder setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
            this.mIntent.putExtra("unification_profile_id", i);
            this.mIntent.putExtra("unification_profile_credential", lockscreenCredential);
            return this;
        }

        public Intent build() {
            return this.mIntent;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ChooseLockPatternFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: package-private */
    public Class<? extends Fragment> getFragmentClass() {
        return ChooseLockPatternFragment.class;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        findViewById(C0444R.C0448id.content_parent).setFitsSystemWindows(false);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public static class ChooseLockPatternFragment extends InstrumentedFragment implements SaveChosenLockWorkerBase.Listener {
        private final List<LockPatternView.Cell> mAnimatePattern = Collections.unmodifiableList(Lists.newArrayList(new LockPatternView.Cell[]{LockPatternView.Cell.of(0, 0), LockPatternView.Cell.of(0, 1), LockPatternView.Cell.of(1, 1), LockPatternView.Cell.of(2, 1)}));
        protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
            public void onPatternCellAdded(List<LockPatternView.Cell> list) {
            }

            public void onPatternStart() {
                ChooseLockPatternFragment chooseLockPatternFragment = ChooseLockPatternFragment.this;
                chooseLockPatternFragment.mLockPatternView.removeCallbacks(chooseLockPatternFragment.mClearPatternRunnable);
                patternInProgress();
            }

            public void onPatternCleared() {
                ChooseLockPatternFragment chooseLockPatternFragment = ChooseLockPatternFragment.this;
                chooseLockPatternFragment.mLockPatternView.removeCallbacks(chooseLockPatternFragment.mClearPatternRunnable);
            }

            public void onPatternDetected(List<LockPatternView.Cell> list) {
                if (ChooseLockPatternFragment.this.mUiStage == Stage.NeedToConfirm || ChooseLockPatternFragment.this.mUiStage == Stage.ConfirmWrong) {
                    if (ChooseLockPatternFragment.this.mChosenPattern != null) {
                        LockscreenCredential createPattern = LockscreenCredential.createPattern(list);
                        try {
                            if (ChooseLockPatternFragment.this.mChosenPattern.equals(createPattern)) {
                                ChooseLockPatternFragment.this.updateStage(Stage.ChoiceConfirmed);
                            } else {
                                ChooseLockPatternFragment.this.updateStage(Stage.ConfirmWrong);
                            }
                            if (createPattern != null) {
                                createPattern.close();
                                return;
                            }
                            return;
                        } catch (Throwable th) {
                            th.addSuppressed(th);
                        }
                    } else {
                        throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
                    }
                } else if (ChooseLockPatternFragment.this.mUiStage != Stage.Introduction && ChooseLockPatternFragment.this.mUiStage != Stage.ChoiceTooShort) {
                    throw new IllegalStateException("Unexpected stage " + ChooseLockPatternFragment.this.mUiStage + " when entering the pattern.");
                } else if (list.size() < 4) {
                    ChooseLockPatternFragment.this.updateStage(Stage.ChoiceTooShort);
                    return;
                } else {
                    ChooseLockPatternFragment.this.mChosenPattern = LockscreenCredential.createPattern(list);
                    ChooseLockPatternFragment.this.updateStage(Stage.FirstChoiceValid);
                    return;
                }
                throw th;
            }

            private void patternInProgress() {
                ChooseLockPatternFragment.this.mHeaderText.setText(C0444R.string.lockpattern_recording_inprogress);
                if (ChooseLockPatternFragment.this.mDefaultHeaderColorList != null) {
                    ChooseLockPatternFragment chooseLockPatternFragment = ChooseLockPatternFragment.this;
                    chooseLockPatternFragment.mHeaderText.setTextColor(chooseLockPatternFragment.mDefaultHeaderColorList);
                }
                ChooseLockPatternFragment.this.mFooterText.setText("");
                ChooseLockPatternFragment.this.mNextButton.setEnabled(false);
            }
        };
        @VisibleForTesting
        protected LockscreenCredential mChosenPattern;
        /* access modifiers changed from: private */
        public Runnable mClearPatternRunnable = new Runnable() {
            public void run() {
                ChooseLockPatternFragment.this.mLockPatternView.clearPattern();
            }
        };
        private LockscreenCredential mCurrentCredential;
        /* access modifiers changed from: private */
        public ColorStateList mDefaultHeaderColorList;
        protected TextView mFooterText;
        protected boolean mForBiometrics;
        protected boolean mForFace;
        protected boolean mForFingerprint;
        protected TextView mHeaderText;
        protected boolean mIsManagedProfile;
        private LockPatternUtils mLockPatternUtils;
        protected LockPatternView mLockPatternView;
        /* access modifiers changed from: private */
        public FooterButton mNextButton;
        private boolean mRequestGatekeeperPassword;
        private SaveAndFinishWorker mSaveAndFinishWorker;
        protected FooterButton mSkipOrClearButton;
        /* access modifiers changed from: private */
        public Stage mUiStage = Stage.Introduction;
        protected int mUserId;

        public int getMetricsCategory() {
            return 29;
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            if (i == 55) {
                if (i2 != -1) {
                    getActivity().setResult(1);
                    getActivity().finish();
                } else {
                    this.mCurrentCredential = intent.getParcelableExtra("password");
                }
                updateStage(Stage.Introduction);
            }
        }

        /* access modifiers changed from: protected */
        public void setRightButtonEnabled(boolean z) {
            this.mNextButton.setEnabled(z);
        }

        /* access modifiers changed from: protected */
        public void setRightButtonText(int i) {
            this.mNextButton.setText(getActivity(), i);
        }

        enum LeftButtonMode {
            Retry(C0444R.string.lockpattern_retry_button_text, true),
            RetryDisabled(C0444R.string.lockpattern_retry_button_text, false),
            Gone(-1, false);
            
            final boolean enabled;
            final int text;

            private LeftButtonMode(int i, boolean z) {
                this.text = i;
                this.enabled = z;
            }
        }

        enum RightButtonMode {
            Continue(C0444R.string.next_label, true),
            ContinueDisabled(C0444R.string.next_label, false),
            Confirm(C0444R.string.lockpattern_confirm_button_text, true),
            ConfirmDisabled(C0444R.string.lockpattern_confirm_button_text, false),
            Ok(17039370, true);
            
            final boolean enabled;
            final int text;

            private RightButtonMode(int i, boolean z) {
                this.text = i;
                this.enabled = z;
            }
        }

        protected enum Stage {
            Introduction(C0444R.string.lock_settings_picker_biometrics_added_security_message, C0444R.string.lockpattern_recording_intro_header, r5, r11, -1, true),
            HelpScreen(-1, C0444R.string.lockpattern_settings_help_how_to_record, r5, RightButtonMode.Ok, -1, false),
            ChoiceTooShort(C0444R.string.lock_settings_picker_biometrics_added_security_message, C0444R.string.lockpattern_recording_incorrect_too_short, r5, r11, -1, true),
            FirstChoiceValid(C0444R.string.lock_settings_picker_biometrics_added_security_message, C0444R.string.lockpattern_pattern_entered_header, r5, RightButtonMode.Continue, -1, false),
            NeedToConfirm(-1, C0444R.string.lockpattern_need_to_confirm, r5, r6, -1, true),
            ConfirmWrong(-1, C0444R.string.lockpattern_need_to_unlock_wrong, r5, r6, -1, true),
            ChoiceConfirmed(-1, C0444R.string.lockpattern_pattern_confirmed_header, r5, RightButtonMode.Confirm, -1, false);
            
            final int footerMessage;
            final int headerMessage;
            final LeftButtonMode leftMode;
            final int messageForBiometrics;
            final boolean patternEnabled;
            final RightButtonMode rightMode;

            private Stage(int i, int i2, LeftButtonMode leftButtonMode, RightButtonMode rightButtonMode, int i3, boolean z) {
                this.headerMessage = i2;
                this.messageForBiometrics = i;
                this.leftMode = leftButtonMode;
                this.rightMode = rightButtonMode;
                this.footerMessage = i3;
                this.patternEnabled = z;
            }
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            if (getActivity() instanceof ChooseLockPattern) {
                Intent intent = getActivity().getIntent();
                this.mUserId = Utils.getUserIdFromBundle(getActivity(), intent.getExtras());
                this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mUserId);
                this.mLockPatternUtils = new LockPatternUtils(getActivity());
                if (intent.getBooleanExtra("for_cred_req_boot", false)) {
                    SaveAndFinishWorker saveAndFinishWorker = new SaveAndFinishWorker();
                    boolean booleanExtra = getActivity().getIntent().getBooleanExtra("extra_require_password", true);
                    LockscreenCredential parcelableExtra = intent.getParcelableExtra("password");
                    saveAndFinishWorker.setBlocking(true);
                    saveAndFinishWorker.setListener(this);
                    saveAndFinishWorker.start(this.mLockPatternUtils, booleanExtra, false, parcelableExtra, parcelableExtra, this.mUserId);
                }
                this.mForFingerprint = intent.getBooleanExtra("for_fingerprint", false);
                this.mForFace = intent.getBooleanExtra("for_face", false);
                this.mForBiometrics = intent.getBooleanExtra("for_biometrics", false);
                return;
            }
            throw new SecurityException("Fragment contained in wrong activity");
        }

        private void updateActivityTitle() {
            String str;
            if (this.mForFingerprint) {
                str = getString(C0444R.string.lockpassword_choose_your_pattern_header_for_fingerprint);
            } else if (this.mForFace) {
                str = getString(C0444R.string.lockpassword_choose_your_pattern_header_for_face);
            } else if (this.mIsManagedProfile) {
                str = ((DevicePolicyManager) getContext().getSystemService(DevicePolicyManager.class)).getString("Settings.SET_WORK_PROFILE_PATTERN_HEADER", new C1198xec9f3c7a(this));
            } else {
                str = getString(C0444R.string.lockpassword_choose_your_pattern_header);
            }
            getActivity().setTitle(str);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$updateActivityTitle$0() throws Exception {
            return getString(C0444R.string.lockpassword_choose_your_profile_pattern_header);
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            GlifLayout glifLayout = (GlifLayout) layoutInflater.inflate(C0444R.C0450layout.choose_lock_pattern, viewGroup, false);
            glifLayout.findViewById(C0444R.C0448id.lockPattern).setOnTouchListener(C1197xec9f3c79.INSTANCE);
            updateActivityTitle();
            glifLayout.setHeaderText(getActivity().getTitle());
            glifLayout.getHeaderTextView().setAccessibilityLiveRegion(1);
            if (!getResources().getBoolean(C0444R.bool.config_lock_pattern_minimal_ui)) {
                glifLayout.setIcon(getActivity().getDrawable(C0444R.C0447drawable.ic_lock));
            } else if (glifLayout.findViewById(C0444R.C0448id.sud_layout_icon) != null) {
                ((IconMixin) glifLayout.getMixin(IconMixin.class)).setVisibility(8);
            }
            FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
            footerBarMixin.setSecondaryButton(new FooterButton.Builder(getActivity()).setText((int) C0444R.string.lockpattern_tutorial_cancel_label).setListener(new C1196xec9f3c78(this)).setButtonType(0).setTheme(C0444R.style.SudGlifButton_Secondary).build());
            footerBarMixin.setPrimaryButton(new FooterButton.Builder(getActivity()).setText((int) C0444R.string.lockpattern_tutorial_continue_label).setListener(new C1195xec9f3c77(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
            this.mSkipOrClearButton = footerBarMixin.getSecondaryButton();
            this.mNextButton = footerBarMixin.getPrimaryButton();
            return glifLayout;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onCreateView$1(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            view.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            TextView textView = (TextView) view.findViewById(C0444R.C0448id.headerText);
            this.mHeaderText = textView;
            this.mDefaultHeaderColorList = textView.getTextColors();
            LockPatternView findViewById = view.findViewById(C0444R.C0448id.lockPattern);
            this.mLockPatternView = findViewById;
            findViewById.setOnPatternListener(this.mChooseNewLockPatternListener);
            this.mLockPatternView.setFadePattern(false);
            this.mFooterText = (TextView) view.findViewById(C0444R.C0448id.footerText);
            view.findViewById(C0444R.C0448id.topLayout).setDefaultTouchRecepient(this.mLockPatternView);
            boolean booleanExtra = getActivity().getIntent().getBooleanExtra("confirm_credentials", true);
            Intent intent = getActivity().getIntent();
            this.mCurrentCredential = intent.getParcelableExtra("password");
            this.mRequestGatekeeperPassword = intent.getBooleanExtra("request_gk_pw_handle", false);
            if (bundle != null) {
                this.mChosenPattern = bundle.getParcelable("chosenPattern");
                this.mCurrentCredential = bundle.getParcelable("currentPattern");
                updateStage(Stage.values()[bundle.getInt("uiStage")]);
                this.mSaveAndFinishWorker = (SaveAndFinishWorker) getFragmentManager().findFragmentByTag("save_and_finish_worker");
            } else if (booleanExtra) {
                updateStage(Stage.NeedToConfirm);
                if (!new ChooseLockSettingsHelper.Builder(getActivity()).setRequestCode(55).setTitle(getString(C0444R.string.unlock_set_unlock_launch_picker_title)).setReturnCredentials(true).setRequestGatekeeperPasswordHandle(this.mRequestGatekeeperPassword).setUserId(this.mUserId).show()) {
                    updateStage(Stage.Introduction);
                }
            } else {
                updateStage(Stage.Introduction);
            }
        }

        public void onResume() {
            super.onResume();
            updateStage(this.mUiStage);
            if (this.mSaveAndFinishWorker != null) {
                setRightButtonEnabled(false);
                this.mSaveAndFinishWorker.setListener(this);
            }
        }

        public void onPause() {
            super.onPause();
            SaveAndFinishWorker saveAndFinishWorker = this.mSaveAndFinishWorker;
            if (saveAndFinishWorker != null) {
                saveAndFinishWorker.setListener((SaveChosenLockWorkerBase.Listener) null);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            LockscreenCredential lockscreenCredential = this.mCurrentCredential;
            if (lockscreenCredential != null) {
                lockscreenCredential.zeroize();
            }
            System.gc();
            System.runFinalization();
            System.gc();
        }

        /* access modifiers changed from: protected */
        public Intent getRedactionInterstitialIntent(Context context) {
            return RedactionInterstitial.createStartIntent(context, this.mUserId);
        }

        public void handleLeftButton() {
            if (this.mUiStage.leftMode == LeftButtonMode.Retry) {
                LockscreenCredential lockscreenCredential = this.mChosenPattern;
                if (lockscreenCredential != null) {
                    lockscreenCredential.zeroize();
                    this.mChosenPattern = null;
                }
                this.mLockPatternView.clearPattern();
                updateStage(Stage.Introduction);
                return;
            }
            throw new IllegalStateException("left footer button pressed, but stage of " + this.mUiStage + " doesn't make sense");
        }

        public void handleRightButton() {
            Stage stage = this.mUiStage;
            RightButtonMode rightButtonMode = stage.rightMode;
            RightButtonMode rightButtonMode2 = RightButtonMode.Continue;
            if (rightButtonMode == rightButtonMode2) {
                Stage stage2 = Stage.FirstChoiceValid;
                if (stage == stage2) {
                    updateStage(Stage.NeedToConfirm);
                    return;
                }
                throw new IllegalStateException("expected ui stage " + stage2 + " when button is " + rightButtonMode2);
            }
            RightButtonMode rightButtonMode3 = RightButtonMode.Confirm;
            if (rightButtonMode == rightButtonMode3) {
                Stage stage3 = Stage.ChoiceConfirmed;
                if (stage == stage3) {
                    startSaveAndFinish();
                    return;
                }
                throw new IllegalStateException("expected ui stage " + stage3 + " when button is " + rightButtonMode3);
            } else if (rightButtonMode != RightButtonMode.Ok) {
            } else {
                if (stage == Stage.HelpScreen) {
                    this.mLockPatternView.clearPattern();
                    this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    updateStage(Stage.Introduction);
                    return;
                }
                throw new IllegalStateException("Help screen is only mode with ok button, but stage is " + this.mUiStage);
            }
        }

        /* access modifiers changed from: protected */
        public void onSkipOrClearButtonClick(View view) {
            handleLeftButton();
        }

        /* access modifiers changed from: protected */
        public void onNextButtonClick(View view) {
            handleRightButton();
        }

        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            bundle.putInt("uiStage", this.mUiStage.ordinal());
            LockscreenCredential lockscreenCredential = this.mChosenPattern;
            if (lockscreenCredential != null) {
                bundle.putParcelable("chosenPattern", lockscreenCredential);
            }
            LockscreenCredential lockscreenCredential2 = this.mCurrentCredential;
            if (lockscreenCredential2 != null) {
                bundle.putParcelable("currentPattern", lockscreenCredential2.duplicate());
            }
        }

        /* access modifiers changed from: protected */
        public void updateStage(Stage stage) {
            Stage stage2 = this.mUiStage;
            this.mUiStage = stage;
            Stage stage3 = Stage.ChoiceTooShort;
            boolean z = false;
            if (stage == stage3) {
                this.mHeaderText.setText(getResources().getString(stage.headerMessage, new Object[]{4}));
            } else {
                this.mHeaderText.setText(stage.headerMessage);
            }
            GlifLayout glifLayout = (GlifLayout) getActivity().findViewById(C0444R.C0448id.setup_wizard_layout);
            boolean z2 = this.mForFingerprint || this.mForFace || this.mForBiometrics;
            if (z2) {
                int i = stage.messageForBiometrics;
                if (i == -1) {
                    glifLayout.setDescriptionText((CharSequence) "");
                } else {
                    glifLayout.setDescriptionText(i);
                }
            } else {
                glifLayout.getDescriptionTextView().setVisibility(8);
            }
            int i2 = stage.footerMessage;
            if (i2 == -1) {
                this.mFooterText.setText("");
            } else {
                this.mFooterText.setText(i2);
            }
            if (stage == Stage.ConfirmWrong || stage == stage3) {
                TypedValue typedValue = new TypedValue();
                getActivity().getTheme().resolveAttribute(C0444R.attr.colorError, typedValue, true);
                this.mHeaderText.setTextColor(typedValue.data);
            } else {
                ColorStateList colorStateList = this.mDefaultHeaderColorList;
                if (colorStateList != null) {
                    this.mHeaderText.setTextColor(colorStateList);
                }
                if (stage == Stage.NeedToConfirm && z2) {
                    this.mHeaderText.setText("");
                    glifLayout.setHeaderText((int) C0444R.string.lockpassword_draw_your_pattern_again_header);
                }
            }
            updateFooterLeftButton(stage);
            setRightButtonText(stage.rightMode.text);
            setRightButtonEnabled(stage.rightMode.enabled);
            if (stage.patternEnabled) {
                this.mLockPatternView.enableInput();
            } else {
                this.mLockPatternView.disableInput();
            }
            this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
            int i3 = C11921.f171x4f217302[this.mUiStage.ordinal()];
            if (i3 == 1) {
                this.mLockPatternView.clearPattern();
            } else if (i3 != 2) {
                if (i3 == 3) {
                    this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    postClearPatternRunnable();
                } else if (i3 == 5) {
                    this.mLockPatternView.clearPattern();
                } else if (i3 == 6) {
                    this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    postClearPatternRunnable();
                }
                z = true;
            } else {
                this.mLockPatternView.setPattern(LockPatternView.DisplayMode.Animate, this.mAnimatePattern);
            }
            if (stage2 != stage || z) {
                TextView textView = this.mHeaderText;
                textView.announceForAccessibility(textView.getText());
            }
        }

        /* access modifiers changed from: protected */
        public void updateFooterLeftButton(Stage stage) {
            if (stage.leftMode == LeftButtonMode.Gone) {
                this.mSkipOrClearButton.setVisibility(8);
                return;
            }
            this.mSkipOrClearButton.setVisibility(0);
            this.mSkipOrClearButton.setText(getActivity(), stage.leftMode.text);
            this.mSkipOrClearButton.setEnabled(stage.leftMode.enabled);
        }

        private void postClearPatternRunnable() {
            this.mLockPatternView.removeCallbacks(this.mClearPatternRunnable);
            this.mLockPatternView.postDelayed(this.mClearPatternRunnable, 2000);
        }

        private void startSaveAndFinish() {
            if (this.mSaveAndFinishWorker != null) {
                Log.w("ChooseLockPattern", "startSaveAndFinish with an existing SaveAndFinishWorker.");
                return;
            }
            setRightButtonEnabled(false);
            SaveAndFinishWorker saveAndFinishWorker = new SaveAndFinishWorker();
            this.mSaveAndFinishWorker = saveAndFinishWorker;
            saveAndFinishWorker.setListener(this);
            getFragmentManager().beginTransaction().add((Fragment) this.mSaveAndFinishWorker, "save_and_finish_worker").commit();
            getFragmentManager().executePendingTransactions();
            Intent intent = getActivity().getIntent();
            boolean booleanExtra = intent.getBooleanExtra("extra_require_password", true);
            if (intent.hasExtra("unification_profile_id")) {
                LockscreenCredential parcelableExtra = intent.getParcelableExtra("unification_profile_credential");
                try {
                    this.mSaveAndFinishWorker.setProfileToUnify(intent.getIntExtra("unification_profile_id", -10000), parcelableExtra);
                    if (parcelableExtra != null) {
                        parcelableExtra.close();
                    }
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
            }
            this.mSaveAndFinishWorker.start(this.mLockPatternUtils, booleanExtra, this.mRequestGatekeeperPassword, this.mChosenPattern, this.mCurrentCredential, this.mUserId);
            return;
            throw th;
        }

        public void onChosenLockSaveFinished(boolean z, Intent intent) {
            Intent redactionInterstitialIntent;
            getActivity().setResult(1, intent);
            LockscreenCredential lockscreenCredential = this.mChosenPattern;
            if (lockscreenCredential != null) {
                lockscreenCredential.zeroize();
            }
            LockscreenCredential lockscreenCredential2 = this.mCurrentCredential;
            if (lockscreenCredential2 != null) {
                lockscreenCredential2.zeroize();
            }
            if (!z && (redactionInterstitialIntent = getRedactionInterstitialIntent(getActivity())) != null) {
                startActivity(redactionInterstitialIntent);
            }
            getActivity().finish();
        }
    }

    /* renamed from: com.android.settings.password.ChooseLockPattern$1 */
    static /* synthetic */ class C11921 {

        /* renamed from: $SwitchMap$com$android$settings$password$ChooseLockPattern$ChooseLockPatternFragment$Stage */
        static final /* synthetic */ int[] f171x4f217302;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage[] r0 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f171x4f217302 = r0
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.Introduction     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.HelpScreen     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.ChoiceTooShort     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.FirstChoiceValid     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.NeedToConfirm     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.ConfirmWrong     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = f171x4f217302     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.settings.password.ChooseLockPattern$ChooseLockPatternFragment$Stage r1 = com.android.settings.password.ChooseLockPattern.ChooseLockPatternFragment.Stage.ChoiceConfirmed     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.password.ChooseLockPattern.C11921.<clinit>():void");
        }
    }

    public static class SaveAndFinishWorker extends SaveChosenLockWorkerBase {
        private LockscreenCredential mChosenPattern;
        private LockscreenCredential mCurrentCredential;
        private boolean mLockVirgin;

        public /* bridge */ /* synthetic */ void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        }

        public /* bridge */ /* synthetic */ void setBlocking(boolean z) {
            super.setBlocking(z);
        }

        public /* bridge */ /* synthetic */ void setListener(SaveChosenLockWorkerBase.Listener listener) {
            super.setListener(listener);
        }

        public /* bridge */ /* synthetic */ void setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
            super.setProfileToUnify(i, lockscreenCredential);
        }

        public void start(LockPatternUtils lockPatternUtils, boolean z, boolean z2, LockscreenCredential lockscreenCredential, LockscreenCredential lockscreenCredential2, int i) {
            prepare(lockPatternUtils, z, z2, i);
            if (lockscreenCredential2 == null) {
                lockscreenCredential2 = LockscreenCredential.createNone();
            }
            this.mCurrentCredential = lockscreenCredential2;
            this.mChosenPattern = lockscreenCredential;
            this.mUserId = i;
            this.mLockVirgin = !this.mUtils.isPatternEverChosen(i);
            start();
        }

        /* access modifiers changed from: protected */
        public Pair<Boolean, Intent> saveAndVerifyInBackground() {
            int i = this.mUserId;
            boolean lockCredential = this.mUtils.setLockCredential(this.mChosenPattern, this.mCurrentCredential, i);
            if (lockCredential) {
                unifyProfileCredentialIfRequested();
            }
            Intent intent = null;
            if (lockCredential && this.mRequestGatekeeperPassword) {
                VerifyCredentialResponse verifyCredential = this.mUtils.verifyCredential(this.mChosenPattern, i, 1);
                if (!verifyCredential.isMatched() || !verifyCredential.containsGatekeeperPasswordHandle()) {
                    Log.e("ChooseLockPattern", "critical: bad response or missing GK PW handle for known good pattern: " + verifyCredential.toString());
                }
                intent = new Intent();
                intent.putExtra("gk_pw_handle", verifyCredential.getGatekeeperPasswordHandle());
            }
            return Pair.create(Boolean.valueOf(lockCredential), intent);
        }

        /* access modifiers changed from: protected */
        public void finish(Intent intent) {
            if (this.mLockVirgin) {
                this.mUtils.setVisiblePatternEnabled(true, this.mUserId);
            }
            super.finish(intent);
        }
    }
}
