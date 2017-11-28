package com.example.cdialoglib

/**
 * 常量管理类
 *
 * @author Chengguo on 2017/11/27.
 */
class CDialogConstantUtil {
    companion object {
        //拍照权限请求码
        val PERMISSION_CAMERA_REQUEST_CODE = 100
        //sdcard中读取数据的权限请求码
        val PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 101
        //写入数据到扩展存储卡SD请求码
        val PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 102
        //裁剪请求吗
        val REQUEST_CROP_CODE = 103
        //从本地相册中获取
        val RESULT_LOAD_CODE = 104
        //拍照
        val RESULT_PHOTO_CODE = 105
    }
}