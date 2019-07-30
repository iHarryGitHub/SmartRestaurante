package pe.smartsystem.smartrestaurante;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pe.smartsystem.smartrestaurante.Utilidades.Utilidades;
import pe.smartsystem.smartrestaurante.sqlite.SQLiteHandler;
import pe.smartsystem.smartrestaurante.ui.fragment.category.CategoriaPlato;
import pe.smartsystem.smartrestaurante.ui.fragment.top.data.DataModel;

public class ConexionSQLITEhelper extends SQLiteOpenHelper {


    //categoria

    public static String categoria_ ="CATEGORIA_PLATO";
    public static String idCategoria = "idCategoria";
    public static String NombreCategoria = "NombreCategoria";

    String CREATE_CATEGORY_TABLE = "CREATE TABLE "
            + categoria_+ " ("
            + idCategoria + " TEXT,"
            + NombreCategoria + " TEXT )";

    //String CREAR_TABLA_CATEGORIA="CREATE TABLE "+categoria_+" ("+idCategoria+" TEXT,"+NombreCategoria+"TEXT )";


    // Logcat tag
    private static final String LOG = "ConexionSQLITEhelper";





    public ConexionSQLITEhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_IP);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(SQLiteHandler.CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS IP");
        db.execSQL("DROP TABLE IF EXISTS platos");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIA_PLATO");
        onCreate(db);
    }
    public List<DataModel> getAllTags() {
        List<DataModel> tags = new ArrayList<DataModel>();
        String selectQuery = "SELECT  * FROM " + "platos";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DataModel t = new DataModel();
                t.setIdproducto(c.getString(0));
                t.setNombreProducto(c.getString(1));
                t.setPrecioUnidad(c.getString(2));
                t.setDestino(c.getString(3));
                t.setNombreCategoria(c.getString(4));

//                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));

                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    public ArrayList<CategoriaPlato> getCategorias() {
        ArrayList<CategoriaPlato> tags = new ArrayList<CategoriaPlato>();
        String selectQuery = "SELECT  * FROM " + categoria_;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CategoriaPlato t = new CategoriaPlato();
                t.setId(Integer.parseInt(c.getString(0)));
                t.setName(c.getString(1));
//                t.setIdproducto(c.getString(0));
//                t.setNombreProducto(c.getString(1));
//                t.setPrecioUnidad(c.getString(2));
//                t.setDestino(c.getString(3));
//                t.setNombreCategoria(c.getString(4));

//
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    public void deleteUser(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("platos",null,null);
        db.close();


    }
    String truncatetable="DELETE FROM "+categoria_;
    public void deleteCategorias(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(truncatetable);
      //  db.delete("CATEGORIA_PLATO",null,null);
        db.close();


    }
    /**
     * Storing category details in database
     * */
    public void guardarCategoria(String id, String Categoryname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(idCategoria, id);
        values.put(NombreCategoria, Categoryname);

        // Inserting Row
        long idd = db.insert(categoria_, null, values);
        db.close(); // Closing database connection

        Log.d("SQLITE", "New user inserted into sqlite: " + idd);
    }
}
