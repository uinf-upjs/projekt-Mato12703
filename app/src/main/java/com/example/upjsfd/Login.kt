package com.example.upjsfd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.upjsfd.entities.Film
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlin.concurrent.thread

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_login)
        val menoEditText = findViewById<EditText>(R.id.menoText)
        val hesloEditText = findViewById<EditText>(R.id.hesloText)
        val loginButton = findViewById<Button>(R.id.prihlasitButton)
        val nemamUcetButton = findViewById<Button>(R.id.nemamUcetButton)
        val pokracovatButton = findViewById<Button>(R.id.pokracovatButton)

        nemamUcetButton.setOnClickListener {
            val intent = Intent(this, Registracia::class.java)
            startActivity(intent)
        }
        pokracovatButton.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            intent.putExtra("isLoggedIn",false)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val meno = menoEditText.text.toString()
            val heslo = hesloEditText.text.toString()

            if (meno.isEmpty() || heslo.isEmpty()) {
                Toast.makeText(this, "Vyplňte všetky polia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {
                val db = UPJSFdDatabase.getDatabase(this)
                val user = db.userDao().getUserByName(meno)


                if (user != null) {
                    if (user.heslo == heslo) {
                        runOnUiThread {
                            Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,SearchActivity::class.java)
                            intent.putExtra("isLoggedIn", true)
                            val idUser = user.idUser
                            intent.putExtra("idUser",idUser)

                            startActivity(intent)

                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Zlé heslo", Toast.LENGTH_SHORT).show()
                            hesloEditText.setText("")
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Používateľ s týmto menom neexistuje", Toast.LENGTH_SHORT).show()
                        menoEditText.setText("")
                        hesloEditText.setText("")
                    }
                }
            }
        }

    }
}