package sa.ksu.swe444;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPrefrence {
    private static SharedPreferences prf;

    private MySharedPrefrence() {
    }

    public static SharedPreferences getInstance(Context context) {
        if (prf == null) {
            prf = context.getSharedPreferences("user_details", MODE_PRIVATE);
        } // end  if (prf == null)
        return prf;
    } // end getInstance


    public static void clearData(Context context) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void clearValue(Context context, String key) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.remove(key);
        editor.commit();
    } // end clearValue

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putString(key, value);
        editor.commit();
    } // end putString

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(key, value);
        editor.commit();
    } // end putInt

    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    } // end putBoolean

    public static void putFloat(Context context, String key, Float value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putFloat(key, value);
        editor.commit();
    } // end putFloat

    public static boolean getBoolean(Context context, String key, Boolean value) {
        return getInstance(context).getBoolean(key, value);
    } // end getBoolean


    public static int getInt(Context context, String key, int value) {
        return getInstance(context).getInt(key, value);
    } // end getInt


    public static String getString(Context context, String key, String value) {
        return getInstance(context).getString(key, value);
    } // end getString

}
