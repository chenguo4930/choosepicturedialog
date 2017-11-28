package com.example.cdialoglib

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.annotation.StyleRes
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

/**
 * 自定义进度条Dialog
 * Created by 邹峰立 on 2017/7/5.
 */
class ProDialog @JvmOverloads constructor(private val context: Context, @StyleRes themeResId: Int = R.style.proDialog) {
    private val dialog: Dialog?
    private var imageView: ImageView? = null
    private var messageTv: TextView? = null
    private var progressBar: ProgressBar? = null

    enum class ProDialogGravity {
        GRAVITY_CENTER,
        GRAVITY_LEFT,
        GRAVITY_RIGHT,
        GRAVITY_TOP,
        GRAVITY_BOTTOM
    }

    init {
        dialog = Dialog(context, themeResId)
        init()
    }

    /**
     * 初始化控件
     */
    private fun init() {
        dialog!!.setContentView(R.layout.layout_progress_dialog)

        imageView = dialog.findViewById<View>(R.id.dialog_statue) as ImageView
        imageView!!.visibility = View.GONE
        messageTv = dialog.findViewById<View>(R.id.dialog_message) as TextView
        messageTv!!.visibility = View.GONE
        progressBar = dialog.findViewById<View>(R.id.dialog_progress) as ProgressBar
        progressBar!!.visibility = View.VISIBLE

        // 按返回键是否取消
        dialog.setCancelable(true)
        // 点击Dialog外围是否取消
        dialog.setCanceledOnTouchOutside(false)
        // 设置默认透明度0.2f
        this.setDimAmount(0.2f)
    }

    /**
     * 给Dialog设置提示信息
     */
    fun setMessage(message: CharSequence): ProDialog {
        if (dialog!!.isShowing && !TextUtils.isEmpty(message)) {
            messageTv!!.visibility = View.VISIBLE
            messageTv!!.text = message
            messageTv!!.invalidate()// 强制刷新
        }
        return this
    }

    /**
     * 给Dialog设置提示信息颜色
     *
     * @param color 16进制颜色
     */
    fun setMessageColor(color: String): ProDialog {
        try {
            if (dialog!!.isShowing && messageTv != null) {
                messageTv!!.setTextColor(Color.parseColor(color))
                messageTv!!.invalidate()// 强制刷新
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 改变dialog的状态图片
     */
    fun setImageStatue(source: Int): ProDialog {
        if (dialog!!.isShowing) {
            progressBar!!.visibility = View.GONE
            imageView!!.visibility = View.VISIBLE
            imageView!!.setImageResource(source)
            imageView!!.invalidate()// 强制刷新
        }
        return this
    }

    /**
     * 显示ProgressBar
     */
    fun setProgressBar(bool: Boolean): ProDialog {
        if (dialog!!.isShowing) {
            if (bool) {
                imageView!!.visibility = View.GONE
                progressBar!!.visibility = View.VISIBLE
            } else
                progressBar!!.visibility = View.GONE
            progressBar!!.invalidate()
        }
        return this
    }

    /**
     * 按返回键是否取消
     *
     * @param cancelable true 取消 false 不取消  默认true
     */
    fun setCancelable(cancelable: Boolean): ProDialog {
        dialog?.setCancelable(cancelable)
        return this
    }

    /**
     * 点击Dialog外围是否取消
     *
     * @param cancelable true 取消 false 不取消  默认false
     */
    fun setCanceledOnTouchOutside(cancelable: Boolean): ProDialog {
        dialog?.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 设置取消事件
     *
     * @param onCancelListener 取消事件
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): ProDialog {
        dialog?.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * 设置Dialog显示位置
     *
     * @param proDialogGravity 左上右下中
     */
    fun setProDialogGravity(proDialogGravity: ProDialogGravity): ProDialog {
        val window = dialog!!.window
        var gravity = Gravity.CENTER
        if (proDialogGravity == ProDialogGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM
        } else if (proDialogGravity == ProDialogGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER
        } else if (proDialogGravity == ProDialogGravity.GRAVITY_LEFT) {
            gravity = Gravity.START
        } else if (proDialogGravity == ProDialogGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END
        } else if (proDialogGravity == ProDialogGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP
        }
        if (window != null)
            window.attributes.gravity = gravity
        return this
    }

    /**
     * 设置背景层透明度
     *
     * @param dimAmount 0~1
     */
    fun setDimAmount(dimAmount: Float): ProDialog {
        val window = dialog!!.window
        if (window != null) {
            val lp = window.attributes
            // 设置背景层透明度
            lp.dimAmount = dimAmount
            window.attributes = lp
        }
        return this
    }

    /**
     * 设置Dialog宽度
     *
     * @param proportion 和屏幕的宽度比(10代表10%) 0~100
     */
    fun setDelDialogWidth(proportion: Int): ProDialog {
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val lp = window.attributes
                lp.width = getScreenW(context) * proportion / 100
                window.attributes = lp
            }
        }
        return this
    }

    /**
     * 设置Dialog高度
     *
     * @param proportion 和屏幕的高度比(10代表10%) 0~100
     */
    fun setProDialogHeight(proportion: Int): ProDialog {
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val lp = window.attributes
                lp.height = getScreenH(context) * proportion / 100
                window.attributes = lp
            }
        }
        return this
    }

    /**
     * 设置Window动画
     *
     * @param style R文件
     */
    fun setWindowAnimations(style: Int): ProDialog {
        if (dialog != null) {
            val window = dialog.window
            window?.setWindowAnimations(style)
        }
        return this
    }

    /**
     * 获取屏幕宽度
     */
    private fun getScreenW(aty: Context): Int {
        val dm = aty.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 获取屏幕的高度
     */
    private fun getScreenH(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 展示Dialog
     */
    fun showProDialog() {
        dialog?.show()
    }

    /**
     * 关闭Dialog
     */
    fun closeProDialog() {
        dialog?.cancel()
    }

}