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
import android.widget.Button

/**
 * 删除Dialog
 * Created by 邹峰立 on 2017/7/5.
 */
class DelDialog @JvmOverloads constructor(private val context: Context, @StyleRes themeResId: Int = R.style.diydialog) {
    private val dialog: Dialog?
    private var delBtn: Button? = null
    private var cancelBtn: Button? = null

    private var onDelListener: OnDelListener? = null

    private var onDelCancelListener: OnDelCancelListener? = null

    enum class DelDialogGravity {
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
        dialog!!.setContentView(R.layout.layout_del_dialog)
        delBtn = dialog.findViewById<View>(R.id.btn_del) as Button
        delBtn!!.setOnClickListener(View.OnClickListener {
            if (CDialogClickUtil.synchronisFastClick())
                return@OnClickListener
            closeDelDialog()
            if (onDelListener != null)
                onDelListener!!.onDel()
        })
        cancelBtn = dialog.findViewById<View>(R.id.btn_cancel) as Button
        cancelBtn!!.setOnClickListener(View.OnClickListener {
            if (CDialogClickUtil.synchronisFastClick())
                return@OnClickListener
            closeDelDialog()
            if (onDelCancelListener != null)
                onDelCancelListener!!.onCancel()
        })

        // 按返回键是否取消
        dialog.setCancelable(true)
        // 点击Dialog外围是否取消
        dialog.setCanceledOnTouchOutside(true)
        // 设置默认透明度0.2f
        this.setDimAmount(0.2f)
        // 设置默认居中显示
        this.setDelDialogGravity(DelDialogGravity.GRAVITY_CENTER)
        // 设置和屏幕的宽度比
        this.setDelDialogWidth(60)
    }

    /**
     * 设置DelBtn文字大小
     *
     * @param size 文字大小
     */
    fun setDelBtnSize(size: Float): DelDialog {
        if (delBtn != null)
            delBtn!!.textSize = size
        return this
    }

    /**
     * 设置DelBtn文字颜色
     *
     * @param color 文字颜色
     */
    fun setDelBtnColor(color: String): DelDialog {
        try {
            if (delBtn != null && !TextUtils.isEmpty(color))
                delBtn!!.setTextColor(Color.parseColor(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 设置CancelBtn文字大小
     *
     * @param size 文字大小
     */
    fun setCancelBtnSize(size: Float): DelDialog {
        if (cancelBtn != null)
            cancelBtn!!.textSize = size
        return this
    }

    /**
     * 设置CancelBtn文字颜色
     *
     * @param color 文字颜色
     */
    fun setCancelBtnColor(color: String): DelDialog {
        try {
            if (cancelBtn != null && !TextUtils.isEmpty(color))
                cancelBtn!!.setTextColor(Color.parseColor(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 按返回键是否取消
     *
     * @param cancelable true 取消 false 不取消  默认true
     */
    fun setCancelable(cancelable: Boolean): DelDialog {
        dialog?.setCancelable(cancelable)
        return this
    }

    /**
     * 点击Dialog外围是否取消
     *
     * @param cancelable true 取消 false 不取消  默认false
     */
    fun setCanceledOnTouchOutside(cancelable: Boolean): DelDialog {
        dialog?.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 设置取消事件
     *
     * @param onCancelListener 取消事件
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): DelDialog {
        dialog?.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * 设置Dialog宽度
     *
     * @param proportion 和屏幕的宽度比(10代表10%) 0~100
     */
    fun setDelDialogWidth(proportion: Int): DelDialog {
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
    fun setDelDialogHeight(proportion: Int): DelDialog {
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
    fun setWindowAnimations(style: Int): DelDialog {
        if (dialog != null) {
            val window = dialog.window
            window?.setWindowAnimations(style)
        }
        return this
    }

    /**
     * 设置Dialog显示位置
     *
     * @param delDialogGravity 左上右下中
     */
    fun setDelDialogGravity(delDialogGravity: DelDialogGravity): DelDialog {
        if (dialog != null) {
            val window = dialog.window
            var gravity = Gravity.CENTER
            if (delDialogGravity == DelDialogGravity.GRAVITY_BOTTOM) {
                gravity = Gravity.BOTTOM
            } else if (delDialogGravity == DelDialogGravity.GRAVITY_CENTER) {
                gravity = Gravity.CENTER
            } else if (delDialogGravity == DelDialogGravity.GRAVITY_LEFT) {
                gravity = Gravity.START
            } else if (delDialogGravity == DelDialogGravity.GRAVITY_RIGHT) {
                gravity = Gravity.END
            } else if (delDialogGravity == DelDialogGravity.GRAVITY_TOP) {
                gravity = Gravity.TOP
            }
            if (window != null)
                window.attributes.gravity = gravity
        }
        return this
    }

    /**
     * 设置背景层透明度
     *
     * @param dimAmount 0~1
     */
    fun setDimAmount(dimAmount: Float): DelDialog {
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val lp = window.attributes
                // 设置背景层透明度
                lp.dimAmount = dimAmount
                window.attributes = lp
            }
        }
        return this
    }

    /**
     * 展示Dialog
     */
    fun showDelDialog() {
        dialog?.show()
    }

    /**
     * 关闭Dialog
     */
    fun closeDelDialog() {
        dialog?.cancel()
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

    // 执行删除接口
    interface OnDelListener {
        fun onDel()
    }

    fun setOnDelListener(onDelListener: OnDelListener) {
        this.onDelListener = onDelListener
    }

    // 执行取消接口
    interface OnDelCancelListener {
        fun onCancel()
    }

    fun setOnDelCancelListener(onDelCancelListener: OnDelCancelListener) {
        this.onDelCancelListener = onDelCancelListener
    }

}