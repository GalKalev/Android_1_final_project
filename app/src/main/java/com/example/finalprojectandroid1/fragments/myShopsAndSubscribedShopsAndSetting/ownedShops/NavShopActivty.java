//package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.navigation.fragment.NavHostFragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.finalprojectandroid1.R;
//import com.example.finalprojectandroid1.activities.ShopInfoActivity;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link NavShopActivty#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class NavShopActivty extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public NavShopActivty() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment NavShopActivty.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static NavShopActivty newInstance(String param1, String param2) {
//        NavShopActivty fragment = new NavShopActivty();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    String TAG = "NavShopActivty";
//    int isOwner;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
////        View view = inflater.inflate(R.layout.fragment_nav_shop_activty, container, false);
////        ShopInfoActivity shopInfoActivity = (ShopInfoActivity) getActivity();
////
////        isOwner = shopInfoActivity.getIsOwner();
////
////
////
////        if(isOwner == 1){
////            Navigation.findNavController(view).navigate(R.id.action_navShopActivty_to_ownedShopStats);
////        }else{
////            Navigation.findNavController(view).navigate(R.id.action_navShopActivty_to_notOwnedShopStats);
////        }
////        return view;
//    }

//}