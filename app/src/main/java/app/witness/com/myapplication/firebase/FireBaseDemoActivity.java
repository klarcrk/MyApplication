/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.main.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FireBaseDemoActivity extends BaseActivity {
    private static final int REQUEST_INVITE = 121;
    @BindView(R.id.btnMakeCoffee)
    Button btnMakeCoffee;
    @BindView(R.id.tvCoffee)
    TextView tvCoffee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Title")
                .setMessage(".....msg")
                .setDeepLink(Uri.parse("https://fb.me/1028512843838503"))
                .setCustomImage(Uri.parse("https://firebase.google.com/_static/bd1d68766c/images/firebase/lockup.png"))
                .setCallToActionText("invate")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("tag", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

    @OnClick(R.id.btnMakeCoffee)
    public void onClick() {
        // [START subscribe_topics]

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("tag", "Subscribed to news topic");
        // [END subscribe_topics]
        Log.d("tag", "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        tvCoffee.append("InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        tvCoffee.append("\n");
        onInviteClicked();
    }
}
