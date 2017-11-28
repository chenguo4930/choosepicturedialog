package com.example.cdialoglib

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button

/**
 *  选择图片Dialog
 *
 * @author Chengguo on 2017/11/27.
 */
class ChoosePictrueDialog(val context: Context, val themeResId: Int = R.style.diydialog) {
    var dialog: Dialog? = null
    private var onLocalListener: OnLocalLlistener? = null
    private var onPhotoListener: OnPhotoLlistener? = null
    private var onCancelListener: OnCancelListener? = null

    private var localBtn: Button
    private var photoBtn: Button
    private var cancelBtn: Button

    init {
        dialog = Dialog(context, themeResId)

        val view = LayoutInflater.from(context).inflate(R.layout.layout_choose_pictrue, null)
        localBtn = view.findViewById<Button>(R.id.btn_local)
        localBtn.setOnClickListener {
            if (CDialogClickUtil.synchronisFastClick()) {
                return@setOnClickListener
            }
            dialog?.cancel()
            onLocalListener?.onLocal() ?: ChoosePictureUtil.startLocal(context) //启动本地相册
        }

        photoBtn = view.findViewById<Button>(R.id.btn_photo)
        photoBtn.setOnClickListener {
            if (CDialogClickUtil.synchronisFastClick()) {
                return@setOnClickListener
            }
            dialog?.cancel()
            startPhoto()
        }

        cancelBtn = view.findViewById<Button>(R.id.btn_cancel)
        cancelBtn.setOnClickListener {
            if (CDialogClickUtil.synchronisFastClick()) {
                return@setOnClickListener
            }
            dialog?.cancel()
            onCancelListener?.onCancle()
        }

        dialog?.run {
            setContentView(view)
            //按返回键是否取消
            setCancelable(false)
            //点击Dialog外围是否取消
            setCanceledOnTouchOutside(true)
            //设置居中
            setChoosePictureDialogGravity(ChoosePictureDialogGravity.GRAVITY_CENTER)
            //设置宽度
            setChoosePictureDialogWith(75)
        }

    }

