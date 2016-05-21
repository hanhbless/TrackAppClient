package com.sunnet.trackapp.client.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;

import com.sunnet.trackapp.client.application.MyApplication;
import com.sunnet.trackapp.client.db.entity.ContactEntity;
import com.sunnet.trackapp.client.db.entity.LocationEntity;
import com.sunnet.trackapp.client.log.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class Utils {
    public static final boolean DEBUG = true;
    public static final int MEDIA_MOUNTED = 0;
    public static final int MEDIA_MOUNTED_READ_ONLY = 1;
    public static final int NO_MEDIA = 2;

    /**
     * checks if an external memory card is available
     *
     * @return
     */
    public static int updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return MEDIA_MOUNTED;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return MEDIA_MOUNTED_READ_ONLY;
        } else {
            return NO_MEDIA;
        }
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w("extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static boolean newFolderIfNotExist(String dir) {

        File file = new File(dir);
        boolean bool = false;
        if (!file.exists()) {
            bool = file.mkdir();
        }

        return bool;
    }

    public static void deleteFile(String path) {
        if (Utils.isEmptyString(path))
            return;
        File f = new File(path);
        f.delete();
    }

    public static boolean isEmptyString(String string) {
        if (string == null || string.trim().equals("") || string.trim().equals("null") || string.trim().length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFullTextSearch(String originText, String inputText) {

        if (originText == null && inputText == null)
            return true;

        if ((originText == null && inputText != null) || (inputText == null && originText != null))
            return false;

        originText = originText.trim().toLowerCase(Locale.ENGLISH);
        inputText = inputText.trim().toLowerCase(Locale.ENGLISH);

        return originText.compareTo(inputText) == 0;
    }

    public static String getStringLocationId(List<LocationEntity> list) {
        StringBuilder buffer = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            buffer.append("'" + list.get(i).getId() + "'");
            if (i < size - 1)
                buffer.append(",");
        }
        if (buffer.length() > 0)
            return buffer.toString();

        return "";
    }

    public static String getStringContactId(List<ContactEntity> list) {
        StringBuilder buffer = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            buffer.append("'" + list.get(i).getId() + "'");
            if (i < size - 1)
                buffer.append(",");
        }
        if (buffer.length() > 0)
            return buffer.toString();

        return "";
    }

    public static boolean isExistFile(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static String getPathVoice(String fileName) {
        newFolderIfNotExist(Constants.DIR_ROOT + Constants.FOLDER_VOICE);
        return Constants.DIR_ROOT + Constants.FOLDER_VOICE + "/" + fileName;
    }

    public static void deleteDir(String path) {
        deleteDir(new File(path));
    }

    public static void deleteDir(File dir) {

        if (dir.isFile()) {
            dir.deleteOnExit();
            Log.i("hanh delete file: " + dir.getPath());
            return;
        }
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                boolean deleted = file.delete();
                if (!deleted)
                    continue;
            }
        }
        dir.delete();
        Log.i("hanh delete dir: " + dir.getPath());
    }

    public static boolean renameFile(String pathFileOld, String pathFileNew) {
        File fOld = new File(pathFileOld);
        File fNew = new File(pathFileNew);
        return fOld.renameTo(fNew);
    }

    /**
     * Defined progress dialog
     */
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity, String title, String msg) {
        if (progressDialog != null && progressDialog.isShowing())
            return;
        progressDialog = new ProgressDialog(activity);
        if (!Utils.isEmptyString(title))
            progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                System.gc();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Defined show alert
     */
    public static void showAlertDialog(Context context, int titleID,
                                       int messageID, boolean isSuccess) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titleID).setIcon(
                    !isSuccess ? android.R.drawable.ic_dialog_alert
                            : android.R.drawable.ic_dialog_info
            ).setTitle(titleID)
                    .setMessage(messageID).setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Generate token for request
     */

    private static final String CRYPT_KEY = "PgdwQqFDJk5AkSVLCFgr9dhZGLCbTVXQ";

    public static String hashMac(String paramString, String secretKey)
            throws SignatureException {
        try {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(
                    secretKey.getBytes(), "hmacSHA512");
            Mac localMac = Mac.getInstance(localSecretKeySpec.getAlgorithm());
            localMac.init(localSecretKeySpec);

            return toHexString(localMac.doFinal(paramString.getBytes()));
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new SignatureException(
                    "error building signature, no such algorithm in device hmacSHA512");
        } catch (InvalidKeyException localInvalidKeyException) {
        }
        throw new SignatureException(
                "error building signature, invalid key hmacSHA512");
    }

    public static String hashMac(String paramString) {
        try {
            return hashMac(paramString, CRYPT_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String toHexString(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder(
                2 * paramArrayOfByte.length);
        Formatter localFormatter = new Formatter(localStringBuilder);
        int i = paramArrayOfByte.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                localFormatter.close();
                return localStringBuilder.toString();
            }
            byte b = paramArrayOfByte[j];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b);
            localFormatter.format("%02x", arrayOfObject);
        }
    }

    /**
     * Network
     */
    public static boolean isNetworkAvailable(Context appContext) {
        if (appContext == null)
            return false;
        Context context = appContext.getApplicationContext();
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager
                        .getActiveNetworkInfo();
                return activeNetworkInfo != null
                        && activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(MyApplication.getContext());
    }

    /**
     * Format date string
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, yyyy-MMM-dd HH:mm");

    public static String getDateFormat(long timeInMillisecond) {
        try {
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(timeInMillisecond);
            return dateFormat.format(cl.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(timeInMillisecond);
    }

    public static long getLongTimeDate(String timeDate) {
        try {
            return dateFormat.parse(timeDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date getTimeDate(String timeDate) {
        try {
            return dateFormat.parse(timeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
