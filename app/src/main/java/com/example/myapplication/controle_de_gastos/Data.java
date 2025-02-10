package com.example.myapplication.controle_de_gastos;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<Gasto> getGastolist() {
        List<Gasto> gastoList = new ArrayList<>();

        Gasto moradia = new Gasto();
        moradia.setName("Moradia");
        moradia.setImage(R.drawable.moradia);
        gastoList.add(moradia);

        Gasto alimentacao = new Gasto();
        alimentacao.setName("Alimentação");
        alimentacao.setImage(R.drawable.comida);
        gastoList.add(alimentacao);

        Gasto saude = new Gasto();
        saude.setName("Saúde");
        saude.setImage(R.drawable.saude);
        gastoList.add(saude);

        Gasto transporte = new Gasto();
        transporte.setName("Transporte");
        transporte.setImage(R.drawable.trasporte);
        gastoList.add(transporte);

        Gasto lazer = new Gasto();
        lazer.setName("Lazer");
        lazer.setImage(R.drawable.lazer);
        gastoList.add(lazer);

        Gasto educacao = new Gasto();
        educacao.setName("Educação");
        educacao.setImage(R.drawable.educacao);
        gastoList.add(educacao);

        Gasto compras = new Gasto();
        compras.setName("Compras");
        compras.setImage(R.drawable.compras);
        gastoList.add(compras);

        return gastoList;
    }
}
