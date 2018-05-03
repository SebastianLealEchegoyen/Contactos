package com.example.charl.contactos;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ContactoFrag.OnFragmentInteractionListener, Favoritos.OnFragmentInteractionListener {

    static final int REQUEST_CODE_ASK_PERMISSION = 2018;
    int Read;
    ContactoFrag one= new ContactoFrag();

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void accessPermission(){
        Read = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);

        if(Read != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE_ASK_PERMISSION);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[ ] grantResults){
        switch(requestCode){
            case REQUEST_CODE_ASK_PERMISSION:
                if(grantResults[0] ==PackageManager.PERMISSION_GRANTED){

                    Toast t= Toast.makeText(getApplicationContext(),"Permiso autorizado",Toast.LENGTH_SHORT);
                    t.show();
                }
                else{

                   Toast t2 =Toast.makeText(getApplicationContext(),"Permiso denegado",Toast.LENGTH_SHORT);
                   t2.show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    //Creando el Viewpager
    private ViewPager mViewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessPermission();


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //preparing the viewpager
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getInfo= new Intent(getApplicationContext(),Main2Activity.class);
                startActivityForResult(getInfo, 2);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = null;

            switch(sectionNumber) {
                case 1: fragment= new ContactoFrag();
                       Fragment fragment2=fragment;
                    break;

                case 2: fragment= new Favoritos();
                    break;
            }

            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // A total of two pages(Tabs) will be shown.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position){
                case 0:
                    return "Series";

                case 1:
                    return "Favoritos";
            }
            return null;
        }
    }
}
