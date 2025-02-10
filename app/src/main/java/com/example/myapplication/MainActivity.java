package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.calculadora.calculadoraDeInvestimentos;
import com.example.myapplication.controle_de_gastos.ControleDeGastos;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_projeção_financeira, btn_add_gastos;
    private TextView total_gasto, resultado_saldo;
    private EditText edt_renda;


    public static MainActivity activity; // Variável estática para acessar a instância de Menu
    private static final String PREFS_NAME = "GastosPrefs";
    private static final String KEY_GASTOS = "gastos";
    private static final String KEY_RENDA_FIXA = "rendaFixa";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_projeção_financeira = findViewById(R.id.btn_projeção_financeira);
        btn_add_gastos = findViewById(R.id.btn_add_gastos);
        total_gasto = findViewById(R.id.total_gastos);
        edt_renda = findViewById(R.id.edt_renda);
        resultado_saldo = findViewById(R.id.saldo);
        activity = this;  // Atribuindo a instância da Activity Menu à variável estática


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        edt_renda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Nada precisa ser feito aqui
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Verifique se a string começa com "R$ " e, se não, adicione o prefixo "R$" apenas quando necessário
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("R$")) {
                    // Verifica se o valor está vazio para não adicionar "R$" quando o campo estiver vazio
                    if (!charSequence.toString().isEmpty()) {
                        edt_renda.setText("R$ " + charSequence.toString());
                        edt_renda.setSelection(edt_renda.getText().length());  // Move o cursor para o final
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveRendaFixa(); // Salvar a renda toda vez que o texto mudar
                updateTotalGastos(); // Atualizar saldo automaticamente
            }
        });



        btn_projeção_financeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intet = new Intent(MainActivity.this, calculadoraDeInvestimentos.class);
                startActivity(intet);
            }
        });

        btn_add_gastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ControleDeGastos.class);
                startActivity(intent);
            }
        });

        // Salvar a renda fixa automaticamente quando o campo perde o foco
        edt_renda.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveRendaFixa();
                updateTotalGastos(); // Atualizar saldo automaticamente
            }
        });

        loadRendaFixa();
        updateTotalGastos();
    }


    private void loadRendaFixa() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String rendaFixa = preferences.getString(KEY_RENDA_FIXA, "0.00");

        // Remove o prefixo "R$ " caso exista
        if (rendaFixa.startsWith("R$ ")) {
            rendaFixa = rendaFixa.replace("R$ ", "");
        }

        // Adiciona novamente "R$ " para exibição no EditText
//        edt_renda.setText(rendaFixa);
    }


    private void saveRendaFixa() {
        String rendaFixa = edt_renda.getText().toString();

        // Remove o prefixo "R$ " e converte para salvar sem formatação
        if (rendaFixa.startsWith("R$ ")) {
            rendaFixa = rendaFixa.replace("R$ ", "").replace(",", ".");
        }

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_RENDA_FIXA, rendaFixa);
        editor.apply();
    }


    public void updateTotalGastos() {
        double totalGastos = 0.0;
        double rendaFixa = 0.0;

        // Recuperar a renda fixa do SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String rendaFixaString = preferences.getString(KEY_RENDA_FIXA, "0.00");

        try {
            // Remove o prefixo "R$ " antes de converter para double
            if (rendaFixaString.startsWith("R$ ")) {
                rendaFixaString = rendaFixaString.replace("R$ ", "");
            }
            rendaFixa = Double.parseDouble(rendaFixaString);
        } catch (NumberFormatException e) {
            rendaFixa = 0.0; // Valor padrão caso a conversão falhe
            e.printStackTrace();
        }

        // Recuperar os gastos salvos
        String gastosString = preferences.getString(KEY_GASTOS, "");
        if (!gastosString.isEmpty()) {
            String[] gastosArray = gastosString.split(",");
            for (String gasto : gastosArray) {
                String[] parts = gasto.split(": R\\$ ");
                if (parts.length == 2) {
                    try {
                        totalGastos += Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        total_gasto.setText(String.format("%.2f", totalGastos));
        // Calcular saldo e atualizar os TextViews
        double saldoTotal = rendaFixa - totalGastos;
        resultado_saldo.setText(String.format(" %.2f", saldoTotal));

        if (saldoTotal >= 0) {
            resultado_saldo.setTextColor(getResources().getColor(android.R.color.holo_green_dark));  // Verde
        } else {
            resultado_saldo.setTextColor(getResources().getColor(android.R.color.holo_red_dark));  // Vermelho
        }

    }
}