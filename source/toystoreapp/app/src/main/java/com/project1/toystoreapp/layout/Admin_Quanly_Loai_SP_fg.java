    package com.project1.toystoreapp.layout;

    import android.os.Bundle;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.core.content.res.ResourcesCompat;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
    import com.project1.toystoreapp.Activities.Admin_screen;
    import com.project1.toystoreapp.R;
    import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_LSP_adapter;
    import com.project1.toystoreapp.model.LoaiSP;

    import java.util.ArrayList;
    import java.util.List;

    import www.sanju.motiontoast.MotionToast;
    import www.sanju.motiontoast.MotionToastStyle;


    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link Admin_Quanly_Loai_SP_fg#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class Admin_Quanly_Loai_SP_fg extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        Admin_QL_LSP_adapter adminQlLspAdapter;
        public Admin_Quanly_Loai_SP_fg() {
            // Required empty public constructor
        }
        public void addLSP(LoaiSP loaiSP){

        }
        public static Admin_Quanly_Loai_SP_fg newInstance(String param1, String param2) {
            Admin_Quanly_Loai_SP_fg fragment = new Admin_Quanly_Loai_SP_fg();
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
        RecyclerView recyclerView;
        LoaiSPEndpoint loaiSPEndpoint;
        ProgressBar progressBar;
        List<LoaiSP> list =new ArrayList<>();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_admin_quanly_loai_sp_fg,container,false);
            recyclerView= view.findViewById(R.id.recyclerview);
            progressBar = view.findViewById(R.id.progress);
            loaiSPEndpoint = new LoaiSPEndpoint();
            return view;
        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            progressBar.setVisibility(View.VISIBLE);
            refresh();

        }

        public void refresh(){
            loaiSPEndpoint.getAllLoaiSP(loaiSPS -> {
                if (loaiSPS.size() > 0) {
                    list = loaiSPS;
                    getActivity().runOnUiThread(() -> {
                        adminQlLspAdapter = new Admin_QL_LSP_adapter(getContext(), list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        recyclerView.setAdapter(adminQlLspAdapter);
                        progressBar.setVisibility(View.GONE);

                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                            MotionToast.Companion.createToast(getActivity(),
                                    "",
                                    "No data found",
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                            progressBar.setVisibility(View.GONE);
                    });
                }
            });
        }

    }