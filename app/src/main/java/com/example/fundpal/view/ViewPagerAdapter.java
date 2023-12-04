package com.example.fundpal.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fundpal.view.fragment.pemasukan.PemasukanFragment;
import com.example.fundpal.view.fragment.pengeluaran.PengeluaranFragment;
import com.example.fundpal.view.fragment.statistik.StatistikFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0) {
            fragment = new PengeluaranFragment();
        } else if (position == 1) {
            fragment = new PemasukanFragment();
        } else if (position == 2) {
            fragment = new StatistikFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        String strTitle = "";
//        if (position == 0) {
//            strTitle = "Pengeluaran";
//        } else if (position == 1) {
//            strTitle = "Pemasukan";
//        } else if (position == 2) {
//            strTitle = "Statistik";
//        }
//        return strTitle;
//    }
}
