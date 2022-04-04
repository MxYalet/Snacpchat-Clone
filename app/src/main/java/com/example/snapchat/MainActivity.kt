package com.example.snapchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.userName)
        passwordEditText = findViewById(R.id.passWord)

        if (mAuth.currentUser != null) {
            logIn()
        }
    }

    fun goClicked(view: View) {
        // Check if we can log in the user
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    // Sign up the user
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val save = FirebaseDatabase.getInstance().reference.child("users").child(
                                task.result.user!!.uid).child("email")
                            save.setValue(emailEditText?.text.toString())
                            logIn()
                        } else {
                            Toast.makeText(this,"Login Failed. Try Again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    fun logIn() {
        // Move to next Activity
        val intent = Intent(this, snapsActivity::class.java)
        startActivity(intent)
    }
}