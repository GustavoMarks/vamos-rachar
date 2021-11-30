package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener,
    OnInitListener {
    var result: TextView? = null
    var edtValor: EditText? = null
    var edtPessoas: EditText? = null
    var compartilha: FloatingActionButton? = null
    var fala: FloatingActionButton? = null
    var ttsPlayer: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Atribuindo variáveis
        setContentView(R.layout.activity_main)
        result = findViewById<View>(R.id.resultado) as TextView
        edtValor = findViewById<View>(R.id.editValor) as EditText
        edtPessoas = findViewById<View>(R.id.editNumeroPessoas) as EditText
        compartilha = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
        fala = findViewById<View>(R.id.floatingActionButton2) as FloatingActionButton

        // Configurando listener de calculo enquanto digita
        edtValor!!.addTextChangedListener(this)

        // Configurando listeners de ação dos botões
        compartilha!!.setOnClickListener(this)
        fala!!.setOnClickListener(this)

        // Checando se dispositivo tem TTS
        val checkTTS = Intent()
        checkTTS.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        startActivityForResult(checkTTS, 1122)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsPlayer = TextToSpeech(this, this)
            } else {
                val installTTSIntent = Intent()
                installTTSIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installTTSIntent)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        try {
            val valor = edtValor!!.text.toString().toDouble()
            val pessoas = edtPessoas!!.text.toString().toDouble()
            val res = valor / pessoas
            val format = DecimalFormat("#.00")
            result!!.text = "R$ " + format.format(res)
        } catch (e: Exception) {
            Log.v("Error", e.toString())
            result!!.text = "ERRO!"
        }
    }

    override fun onClick(v: View) {
        if (v === compartilha) {
            val cmptlhintent = Intent(Intent.ACTION_SEND)
            cmptlhintent.type = "text/plain"
            cmptlhintent.putExtra(
                Intent.EXTRA_TEXT,
                "Resultado de divisão da conta por pessoa: " + result!!.text.toString()
            )
            startActivity(cmptlhintent)
        }
        if (v === fala) {
            if (ttsPlayer != null) {
                ttsPlayer!!.speak(
                    "Resultado de divisão da conta por pessoa: " + result!!.text.toString(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "ID1"
                )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show()
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "TTS não habilitado...", Toast.LENGTH_LONG).show()
        }
    }
}