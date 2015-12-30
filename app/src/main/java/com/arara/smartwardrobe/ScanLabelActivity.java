package com.arara.smartwardrobe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScanLabelActivity extends AppCompatActivity {

    /*
    Activity que permite ler o conteúdo de uma label, verifica se ela já foi cadastrada
    no banco de dados e caso não tenha sido, permite que isso seja feito.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_label);
    }
}
