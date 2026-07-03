package com.example.myapplication.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;
    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);

        dbHelper = new FeedReaderDbHelper(this);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());
        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);
        db.close();
        Toast.makeText(getApplicationContext(), "Se guardo el registro " +
                newRowId, Toast.LENGTH_LONG).show();
    }

    public void Buscar(View vista) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null, null, null);
        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
        }
        cursor.close();
        db.close();
    }

    public void Actualizar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};
        int count = db.update(FeedReaderContract.FeedEntry.nameTable,
                values,
                selection,
                selectionArgs);
        db.close();
        Toast.makeText(getApplicationContext(), "Se actualizo " + count +
                " registro(s)", Toast.LENGTH_LONG).show();
    }

    public void Eliminar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);
        db.close();
        Toast.makeText(getApplicationContext(), "Se elimino " +
                deletedRows + " registro(s)", Toast.LENGTH_LONG).show();
    }

    public void Listar(View vista) {
        Intent listar = new Intent(this, Listado.class);
        startActivity(listar);
    }
}
