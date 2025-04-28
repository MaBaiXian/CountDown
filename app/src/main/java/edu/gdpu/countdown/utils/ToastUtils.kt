package edu.gdpu.countdown.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import edu.gdpu.countdown.R

object ToastUtils {
    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        // 设置文本
        val textView = layout.findViewById<TextView>(R.id.custom_toast_text)
        textView.text = message


        // 创建并显示 Toast
        val toast = Toast(context).apply {
            setGravity(Gravity.CENTER or Gravity.BOTTOM, 0, 100) // 位置：底部居中，向上偏移100px
            this.duration = duration
            view = layout
        }

        toast.show()
    }

}