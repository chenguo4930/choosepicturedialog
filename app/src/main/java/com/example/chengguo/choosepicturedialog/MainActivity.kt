package com.example.chengguo.choosepicturedialog

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Toast
import com.example.cdialoglib.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var mChoosePictureDialog: ChoosePictrueDialog
    private var mProgressDialog: ProDialog? = null

    private lateinit var imgPath: String
    private var imgFile: String? = null
    private var picBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        choosePictureBtn.setOnClickListener {
            showChoosePictureDialog()
        }
    }

    /**
     * 显示图片选择框
     */
    private fun showChoosePictureDialog() {
        mChoosePictureDialog = ChoosePictrueDialog(this)
        mChoosePictureDialog.showChoosePictureDialog()
    }

    /**
     * 关闭图片选择框
     */
    private fun closeChoosePictureDialog() {
        mChoosePictureDialog.closeChoosePictureDialog()
    }

    /**
     * 显示进度条Dialog
     */
    private fun showProgressDialog() {
        mProgressDialog = ProDialog(this)
        mProgressDialog?.setProgressBar(true)?.showProDialog()
    }

    /**
     * 关闭进度条Dialog
     */
    private fun closeProgressDialog() {
        mProgressDialog?.closeProDialog()
    }

    /**
     * 通过点击ChoosePictureDialog按钮的回调方法
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CDialogConstantUtil.RESULT_PHOTO_CODE -> {
                //拍照
                closeChoosePictureDialog()
                val photoUri = ChoosePictureUtil.photoUri
                if (photoUri != null) {
                    //通知系统刷新图库
                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photoUri)
                    sendBroadcast(intent)
                    //启动裁剪
                    cropImageUri(photoUri)
                }
            }

            CDialogConstantUtil.RESULT_LOAD_CODE -> {
                //从相册中选择图片
                closeChoosePictureDialog()
                if (data != null) {
                    val uri = data.data //获取图片是以content开头
                    if (uri != null) {
                        cropImageUri(uri) // 开始裁剪
                    }
                }
            }

            CDialogConstantUtil.REQUEST_CROP_CODE -> {
                if (data != null) {
                    compressUri(imgPath)
                }
            }
        }
    }


    private fun getPictureDegress(filePath: String): Int {
        val degree: Int
        var exifInterface: ExifInterface? = null
        try {
            exifInterface = ExifInterface(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (exifInterface != null) {
            //获得图片拍摄角度，第二个的作用是如果这个属性不存在，则作为默认值返回
            val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
            return degree
        }
        return 0
    }

    /**
     * 解决图片旋转的问题
     * 旋转图片
     * @param imgPath 原图路径
     * @ param imgPath
     */
    private fun setBitmapDegreeZero(imgPath: String) {
        val degree = getPictureDegress(imgPath)
        var mBitmap = BitmapFactory.decodeFile(imgPath)
        if (degree != 0) {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap!!.width,
                    mBitmap.height, matrix, true)
            saveBitmap(mBitmap, imgPath, 100)
        }
    }

    /**
     * 添加图片到sd卡并规定压缩比例，100默认原图
     */
    private fun saveBitmap(bitmap: Bitmap?, savePath: String, quality: Int): File? {
        if (bitmap == null)
            return null
        try {
            val f = File(savePath)
            if (f.exists()) f.delete()
            val fos = FileOutputStream(f)
            f.createNewFile()
            // 把Bitmap对象解析成流
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
            fos.flush()
            fos.close()
            bitmap.recycle()
            return f
        } catch (e: IOException) {
            e.printStackTrace()
            bitmap.recycle()
            return null
        }

    }

    /**
     * 压缩图片
     */
    fun compressUri(imgPath: String) {

        showProgressDialog()
        mProgressDialog?.setMessage("图片压缩中...")
        doAsync {
            picBitmap = BitmapFun.imgPathToBitmap1(imgPath, BitmapFun.BitmapFunConfig.RGB_565)
            if (picBitmap == null) {
                imgFile = ""
            } else {
                //压缩
                picBitmap = BitmapFun.compressBitmapByRatio(
                        picBitmap!!,
                        400F,
                        400F,
                        BitmapFun.BitmapFunConfig.RGB_565,
                        BitmapFun.BitmapFunCompressFormat.JPEG)
                //将字节码转换成base64码
                imgFile = bitmapToBase64(picBitmap!!)
            }

            uiThread {
                if (picBitmap != null) {
                    closeProgressDialog()
                    closeChoosePictureDialog()
                    imgView.setImageBitmap(picBitmap)
                } else {
                    toast("图片获取失败")
                }
            }
        }
    }

    /**
     * 将bitmap转换成Base64
     */
    private fun bitmapToBase64(picBitmap: Bitmap): String? {
        try {
            val stream = ByteArrayOutputStream()
            picBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val bytes = stream.toByteArray()
            //将字节码转换成base64码
            val str = Base64.encodeToString(bytes, Base64.DEFAULT)
            stream.close()
            return str
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 开启Android裁剪的功能
     */
    private fun cropImageUri(photoUri: Uri) {
        val dirfilePath = Environment.getExternalStorageDirectory().absolutePath + File.separator +
                "demo" + File.separator + "photoCache"
        if (!FileUtil.isFileExist(dirfilePath)) {// 创建文件夹
            FileUtil.createSDDirs(dirfilePath)
        }
        // 照片URL地址-获取系统时间 然后将裁剪后的图片保存至指定的文件夹
        imgPath = dirfilePath + File.separator + System.currentTimeMillis() + ".jpg"
        val imgFile = File(imgPath)
        val imageUri = Uri.fromFile(imgFile)

        /**
         * 开启Android截图的功能
         */
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(photoUri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 100)
        intent.putExtra("aspectY", 100)
        intent.putExtra("outputX", 500)//X方向上的比例
        intent.putExtra("outputY", 500)//Y方向上的比例
        intent.putExtra("scale", true)//是否保留比例
        // 添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // 输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        // 输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true)
        intent.putExtra("return-data", false)
        // 竖屏
        intent.putExtra(MediaStore.Images.ImageColumns.ORIENTATION, 0)
        startActivityForResult(intent, CDialogConstantUtil.REQUEST_CROP_CODE)

    }

    /**
     *     权限设置结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CDialogConstantUtil.PERMISSION_CAMERA_REQUEST_CODE -> if (grantResults.size == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mChoosePictureDialog.startPhoto()
            } else {
                Toast.makeText(this, "获取拍照权限失败", Toast.LENGTH_SHORT).show()
            }
            CDialogConstantUtil.PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE -> if (grantResults.size == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mChoosePictureDialog.startPhoto()
            } else {
                Toast.makeText(this, "sdcard中读取数据的权限失败", Toast.LENGTH_SHORT).show()
            }
            CDialogConstantUtil.PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> if (grantResults.size == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mChoosePictureDialog.startPhoto()
            } else {
                Toast.makeText(this, "写入数据到扩展存储卡(SD)权限失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
