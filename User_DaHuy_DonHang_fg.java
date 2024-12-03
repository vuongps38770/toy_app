package com.project1.toystoreapp.layout.user_DonHang_proccess_fgs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project1.toystoreapp.API_end_points.DonHangEndPoint;
import com.project1.toystoreapp.Enum.DonHangStatus;
import com.project1.toystoreapp.RecyclerAdapters.User_Bill_adapter;
import com.project1.toystoreapp.databinding.FragmentUserDaHuyDonHangFgBinding;
import com.project1.toystoreapp.model.DonHang;
import com.project1.toystoreapp.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User_DaHuy_DonHang_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_DaHuy_DonHang_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public User_DaHuy_DonHang_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_DaHuy_DonHang_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static User_DaHuy_DonHang_fg newInstance(String param1, String param2) {
        User_DaHuy_DonHang_fg fragment = new User_DaHuy_DonHang_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    List<DonHang> list;

    DonHangEndPoint donHangEndPoint;
    private User user;
    User_Bill_adapter.OnItemclickedListenner listenner;
    public void setListenner(User_Bill_adapter.OnItemclickedListenner listenner) {
        this.listenner = listenner;
    }
    public User_DaHuy_DonHang_fg(User user) {
        this.user = user;
    }
    FragmentUserDaHuyDonHangFgBinding binding;
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
        binding =FragmentUserDaHuyDonHangFgBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donHangEndPoint = new DonHangEndPoint();
        Log.e( "createFragment: u",user.getId() );
        loading(true);
        donHangEndPoint.getAllDonHangByUserID(user.getId(), new DonHangEndPoint.getListDonHangListenner() {
            @Override
            public void onSucsess(List<DonHang> donHang) {

                if(getActivity() == null || getActivity().isFinishing()){
//                    Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                    loading(false);
                    return;
                }
                Log.e("onSucsess: ",donHang.size()+"");
                getActivity().runOnUiThread(() -> {
                    if(donHang==null) {
                        binding.checkEmty.setVisibility(View.VISIBLE);
                        loading(false);
                        return;
                    }
                    list = donHang.stream().filter(donHang1 ->
                            Objects.equals(donHang1.getStatus(), DonHangStatus.CANCELLED.name())
                    ).collect(Collectors.toList());
                    if(list.isEmpty()){
                        loading(false);
                        binding.checkEmty.setVisibility(View.VISIBLE);
                        return;
                    }
                    loading(false);
                    User_Bill_adapter adapter = new User_Bill_adapter(getActivity(),list);
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    binding.recyclerview.setAdapter(adapter);
                    adapter.setListenner(listenner);
                    Log.e("onSucsess: ",list.size()+"");
                });
            }

            @Override
            public void onFailure() {
                loading(false);
            }

            @Override
            public void onError() {
                loading(false);
            }
        });
    }
    private void loading(boolean isLoad){
        binding.imgoverlay.setVisibility(isLoad?View.VISIBLE:View.GONE);
    }
}