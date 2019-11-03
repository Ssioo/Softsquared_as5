package com.softsqaured.softsquared_as5.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.User;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.softsqaured.softsquared_as5.Objects.UserInfo;
import com.softsqaured.softsquared_as5.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private OAuthLoginButton btnNaverLogin;
    private SignInButton btnGoogleLogin;
    private GoogleSignInClient googleSignInClient;

    private LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;

    private String naverAT;

    private OAuthLogin mOAuthLogin;
    private OAuthLoginHandler mOAuthLoginHandler;

    private KakaoSessionCallback kakaoSessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* findViewById */
        btnNaverLogin = findViewById(R.id.btn_naver_login);
        btnGoogleLogin = findViewById(R.id.btn_google_login);
        btnFacebookLogin = findViewById(R.id.btn_facebook_login);

        /* Kakao Login */
        kakaoSessionCallback = new KakaoSessionCallback();
        Session.getCurrentSession().addCallback(kakaoSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        /* Naver Login */
        mOAuthLogin = OAuthLogin.getInstance();
        mOAuthLogin.init(LoginActivity.this, "UNmUKxnrB2isgc5O1JlW", "gw6l8Dn_DQ", "소프트스퀘어드미세미세");
        mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    final Context mContext = getApplicationContext();
                    // 로그인 인증 성공후 토큰 정보
                    naverAT = mOAuthLogin.getAccessToken(mContext);
                    Log.i("ACCESS TOKEN", naverAT);

                    // 프로필 정보 조회 : json
                    Thread naverThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String naverProfileResult = mOAuthLogin.requestApi(mContext, naverAT, "https://openapi.naver.com/v1/nid/me");
                            Log.i("naverLoginSuccess", naverProfileResult);
                            UserInfo.getInstance().setJSON(naverProfileResult);
                        }
                    });
                    naverThread.start();

                    try {
                        naverThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                } else {
                    String errorCode = mOAuthLogin.getLastErrorCode(getApplicationContext()).getCode();
                    String errorDesc = mOAuthLogin.getLastErrorDesc(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "errorCode: " + errorCode + ", errorDesc: " + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnNaverLogin.setOAuthLoginHandler(mOAuthLoginHandler);

        /* Google Login */

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                Intent signInIntent = GoogleSignIn.getClient(LoginActivity.this , gso).getSignInIntent();
                startActivityForResult(signInIntent, 1010);
            }
        });

        /* Facebook Login Init */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        btnFacebookLogin.setReadPermissions("email", "public_profile");
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // 로그인 성공
                AccessToken facebookAT = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(facebookAT, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        UserInfo userInfo = UserInfo.getInstance();


                        try {
                            Log.i("FacebookRequest", object.toString());
                            userInfo.setName(object.getString("name"));
                            userInfo.setEmail(object.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1010) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // SignIn Success -> move to MainActivity
                UserInfo.getInstance().setEmail(account.getEmail());
                UserInfo.getInstance().setName(account.getFamilyName() + account.getGivenName());
                UserInfo.getInstance().setProfileUrl(account.getPhotoUrl().toString());
                finish();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakaoSessionCallback);
    }

    private class KakaoSessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }
}
