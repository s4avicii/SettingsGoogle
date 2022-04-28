package com.android.settings.users;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.NewUserRequest;
import android.os.NewUserResponse;
import android.os.UserManager;
import android.view.View;
import androidx.window.C0444R;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AddSupervisedUserActivity extends Activity {
    private ActivityManager mActivityManager;
    private UserManager mUserManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUserManager = (UserManager) getSystemService(UserManager.class);
        this.mActivityManager = (ActivityManager) getSystemService(ActivityManager.class);
        setContentView(C0444R.C0450layout.add_supervised_user);
        findViewById(C0444R.C0448id.createSupervisedUser).setOnClickListener(new AddSupervisedUserActivity$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        createUser();
    }

    private void createUserAsync(NewUserRequest newUserRequest, Consumer<NewUserResponse> consumer) {
        Objects.requireNonNull(consumer);
        Executors.newSingleThreadExecutor().execute(new AddSupervisedUserActivity$$ExternalSyntheticLambda1(this, newUserRequest, new Handler(Looper.getMainLooper()), consumer));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createUserAsync$2(NewUserRequest newUserRequest, Handler handler, Consumer consumer) {
        handler.post(new AddSupervisedUserActivity$$ExternalSyntheticLambda2(consumer, this.mUserManager.createUser(newUserRequest)));
    }

    private void createUser() {
        NewUserRequest build = new NewUserRequest.Builder().setName(getString(C0444R.string.user_new_user_name)).build();
        AlertDialog create = new AlertDialog.Builder(this).setMessage(getString(C0444R.string.creating_new_user_dialog_message)).setCancelable(false).create();
        create.show();
        createUserAsync(build, new AddSupervisedUserActivity$$ExternalSyntheticLambda3(this, create));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createUser$3(AlertDialog alertDialog, NewUserResponse newUserResponse) {
        alertDialog.dismiss();
        if (newUserResponse.isSuccessful()) {
            this.mActivityManager.switchUser(newUserResponse.getUser());
            finish();
            return;
        }
        AlertDialog.Builder title = new AlertDialog.Builder(this).setTitle(getString(C0444R.string.add_user_failed));
        title.setMessage(UserManager.UserOperationResult.class.getName() + " = " + newUserResponse.getOperationResult()).setNeutralButton(getString(C0444R.string.okay), (DialogInterface.OnClickListener) null).show();
    }
}
