package com.faditor.faditorexample.PaperActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position) {
           case 0:
               return Frag_news01.newInstance();
           case 1:
               return Frag_news02.newInstance();
           case 2:
               return Frag_news03.newInstance();
           case 3:
               return Frag_news04.newInstance();
           case 4:
               return Frag_news05.newInstance();
           case 5:
               return Frag_news06.newInstance();
           default:
               return null;
       }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
