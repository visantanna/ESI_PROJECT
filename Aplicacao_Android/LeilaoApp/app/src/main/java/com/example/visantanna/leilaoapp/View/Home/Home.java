package com.example.visantanna.leilaoapp.View.Home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.controllers.GerenciadorAbasHome;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ViewPager abaViews        =  (ViewPager)findViewById(R.id.Pager);
        TabLayout barrasDeSelecao =  (TabLayout)findViewById(R.id.barrasSelecao);
        PagerAdapter gerenciadorAbas = new GerenciadorAbasHome(getSupportFragmentManager(), this);

        abaViews.setAdapter(gerenciadorAbas);

        barrasDeSelecao.setupWithViewPager(abaViews);

    }
}
