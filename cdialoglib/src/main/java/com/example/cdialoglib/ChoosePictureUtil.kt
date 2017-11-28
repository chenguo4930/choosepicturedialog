package com.example.cdialoglib

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File

/**
 *
 *  选择图片管理类
 *
 * @author Chengguo on 2017/11/27.
 */
class ChoosePictureUtil {
    companion object {
        var photoUri: Uri? = null  // 拍照URI,拍照相对于新生成的，所以要进行转换
        var imgPath: String? = null //图片地址

        /**
         * 生成一个URI地址
         */
        fun createImageViewUri(context: Context): Uri {
            val name = "temp" + System.currentTimeMillis()
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, name)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".png")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }

        /**
         * 删除Uri
         */
        fun deleteUri(context: Context, uri: Uri?) {
            if (uri != null) {
                context.contentResolver.delete(uri, null, null)
            }
        }

        /**
         * 启动拍照1
         */
        fun startPhoto(context: Context) {
            photoUri = createImageViewUri(context)
            if (photoUri != null) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //将拍照后的图像保存到photoUri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                (context as Activity).startActivityForResult(intent, CDialogConstantUtil.RESULT_PHOTO_CODE)
            }
        }

        /**
         * 启动拍照2
         */
        fun startPhoto2(context: Context) {
            (context as Activity).startActivityForResult(
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                    CDialogConstantUtil.RESULT_PHOTO_CODE)
        }

        /**
         * 启动拍照3
         */
        fun startPhoto3(context: Context) {
            try {
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    //创建文件夹
                    var isSuccess = true
                    val dirFile = File(Environment.getExternalStorageDirectory().absolutePath, "CDialogPhotoCache")
                    if (!dirFile.exists()) {
                        isSuccess = dirFile.mkdirs()
                    }
                    if (isSuccess) {
                        //创建文件
                        imgPath = dirFile.absolutePath + File.separator + System.currentTimeMillis() + ".png"
                        val imgFile = File(imgPath)

                        val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        //判断是否是AndroidM以及更高版本
                        photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            CFileProvider.getUriForFile(context,
                                    BuildConfig.APPLICATION_ID + ".fileProvider",
                                    imgFile)
                        } else {
                            Uri.fromFile(imgFile)
                        }
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        (context as Activity).startActivityForResult(openCameraIntent, CDialogConstantUtil.RESULT_PHOTO_CODE)
                    }
                } else {
                    Toast.makeText(context, "SD卡不存在，请插入SD卡", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 从相册中获取1 调用android的图库
         */
        fun startLocal(context: Context) {
            (context as Activity).startActivityForResult(
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                    CDialogConstantUtil.RESULT_LOAD_CODE
            )
        }

        /**
         * 从相册中获取2 调用android的图库
         */
        fun startLocal2(context: Context) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            (context as Activity).startActivityForResult(intent, CDialogConstantUtil.RESULT_LOAD_CODE)
        }

    }
}