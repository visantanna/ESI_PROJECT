package com.example.visantanna.leilaoapp.View;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.frangments.FirstFragment;
import com.example.visantanna.leilaoapp.frangments.ItensFragment;
import com.example.visantanna.leilaoapp.frangments.LeilaoesFragment;
import com.example.visantanna.leilaoapp.frangments.PerfilFragment;

import java.security.PrivateKey;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private TextView mostraEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        email = getIntent().getExtras().getString("email");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        mostraEmail = (TextView)hView.findViewById(R.id.textViewEmail);
        mostraEmail.setText(email);
        if (savedInstanceState == null){
            FirstFragment ola = new FirstFragment();
            android.support.v4.app.FragmentTransaction fragmentTrasaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTrasaction.replace(R.id.fragment_container, ola );
            fragmentTrasaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            //super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.item_perfil:
                // abra o fragmento pagamentos
                PerfilFragment fragment_pag = new PerfilFragment();
                android.support.v4.app.FragmentTransaction fragmentTrasaction_pag =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction_pag.replace(R.id.fragment_container, fragment_pag);
                fragmentTrasaction_pag.commit();
                break;
            case R.id.item_itens:
                // abra o fragmento depositos
                ItensFragment fragment_dep = new ItensFragment();
                android.support.v4.app.FragmentTransaction fragmentTrasaction_dep =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction_dep.replace(R.id.fragment_container, fragment_dep);
                fragmentTrasaction_dep.commit();
                break;
            case R.id.item_leiloes:
                // abra o fragmento despesas
                LeilaoesFragment fragment_des = new LeilaoesFragment();
                android.support.v4.app.FragmentTransaction fragmentTrasaction_des =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction_des.replace(R.id.fragment_container, fragment_des);
                fragmentTrasaction_des.commit();
                break;

            case R.id.item_sair:
                // abra o fragmento caixa

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
