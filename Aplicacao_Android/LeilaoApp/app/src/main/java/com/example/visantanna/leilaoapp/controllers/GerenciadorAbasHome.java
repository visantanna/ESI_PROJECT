package com.example.visantanna.leilaoapp.controllers;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.ContextHolder;
import com.example.visantanna.leilaoapp.frangments.FragmentCardsManager;

/**
 * Created by vis_a on 03-Nov-17.
 */

public class GerenciadorAbasHome extends FragmentPagerAdapter{
    private Activity activity;
    FragmentCardsManager abaEmAndamento;
    FragmentCardsManager abaAgendado;
    FragmentCardsManager abaInteresses;


    public GerenciadorAbasHome(FragmentManager fm , Activity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                abaEmAndamento = new FragmentCardsManager();
                abaEmAndamento.setActivityUI(activity);
                return abaEmAndamento;
            case 1:
                abaAgendado = new FragmentCardsManager();
                abaAgendado.setActivityUI(activity);
                return abaAgendado;

            case 2:
                abaInteresses = new FragmentCardsManager();
                abaInteresses.setActivityUI(activity);
                return abaInteresses;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ContextHolder.getAplicationContext().getResources().getString(R.string.AbaEmAndamento);
            case 1:
                return ContextHolder.getAplicationContext().getResources().getString(R.string.AbaProgramado);
            case 2:
                return ContextHolder.getAplicationContext().getResources().getString(R.string.AbaInteresses);
            default :
                return "";
        }
    }
    public void atualizaListaPesquisa(ArgumentosPesquisa argumentos){
        switch(argumentos.getPosicaoTab()){
            case 0:
                abaEmAndamento.changeSearch(argumentos);
                break;
            case 1:
                abaAgendado.changeSearch(argumentos);
                break;
            case 2:
                abaInteresses.changeSearch(argumentos);
                break;
        }
    }
}
