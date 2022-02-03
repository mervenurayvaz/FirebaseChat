package com.alp.chatuygulamasi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> fragmentsName = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout =view.findViewById(R.id.homeFragmentTablayout);
        viewPager2 = view.findViewById(R.id.homeFragmentViewPager2);

        fragments.add(new FrendFragment());
        fragments.add(new UserAllFragment());
        fragmentsName.add("Friend");
        fragmentsName.add("All User");
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity());
        viewPager2.setAdapter(adapter);


        new TabLayoutMediator(tabLayout,viewPager2,
                (tab,position)-> tab.setText(fragmentsName.get(position))).attach();
        return view;
    }


    private class MyViewPagerAdapter extends FragmentStateAdapter{
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }


}