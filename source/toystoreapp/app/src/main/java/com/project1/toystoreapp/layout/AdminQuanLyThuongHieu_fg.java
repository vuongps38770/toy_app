package com.project1.toystoreapp.layout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project1.toystoreapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminQuanLyThuongHieu_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminQuanLyThuongHieu_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminQuanLyThuongHieu_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminQuanLyThuongHieu_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminQuanLyThuongHieu_fg newInstance(String param1, String param2) {
        AdminQuanLyThuongHieu_fg fragment = new AdminQuanLyThuongHieu_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_quan_ly_thuong_hieu_fg, container, false);
    }
}