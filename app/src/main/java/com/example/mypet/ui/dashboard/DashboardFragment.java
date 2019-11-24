package com.example.mypet.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mypet.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private ImageView mThumbnail;
    private TextView mPath;
    private Button mBtnTakePhoto;
    private static final int CAMERA_REQUEST_CODE = 1;
    private File img;
    private Dialog dialog;
    Uri uri;
    Bitmap bm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        initViews(root);
        return root;
    }

    private void initViews(final View root) {
        mThumbnail = root.findViewById(R.id.iv_thumbil);
        mPath = root.findViewById(R.id.tv_path);
        mBtnTakePhoto = root.findViewById(R.id.btn_take_photo);

        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

        //长按删除照片
        mThumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getLayoutInflater();
                final View v1 = inflater.inflate(R.layout.delete_photo, null);
                builder.setView(v1).setTitle("删除照片").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface diaog, int which) {
                        mThumbnail = root.findViewById(R.id.iv_thumbil);
                        mThumbnail.setImageDrawable(null);
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


        //点击查看照片
        mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(root);
                dialog.show();
            }
        });
    }

    private void init(View view) {
        mThumbnail = view.findViewById(R.id.image);
        //展示在dialog上面的大图
        dialog = new Dialog(getActivity(),R.style.FullActivity);
        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);
        mThumbnail = getImageView();
        dialog.setContentView(mThumbnail);
        //大图的点击事件（点击让他消失）
        mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //动态的ImageView
    private ImageView getImageView(){
        ImageView imageView = new ImageView(getContext());
        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //imageView设置图片
        imageView.setImageBitmap(bm);
//        @SuppressLint("ResourceType")
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        imageView.setImageBitmap(bitmap);
        return imageView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    bm = extras.getParcelable("data");
                    mThumbnail.setImageBitmap(bm);
                    uri = saveBitmap(bm);
                    //mPath.setText(img.getPath());
//					startImageZoom(uri);
                }
            }
        }
    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir;
        if (hasSD()) {
            tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.upc.avatar");
        } else {
            tmpDir = new File(Environment.getDataDirectory() + "/com.upc.avatar");
        }

        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        img = new File(tmpDir.getAbsolutePath() + "avater.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否有SD卡
     */
    private boolean hasSD() {
        //如果有SD卡 则下载到SD卡中
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;

        } else {
            //如果没有SD卡
            return false;
        }
    }

}