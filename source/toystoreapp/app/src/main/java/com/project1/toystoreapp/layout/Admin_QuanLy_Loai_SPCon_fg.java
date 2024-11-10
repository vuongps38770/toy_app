package com.project1.toystoreapp.layout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.Activities.Admin_qlSP;
import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_LSP_SPCON_outter_adapter;
import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_LSP_adapter;
import com.project1.toystoreapp.databinding.FragmentAdminQuanlyLoaiSpconFgBinding;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Admin_QuanLy_Loai_SPCon_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Admin_QuanLy_Loai_SPCon_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Admin_QuanLy_Loai_SPCon_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Admin_QuanLy_Loai_SPCon_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static Admin_QuanLy_Loai_SPCon_fg newInstance(String param1, String param2) {
        Admin_QuanLy_Loai_SPCon_fg fragment = new Admin_QuanLy_Loai_SPCon_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    FragmentAdminQuanlyLoaiSpconFgBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    LoaiSPEndpoint loaiSPEndpoint;
    List<LoaiSP> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAdminQuanlyLoaiSpconFgBinding.inflate(inflater,container,false);
        loaiSPEndpoint = new LoaiSPEndpoint();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaiSPEndpoint.getAllLoaiSPPopulate(loaiSPS -> {
            if(loaiSPS.size()>0){
                list = loaiSPS;
                getActivity().runOnUiThread(() -> {
                    Admin_QL_LSP_SPCON_outter_adapter adapter = new Admin_QL_LSP_SPCON_outter_adapter(list, getContext(), new OnItemOnclicked() {
                        @Override
                        public <T> void onClickedItem(T item) {
                            if(item instanceof LoaiSPCon){
                                LoaiSPCon loaiSPCon = (LoaiSPCon) item;

                                Intent intent = new Intent(getContext(), Admin_qlSP.class);
                                intent.putExtra("IDSPCON",loaiSPCon.getId());
                                startActivity(intent);
                            }
                        }
                    });
                    binding.recyclerviewLsp.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    binding.recyclerviewLsp.setAdapter(adapter);
                });
            }else {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }


}