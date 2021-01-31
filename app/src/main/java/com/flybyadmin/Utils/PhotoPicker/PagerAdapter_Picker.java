/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.flybyadmin.Utils.PhotoPicker;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flybyadmin.appusekotlin.R;


public class PagerAdapter_Picker extends FragmentPagerAdapter {


    String[] tab_titles;


    public PagerAdapter_Picker(Context context, FragmentManager fm) {
        super(fm);
            tab_titles = context.getResources().getStringArray(R.array.tab_titles);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles[position];
    }

    @Override
    public int getCount() {
        return tab_titles.length;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                CwacCameraFragment profileInfoFragment = new CwacCameraFragment();
                CwacCameraFragment.setConfig(ImagePickerActivity.getConfig());
                return profileInfoFragment;


            case 1:
                return new GalleryFragment();


            default:
                return null;
        }


    }


}
