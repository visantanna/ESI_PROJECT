package com.example.visantanna.leilaoapp.View.Home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.visantanna.leilaoapp.Enum.Categorias;
import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.controllers.GerenciadorAbasHome;
import com.example.visantanna.leilaoapp.controllers .ArgumentosPesquisa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private PopupMenu categoriesMenu;
    private Categorias selectedCategorie = Categorias.NENHUM;
    private EditText pesquisaField;
    private TabLayout barrasDeSelecao;
    GerenciadorAbasHome gerenciadorAbas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jbInit();
    }

    private void jbInit() {
        setContentView(R.layout.home);

        pesquisaField = (EditText)findViewById(R.id.SearchText);

        ViewPager abaViews = (ViewPager) findViewById(R.id.Pager);
        barrasDeSelecao = (TabLayout) findViewById(R.id.barrasSelecao);
        gerenciadorAbas = new GerenciadorAbasHome(getSupportFragmentManager(), this);
        abaViews.setAdapter(gerenciadorAbas);

        barrasDeSelecao.setupWithViewPager(abaViews);
    }

    public void pesquisaClicked(View v){
        ArgumentosPesquisa argPesq = new ArgumentosPesquisa();
        argPesq.setCategoria(selectedCategorie );
        argPesq.setPosicaoTab(barrasDeSelecao.getSelectedTabPosition());
        argPesq.setTextoPesquisa(pesquisaField.getText().toString());
        gerenciadorAbas.atualizaListaPesquisa(argPesq);

    }

    public void categoriesClicked(View v){

        categoriesMenu = new PopupMenu(Home.this ,v);
        categoriesMenu.setOnMenuItemClickListener(Home.this);
        categoriesMenu.inflate(R.menu.categories_menu );
        //show icons

        try {
            Field[] fields = categoriesMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(categoriesMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoriesMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_nenhum:
                selectedCategorie = Categorias.NENHUM;
                return true;
            case R.id.item_eletronicos:
                selectedCategorie = Categorias.ELETRONICOS;
                return true;
            case R.id.item_lazer:
                selectedCategorie = Categorias.LAZER;
                return true;
            case R.id.item_para_casa:
                selectedCategorie = Categorias.PARACASA;
                return true;
            case R.id.item_veiculos:
                selectedCategorie = Categorias.VEICULO;
                return true;
            case R.id.item_roupa:
                selectedCategorie = Categorias.ROUPAS;
                return true;
            default: return false;
        }
    }

}
