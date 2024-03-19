package com.example.culture_reminder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.culture_reminder.ui.theme.CultureReminderTheme
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.appcompat.app.AppCompatActivity


class MainActivity : ComponentActivity() {

    lateinit var button: Button
    lateinit var auth: FirebaseAuth
    lateinit var textView: TextView
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.userDetails)
        user = auth.currentUser!!
        if (user == null){
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        else {
            textView.setText(user.getEmail())
        }

        button.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, Login::class.java)
                startActivity(intent)
                finish()

        }
    }
}


