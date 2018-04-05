package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.ManejoArchivos;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HistorialMedicoActivity extends MenuActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
      /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HistorialMedicoActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;
    private TokenManager tokenManager;
    Call<ResponseBody> descargar;
    int posicion=0;
    FloatingActionButton fab;
    Letrero objLetrero = new Letrero(this);
    String idMascota,nombreMascota;
    ManejoArchivos ma = new ManejoArchivos("",this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_medico);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados = tokenManager.getToken();

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
        mSectionsPagerAdapter = new HistorialMedicoActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(posicion==1){
                   // ma.descargarPdf(idMascota,nombreMascota,service);
                    Intent i = new Intent(HistorialMedicoActivity.this,navegacionActivity.class);
                    i.putExtra("idMascota",idMascota);
                    i.putExtra("nombreMascota",nombreMascota);
                    startActivity(i);
                }else{
                    Intent i = new Intent(HistorialMedicoActivity.this,RegistroRegMedicoActivity.class);
                    i.putExtra("idMascota",idMascota);
                    startActivity(i);

                }
            }
        });


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                posicion = tab.getPosition();
                cambiarFabButton(fab, posicion);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}


        });



        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idMascota=extras.getString("idMascota");
            nombreMascota=extras.getString("nombreMascota");
        }

    }

    @SuppressLint("ResourceAsColor")
    void cambiarFabButton(FloatingActionButton fab, int posicion) {

        switch (posicion){

            case 0:

                fab.setImageResource(R.drawable.add);
                mostrarFab(fab);
                break;

            case 1:
                fab.setImageResource(R.drawable.pdf);
                mostrarFab(fab);
                break;

        }

    }

    private void mostrarFab(FloatingActionButton fab) {

        fab.animate().translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(100); // Cambiar al tiempo deseado

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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_historial_medico, container, false);
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

            switch (position){

                case 0:
                    RegistrosFragment fragmentRegistros = new RegistrosFragment(objLetrero,fab, idMascota);
                    return fragmentRegistros;

                case 1:
                    ArchivosFragment fragmentArchivos = new ArchivosFragment(objLetrero, fab, ma, idMascota, nombreMascota);
                    return fragmentArchivos;

            }

            return null;



        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
