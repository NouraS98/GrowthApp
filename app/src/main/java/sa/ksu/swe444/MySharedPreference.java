package sa.ksu.swe444;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    private static SharedPreferences prf;

    private MySharedPreference(){
    }//end of MySharedPreference

    public static SharedPreferences getInstance(Context context){
        if(prf==null){
            prf=context.getSharedPreferences(Constants.keys.USER_DETAILS,Context.MODE_PRIVATE);
        }//end of if statement
        return prf;
    }//end of getInstance

    public static void clearData(Context context){
        SharedPreferences.Editor editor=getInstance(context).edit();
        editor.clear();
        editor.apply();

    }//end of clearData

    public static void clearValue(Context context,String key){
        SharedPreferences.Editor editor=getInstance(context).edit();
        editor.remove(key);
        editor.apply();
    }//end of clearValue

    public static void putString(Context context,String key,String value){
        SharedPreferences.Editor editor=getInstance(context).edit();
        editor.putString(key,value);
        editor.apply();
    }//end of putString

    public static void putInt(Context context,String key,int value){
        SharedPreferences.Editor editor=getInstance(context).edit();
        editor.putInt(key,value);
        editor.apply();
    }//end of putInt

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences.Editor editor=getInstance(context).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }//end of putBoolean

    public static String getString(Context context,String key,String valueDefault){
        return getInstance(context).getString(key,valueDefault);
    }//end of getString()

    public static int getInt(Context context,String key,int valueDefault){
        return getInstance(context).getInt(key,valueDefault);
    }//end of getInt
    public static boolean getBoolean(Context context,String key,boolean valueDefault){
        return getInstance(context).getBoolean(key,valueDefault);
    }//end of getBoolean

}//end of the class