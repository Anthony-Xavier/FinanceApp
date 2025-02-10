package com.example.myapplication.calculadora;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.charts.LineChart;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class ResultadoCalcualdora extends AppCompatActivity {
    private TextView txtValorBruto, txtValorInvestido, txtRendimentoLiquido, tempo_investido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_calcualdora);

        txtValorBruto = findViewById(R.id.text_valor_bruto);
        txtValorInvestido = findViewById(R.id.text_valor_investido);
        txtRendimentoLiquido = findViewById(R.id.text_valor_juros);
        tempo_investido = findViewById(R.id.text_tempo_investido);

        PieChart pieChart = findViewById(R.id.pie_chart);

        // Recebe os dados da Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double valorBruto = extras.getDouble("valorBruto");
            double valorInvestido = extras.getDouble("valorInvestido");
            double rendimentoLiquido = extras.getDouble("rendimentoLiquido");
            int valorAnos = extras.getInt("valorAnos");
            double mes= extras.getInt("mes");

            // Atualiza os TextViews com os valores recebidos formatados
            txtValorBruto.setText("R$ " + formatNumberWithThousandSeparator(valorBruto));
            txtValorInvestido.setText("R$ " + formatNumberWithThousandSeparator(valorInvestido));
            txtRendimentoLiquido.setText("R$ " + formatNumberWithThousandSeparator(rendimentoLiquido));
            tempo_investido.setText(String.valueOf((int) valorAnos) + " anos");

            // Adiciona dados ao gráfico de pizza
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry((float) valorInvestido, "Investido"));
            entries.add(new PieEntry((float) valorBruto, "Resultado"));
            entries.add(new PieEntry((float) (rendimentoLiquido), "valor em juros"));

            PieDataSet pieDataSet = new PieDataSet(entries, "Investimentos");

            // Define as cores para cada fatia
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ContextCompat.getColor(this, R.color.green));  // Verde (#3CCD2F)
            colors.add(ContextCompat.getColor(this, R.color.roxo));    // Vermelho (#F8171A)
            colors.add(ContextCompat.getColor(this, R.color.orange)); // Laranja (#FFCC00)
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate(); // Atualiza o gráfico de pizza

            // Adiciona dados ao gráfico de linha
            ArrayList<Entry> lineEntries = new ArrayList<>();
            float valorAtual = (float) valorInvestido;

            // Preencher os dados do gráfico de linha com a evolução do investimento
            for (int i = 1; i <= valorAnos; i++) {
                for (int j = 1; j <= 12; j++) {  // Para cada mês de cada ano
                    valorAtual += mes ; // Aporte mensal
                    valorAtual += valorAtual * (float) rendimentoLiquido / 100 / 12; // Rendimento mensal (dividido por 12)

                    // Adiciona o valor para o mês ao gráfico
                    lineEntries.add(new Entry((i - 1) * 12 + j, valorAtual));
                }
            }

            LineDataSet lineDataSet = new LineDataSet(lineEntries, "Evolução do Investimento");
            lineDataSet.setColor(Color.BLUE);
            lineDataSet.setValueTextColor(Color.BLACK);
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setDrawValues(true);

        }
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
