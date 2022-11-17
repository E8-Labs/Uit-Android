package com.antizon.uit_android.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    List<Fragment> fragmentList;

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity activity, List<Fragment> list) {
        super(activity);
        fragmentList = list;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
