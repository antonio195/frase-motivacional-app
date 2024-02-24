package com.antoniocostadossantos.frasesmotivacionais

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antoniocostadossantos.frasesmotivacionais.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlin.random.Random

const val FILE_NAME = "frases.json"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var frase = ""
    private lateinit var fraseList: FraseList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fraseAleatoria(applicationContext)

        binding.btnNovaFrase.setOnClickListener {
            fraseAleatoria(applicationContext)
        }

        binding.compartilharFrase.setOnClickListener {
            compartilharFrase()
        }

        binding.politicsInfo.setOnClickListener {
            abrirPolitica()
        }
    }

    private fun compartilharFrase() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, frase)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun fraseAleatoria(context: Context) {
        pegarCorAleatoria()

        val numeroAleatorio = Random(System.currentTimeMillis())
        val indice = numeroAleatorio.nextInt(0, 999)

        if (!::fraseList.isInitialized) {
            val gson = Gson()
            val json = context.assets.open(FILE_NAME).bufferedReader().use {
                it.readText()
            }

            fraseList = gson.fromJson(json, FraseList::class.java)
        }
        frase = "_${fraseList[indice].frase}_ \n\n*_${fraseList[indice].autor}_*"

        binding.autorTitle.text = fraseList[indice].autor
        binding.fraseTitle.text = fraseList[indice].frase
    }

    private fun pegarCorAleatoria(): Int {
        val random = Random(System.currentTimeMillis())
        val color = Color.argb(255, random.nextInt(100), random.nextInt(100), random.nextInt(100))
        binding.root.setBackgroundColor(color)
        return color
    }

    private fun abrirPolitica() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://frasediariaapp.blogspot.com/2022/12/politica-de-privacidade.html")
        )
        startActivity(browserIntent)
    }
}