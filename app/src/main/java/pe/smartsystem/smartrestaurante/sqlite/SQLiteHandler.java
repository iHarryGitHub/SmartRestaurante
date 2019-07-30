package pe.smartsystem.smartrestaurante.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "restaurant_sqlite";

    // Login table name
    public static final String TABLE_TOPLAS= "platos";


    // Login Table Columns names
//    private static final String KEY_ID = "id";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_UID = "uid";
//    private static final String KEY_CREATED_AT = "created_at";



    //top
    private static final String idproducto="idproducto";
    private static final String nombreProducto="nombreProducto";
    private static final String precioUnidad="precioUnidad";
    private static final String destino="destino";
    private static final String nombrecategoria="nombrecategoria";




    public  static  String CREATE_LOGIN_TABLE= "CREATE TABLE "
            + TABLE_TOPLAS + "("
            + idproducto + " TEXT,"
            + nombreProducto + " TEXT,"
            + precioUnidad + " TEXT,"
            + destino + " TEXT,"
            + nombrecategoria + " TEXT" + ")";





    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_LOGIN_TABLE);

      //  Log.d(TAG, "Database tables created");

//        addToplaTop("1", "nobmre1", "10", "des1", "cat1");
//        addToplaTop("2", "nobmre2", "20", "des2", "cat2");
//        addToplaTop("3", "nobmre3", "30", "des3", "cat3");
    }






    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPLAS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
   /* public void addToplaTop(String id, String nombre, String precio, String dest,String categria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(idproducto, id); // Name
        values.put(nombreProducto, nombre); // Email
        values.put(precioUnidad, precio); // Email
        values.put(destino, dest); // Created At
        values.put(nombrecategoria, categria); // Created At

        //db.insert(TABLE_TOPLAS, null, values);
        // Inserting Row
        long id_ = db.insert(TABLE_TOPLAS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id_);
    }*/

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOPLAS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("idproducto", cursor.getString(0));
            user.put("nombreProducto", cursor.getString(1));
            user.put("precioUnidad", cursor.getString(2));
            user.put("destino", cursor.getString(3));
            user.put("nombrecategoria", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_TOPLAS, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}