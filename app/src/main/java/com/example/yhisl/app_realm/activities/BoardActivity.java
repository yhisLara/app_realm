package com.example.yhisl.app_realm.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yhisl.app_realm.R;
import com.example.yhisl.app_realm.models.Board;

import io.realm.Realm;

public class BoardActivity extends AppCompatActivity {

    private Realm realm;
    private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //DB Realm

        realm = Realm.getDefaultInstance();

        fab = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        showAlertForCreatingBoard("title", "message");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Add new Board", "Type a name for your new board");
            }
        });
    }

    //metodo que crea el board
    //**CRUD actions **//
    private  void createNewBoard(final String BoardName){
        realm.beginTransaction();
        Board board = new Board(BoardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    //pop up con input
    private void showAlertForCreatingBoard(String title, String message){
        //se crea el builder, la vista que contendrá el alerdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //se setea el titulo y el mensaje si es que no son nulos
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        //se crea una vista inflada con el layout para el alerdialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        //se setea esta vista inflada al builder
        builder.setView(viewInflated);

        //se hace referencia al editText del layout
        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextInput);

        //se agrega el evento click para leer el texto escrito por el usuario y crear así el board
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String BoardName = input.getText().toString().trim();

                //si ha escrito algo se crea el board
                if(BoardName.length() > 0)
                    createNewBoard(BoardName);
                //sino se muestra un toast con el mensaje de que no ha escrito nada
                else
                    Toast.makeText(getApplicationContext(),"The name is required to create a new Board", Toast.LENGTH_LONG).show();
            }
        });

        //con esto se crea y se ejecuta el builder
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
