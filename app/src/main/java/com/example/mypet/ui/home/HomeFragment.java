package com.example.mypet.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mypet.R;

import java.io.File;
import java.io.FileNotFoundException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final int ALBUM_RESULT_CODE = 1;
    private ImageView picture;
    private File img;
    private  final int REQUEST_EXTERNAL_STORAGE = 1;
    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.append(s);
//            }
//        });

        verifyStoragePermissions(getActivity());

        final TextView name = root.findViewById(R.id.name);
        final TextView sex = root.findViewById(R.id.sex);
        final TextView birth = root.findViewById(R.id.birth);
        final TextView food = root.findViewById(R.id.food);
        final TextView hobby = root.findViewById(R.id.hobby);
        final Button modify = root.findViewById(R.id.modify);
        picture = root.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                //调用打开相册
                openSysAlbum();

            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modify_builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater1 = getLayoutInflater();
                final View modify_view = inflater1.inflate(R.layout.edit_data, null);
                modify_builder.setView(modify_view).setTitle("资料修改").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editName = modify_view.findViewById(R.id.editName);
                        EditText editSex = modify_view.findViewById(R.id.editSex);
                        EditText editBirth = modify_view.findViewById(R.id.editBirth);
                        EditText editFood = modify_view.findViewById(R.id.editFood);
                        EditText editHobby = modify_view.findViewById(R.id.editHobby);

                        String newName = editName.getText().toString();
                        String newSex = editSex.getText().toString();
                        String newBirth = editBirth.getText().toString();
                        String newFood = editFood.getText().toString();
                        String newHobby = editHobby.getText().toString();

                        name.setText(" 名字： " + newName);
                        sex.setText(" 性别： " + newSex);
                        birth.setText(" 生日： " + newBirth);
                        food.setText(" 喜欢的食物： " + newFood);
                        hobby.setText(" 习惯： " + newHobby);

                    }

                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                modify_builder.show();
            }
        });

        return root;
    }

    /**
     * 打开系统相册
     */
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALBUM_RESULT_CODE) {
            if (data == null) {
                Log.e("!!!", "null!");
                return;
            } else {
                Uri uri = data.getData();
                //String imagePath = getImagePath(uri, null);
                //displayImage(imagePath);
                displayImage(uri);
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            Log.e("path:", imagePath);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);
//
//        } else {
//            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void displayImage(Uri uri)  {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        picture.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openSysAlbum();
                }else{
                    Toast.makeText(getContext(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

}