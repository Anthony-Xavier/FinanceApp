package com.example.myapplication.calculadora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class calculadoraDeInvestimentos extends AppCompatActivity {
    private EditText text_investimento_inicial, text_prazo, text_investimento_mensal, text_rentabilidade;
    private Button btn_calcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_de_investimentos);

        text_investimento_inicial = findViewById(R.id.investimento_inicial);
        text_prazo = findViewById(R.id.tempo_investimento);
        text_investimento_mensal = findViewById(R.id.investimento_mensal);
        text_rentabilidade = findViewById(R.id.taxa_juros);

        btn_calcular = findViewById(R.id.btn_calcular);

        btn_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double inicial = Double.parseDouble(text_investimento_inicial.getText().toString());
                    double mensal = Double.parseDouble(text_investimento_mensal.getText().toString());
                    int anos = Integer.parseInt(text_prazo.getText().toString());
                    double taxaAnual = Double.parseDouble(text_rentabilidade.getText().toString());

                    double taxaMensal = Math.pow(1 + (taxaAnual / 100), 1.0 / 12) - 1;
                    double montante = calcularJurosCompostos(inicial, mensal, taxaMensal, anos * 12);
                    double totalInvestido = inicial + (mensal * anos * 12);
                    double juros = montante - totalInvestido;
                    int tempoInvestido = anos;
                    double mes = mensal;


                    // Formata os valores
                    String montanteFormatado = "R$ " + formatNumberWithThousandSeparator(montante);
                    String totalInvestidoFormatado = "R$ " + formatNumberWithThousandSeparator(totalInvestido);
                    String jurosFormatado = "R$ " + formatNumberWithThousandSeparator(juros);
                    String anosFormatado = ""+ formatNumberWithThousandSeparator(tempoInvestido);


                    // Cria a Intent para abrir a ResultadosActivity
                    Intent intent = new Intent(calculadoraDeInvestimentos.this, ResultadoCalcualdora.class);
                    intent.putExtra("valorBruto", montante);
                    intent.putExtra("valorInvestido", totalInvestido);
                    intent.putExtra("rendimentoLiquido", juros);
                    intent.putExtra("valorAnos", tempoInvestido);
                    intent.putExtra("mes", mes);
                    intent.putExtra("taxaAnual", taxaAnual); // Adiciona a taxa anual correta na Intent
                    startActivity(intent);



                    startActivity(intent);


                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Log do erro para depuração
                }
            }
        });
    }

    // Método para calcular juros compostos
    private double calcularJurosCompostos(double inicial, double mensal, double taxaMensal, int meses) {
        double montanteInicial = inicial * Math.pow(1 + taxaMensal, meses);
        double montanteMensal = mensal * ((Math.pow(1 + taxaMensal, meses) - 1) / taxaMensal);
        return montanteInicial + montanteMensal;
    }

    // Método para formatar números com separadores de milhar e duas casas decimais
    public String formatNumberWithThousandSeparator(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00", symbols);
        return decimalFormat.format(number);

    }
}