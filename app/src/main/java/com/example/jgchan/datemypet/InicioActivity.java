package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.entidades.AccessToken;

import static com.example.jgchan.datemypet.R.string.*;

public class InicioActivity extends MenuActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;
    private TokenManager tokenManager;
    int posicion=0;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        //
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);


        //ANEXANDO EL TOOLBAR
        Toolbar toolbar = (Toolbar) appBarLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ANEXANDO EL MENU DESPLEGABLE

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //OBTENIENDO EL PADRE HEADER
        View headerView = navigationView.getHeaderView(0);

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario=(TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario=(TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " +datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

       final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(posicion==1){
                   Intent i = new Intent(InicioActivity.this,CrearMascotactivity.class);
                   startActivity(i);
               }else{
                   Toast.makeText(InicioActivity.this, "Falta Activity", Toast.LENGTH_SHORT).show();
               }
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                posicion = tab.getPosition();
              //  Toast.makeText(InicioActivity.this, "posicion: "+posicion, Toast.LENGTH_SHORT).show();
                cambiarFabButton(fab, posicion);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


    }


    @SuppressLint("ResourceAsColor")
    void cambiarFabButton(FloatingActionButton fab, int posicion) {

        switch (posicion){

            case 0:

                fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

                break;

            case 1:
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                break;

        }

    }


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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            Toast.makeText(fragment.getContext(), "posicion: "+sectionNumber, Toast.LENGTH_SHORT).show();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
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
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position){

                case 0:
                        CitasFragment fragmentCitas = new CitasFragment();
                        return fragmentCitas;

                case 1:
                        MascotasFragment fragmentMasco = new MascotasFragment();
                       return  fragmentMasco;

            }

            return null;

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
