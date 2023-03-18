package com.pinleen.mobile.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.loader.content.CursorLoader
import com.pinleen.mobile.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object RealPathUtil {
    fun getRealPath(context: Context, fileUri: Uri): String? {
        val realPath: String?
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = getRealPathFromURI_BelowAPI11(context, fileUri)
        } else if (Build.VERSION.SDK_INT < 19) {
            realPath = getRealPathFromURI_API11to18(context, fileUri)
        } else {
            realPath = getRealPathFromURI_API19(context, fileUri)
        }
        return realPath
    }


    @SuppressLint("NewApi")
    fun getRealPathFromURI_API11to18(context: Context?, contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null
        val cursorLoader = CursorLoader(
            context!!, contentUri!!, proj, null, null, null
        )
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(column_index)
            cursor.close()
        }
        return result
    }

    fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        var column_index = 0
        var result: String? = ""
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(column_index)
            cursor.close()
            return result
        }
        return result
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Create a File for saving an image or video
     */
    //    public static File getOutputMediaFile(Context mContext, int type) {
    //        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/."+mContext.getString(R.string.app_name));
    //        if (!mediaStorageDir.exists()) {
    //            if (!mediaStorageDir.mkdirs()) {
    //                Log.d("", "failed to create directory");
    //                return null;
    //            }
    //        }
    //
    //        // Create a media file name
    //        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
    //        File mediaFile;
    //        if (type == MEDIA_TYPE_IMAGE) {
    //            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    //                    "IMG_" + timeStamp + ".jpg");
    //        } else if (type == MEDIA_TYPE_VIDEO) {
    //            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    //                    "VID_" + timeStamp + ".mp4");
    //        } else {
    //            return null;
    //        }
    //
    //        return mediaFile;
    //    }
    //    public static void saveFile(Context mContext,File file,File fileDest,boolean isDeleteSource) throws IOException {
    ////        final File fileDest = new File(Environment.getExternalStorageDirectory()  /*"/" + getString(R.string.images) */+ "/" + getString(R.string.images));
    //        if (!fileDest.exists()) {
    //            fileDest.mkdirs();
    //        }
    //
    //        File newFile = new File(fileDest, file.getName());
    //        FileChannel outputChannel = null;
    //        FileChannel inputChannel = null;
    //        try {
    //            outputChannel = new FileOutputStream(newFile).getChannel();
    //            inputChannel = new FileInputStream(file).getChannel();
    //            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
    //            inputChannel.close();
    //            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(newFile)));
    //            if(isDeleteSource){
    //                file.delete();
    //            }
    //            Toast.makeText(mContext, mContext.getString(R.string.story_saved), Toast.LENGTH_SHORT).show();
    //        } finally {
    //            if (inputChannel != null) inputChannel.close();
    //            if (outputChannel != null) outputChannel.close();
    //        }
    //    }
    fun deleteFileByPath(path: String) {
        val fdelete = File(path)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                println("file Deleted :$path")
            } else {
                println("file not Deleted :$path")
            }
        }
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    ////    public static File getOutputMediaFileTemp(Context mContext, int type) {
    ////        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/." + mContext.getString(R.string.app_name));
    ////
    ////        if (!mediaStorageDir.exists()) {
    ////            if (!mediaStorageDir.mkdirs()) {
    ////                Log.d("", "failed to create directory");
    ////                return null;
    ////            }
    ////        }
    ////
    ////        // Create a media file name
    ////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    ////        File mediaFile;
    ////        if (type == MEDIA_TYPE_IMAGE) {
    ////            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    ////                    "IMG_" + timeStamp + ".jpg");
    ////        } else if (type == MEDIA_TYPE_VIDEO) {
    ////            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    ////                    "VID_" + timeStamp + ".mp4");
    ////        } else {
    ////            return null;
    ////        }
    ////
    ////        return mediaFile;
    ////    }
    //
    //    public static File getOutputMediaFileTemp(Context mContext, int type, String foldername) {
    //        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/." + mContext.getString(R.string.app_name));
    //        if (!foldername.isEmpty()) {
    //            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/." + mContext.getString(R.string.app_name) + "/" + foldername);
    //        }
    //        if (!mediaStorageDir.exists()) {
    //            if (!mediaStorageDir.mkdirs()) {
    //                Log.d("", "failed to create directory");
    //                return null;
    //            }
    //        }
    //
    //        // Create a media file name
    //        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //        File mediaFile;
    //        if (type == MEDIA_TYPE_IMAGE) {
    //            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    //                    "IMG_" + timeStamp + ".jpg");
    //        } else if (type == MEDIA_TYPE_VIDEO) {
    //            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
    //                    "VID_" + timeStamp + ".mp4");
    //        } else {
    //            return null;
    //        }
    //
    //        return mediaFile;
    //    }
    //

//
//    public static ArrayList<MultipartBody.Part> getParamsRequestBodyImage(Context mContext, Uri profileUri) throws URISyntaxException {
//
//        ArrayList<MultipartBody.Part> attachmentName = new ArrayList<MultipartBody.Part>();
//        File file = new File(/*getFilePathFromContentUri(mContext,profileUri)*/"storage/emulated/0/DCIM/Screenshots/IMG_20201020_184737.jpg");
//        okhttp3.RequestBody requestFile =
//                okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/png"), file);
//        /* val surveyBody = RequestBody.create(
//                "image/png".toMediaTypeOrNull(),
//                file)*/
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("attachmentName[" + 0 + "]", file.getName(), requestFile);
//        attachmentName.add(body);
//        return attachmentName;
//    }
//
//    public static ArrayList<MultipartBody.Part> getParamsRequestBodyVideo(Context mContext, Uri profileUri) throws URISyntaxException {
//
//        ArrayList<MultipartBody.Part> attachmentName = new ArrayList<MultipartBody.Part>();
//        File file = new File(getFilePathFromContentUri(mContext, profileUri));
//        okhttp3.RequestBody requestFile =
//                okhttp3.RequestBody.create(okhttp3.MediaType.parse("video/*"), file);
//        /* val surveyBody = RequestBody.create(
//                "image/png".toMediaTypeOrNull(),
//                file)*/
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("attachmentName[" + 0 + "]", file.getName(), requestFile);
//        attachmentName.add(body);
//        return attachmentName;
//    }

}
