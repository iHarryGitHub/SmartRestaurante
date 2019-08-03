package pe.smartsystem.smartrestaurante.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.security.PublicKey;

public class SessionManager {

    private static final String KEY_ID = "id_";
    private static final String KEY_IP = "ip_";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "smartSystemLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String KEY_NAME = "nombredelmosaico";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setId(int id){
        editor.putInt(KEY_ID, id);
        editor.commit();
    }

    //[return id]
    public int getId(){
        return pref.getInt(KEY_ID,-1);
    }
    //[return id]

    public void setIp(String ip){
        editor.putString(KEY_IP,ip);
        editor.commit();
    }

    public String getIp(){
        return pref.getString(KEY_IP, "");
    }

    //[START GET NAME]
    public String getFullName(){
        return pref.getString(KEY_NAME,"");
    }
    //[END ]

    //[START ID]
    public void setFullName(String name){
        editor.putString(KEY_NAME,name);
        editor.commit();
        Log.d(TAG,"setFullName modified");
    }
    //[END ]
}
