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
import android.widget.TextView

/**
 * 提示Dialog
 * Created by 邹峰立 on 2017/7/5.
 */
class TipDialog @JvmOverloads constructor(private val context: Context, @StyleRes themeResId: Int = R.style.diydialog) {
    private val dialog: Dialog?
    private var titleTv: TextView? = null
    private var descTv: TextView? = null
    private var ensureTv: TextView? = null
    private var cancelTv: TextView? = null

    private var onTipEnsureListener: OnTipEnsureListener? = null

    private var onTipCancelListener: OnTipCancelListener? = null

    enum class TipDialogGravity {
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
     * 初始化
     */
    private fun init() {
        dialog!!.setContentView(R.layout.layout_tip_dialog)
        titleTv = dialog.findViewById<View>(R.id.tv_title) as TextView
        descTv = dialog.findViewById<View>(R.id.tv_desc) as TextView
        ensureTv = dialog.findViewById<View>(R.id.tv_ensure) as TextView
        ensureTv!!.setOnClickListener(View.OnClickListener {
            if (CDialogClickUtil.synchronisFastClick())
                return@OnClickListener
            // 关闭Dialog
            closeTipDialog()
            if (onTipEnsureListener != null) {
                onTipEnsureListener!!.onEnsure()
            }
        })
        cancelTv = dialog.findViewById<View>(R.id.tv_cancel) as TextView
        cancelTv!!.setOnClickListener(View.OnClickListener {
            if (CDialogClickUtil.synchronisFastClick())
                return@OnClickListener
            // 关闭Dialog
            closeTipDialog()
            if (onTipCancelListener != null) {
                onTipCancelListener!!.onCancel()
            }
        })

        // 按返回键是否取消
        dialog.setCancelable(true)
        // 点击Dialog外围是否取消
        dialog.setCanceledOnTouchOutside(true)
        // 设置默认透明度0.2f
        this.setDimAmount(0.2f)
        // 设置Dialog宽度
        this.setTipDialogWidth(70)
        // 设置Dialog默认位置居中
        this.setTipDialogGravity(TipDialogGravity.GRAVITY_CENTER)
    }