    /**
     * 启动拍照
     */
    public fun startPhoto() {
        if (onPhotoListener != null) {
            onPhotoListener?.onPhoto()
        } else {
            //判断是否需要拍照权限
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(
                        Manifest.permission.CAMERA),
                        CDialogConstantUtil.PERMISSION_CAMERA_REQUEST_CODE)
            } else {
                // 判断是否需要sdcard中读取数据的权限
                val checkCallSDReadPermission = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                if (checkCallSDReadPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            CDialogConstantUtil.PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE)
                } else {
                    // 判断是否需要写入数据到扩展存储卡(SD)的权限
                    val checkCallSDWritePermission = ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (checkCallSDWritePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context as Activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                CDialogConstantUtil.PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
                    } else {
                        // 启动拍照
                        ChoosePictureUtil.startPhoto3(context)
                    }
                }
            }
        }
    }

    /**
     * 显示Dialog
     */
    public fun showChoosePictureDialog(){
        dialog?.show()
    }

    /**
     * 关闭Dialog
     */
    public fun closeChoosePictureDialog(){
        dialog?.cancel()
    }

    /**
     * 设置dialog宽度
     */
    public fun setChoosePictureDialogWith(proportion: Int): ChoosePictrueDialog {
        if (dialog != null) {
            val window = dialog!!.window
            val lp = window.attributes
            lp.width = getScreenW(context) * proportion / 100
            window.attributes = lp
        }
        return this
    }

    /**
     * 设置dialog高度
     */
    public fun setChoosePictureDialogHeight(proportion: Int): ChoosePictrueDialog {
        if (dialog != null) {
            val window = dialog!!.window
            val lp = window.attributes
            lp.height = getScreenH(context) * proportion / 100
            window.attributes = lp
        }
        return this
    }

    /**
     * 获取屏幕宽度
     */
    private fun getScreenW(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     */
    private fun getScreenH(context: Context) = context.resources.displayMetrics.heightPixels

    /**
     * 设置Dialog显示位置
     */
    public fun setChoosePictureDialogGravity(choosePictureDialogGravity: ChoosePictureDialogGravity)
            : ChoosePictrueDialog {
        val window = dialog?.window
        var gravity = Gravity.CENTER
        gravity = when (choosePictureDialogGravity) {
            ChoosePictureDialogGravity.GRAVITY_BOTTOM -> Gravity.BOTTOM
            ChoosePictureDialogGravity.GRAVITY_CENTER -> Gravity.CENTER
            ChoosePictureDialogGravity.GRAVITY_LEFT -> Gravity.LEFT
            ChoosePictureDialogGravity.GRAVITY_RIGHT -> Gravity.RIGHT
            ChoosePictureDialogGravity.GRAVITY_TOP -> Gravity.TOP
        }
        window?.attributes?.gravity = gravity
        return this
    }

    /**
     * 设置三个按钮的字体颜色
     */
    fun setBtnColor(color: String): ChoosePictrueDialog {
        localBtn.setTextColor(Color.parseColor(color))
        photoBtn.setTextColor(Color.parseColor(color))
        cancelBtn.setTextColor(Color.parseColor(color))
        return this
    }

    /**
     * 设置字体大小
     */
    fun setBtnSize(size: Float): ChoosePictrueDialog {
        localBtn.textSize = size
        photoBtn.textSize = size
        cancelBtn.textSize = size
        return this
    }

    /**
     * 本地按钮点击接口
     */
    interface OnLocalLlistener {
        fun onLocal()
    }

    fun setOnLocalListener(onLocalListener: OnLocalLlistener) {
        this.onLocalListener = onLocalListener
    }

    /**
     * 修改本地按钮文本颜色
     *
     * @param color 文本颜色
     */
    fun setLocalBtnColor(color: String): ChoosePictrueDialog {
        try {
            localBtn.setTextColor(Color.parseColor(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改拍照按钮文本颜色
     *
     * @param color 文本颜色
     */
    fun setPhotoBtnColor(color: String): ChoosePictrueDialog {
        try {
            photoBtn.setTextColor(Color.parseColor(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改取消按钮文本颜色
     *
     * @param color 文本颜色
     */
    fun setCancelBtnColor(color: String): ChoosePictrueDialog {
        try {
            cancelBtn.setTextColor(Color.parseColor(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * 修改本地按钮文本字体大小
     *
     * @param size 字体大小
     */
    fun setLocalBtnSize(size: Float): ChoosePictrueDialog {
        localBtn.textSize = size
        return this
    }

    /**
     * 修改拍照按钮文本字体大小
     *
     * @param size 字体大小
     */
    fun setPhotoBtnSize(size: Float): ChoosePictrueDialog {
        photoBtn.textSize = size
        return this
    }

    /**
     * 修改取消按钮文本字体大小
     *
     * @param size 字体大小
     */
    fun setCancelBtnSize(size: Float): ChoosePictrueDialog {
        cancelBtn.textSize = size
        return this
    }

    /**
     * 修改本地按钮文本
     *
     * @param text 文本信息
     */
    fun setLocalBtnText(text: String): ChoosePictrueDialog {
        if (!TextUtils.isEmpty(text))
            localBtn.text = text
        return this
    }

    /**
     * 修改拍照按钮文本
     *
     * @param text 文本信息
     */
    fun setPhotoBtnText(text: String): ChoosePictrueDialog {
        if (!TextUtils.isEmpty(text))
            photoBtn.text = text
        return this
    }

    /**
     * 修改取消按钮文本
     *
     * @param text 文本信息
     */
    fun setCancelBtnText(text: String): ChoosePictrueDialog {
        if (!TextUtils.isEmpty(text))
            cancelBtn.text = text
        return this
    }

    /**
     * 按返回键是否取消
     *
     * @param cancelable true 取消 false 不取消  默认true
     */
    fun setCancelable(cancelable: Boolean): ChoosePictrueDialog {
            dialog?.setCancelable(cancelable)
        return this
    }

    /**
     * 点击Dialog外围是否取消
     *
     * @param cancelable true 取消 false 不取消  默认false
     */
    fun setCanceledOnTouchOutside(cancelable: Boolean): ChoosePictrueDialog {
            dialog?.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 设置取消事件
     *
     * @param onCancelListener 取消事件
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): ChoosePictrueDialog {
            dialog?.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * 设置背景层透明度
     *
     * @param dimAmount 0~1
     */
    fun setDimAmount(dimAmount: Float): ChoosePictrueDialog {
        val window = dialog?.window
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
    fun setWindowAnimations(style: Int): ChoosePictrueDialog {
        if (dialog != null) {
            val window = dialog?.window
            window?.setWindowAnimations(style)
        }
        return this
    }

    /**
     * 本地按钮点击接口
     */
    interface OnPhotoLlistener {
        fun onPhoto()
    }

    fun setOnPhotoListener(onPhotoListener: OnPhotoLlistener) {
        this.onPhotoListener = onPhotoListener
    }

    /**
     * 取消按钮点击接口
     */
    public interface OnCancelListener {
        fun onCancle()
    }

    fun setOnCancelListener(onCancelListener: OnCancelListener) {
        this.onCancelListener = onCancelListener
    }

}

