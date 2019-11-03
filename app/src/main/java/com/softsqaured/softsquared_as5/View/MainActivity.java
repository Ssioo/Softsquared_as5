package com.softsqaured.softsquared_as5.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.softsqaured.softsquared_as5.Adapter.MainPagerAdapter;
import com.softsqaured.softsquared_as5.Objects.UserInfo;
import com.softsqaured.softsquared_as5.Room.LocationModel;
import com.softsqaured.softsquared_as5.R;
import com.softsqaured.softsquared_as5.Objects.GeneralLocation;
import com.softsqaured.softsquared_as5.Room.MyLocation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager vpMain;
    public DrawerLayout dlMain;
    private MainPagerAdapter mpAdapter;
    private TextView tvLoginUserId;
    private TextView tvUserEmail;
    private ImageView ivLogout;
    private ImageView ivProfile;
    private TextView tvLogout;

    public UserInfo userInfo = UserInfo.getInstance();

    private GoogleSignInClient googleSignInClient;

    public ArrayList<MyLocation> myLocationList = new ArrayList<>();
    private LocationModel locationModel;
    public ArrayList<GeneralLocation> generalLocationList = new ArrayList<>();

    private void initLocation() {
        generalLocationList.add(new GeneralLocation("서울특별시 강남구", 37.517360, 127.047451));
        generalLocationList.add(new GeneralLocation("서울특별시 강동구", 37.530270, 127.123794));
        generalLocationList.add(new GeneralLocation("서울특별시 강북구", 37.639775, 127.025514));
        generalLocationList.add(new GeneralLocation("서울특별시 강서구", 37.550926, 126.849573));
        generalLocationList.add(new GeneralLocation("서울특별시 관악구", 37.478126, 126.951501));
        generalLocationList.add(new GeneralLocation("서울특별시 광진구", 37.538523, 127.082364));
        generalLocationList.add(new GeneralLocation("서울특별시 구로구", 37.495506, 126.887643));
        generalLocationList.add(new GeneralLocation("서울특별시 금천구", 37.456759, 126.895369));
        generalLocationList.add(new GeneralLocation("서울특별시 노원구", 37.654072, 127.056318));
        generalLocationList.add(new GeneralLocation("서울특별시 도봉구", 37.668906, 127.046996));
        generalLocationList.add(new GeneralLocation("서울특별시 동대문구", 37.574398, 127.039753));
        generalLocationList.add(new GeneralLocation("서울특별시 동작구", 37.512444, 126.939801));
        generalLocationList.add(new GeneralLocation("서울특별시 마포구", 37.566209, 126.901634));
        generalLocationList.add(new GeneralLocation("서울특별시 서대문구", 37.579172, 126.936789));
        generalLocationList.add(new GeneralLocation("서울특별시 서초구", 37.483576, 127.032655));
        generalLocationList.add(new GeneralLocation("서울특별시 성동구", 37.563087, 127.036665));
        generalLocationList.add(new GeneralLocation("서울특별시 성북구", 37.589359, 127.016742));
        generalLocationList.add(new GeneralLocation("서울특별시 송파구", 37.514456, 127.106075));
        generalLocationList.add(new GeneralLocation("서울특별시 양천구", 37.516958, 126.866564));
        generalLocationList.add(new GeneralLocation("서울특별시 영등포구", 37.526333, 126.896252));
        generalLocationList.add(new GeneralLocation("서울특별시 용산구", 37.532607, 126.990043));
        generalLocationList.add(new GeneralLocation("서울특별시 은평구", 37.602755, 126.929231));
        generalLocationList.add(new GeneralLocation("서울특별시 종로구", 37.572798, 126.979173));
        generalLocationList.add(new GeneralLocation("서울특별시 중구", 37.563759, 126.997543));
        generalLocationList.add(new GeneralLocation("서울특별시 중랑구", 37.606555, 127.092625));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* findViewByID */
        vpMain = findViewById(R.id.viewpager_main);
        dlMain = findViewById(R.id.drawer_main);
        tvLoginUserId = findViewById(R.id.text_userid_main);
        tvUserEmail = findViewById(R.id.text_useremail_main);
        ivLogout = findViewById(R.id.iv_logout_drawer);
        ivProfile = findViewById(R.id.iv_profile_main);
        tvLogout = findViewById(R.id.text_logout_main);


        /* General Locaitons*/
        initLocation();

        /* MyLocation Database */
        locationModel = new LocationModel(MainActivity.this);

        /* Initial Place */
        locationModel.addLocation(generalLocationList.get(0));

        /* Set on Click Listener */
        ivLogout.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvLoginUserId.setOnClickListener(this);
        tvUserEmail.setOnClickListener(this);

        /* Init View */
        ivLogout.setVisibility(View.INVISIBLE);
        tvLogout.setVisibility(View.INVISIBLE);
        ivProfile.setClipToOutline(true);

        /* Google Login Init */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    protected void onStart() {
        super.onStart();

        /* Google 로그인 되어있는지 확인 */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (!userInfo.getName().equals("")) {
            KakaoRequestMeNotLogin(); // Kakao 로그인 확인
            loginSuccessSetView();
        }
        if (account != null) {
            userInfo.setName(account.getFamilyName() + account.getGivenName());
            userInfo.setEmail(account.getEmail());
            userInfo.setProfileUrl(account.getPhotoUrl().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* ViewPager Adpater */
        Log.i("MainActivity", "onResume: viewpagerAdapter");

        /* MyLocation DataSet for Searching Mise Data */
        myLocationList = locationModel.getAllLocations();
        mpAdapter = new MainPagerAdapter(this, myLocationList);
        vpMain.setAdapter(mpAdapter);


    }

    @Override
    public void onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_userid_main:
            case R.id.text_useremail_main:
            case R.id.iv_profile_main:
                Log.i("Click Event", "text_userid_main");
                KakaoRequestMeAndLogin();
                break;

            case R.id.iv_logout_drawer:
            case R.id.text_logout_main:
                new AlertDialog.Builder(this)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KakaoLogout();
                        NaverLogout();
                        GoogleLogout();
                        LoginManager.getInstance().logOut();
                        userInfo.clear();
                        /* Set View */
                        tvLoginUserId.setText("로그인 하기");
                        tvUserEmail.setText("로그인 해주세요");
                        ivLogout.setVisibility(View.INVISIBLE);
                        tvLogout.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                        dlMain.closeDrawers();
                        ivProfile.setImageResource(R.drawable.kakao_default_profile_image);
                        tvLoginUserId.setEnabled(true);
                        tvUserEmail.setEnabled(true);
                        ivProfile.setEnabled(true);
                        dialog.dismiss();
                    }
                })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();

                break;
        }
    }

    private void NaverLogout() {
        OAuthLogin.getInstance().logout(this);
    }

    private void GoogleLogout() {
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void KakaoLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                // 로그아웃
            }

        });
    }

    private void KakaoRequestMeNotLogin() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(MeV2Response result) {
                // 로그인 정보 있음. -> 계정 정보 획득
                userInfo.setEmail(result.getKakaoAccount().getEmail());
                userInfo.setName(result.getKakaoAccount().getProfile().getNickname());
                userInfo.setProfileUrl(result.getKakaoAccount().getProfile().getProfileImageUrl());

                // 로그인 성공
                loginSuccessSetView();
            }
        });
    }

    private void KakaoRequestMeAndLogin() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                // 세션이 종료되어 있으면 로그인 액티비티로 이동.
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSuccess(MeV2Response result) {
                // 로그인 정보 있음. -> 계정 정보 획득
                userInfo.setEmail(result.getKakaoAccount().getEmail());
                userInfo.setName(result.getKakaoAccount().getProfile().getNickname());
                userInfo.setProfileUrl(result.getKakaoAccount().getProfile().getProfileImageUrl());

                // 로그인 성공
                loginSuccessSetView();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Logger.d(String.valueOf(errorResult));
            }
        });
    }

    private void loginSuccessSetView() {
        tvLoginUserId.setText(userInfo.getName());
        tvUserEmail.setText(userInfo.getEmail());
        ivLogout.setVisibility(View.VISIBLE);
        tvLogout.setVisibility(View.VISIBLE);
        Glide.with(MainActivity.this).load(userInfo.getProfileUrl()).into(ivProfile);
        tvLoginUserId.setEnabled(false);
        tvUserEmail.setEnabled(false);
        ivProfile.setEnabled(false);
    }

}
