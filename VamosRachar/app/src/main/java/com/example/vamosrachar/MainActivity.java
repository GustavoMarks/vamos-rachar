package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    TextView result;
    EditText edtValor, edtPessoas;
    FloatingActionButton compartilha, fala;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.resultado);
        edtValor = (EditText) findViewById(R.id.editValor);
        edtPessoas = (EditText) findViewById(R.id.editNumeroPessoas);

        edtValor.addTextChangedListener(this);

        compartilha = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fala = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        compartilha.setOnClickListener(this);
        fala.setOnClickListener(this);

        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 1122);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1122){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            double valor = Double.parseDouble(edtValor.getText().toString());
            double pessoas = Double.parseDouble(edtPessoas.getText().toString());
            double res = valor/pessoas;
            DecimalFormat format = new DecimalFormat("#.00");
            result.setText("R$ "+format.format(res));

        } catch (Exception e) {
            Log.v("Error", e.toString());
            result.setText("ERRO!");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == compartilha){
            Intent cmptlhintent = new Intent(Intent.ACTION_SEND);
            cmptlhintent.setType("text/plain");
            cmptlhintent.putExtra(Intent.EXTRA_TEXT, "Resultado de divisão da conta por pessoa: " + result.getText().toString());
            startActivity(cmptlhintent);
        }

        if(v == fala) {
            if(ttsPlayer != null){
                ttsPlayer.speak("Resultado de divisão da conta por pessoa: " + result.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show();
        } else if(status == TextToSpeech.ERROR){
            Toast.makeText(this, "TTS não habilitado...", Toast.LENGTH_LONG).show();
        }
    }
}