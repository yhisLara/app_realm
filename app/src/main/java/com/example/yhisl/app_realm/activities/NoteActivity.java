package com.example.yhisl.app_realm.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yhisl.app_realm.R;
import com.example.yhisl.app_realm.adapters.NoteAdapter;
import com.example.yhisl.app_realm.models.Board;
import com.example.yhisl.app_realm.models.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board>{
    private ListView listView;
    private FloatingActionButton fab;

    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;

    private int boardId;
    private Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();
        if(getIntent().getExtras() != null)
            boardId = getIntent().getExtras().getInt("id");

        board = realm.where(Board.class).equalTo("id",boardId).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        this.setTitle(board.getTitle());

        fab = (FloatingActionButton)findViewById(R.id.fabAddNote);
        listView = (ListView) findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this,notes,R.layout.list_view_note_item);

        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNote("title", "message");
            }
        });
    }

    private void showAlertForCreatingNote(String title, String message){
        //se crea el builder, la vista que contendrá el alerdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //se setea el titulo y el mensaje si es que no son nulos
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        //se crea una vista inflada con el layout para el alerdialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note,null);
        //se setea esta vista inflada al builder
        builder.setView(viewInflated);

        //se hace referencia al editText del layout
        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNote);

        //se agrega el evento click para leer el texto escrito por el usuario y crear así el board
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String note = input.getText().toString().trim();

                //si ha escrito algo se crea el board
                if(note.length() > 0) {
                    createNewNote(note);
                }
                //sino se muestra un toast con el mensaje de que no ha escrito nada
                else{
                    Toast.makeText(getApplicationContext(),"The note can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        //con esto se crea y se ejecuta el builder
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createNewNote(String note){
        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note);
        realm.commitTransaction();
    }

    @Override
    public void onChange(Board element) {
        adapter.notifyDataSetChanged();
    }
}