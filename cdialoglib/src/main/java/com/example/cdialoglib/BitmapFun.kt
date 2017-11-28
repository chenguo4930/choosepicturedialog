package com.example.cdialoglib

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.BitmapFactory.decodeFile
import android.graphics.Rect
import android.net.Uri
import java.io.*

/**
 * @author Chengguo on 2017/11/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

class BitmapFun {

    enum class BitmapFunConfig private constructor() {
        RGB_565,
        ALPHA_8,
        ARGB_4444,
        ARGB_8888
    }

    enum class BitmapFunCompressFormat private constructor() {
        PNG,
        JPEG,
        WEBP
    }

    companion object {

        fun imgPathToBitmap1(imgPath: String, bitmapFunConfig: BitmapFun.BitmapFunConfig?): Bitmap {
            val options = Options()
            options.inJustDecodeBounds = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inSampleSize = 1
            options.inPreferredConfig = Config.RGB_565
            if (bitmapFunConfig != null) {
                when (bitmapFunConfig) {
                    BitmapFun.BitmapFunConfig.ALPHA_8 -> options.inPreferredConfig = Config.ALPHA_8
                    BitmapFun.BitmapFunConfig.ARGB_4444 -> options.inPreferredConfig = Config.ARGB_4444
                    BitmapFun.BitmapFunConfig.ARGB_8888 -> options.inPreferredConfig = Config.ARGB_8888
                    else -> {
                        options.inPreferredConfig = Config.RGB_565
                    }
                }
            }
            return decodeFile(imgPath,options)
        }


        fun imgPathToBitmap2(imgPath: String): Bitmap? {
            return try {
                val fis = FileInputStream(imgPath)
                BitmapFactory.decodeStream(fis)
            } catch (var2: FileNotFoundException) {
                var2.printStackTrace()
                null
            }

        }

        fun saveBitmapToUri(bitmap: Bitmap, filePath: String, bitmapFunCompressFormat: BitmapFun.BitmapFunCompressFormat?): Uri? {
            var isMkdirsSuccess = false
            val tmpDir = File(filePath)
            if (!tmpDir.exists()) {
                isMkdirsSuccess = tmpDir.mkdirs()
            }

            if (isMkdirsSuccess) {
                val imgFile = File(tmpDir.absolutePath + System.currentTimeMillis())

                try {
                    val fos = FileOutputStream(imgFile)
                    bitmap.compress(CompressFormat.PNG, 100, fos)
                    if (bitmapFunCompressFormat != null) {
                        if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.JPEG) {
                            bitmap.compress(CompressFormat.JPEG, 100, fos)
                        } else if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.WEBP) {
                            bitmap.compress(CompressFormat.WEBP, 100, fos)
                        }
                    }

                    fos.flush()
                    fos.close()
                    return Uri.fromFile(imgFile)
                } catch (var7: Exception) {
                    var7.printStackTrace()
                }

            }

            return null
        }

        fun saveBitmapToImgPath(bitmap: Bitmap, filePath: String, bitmapFunCompressFormat: BitmapFun.BitmapFunCompressFormat?): String? {
            var isMkdirsSuccess = false
            val tmpDir = File(filePath)
            if (!tmpDir.exists()) {
                isMkdirsSuccess = tmpDir.mkdirs()
            }

            if (isMkdirsSuccess) {
                val imgPath = tmpDir.absolutePath + System.currentTimeMillis() + ".png"
                val imgFile = File(imgPath)

                try {
                    val fos = FileOutputStream(imgFile)
                    bitmap.compress(CompressFormat.PNG, 100, fos)
                    if (bitmapFunCompressFormat != null) {
                        if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.JPEG) {
                            bitmap.compress(CompressFormat.JPEG, 100, fos)
                        } else if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.WEBP) {
                            bitmap.compress(CompressFormat.WEBP, 100, fos)
                        }
                    }

                    fos.flush()
                    fos.close()
                    return imgPath
                } catch (var8: Exception) {
                    var8.printStackTrace()
                    return null
                }

            } else {
                return null
            }
        }

        fun imgPathToReSizeBitmap(imgPath: String, width: Int, height: Int): Bitmap? {
            try {
                var `in` = FileInputStream(File(imgPath))
                val options = Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(`in`, null as Rect?, options)
                `in`.close()

                var i: Int
                i = 0
                while (options.outWidth shr i > width || options.outHeight shr i > height) {
                    ++i
                }

                `in` = FileInputStream(File(imgPath))
                options.inSampleSize = Math.pow(2.0, i.toDouble()).toInt()
                options.inJustDecodeBounds = false
                return BitmapFactory.decodeStream(`in`, null as Rect?, options)
            } catch (var7: Exception) {
                var7.printStackTrace()
                return null
            }

        }

        fun uriToReSizeBitmap(activity: Activity, uri: Uri, bitmapFunConfig: BitmapFun.BitmapFunConfig?, bitmapFunCompressFormat: BitmapFun.BitmapFunCompressFormat?): Bitmap? {
            try {
                var bConfig: Bitmap.Config = Config.ARGB_8888
                if (bitmapFunConfig != null) {
                    if (bitmapFunConfig == BitmapFun.BitmapFunConfig.RGB_565) {
                        bConfig = Config.RGB_565
                    } else if (bitmapFunConfig == BitmapFun.BitmapFunConfig.ALPHA_8) {
                        bConfig = Config.ALPHA_8
                    } else if (bitmapFunConfig == BitmapFun.BitmapFunConfig.ARGB_4444) {
                        bConfig = Config.ARGB_4444
                    }
                }

                var bCompressFormat = CompressFormat.PNG
                if (bitmapFunCompressFormat != null) {
                    if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.JPEG) {
                        bCompressFormat = CompressFormat.JPEG
                    } else if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.WEBP) {
                        bCompressFormat = CompressFormat.WEBP
                    }
                }

                var input = activity.contentResolver.openInputStream(uri)
                val onlyBoundsOptions = Options()
                onlyBoundsOptions.inJustDecodeBounds = true
                onlyBoundsOptions.inDither = true
                onlyBoundsOptions.inPreferredConfig = bConfig
                BitmapFactory.decodeStream(input, null as Rect?, onlyBoundsOptions)
                if (input != null) {
                    input.close()
                }

                val originalWidth = onlyBoundsOptions.outWidth
                val originalHeight = onlyBoundsOptions.outHeight
                if (originalWidth != -1 && originalHeight != -1) {
                    val hh = 800.0f
                    val ww = 480.0f
                    var be = 1
                    if (originalWidth > originalHeight && originalWidth.toFloat() > ww) {
                        be = (originalWidth.toFloat() / ww).toInt()
                    } else if (originalWidth < originalHeight && originalHeight.toFloat() > hh) {
                        be = (originalHeight.toFloat() / hh).toInt()
                    }

                    if (be <= 0) {
                        be = 1
                    }

                    val bitmapOptions = Options()
                    bitmapOptions.inSampleSize = be
                    bitmapOptions.inDither = true
                    bitmapOptions.inPreferredConfig = bConfig
                    input = activity.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(input, null as Rect?, bitmapOptions)
                    if (input != null) {
                        input.close()
                    }

                    val baos = ByteArrayOutputStream()
                    bitmap.compress(bCompressFormat, 100, baos)
                    if (baos.toByteArray().size / 1024 > 1024) {
                        baos.reset()
                        bitmap.compress(bCompressFormat, 50, baos)
                    }

                    return bitmap
                } else {
                    return null
                }
            } catch (var16: Exception) {
                var16.printStackTrace()
                return null
            }

        }

        fun compressBitmapByQuality(bitmap: Bitmap, maxSize: Int, bitmapFunCompressFormat: BitmapFun.BitmapFunCompressFormat?): Bitmap? {
            try {
                var bCompressFormat = CompressFormat.PNG
                if (bitmapFunCompressFormat != null) {
                    if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.JPEG) {
                        bCompressFormat = CompressFormat.JPEG
                    } else if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.WEBP) {
                        bCompressFormat = CompressFormat.WEBP
                    }
                }

                val baos = ByteArrayOutputStream()
                bitmap.compress(bCompressFormat, 100, baos)
                var options = 100

                while (baos.toByteArray().size / 1024 > maxSize && options > 0) {
                    baos.reset()
                    options -= 20
                    bitmap.compress(bCompressFormat, options, baos)
                }

                val isBm = ByteArrayInputStream(baos.toByteArray())
                val realBitmap = BitmapFactory.decodeStream(isBm, null as Rect?, null as Options?)
                baos.close()
                isBm.close()
                return realBitmap
            } catch (var8: Exception) {
                var8.printStackTrace()
                return null
            }

        }

        fun compressBitmapByRatio(bitmap: Bitmap, pixelW: Float, pixelH: Float, bitmapFunConfig: BitmapFun.BitmapFunConfig?, bitmapFunCompressFormat: BitmapFun.BitmapFunCompressFormat?): Bitmap {
            var bConfig: Bitmap.Config = Config.RGB_565
            if (bitmapFunConfig != null) {
                if (bitmapFunConfig == BitmapFun.BitmapFunConfig.ARGB_8888) {
                    bConfig = Config.ARGB_8888
                } else if (bitmapFunConfig == BitmapFun.BitmapFunConfig.ALPHA_8) {
                    bConfig = Config.ALPHA_8
                } else if (bitmapFunConfig == BitmapFun.BitmapFunConfig.ARGB_4444) {
                    bConfig = Config.ARGB_4444
                }
            }

            var bCompressFormat = CompressFormat.PNG
            if (bitmapFunCompressFormat != null) {
                if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.JPEG) {
                    bCompressFormat = CompressFormat.JPEG
                } else if (bitmapFunCompressFormat == BitmapFun.BitmapFunCompressFormat.WEBP) {
                    bCompressFormat = CompressFormat.WEBP
                }
            }

            val os = ByteArrayOutputStream()
            bitmap.compress(bCompressFormat, 100, os)
            if (os.toByteArray().size / 1024 > 2048) {
                os.reset()
                bitmap.compress(bCompressFormat, 50, os)
            }

            var `is` = ByteArrayInputStream(os.toByteArray())
            val newOpts = Options()
            newOpts.inJustDecodeBounds = true
            newOpts.inPreferredConfig = bConfig
            BitmapFactory.decodeStream(`is`, null as Rect?, newOpts)
            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            var be = 1
            if (w > h && w.toFloat() > pixelW) {
                be = (newOpts.outWidth.toFloat() / pixelW).toInt()
            } else if (w < h && h.toFloat() > pixelH) {
                be = (newOpts.outHeight.toFloat() / pixelH).toInt()
            }

            if (be <= 0) {
                be = 1
            }

            newOpts.inSampleSize = be
            `is` = ByteArrayInputStream(os.toByteArray())
            return BitmapFactory.decodeStream(`is`, null as Rect?, newOpts)
        }
    }
}
