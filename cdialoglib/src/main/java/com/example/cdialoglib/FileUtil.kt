package com.example.cdialoglib

/**
 * @author Chengguo on 2017/11/28.
 */

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat

class FileUtil {
    companion object {
        var SDPATH = Environment.getExternalStorageDirectory().absolutePath
        var ZFILEPATH: String
        val SIZETYPE_B = 1
        val SIZETYPE_KB = 2
        val SIZETYPE_MB = 3
        val SIZETYPE_GB = 4

        fun createSDDirs(path: String): File? {
            return if (Environment.getExternalStorageState() == "mounted") {
                val dir = File(path)
                var bool = true
                if (!dir.exists()) {
                    bool = dir.mkdirs()
                }

                if (!bool) null else dir
            } else {
                null
            }
        }

        fun createSDDir(path: String): File? {
            return if (Environment.getExternalStorageState() == "mounted") {
                val dir = File(path)
                var bool = true
                if (!dir.exists()) {
                    bool = dir.mkdir()
                }

                if (!bool) null else dir
            } else {
                null
            }
        }

        fun createTimeMillisFile(): File? {
            return try {
                val timeMillis = System.currentTimeMillis()
                val filePath = SDPATH + File.separator + timeMillis
                val file = File(filePath)
                val bool = file.createNewFile()
                if (!bool) null else file
            } catch (var5: IOException) {
                var5.printStackTrace()
                null
            }

        }

        fun createNameFile(fileName: String): File? {
            return try {
                val filePath = SDPATH + File.separator + fileName
                val file = File(filePath)
                val bool = file.createNewFile()
                if (!bool) null else file
            } catch (var4: IOException) {
                var4.printStackTrace()
                null
            }

        }

        fun delFile(path: String): Boolean {
            val file = File(path)
            return file.isFile && file.exists() && file.delete()
        }

        fun deleteDir(path: String): Boolean {
            try {
                val dir = File(path)
                if (dir.exists() && dir.isDirectory) {
                    val var2 = dir.listFiles()
                    val var3 = var2.size

                    for (var4 in 0 until var3) {
                        val file = var2[var4]
                        if (file != null) {
                            if (file.isFile) {
                                if (!file.delete()) {
                                    return false
                                }
                            } else if (file.isDirectory) {
                                deleteDir(file.absolutePath)
                            }
                        }
                    }

                    return dir.delete()
                } else {
                    return false
                }
            } catch (var6: Exception) {
                return false
            }

        }

        fun readFile(context: Context, filename: String): String {
            var fis: FileInputStream? = null
            var buffer: ByteArray? = null

            try {
                fis = context.openFileInput(filename)
                buffer = ByteArray(fis!!.available())
                fis.read(buffer)
            } catch (var13: IOException) {
                var13.printStackTrace()
            } finally {
                try {
                    if (fis != null) {
                        fis.close()
                    }
                } catch (var12: IOException) {
                    var12.printStackTrace()
                }

            }

            return String(if (buffer != null) buffer else ByteArray(0))
        }

        fun writeFile(context: Context, content: String, filename: String, mode: Int) {
            var fos: FileOutputStream? = null

            try {
                fos = context.openFileOutput(filename, mode)
                fos!!.write(content.toByteArray())
            } catch (var14: IOException) {
                var14.printStackTrace()
            } finally {
                try {
                    if (fos != null) {
                        fos.close()
                    }
                } catch (var13: IOException) {
                    var13.printStackTrace()
                }

            }

        }

        fun copyFile(oldPath: String, newPath: String) {
            try {
                val oldFile = File(oldPath)
                if (oldFile.exists()) {
                    val inStream = FileInputStream(oldPath)
                    val fs = FileOutputStream(newPath)
                    val buffer = ByteArray(5120)

                    while (inStream.read(buffer) != -1) {
                        fs.write(buffer, 0, inStream.read(buffer))
                    }

                    inStream.close()
                    fs.close()
                }
            } catch (var7: Exception) {
                var7.printStackTrace()
            }

        }

        fun copyFolder(oldPath: String, newPath: String) {
            try {
                val newFile = File(newPath)
                if (!newFile.exists()) {
                    newFile.mkdirs()
                }

                val oldFile = File(oldPath)
                val files = oldFile.list()

                for (i in files.indices) {
                    val temp: File = if (oldPath.endsWith(File.separator)) {
                        File(oldPath + files[i])
                    } else {
                        File(oldPath + File.separator + files[i])
                    }

                    if (temp.isFile) {
                        val input = FileInputStream(temp)
                        val output = FileOutputStream(newPath + File.separator + temp.name)
                        val b = ByteArray(5120)
                        while (input.read(b) != -1) {
                            output.write(b, 0, input.read(b))
                        }

                        output.flush()
                        output.close()
                        input.close()
                    }

                    if (temp.isDirectory) {
                        copyFolder(oldPath + File.separator + files[i], newPath + File.separator + files[i])
                    }
                }
            } catch (var11: Exception) {
                var11.printStackTrace()
            }

        }

        fun isFileExist(path: String): Boolean {
            val file = File(path)
            return file.exists()
        }

        fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {
            val file = File(filePath)
            var blockSize = 0L

            try {
                if (file.isDirectory) {
                    blockSize = getFileSizes(file)
                } else {
                    blockSize = getFileSize(file)
                }
            } catch (var6: Exception) {
                var6.printStackTrace()
            }

            return formatFileSize(blockSize, sizeType)
        }

        fun getAutoFileOrFilesSize(filePath: String): String? {
            val file = File(filePath)
            var blockSize = 0L

            try {
                if (file.isDirectory) {
                    blockSize = getFileSizes(file)
                } else {
                    blockSize = getFileSize(file)
                }
            } catch (var5: Exception) {
                var5.printStackTrace()
            }

            return formatFileSize(blockSize)
        }

        fun getFileSize(file: File): Long {
            var size = 0L

            try {
                if (file.exists()) {
                    val fis = FileInputStream(file)
                    size = fis.available().toLong()
                }
            } catch (var4: IOException) {
                var4.printStackTrace()
            }

            return size
        }

        fun getFileSizes(files: File?): Long {
            var size = 0L

            try {
                if (files!!.exists()) {
                    val fList = files.listFiles()
                    val var5 = fList.size

                    (0 until var5)
                            .asSequence()
                            .map { fList[it] }
                            .forEach {
                                size += if (it.isDirectory) {
                                    getFileSizes(it)
                                } else {
                                    getFileSize(it)
                                }
                            }
                }
            } catch (var8: Exception) {
                var8.printStackTrace()
            }

            return size
        }

        fun formatFileSize(fileSize: Long): String? {
            try {
                val df = DecimalFormat("#.00")
                val wrongSize = "0B"
                if (fileSize == 0L) {
                    return wrongSize
                } else {
                    val fileSizeStr: String
                    if (fileSize < 1024L) {
                        fileSizeStr = df.format(fileSize.toDouble()) + "B"
                    } else if (fileSize < 1048576L) {
                        fileSizeStr = df.format(fileSize.toDouble() / 1024.0) + "KB"
                    } else if (fileSize < 1073741824L) {
                        fileSizeStr = df.format(fileSize.toDouble() / 1048576.0) + "MB"
                    } else {
                        fileSizeStr = df.format(fileSize.toDouble() / 1.073741824E9) + "GB"
                    }

                    return fileSizeStr
                }
            } catch (var5: Exception) {
                return null
            }

        }

        fun formatFileSize(fileSize: Long, sizeType: Int): Double {
            return try {
                val df = DecimalFormat("#.00")
                var fileSizeLong = 0.0
                when (sizeType) {
                    1 -> fileSizeLong = java.lang.Double.valueOf(df.format(fileSize.toDouble()))!!.toDouble()
                    2 -> fileSizeLong = java.lang.Double.valueOf(df.format(fileSize.toDouble() / 1024.0))!!.toDouble()
                    3 -> fileSizeLong = java.lang.Double.valueOf(df.format(fileSize.toDouble() / 1048576.0))!!.toDouble()
                    4 -> fileSizeLong = java.lang.Double.valueOf(df.format(fileSize.toDouble() / 1.073741824E9))!!.toDouble()
                }

                fileSizeLong
            } catch (var6: Exception) {
                0.0
            }

        }

        fun getTotalCacheSize(context: Context): Long {
            var cacheSize = 0L

            try {
                cacheSize = getFileSizes(context.cacheDir)
                if (Environment.getExternalStorageState() == "mounted") {
                    cacheSize += getFileSizes(context.externalCacheDir)
                }
            } catch (var4: Exception) {
                var4.printStackTrace()
            }

            return cacheSize
        }

        fun getFormatTotalCacheSize(context: Context): String? {
            return try {
                var cacheSize = getFileSizes(context.cacheDir)
                if (Environment.getExternalStorageState() == "mounted") {
                    cacheSize += getFileSizes(context.externalCacheDir)
                }

                formatFileSize(cacheSize)
            } catch (var3: Exception) {
                null
            }

        }

        fun clearAllCache(context: Context) {
            try {
                deleteDir(context.cacheDir.absolutePath)
                if (Environment.getExternalStorageState() == "mounted" && context.externalCacheDir != null) {
                    deleteDir(context.externalCacheDir!!.absolutePath)
                }
            } catch (var2: Exception) {
                var2.printStackTrace()
            }

        }

        fun cleanSharedPreference(context: Context) {
            deleteDir(File(File.separator + "data" + File.separator + "data" + File.separator + context.packageName + File.separator + "sharedprefs").absolutePath)
        }

        fun delDatabaseByName(context: Context, dbName: String) {
            context.deleteDatabase(dbName)
        }

        init {
            ZFILEPATH = Environment.getExternalStorageDirectory().absolutePath + File.separator + "ZFile" + File.separator
        }
    }
}
