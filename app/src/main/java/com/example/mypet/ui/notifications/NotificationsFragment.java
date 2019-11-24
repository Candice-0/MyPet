package com.example.mypet.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mypet.R;
import com.example.mypet.action.edit_diary;
import com.example.mypet.domain.Note;

import java.lang.reflect.Array;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    Note[] notes;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        notes = new Note[2];
        Note note = new Note();
        note.setTitle("收养记");
        note.setDate("2015-10-20");
        note.setContent("今天是莎莎第一天来我家，灰灰的她，真是太可爱了！！！");
        notes[0] = note;
        Note note1 = new Note();
        note1.setTitle("公园记");
        note1.setDate("2019-10-20");
        note1.setContent("今天带莎莎去了北京的公园，她好开心，我也好开心，希望她健康成长。");
        notes[1] = note1;

        final String[] noteList = new String[notes.length];
        for(int i = 0; i < notes.length; i++){
            noteList[i] = notes[i].getTitle() + "\n" + notes[i].getDate();
        }

        //日志列表
        noteList(root, noteList);

        //添加日志
        ImageView add = root.findViewById(R.id.add_note);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder add = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getLayoutInflater();
                final View v1 = inflater.inflate(R.layout.add_diary, null);
                add.setView(v1).setTitle("添加日志").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText addTitle = v1.findViewById(R.id.addTitle);
                        EditText addDate = v1.findViewById(R.id.addDate);
                        EditText addContent = v1.findViewById(R.id.addContent);

                        String title = addTitle.getText().toString();
                        String date = addDate.getText().toString();
                        String content = addContent.getText().toString();

                        Note addNote = new Note();
                        addNote.setTitle(title);
                        addNote.setDate(date);
                        addNote.setContent(content);

                        Note[] newNotes = new Note[notes.length + 1];
                        for(int i = 0; i < notes.length; i++){
                            newNotes[i] = notes[i];
                        }
                        newNotes[notes.length] = addNote;
                        notes = new Note[notes.length + 1];
                        for(int i = 0; i < notes.length; i++){
                            notes[i] = newNotes[i];
                        }
                        String[] noteList = new String[notes.length];
                        for(int i = 0; i < notes.length; i++)
                            noteList[i] = notes[i].getTitle() + "\n" + notes[i].getDate();
                        noteList(root, noteList);


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                add.show();
            }
        });

        return root;
    }

    ListView myListView;
    ArrayAdapter<String> adapter;
    Array list;

    public void noteList(View view, String[] note){
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, note);
        myListView = view.findViewById(R.id.note_list);
        myListView.setAdapter(adapter);

        //查看修改日志
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), edit_diary.class);
                intent.putExtra("showTitle", notes[position].getTitle());
                intent.putExtra("showDate", notes[position].getDate());
                intent.putExtra("showContent", notes[position].getContent());
                startActivity(intent);
            }
        });
        //删除日志
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getLayoutInflater();
                final View v1 = inflater.inflate(R.layout.delete_diary, null);
                builder.setView(v1).setTitle("删除日志").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface diaog, int which) {
                        Note[] noteAfterDelete = new Note[notes.length - 1];
                        for (int i = 0; i < position; i++) {
                            noteAfterDelete[i] = notes[i];
                        }
                        for (int i = position; i < notes.length - 1; i++) {
                            noteAfterDelete[i] = notes[i + 1];
                        }
                        String[] newNote = new String[noteAfterDelete.length];
                        for (int i = 0; i < noteAfterDelete.length; i++) {
                            newNote[i] = noteAfterDelete[i].getTitle() + "\n" + noteAfterDelete[i].getDate();
                        }
                        notes = new Note[noteAfterDelete.length];
                        for (int i = 0; i < notes.length; i++) {
                            notes[i] = noteAfterDelete[i];
                        }
                        noteList(root, newNote);
                    }


                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
    }



}