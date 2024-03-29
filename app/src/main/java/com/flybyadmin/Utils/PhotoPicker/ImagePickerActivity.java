/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.flybyadmin.Utils.PhotoPicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.flybyadmin.Utils.PhotoPicker.custom.adapter.SpacesItemDecoration;
import com.flybyadmin.Utils.PhotoPicker.util.Util;
import com.flybyadmin.Utils.ToastUtils;
import com.flybyadmin.appusekotlin.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImagePickerActivity extends AppCompatActivity implements CameraHostProvider {

    /**
     * Returns the parcelled image uris in the intent with this extra.
     */
    public static final String EXTRA_IMAGE_URIS = "image_uris";
    public static CwacCameraFragment.MyCameraHost mMyCameraHost;
    // initialize with default config.
    private static Config mConfig = new Config();
    /**
     * Key to persist the list when saving the state of the activity.
     */
    int Select_Max = 0;
    public ArrayList<Uri> mSelectedImages;

    View view_root;
    TextView mSelectedImageEmptyMessage;
    View view_selected_photos_container;
    RecyclerView rc_selected_photos;
    TextView tv_selected_title;
    ViewPager mViewPager;
    TabLayout tabLayout;
    PagerAdapter_Picker adapter;
    Adapter_SelectedPhoto adapter_selectedPhoto;
    @BindView(R.id.Done_btn)
    ImageView DoneBtn;
    @BindView(R.id.Done_view)
    LinearLayout DoneView;
    @BindView(R.id.Backward_btn)
    ImageButton BackwardBtn;

    public static Config getConfig() {
        return mConfig;
    }

    public static void setConfig(Config config) {

        if (config == null) {
            throw new NullPointerException("Config cannot be passed null. Not setting config will use default values.");
        }

        mConfig = config;
    }

    @Override
    public CameraHost getCameraHost() {
        return mMyCameraHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFromSavedInstanceState(savedInstanceState);
        setContentView(R.layout.picker_activity_main_pp);
        ButterKnife.bind(this);
        initView();


        try {
            Select_Max = Integer.valueOf(getIntent().getStringExtra("Select_Max"));
            mConfig.setSelectionLimit(Select_Max);
        } catch (Exception e) {
            Select_Max = 0;
            mConfig.setSelectionLimit(Select_Max);

        }

        setupTabs();
        setSelectedPhotoRecyclerView();

        BackwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {


        view_root = findViewById(R.id.view_root);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        tv_selected_title = (TextView) findViewById(R.id.tv_selected_title);

        rc_selected_photos = (RecyclerView) findViewById(R.id.rc_selected_photos);
        mSelectedImageEmptyMessage = (TextView) findViewById(R.id.selected_photos_empty);

        view_selected_photos_container = findViewById(R.id.view_selected_photos_container);
        view_selected_photos_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view_selected_photos_container.getViewTreeObserver().removeOnPreDrawListener(this);

                int selected_bottom_size = (int) getResources().getDimension(mConfig.getSelectedBottomHeight());

                ViewGroup.LayoutParams params = view_selected_photos_container.getLayoutParams();
                params.height = selected_bottom_size;
                view_selected_photos_container.setLayoutParams(params);


                return true;
            }
        });


        if (mConfig.getSelectedBottomColor() > 0) {
            tv_selected_title.setBackgroundColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
            mSelectedImageEmptyMessage.setTextColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
        }


    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {


        if (savedInstanceState != null) {
            mSelectedImages = savedInstanceState.getParcelableArrayList(EXTRA_IMAGE_URIS);
        } else {
            mSelectedImages = getIntent().getParcelableArrayListExtra(EXTRA_IMAGE_URIS);
        }


        if (mSelectedImages == null) {
            mSelectedImages = new ArrayList<>();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedImages != null) {
            outState.putParcelableArrayList(EXTRA_IMAGE_URIS, mSelectedImages);
        }

    }

    private void setupTabs() {
        adapter = new PagerAdapter_Picker(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);


        if (mConfig.getTabBackgroundColor() > 0)
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, mConfig.getTabBackgroundColor()));

        if (mConfig.getTabSelectionIndicatorColor() > 0)
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, mConfig.getTabSelectionIndicatorColor()));

    }

    private void setSelectedPhotoRecyclerView() {


        LinearLayoutManager mLayoutManager_Linear = new LinearLayoutManager(this);
        mLayoutManager_Linear.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc_selected_photos.setLayoutManager(mLayoutManager_Linear);
        rc_selected_photos.addItemDecoration(new SpacesItemDecoration(Util.dpToPx(this, 5), SpacesItemDecoration.TYPE_VERTICAL));
        rc_selected_photos.setHasFixedSize(true);

        int closeImageRes = mConfig.getSelectedCloseImage();

        adapter_selectedPhoto = new Adapter_SelectedPhoto(this, closeImageRes);
        adapter_selectedPhoto.updateItems(mSelectedImages);
        rc_selected_photos.setAdapter(adapter_selectedPhoto);


        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
            DoneView.setVisibility(View.VISIBLE);
        } else {
            DoneView.setVisibility(View.GONE);
        }


    }


    public GalleryFragment getGalleryFragment() {

        if (adapter == null || adapter.getCount() < 2)
            return null;

        return (GalleryFragment) adapter.getItem(1);

    }

    public void addImage(final Uri uri) {


        if (mSelectedImages.size() == mConfig.getSelectionLimit()) {
            String text = String.format(getResources().getString(R.string.max_count_msg), mConfig.getSelectionLimit());
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }


        mSelectedImages.add(uri);
        adapter_selectedPhoto.updateItems(mSelectedImages);


        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
        }
        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
            DoneView.setVisibility(View.VISIBLE);
        } else {
            DoneView.setVisibility(View.GONE);
        }


        rc_selected_photos.smoothScrollToPosition(adapter_selectedPhoto.getItemCount() - 1);


    }

    public void removeImage(Uri uri) {


        mSelectedImages.remove(uri);

        adapter_selectedPhoto.updateItems(mSelectedImages);

        if (mSelectedImages.size() == 0) {
            mSelectedImageEmptyMessage.setVisibility(View.VISIBLE);
        }
        GalleryFragment.mGalleryAdapter.notifyDataSetChanged();
        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
            DoneView.setVisibility(View.VISIBLE);
        } else {
            DoneView.setVisibility(View.GONE);
        }

    }

    public boolean containsImage(Uri uri) {
        return mSelectedImages.contains(uri);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void updatePicture() {

        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
            String text = String.format(getResources().getString(R.string.min_count_msg), mConfig.getSelectionMin());
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        } else if (mSelectedImages.size() == 0) {
            ToastUtils.obj.shortToast( "Please select an image");
        } else {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(EXTRA_IMAGE_URIS, mSelectedImages);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }


    }


    @OnClick(R.id.Done_btn)
    public void onViewClicked() {
        try {
            updatePicture();
        } catch (Exception e) {
        }

    }
}
