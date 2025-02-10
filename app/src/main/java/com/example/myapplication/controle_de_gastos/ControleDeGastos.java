package com.example.myapplication.controle_de_gastos;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ControleDeGastos extends AppCompatActivity {
    private EditText edtNomeGasto, edtValorGasto;
    private Spinner spinnerCategoria;
    private View btnSalvar;
    private ListView lvGastos;
    private ArrayList<String> gastosList;
    private ArrayAdapter<String> adapter;


    private static final String PREFS_NAME = "GastosPrefs";
    private static final String KEY_GASTOS = "gastos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_controle_de_gastos);

        edtNomeGasto = findViewById(R.id.nome_gasto);
        edtValorGasto = findViewById(R.id.nome_valor);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSalvar = findViewById(R.id.btn_calcular_gastos);
        lvGastos = findViewById(R.id.lvGastos);

        List<Gasto> gastoList = Data.getGastolist();
        GastosAdapter gastosAdapter = new GastosAdapter(this, gastoList);

        // Definir adapter para o Spinner e ListView
        spinnerCategoria.setAdapter(gastosAdapter);
        lvGastos.setAdapter(gastosAdapter);
        // Lista de gastos
        gastosList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gastosList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        };
        lvGastos.setAdapter(adapter);



        // Carregar os gastos salvos
        loadGastos();
        updateTotal();

        // Ação do botão Salvar
        btnSalvar.setOnClickListener(v -> {
            String nome = edtNomeGasto.getText().toString();
            String valorStr = edtValorGasto.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();

            if (!nome.isEmpty() && !valorStr.isEmpty()) {
                double valor = Double.parseDouble(valorStr);
                String gasto = nome + " - " + categoria + ": R$ " + valor;
                gastosList.add(gasto);
                adapter.notifyDataSetChanged();

                // Salvar os gastos
                saveGastos();
                updateTotal();

                // Limpa os campos após salvar
                edtNomeGasto.setText("");
                edtValorGasto.setText("");
                spinnerCategoria.setSelection(0);

                // Atualiza diretamente o total de gastos na Activity Menu
                if (MainActivity.activity != null) {
                    MainActivity.activity.updateTotalGastos();  // Atualiza a tela Menu sem precisar navegar para ela
                }
            } else {
                Toast.makeText(ControleDeGastos.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação para excluir um item individualmente (long click)
        lvGastos.setOnItemLongClickListener((adapterView, view, position, id) -> {
            String gastoRemovido = gastosList.get(position);
            gastosList.remove(position);
            adapter.notifyDataSetChanged();
            saveGastos();
            updateTotal();

            Toast.makeText(ControleDeGastos.this, "Gasto removido: " + gastoRemovido, Toast.LENGTH_SHORT).show();

            // Atualiza diretamente o total de gastos na Activity Menu

            if (MainActivity.activity != null) {
               MainActivity.activity.updateTotalGastos();  // Atualiza a tela Menu sem precisar navegar para ela
          }

            return true;
        });
    }
    // Função para salvar os gastos no SharedPreferences
    private void saveGastos() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Convertendo a lista de gastos para uma String separada por vírgulas
        StringBuilder sb = new StringBuilder();
        for (String gasto : gastosList) {
            sb.append(gasto).append(",");
        }
        editor.putString(KEY_GASTOS, sb.toString());
        editor.apply();
    }

    // Função para carregar os gastos do SharedPreferences
    private void loadGastos() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String gastosString = preferences.getString(KEY_GASTOS, "");

        if (!gastosString.isEmpty()) {
            // Convertendo a String de volta para a lista
            String[] gastosArray = gastosString.split(",");
            for (String gasto : gastosArray) {
                if (!gasto.isEmpty()) {
                    gastosList.add(gasto);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    // Função para atualizar o total de gastos
    private void updateTotal() {
        double total = 0.0;
        for (String gasto : gastosList) {
            String[] parts = gasto.split(": R\\$ ");
            if (parts.length == 2) {
                try {
                    total += Double.parseDouble(parts[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}