package com.example.yhisl.app_realm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yhisl.app_realm.R;
import com.example.yhisl.app_realm.adapters.BoardAdapter;
import com.example.yhisl.app_realm.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    private Realm realm;
    private FloatingActionButton fab;
    private ListView listView;
    private BoardAdapter adapter;
    //lista de board, desde la base de datos
    private RealmResults<Board> boards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //DB Realm

        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
        listView = (ListView) findViewById(R.id.listViewBoards);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        showAlertForCreatingBoard("title", "message");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Add new Board", "Type a name for your new board");
            }
        });

        registerForContextMenu(listView);
    }

    //metodo que crea el board
    //**CRUD actions **//
    private  void createNewBoard(final String BoardName){
        realm.beginTransaction();
        Board board = new Board(BoardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    private void deleteAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
    private  void deleteBoard(Board board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editName(String newName, Board board){
        realm.beginTransaction();
        board.setTitle(newName);
        realm.copyToRealmOrUpdate(board);
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
                if(BoardName.length() > 0) {
                    createNewBoard(BoardName);
                }
                //sino se muestra un toast con el mensaje de que no ha escrito nada
                else{
                    Toast.makeText(getApplicationContext(),"The name is required to create a new Board", Toast.LENGTH_LONG).show();
                }
            }
        });

        //con esto se crea y se ejecuta el builder
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertForEditingBoard(String title, String message, final Board board) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextInput);
        input.setText(board.getTitle());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0)
                    Toast.makeText(getApplicationContext(), "The name is required to edit the current board", Toast.LENGTH_LONG).show();
                else if (boardName.equals(board.getTitle()))
                    Toast.makeText(getApplicationContext(), "The name is the same than it was before", Toast.LENGTH_LONG).show();
                else
                    editName(boardName, board);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /* eventos*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Edit Board", "Change the name of the board", boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(RealmResults<Board> element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }
}
