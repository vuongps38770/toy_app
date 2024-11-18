package com.project1.toystoreapp.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project1.toystoreapp.Activities.Admin_QL_Banner;
import com.project1.toystoreapp.Activities.Admin_screen;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.FragmentAdminQuanlyAllFgBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Admin_QuanLy_all_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Admin_QuanLy_all_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Admin_QuanLy_all_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Admin_QuanLy_all_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static Admin_QuanLy_all_fg newInstance(String param1, String param2) {
        Admin_QuanLy_all_fg fragment = new Admin_QuanLy_all_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    FragmentAdminQuanlyAllFgBinding binding;
    Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    String[] texts = {"Chào mừng!", "Xem thống kê...", "Duyệt đơn hàng..."};
    int currentIndex = 0;
    Runnable animationRunnable;

    private void setTitle() {
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                if(!isAdded()) return;
                animateTextView();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(animationRunnable);
    }
    private void animateTextView() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(binding.title, "alpha", 1f, 0f);
        ObjectAnimator moveDown = ObjectAnimator.ofFloat(binding.title, "translationY", 0f, 100f);
        AnimatorSet setDown = new AnimatorSet();
        setDown.playTogether(fadeOut, moveDown);
        setDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.title.setText(texts[currentIndex]);
                currentIndex = (currentIndex + 1) % texts.length;
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.title, "alpha", 0f, 1f);
                ObjectAnimator moveUp = ObjectAnimator.ofFloat(binding.title, "translationY", 100f, 0f);

                AnimatorSet setUp = new AnimatorSet();
                setUp.playTogether(fadeIn, moveUp);
                setUp.start();
            }
        });
        setDown.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (animationRunnable != null) {
            handler.removeCallbacks(animationRunnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (animationRunnable != null) {
            handler.removeCallbacks(animationRunnable);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (animationRunnable != null) {
            handler.removeCallbacks(animationRunnable);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAdminQuanlyAllFgBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler.postDelayed(()->setTitle(),5000);
        binding.qlsp.setOnClickListener(v -> {
            ((Admin_screen) getActivity()).setFragment(new Admin_Quanly_Loai_SP_fg(),true);
        });
        binding.qlylspcon.setOnClickListener(v -> {
            ((Admin_screen) getActivity()).setFragment(new Admin_QuanLy_Loai_SPCon_fg(),true);
        });
        binding.QlyThuongHieu.setOnClickListener(v -> {
            ((Admin_screen) getActivity()).setFragment(new AdminQuanLyThuongHieu_fg(),true);
        });
        binding.qlyBanner.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Admin_QL_Banner.class);
            startActivity(intent);
        });
        binding.qlysp.setOnClickListener(v -> {
            ((Admin_screen) getActivity()).setFragment(new Admin_QuanLy_SP_fg(),true);
        });

    }
}