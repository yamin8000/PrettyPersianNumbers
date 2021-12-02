package ir.yamin.digitstest

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ir.yamin.digits.Digits

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.numberEditText)
        val textView = findViewById<TextView>(R.id.textView)

        input.doAfterTextChanged {
            val number = it.toString()
            textView.text = Digits().spellToFarsi(number)
        }
    }
}