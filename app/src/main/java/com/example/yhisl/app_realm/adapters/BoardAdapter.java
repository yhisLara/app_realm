package com.example.yhisl.app_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yhisl.app_realm.R;
import com.example.yhisl.app_realm.models.Board;

import java.util.List;

/**
 * Created by yhisl on 01/04/2017.
 */

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> list;
    private int layout;

    public BoardAdapter(Context context, List<Board> list, int layout){
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView.findViewById(R.id.textViewBoardTitle);
            vh.Notes = (TextView) convertView.findViewById(R.id.textViewBoardNotes);
            vh.CreatedAt = (TextView) convertView.findViewById(R.id.textViewBoardDate);

            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }

        Board board = list.get(position);
        vh.title.setText(board.getTitle());
        int numberOfNotes = board.getNotes().size();
        String textForNotes = (numberOfNotes == 1) ? numberOfNotes + "Note" : numberOfNotes + "Notes";
        vh.CreatedAt.setText(board.getCreateDate().toString());


    }

    public class ViewHolder{
        TextView title;
        TextView Notes;
        TextView CreatedAt;
    }
}
