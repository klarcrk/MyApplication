package app.witness.com.myapplication.googleauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.main.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleAuthActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.btnMakeCoffee)
    Button btnMakeCoffee;
    @BindView(R.id.tvCoffee)
    TextView tvCoffee;

    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
        mGson = new Gson();
    }

    GoogleApiClient apiClient;

    @OnClick(R.id.btnMakeCoffee)
    public void onClick() {
        if (apiClient == null) {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .requestId()
                    .build();
            apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                    .build();
        }
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String errorMessage = connectionResult.getErrorMessage();
        String error = mGson.toJson(connectionResult);
        tvCoffee.append("\n");
        tvCoffee.append(error);
    }

    private final int RC_SIGN_IN = 1212;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount signInAccount = result.getSignInAccount();
                String displayName = signInAccount.getDisplayName();
                String email = signInAccount.getEmail();
                Uri photoUrl = signInAccount.getPhotoUrl();
                String serverAuthCode = signInAccount.getServerAuthCode();
                String id = signInAccount.getId();
                String idToken = signInAccount.getIdToken();
                String zzafm = signInAccount.zzafm();
            }
            String resultData = mGson.toJson(result);
            tvCoffee.append("\n");
            tvCoffee.append(resultData);
        }
    }
}
