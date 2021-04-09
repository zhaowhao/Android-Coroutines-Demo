package com.example.coroutinesapplication

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val iv_bitmap_with_watermark = findViewById<ImageView>(R.id.iv_bitmap_with_watermark)

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = getImageFromNetwork()
            val bitmapWithWatermark = createWatermark(bitmap, "CHIU")
            iv_bitmap_with_watermark.setImageBitmap(bitmapWithWatermark)
        }

    }

    private suspend fun getImageFromNetwork() = withContext(Dispatchers.IO) {
        val url = URL("https://static.wikia.nocookie.net/dogelore/images/9/97/Doge.jpg/revision/latest?cb=20190205113053")
        val connection = url.openConnection() as URLConnection
        val inputStream = connection.getInputStream()
        BitmapFactory.decodeStream(inputStream)
    }

    private suspend fun createWatermark(bitmap: Bitmap, mark: String) = withContext(Dispatchers.IO) {
        val w = bitmap.width
        val h = bitmap.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint()
        paint.color = Color.parseColor("#C5FF0000")
        paint.textSize = 150F
        paint.isAntiAlias = true
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText(mark, 0f, (h / 2).toFloat(), paint)
        canvas.save()
        canvas.restore()
        return@withContext bmp
    }
}