package com.example.upjsfd

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.upjsfd.entities.User
import com.google.android.material.textfield.TextInputEditText
import kotlin.concurrent.thread

class Registracia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registracia)

        val menoEditText = findViewById<EditText>(R.id.menoText)
        val hesloEditText = findViewById<EditText>(R.id.hesloText)
        val hesloZnovuEditText = findViewById<EditText>(R.id.hesloZnovuText)
        val registerButton = findViewById<Button>(R.id.button)

        registerButton.setOnClickListener {
            val meno = menoEditText.text.toString()
            val heslo = hesloEditText.text.toString()
            val hesloZnovu = hesloZnovuEditText.text.toString()

            if (meno.isEmpty() || heslo.isEmpty() || hesloZnovu.isEmpty()) {
                Toast.makeText(this, "Vyplňte všetky polia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (heslo != hesloZnovu) {
                Toast.makeText(this, "Heslá sa nezhodujú", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {
                val db = UPJSFdDatabase.getDatabase(this)
                val existingUser = db.userDao().getUserByName(meno)

                if (existingUser != null) {
                    runOnUiThread {
                        Toast.makeText(this, "Používateľ s týmto menom už existuje", Toast.LENGTH_SHORT).show()
                        findViewById<TextInputEditText>(R.id.menoText).setText("")
                        findViewById<TextInputEditText>(R.id.hesloText).setText("")
                        findViewById<TextInputEditText>(R.id.hesloZnovuText).setText("")
                    }
                } else {
                    val user = User(idUser = 0, meno = meno, heslo = heslo)
                    db.userDao().insertUser(user)

                    runOnUiThread {
                        Toast.makeText(this, "Registrácia úspešná", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

            }
        }
    }
}
