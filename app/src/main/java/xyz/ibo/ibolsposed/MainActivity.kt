package xyz.ibo.ibolsposed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a simple UI programmatically
        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL

        val textView = TextView(this)
        textView.text = "Original TextView Content"
        textView.textSize = 20f
        textView.setPadding(50, 50, 50, 50)

        rootLayout.addView(textView)

        setContentView(rootLayout) // Set this as the main content view
    }
}