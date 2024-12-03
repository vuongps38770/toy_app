package com.project1.toystoreapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.API_end_points.UserEndpoint;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityForgotPwBinding;
import com.project1.toystoreapp.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ForgotPW extends AppCompatActivity {
    ActivityForgotPwBinding binding;
    UserEndpoint userEndpoint;
    User[] dummy= {null};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPwBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userEndpoint = new UserEndpoint();
        Glide.with(binding.img).load(R.drawable.gif).into(binding.img);
        animateImage(binding.img);
        binding.code1.addTextChangedListener(textWatcher);
        binding.code2.addTextChangedListener(textWatcher);
        binding.code3.addTextChangedListener(textWatcher);
        binding.code4.addTextChangedListener(textWatcher);
        binding.btnXacnhan.setVisibility(View.GONE);
        binding.codeView.setVisibility(View.GONE);
        binding.back.setVisibility(View.GONE);
        binding.backtologin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPW.this, Login.class);
            startActivity(intent);
            finish();
        });
        binding.backtoemail.setOnClickListener(v -> {
            animateout(binding.xnmkcontainer);
            animateout(binding.mkcontainer);
            animateout(binding.btnxnmk);
            animateout(binding.backtoemail);
            animatein(binding.brnsend);
            animatein(binding.backtologin);
            animatein(binding.emailcontainer);

        });
        binding.back.setOnClickListener(v -> {
            animateout(binding.btnXacnhan);
            animateout(binding.codeView);
            animateout(binding.back);
            animatein(binding.brnsend);
            animatein(binding.backtologin);
            animatein(binding.emailcontainer);
        });
        binding.brnsend.setOnClickListener(v -> {
            if(binding.email.getText().toString().trim().isEmpty()){
                binding.email.requestFocus();
                binding.emailcontainer.setError("Vui lòng nhập email");
                return;
            }
            String emailregx="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailregx);
            Matcher matcher=pattern.matcher(binding.email.getText().toString());
            if(!matcher.matches()){
                binding.email.requestFocus();
                binding.emailcontainer.setError("Sai định dạng email");
                return;
            }
            binding.progress.setVisibility(View.VISIBLE);
            userEndpoint.findAccountByEmail(binding.email.getText().toString(), new UserEndpoint.FindUserByEmailListener() {
                @Override
                public void onSucsess(User user) {
                    dummy[0]=user;
                    sendEmail();

                }

                @Override
                public void onFailure() {
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Lỗi!",
                            "Có lỗi xảy ra không thể tìm!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    binding.progress.setVisibility(View.GONE);
                }

                @Override
                public void onErrorr() {
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Lỗi!",
                            "Không thể kết nối với máy chủ",
                            MotionToastStyle.NO_INTERNET,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    binding.progress.setVisibility(View.GONE);
                }

                @Override
                public void onNotFound() {
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Không tìm thấy!",
                            "Tài khoản không tồn tại!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    binding.progress.setVisibility(View.GONE);
                }
            });

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail(){
        userEndpoint.sendEmail(dummy[0], new UserEndpoint.SendEmaillistener() {
            @Override
            public void onSucsess(String code) {
                binding.progress.setVisibility(View.GONE);
                gotoCodePage();
                codepage(code);
            }

            @Override
            public void onFailure() {
                MotionToast.Companion.createToast(ForgotPW.this,
                        "Lỗi!",
                        "Có lỗi xảy ra, không thể gửi email!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onErrorr() {
                MotionToast.Companion.createToast(ForgotPW.this,
                        "Lỗi!",
                        "Không thể kết nối với máy chủ",
                        MotionToastStyle.NO_INTERNET,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    ///setupverifyCodePage
    private void codepage (String code){
        binding.progress.setVisibility(View.GONE);
        binding.btnXacnhan.setOnClickListener(v-> {
            if (binding.code1.getText().toString().isEmpty()
                    || binding.code2.getText().toString().isEmpty()
                    || binding.code3.getText().toString().isEmpty()
                    || binding.code4.getText().toString().isEmpty()) {
                MotionToast.Companion.createToast(ForgotPW.this,
                        "Nhập thiếu!",
                        "Vui lòng nhập mã xác nhận",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            String codein=binding.code1.getText().toString().trim()+binding.code2.getText().toString().trim()+binding.code3.getText().toString().trim()+binding.code4.getText().toString().trim();
            if(!codein.equals(code)){
                Log.e("onSucsess: ", codein);
                MotionToast.Companion.createToast(ForgotPW.this,
                        "Sai mã!",
                        "Mã không hợp lệ",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            gotoResetPWpage();
            resetPage();
        });
    }

    ///setupResetPWpage
    private void resetPage() {
        binding.btnxnmk.setOnClickListener(v -> {

            if(binding.mk.getText().toString().isEmpty()){
                binding.mkcontainer.setError("Vui lòng nhập mật khẩu");
                return;
            }
            if(binding.xnmk.getText().toString().isEmpty()){
                binding.xnmkcontainer.setError("Vui lòng xác nhận mật khẩu");
                return;
            }
            if(!binding.mk.getText().toString().equals(binding.xnmk.getText().toString())){
                binding.mkcontainer.setError("Mật khẩu xác nhận sai");
                return;
            }
            binding.progress.setVisibility(View.VISIBLE);
            dummy[0].setPassword(binding.mk.getText().toString());
            userEndpoint.changePW(dummy[0], new UserEndpoint.ChangePWListener() {
                @Override
                public void onSucsess() {
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Thành công!",
                            "Bạn đã đổi mật khẩu thành công.",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    binding.progress.setVisibility(View.GONE);
                    binding.btnxnmk.setText("Về trang đăng nhập");
                    binding.btnxnmk.setOnClickListener(v1 -> {
                        Intent i = new Intent(ForgotPW.this, Login.class);
                        startActivity(i);
                        finish();
                    });
                }
                @Override
                public void onFailure() {
                    binding.progress.setVisibility(View.GONE);
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Có lỗi xảy ra",
                            "Không thể đổi mật khẩu",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }

                @Override
                public void onErrorr() {
                    binding.progress.setVisibility(View.GONE);
                    MotionToast.Companion.createToast(ForgotPW.this,
                            "Lỗi!",
                            "Không thể kết nối với máy chủ",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(ForgotPW.this, www.sanju.motiontoast.R.font.helvetica_regular));

                }
            });
        });
    }

    private void gotoResetPWpage() {
        animateout(binding.btnXacnhan);
        animateout(binding.codeView);
        animateout(binding.back);

        animatein(binding.xnmkcontainer);
        animatein(binding.mkcontainer);
        animatein(binding.btnxnmk);
        animatein(binding.backtoemail);
    }
    private void gotoCodePage() {
        animateout(binding.brnsend);
        animateout(binding.backtologin);
        animateout(binding.emailcontainer);

        animatein(binding.btnXacnhan);
        animatein(binding.codeView);
        animatein(binding.back);
    }
    private boolean isDeleting=false;
    private final TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isDeleting = count>0;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 1) {
                Editable editable = (Editable) s;
                editable.replace(1, editable.length(), "");
            }

            if (s.length() == 1) {
                isDeleting = false;
                if (binding.code1.hasFocus()) {
                    binding.code2.requestFocus();
                } else if (binding.code2.hasFocus()) {
                    binding.code3.requestFocus();
                } else if (binding.code3.hasFocus()) {
                    binding.code4.requestFocus();
                }
            } else if (s.length() == 0 && isDeleting) {
                if (binding.code4.hasFocus()) {
                    binding.code3.requestFocus();
                } else if (binding.code3.hasFocus()) {
                    binding.code2.requestFocus();
                } else if (binding.code2.hasFocus()) {
                    binding.code1.requestFocus();
                }
                isDeleting = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
    private void animateout(View v) {
        v.setVisibility(View.GONE);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);
        ObjectAnimator moveDown = ObjectAnimator.ofFloat(v, "translationY", 0f, 100f);
        moveDown.setDuration(1000);
        AnimatorSet setDown = new AnimatorSet();
        setDown.playTogether(fadeOut, moveDown);
        setDown.start();
    }
    private void animatein(View v) {
        v.setVisibility(View.VISIBLE);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(v, "translationY", 100f, 0f);
        moveUp.setDuration(1000);
        AnimatorSet setUp = new AnimatorSet();
        setUp.playTogether(fadeIn, moveUp);
        setUp.start();
    }
    private void animateImage(View imageView) {
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator moveLeftToRight = ObjectAnimator.ofFloat(imageView, "translationX", -screenWidth, screenWidth);
        moveLeftToRight.setDuration(16000);
        moveLeftToRight.setRepeatCount(ObjectAnimator.INFINITE);
        moveLeftToRight.setRepeatMode(ObjectAnimator.RESTART);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveLeftToRight);
        animatorSet.start();
    }
}