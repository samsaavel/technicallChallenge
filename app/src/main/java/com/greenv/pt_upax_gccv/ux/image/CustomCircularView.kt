package com.greenv.pt_upax_gccv.ux.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.greenv.pt_upax_gccv.R

class CustomCircularView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var placeholder: Drawable? = null
    private var backgroundColor: Int = Color.GRAY
    private var textColor: Int = Color.WHITE
    private var initials: String = ""

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularImageView,
            0,
            0
        ).apply {
            try {
                placeholder = getDrawable(R.styleable.CircularImageView_placeholder)
                backgroundColor =
                    getColor(R.styleable.CircularImageView_backgroundColor, Color.GREEN)
                textColor = getColor(R.styleable.CircularImageView_textColor, Color.WHITE)
            } finally {
                recycle()
            }
        }
    }

    fun loadImageOrInitials(imageUrl: String?, name: String) {
        if (imageUrl.isNullOrEmpty()) {
            showInitials(name)
        } else {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(placeholder)
                .error { showInitials(name) }
                .into(this)
        }
    }

    private fun showInitials(name: String) {
        initials = extractInitials(name)
        val bitmap = createInitialsBitmap(initials)
        setImageBitmap(bitmap)
    }

    private fun extractInitials(name: String): String {
        val words = name.trim().split(" ")
        return when (words.size) {
            0 -> ""
            1 -> words[0].substring(0, 1).uppercase()
            else -> (words[0].substring(0, 1) + words[1].substring(0, 1)).uppercase()
        }
    }

    private fun createInitialsBitmap(initials: String): Bitmap {
        val diameter = width.coerceAtMost(height)
        val bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // Dibujar fondo circular
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(diameter / 2f, diameter / 2f, diameter / 2f, paint)

        // Dibujar iniciales
        paint.color = textColor
        paint.textSize = diameter / 2.5f
        paint.textAlign = Paint.Align.CENTER

        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(initials, xPos.toFloat(), yPos, paint)

        return bitmap
    }

    // Métodos públicos para cambiar propiedades dinámicamente
    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    fun setPlaceholder(placeholderDrawable: Drawable?) {
        placeholder = placeholderDrawable
        invalidate()
    }
}