    /**
     * 修改主题文字
     *
     * @param text 文字内容
     */
    fun setTitleText(text: String): TipDialog {
        if (titleTv != null && !TextUtils.isEmpty(text)) {
            titleTv!!.text = text
            titleTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改主题文字大小
     *
     * @param size 文字大小
     */
    fun setTitleTextSize(size: Int): TipDialog {
        if (titleTv != null) {
            titleTv!!.textSize = size.toFloat()
            titleTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改主题文字颜色
     *
     * @param color 文字颜色
     */
    fun setTitleColor(color: String): TipDialog {
        try {
            if (titleTv != null && !TextUtils.isEmpty(color)) {
                titleTv!!.setTextColor(Color.parseColor(color))
                titleTv!!.invalidate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改主题是否显示
     *
     * @param visible true显示  false不显示 默认显示
     */
    fun setTitleVisible(visible: Boolean): TipDialog {
        if (titleTv != null) {
            titleTv!!.visibility = if (visible) View.VISIBLE else View.GONE
        }
        return this
    }

    /**
     * 修改描述文字
     *
     * @param text 文字内容
     */
    fun setDescText(text: String): TipDialog {
        if (descTv != null && !TextUtils.isEmpty(text)) {
            descTv!!.text = text
            descTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改描述文字大小
     *
     * @param size 文字大小
     */
    fun setDescTextSize(size: Int): TipDialog {
        if (descTv != null) {
            descTv!!.textSize = size.toFloat()
            descTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改描述文字颜色
     *
     * @param color 文字颜色
     */
    fun setDescColor(color: String): TipDialog {
        try {
            if (descTv != null && !TextUtils.isEmpty(color)) {
                descTv!!.setTextColor(Color.parseColor(color))
                descTv!!.invalidate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改描述是否显示
     *
     * @param visible true显示  false不显示 默认显示
     */
    fun setDescVisible(visible: Boolean): TipDialog {
        if (descTv != null) {
            descTv!!.visibility = if (visible) View.VISIBLE else View.GONE
        }
        return this
    }

    /**
     * 修改确定文字
     *
     * @param text 文字内容
     */
    fun setEnsureText(text: String): TipDialog {
        if (ensureTv != null && !TextUtils.isEmpty(text)) {
            ensureTv!!.text = text
            ensureTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改确定文字
     *
     * @param size 文字大小
     */
    fun setEnsureTextSize(size: Int): TipDialog {
        if (ensureTv != null) {
            ensureTv!!.textSize = size.toFloat()
            ensureTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改确定文字颜色
     *
     * @param color 文字颜色
     */
    fun setEnsureColor(color: String): TipDialog {
        try {
            if (ensureTv != null && !TextUtils.isEmpty(color)) {
                ensureTv!!.setTextColor(Color.parseColor(color))
                ensureTv!!.invalidate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改取消文字
     *
     * @param text 文字内容
     */
    fun setCancelText(text: String): TipDialog {
        if (cancelTv != null && !TextUtils.isEmpty(text)) {
            cancelTv!!.text = text
            cancelTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改取消文字大小
     *
     * @param size 文字大小
     */
    fun setCancelTextSize(size: Int): TipDialog {
        if (cancelTv != null) {
            cancelTv!!.textSize = size.toFloat()
            cancelTv!!.invalidate()
        }
        return this
    }

    /**
     * 修改取消文字颜色
     *
     * @param color 文字颜色
     */
    fun setCancelColor(color: String): TipDialog {
        try {
            if (cancelTv != null && !TextUtils.isEmpty(color)) {
                cancelTv!!.setTextColor(Color.parseColor(color))
                cancelTv!!.invalidate()
            }
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
    fun setCancelable(cancelable: Boolean): TipDialog {
        dialog?.setCancelable(cancelable)
        return this
    }

    /**
     * 点击Dialog外围是否取消
     *
     * @param cancelable true 取消 false 不取消  默认true
     */
    fun setCanceledOnTouchOutside(cancelable: Boolean): TipDialog {
        dialog?.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 设置取消事件
     *
     * @param onCancelListener 取消事件
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): TipDialog {
        dialog?.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * 设置Dialog显示位置
     *
     * @param tipDialogGravity 左上右下中
     */
    fun setTipDialogGravity(tipDialogGravity: TipDialogGravity): TipDialog {
        val window = dialog!!.window
        var gravity = Gravity.CENTER
        if (tipDialogGravity == TipDialogGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM
        } else if (tipDialogGravity == TipDialogGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER
        } else if (tipDialogGravity == TipDialogGravity.GRAVITY_LEFT) {
            gravity = Gravity.START
        } else if (tipDialogGravity == TipDialogGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END
        } else if (tipDialogGravity == TipDialogGravity.GRAVITY_TOP) {
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
    fun setDimAmount(dimAmount: Float): TipDialog {
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
     * 设置Window动画
     *
     * @param style R文件
     */
    fun setWindowAnimations(style: Int): TipDialog {
        if (dialog != null) {
            val window = dialog.window
            window?.setWindowAnimations(style)
        }
        return this
    }

    /**
     * 设置Dialog宽度
     *
     * @param proportion 和屏幕的宽度比(10代表10%) 0~100
     */
    fun setTipDialogWidth(proportion: Int): TipDialog {
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
    fun setTipDialogHeight(proportion: Int): TipDialog {
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
    fun showTipDialog() {
        dialog?.show()
    }

    /**
     * 关闭Dialog
     */
    fun closeTipDialog() {
        dialog?.cancel()
    }

    // 确定事件监听
    interface OnTipEnsureListener {
        fun onEnsure()
    }

    fun setOnTipEnsureListener(onTipEnsureListener: OnTipEnsureListener) {
        this.onTipEnsureListener = onTipEnsureListener
    }

    // 取消事件监听
    interface OnTipCancelListener {
        fun onCancel()
    }

    fun setOnTipCancelListener(onTipCancelListener: OnTipCancelListener) {
        this.onTipCancelListener = onTipCancelListener
    }
}
