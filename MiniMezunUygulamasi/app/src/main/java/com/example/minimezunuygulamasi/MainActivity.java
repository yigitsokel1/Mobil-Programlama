package com.example.minimezunuygulamasi;

import com.example.minimezunuygulamasi.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomView;
    private ProfilFragment profilFragment;
    private AraFragment araFragment;
    private DuyuruFragment duyuruFragment;
    private SosyalFragment sosyalFragment;
    private FragmentTransaction transaction;
    private MenuItem item;

    private void init(){
        mBottomView = (BottomNavigationView) findViewById(R.id.main_activity_bottomView);
        profilFragment = new ProfilFragment();
        araFragment = new AraFragment();
        duyuruFragment = new DuyuruFragment();
        sosyalFragment = new SosyalFragment();
        fragmentAyarla(sosyalFragment);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = sosyalFragment;
                if(R.id.bottom_nav_ic_profil == item.getItemId()){
                    fragmentAyarla(profilFragment);
                    return true;
                }
                else if(R.id.bottom_nav_ic_arama == item.getItemId()){
                    fragmentAyarla(araFragment);
                    return true;
                }else if (R.id.bottom_nav_ic_duyuru==item.getItemId()){
                    fragmentAyarla(duyuruFragment);
                    return true;
                }else if(R.id.bottom_nav_ic_sosyal==item.getItemId()){
                    fragmentAyarla(sosyalFragment);
                    return true;
                }else
                    return false;
            }
        });


    }

    private void fragmentAyarla(Fragment fragment){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_frameLayout, fragment);
        transaction.commit();
    }






}