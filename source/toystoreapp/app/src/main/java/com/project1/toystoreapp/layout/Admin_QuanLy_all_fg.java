package com.project1.toystoreapp.layout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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