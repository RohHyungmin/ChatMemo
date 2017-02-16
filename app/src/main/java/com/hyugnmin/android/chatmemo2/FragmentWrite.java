package com.hyugnmin.android.chatmemo2;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyugnmin.android.chatmemo2.data.DBHelper;
import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.hyugnmin.android.chatmemo2.interfaces.DetailInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWrite extends Fragment implements View.OnClickListener {

    Button btnInput, btnDone, btnCamera;
    EditText editTextInput;
    View view = null;
    RecyclerView recyclerViewMain;
    RecyclerViewMainAdapter recyclerViewMainAdapter;

    Context context = null;
    DetailInterface detailInterface = null;
    List<Memo> datas = new ArrayList<>();
    CardAdapter cardAdapter;

    private final int REQ_CAMERA = 101;
    Uri fileUri = null;
    List<String> datasCamera = new ArrayList<>();



    public FragmentWrite() {
    }

    public static FragmentWrite newInstance() {
        FragmentWrite fragment = new FragmentWrite();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_fragment_write, container, false);

            btnInput = (Button) view.findViewById(R.id.btnInput);
            btnCamera = (Button) view.findViewById(R.id.btnCamera);
            btnDone = (Button) view.findViewById(R.id.btnDone);
            editTextInput = (EditText) view.findViewById(R.id.editTextInput);
            recyclerViewMain = (RecyclerView) view.findViewById(R.id.recyclerViewMain);

            List<Memo> datas = new ArrayList<>();

            CardAdapter cardAdapter = new CardAdapter(datas, getContext());
            recyclerViewMain.setAdapter(cardAdapter);
            recyclerViewMain.setLayoutManager(new LinearLayoutManager(getContext()));



            btnDone.setOnClickListener(this);
            btnInput.setOnClickListener(this);
            btnCamera.setOnClickListener(this);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.detailInterface = (DetailInterface) context;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.btnInput:
                try {
                    Memo memo = new Memo();
                    String string = editTextInput.getText().toString();
                    memo.setMemo(string);
                    memo.setDate(new Date(System.currentTimeMillis()));

                    detailInterface.saveToList(memo);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                editTextInput.setText("");
                break;

            case R.id.btnDone:
                try {
                    detailInterface.saveToRead();
                    detailInterface.resetData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnCamera :
                Log.i("카메라", "카메라~~~~~~~~~~~~~~~~~~~~```");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    // 저장할 미디어 속성을 정의하는 클래스
                    ContentValues values = new ContentValues(1);
                    // 속성중에 파일의 종류를 정의
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    // 전역변수로 정의한 fileUri에 외부저장소 컨텐츠가 있는 Uri 를 임시로 생성해서 넣어준다.
                    fileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    // 위에서 생성한 fileUri를 사진저장공간으로 사용하겠다고 설정
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    // Uri에 읽기와 쓰기 권한을 시스템에 요청
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(intent, REQ_CAMERA);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQ_CAMERA :
                if (resultCode == RESULT_OK && fileUri != null) {
                    datasCamera = loadDataCamera();

                    refreshCardAdapter();

                }
                else {
                    Toast.makeText(getContext(), "사진파일이 없습니다", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    public List<String> loadDataCamera(){

        datasCamera = new ArrayList<>();

        // 폰에서 이미지를 가져온후 datas 에 세팅한다
        ContentResolver resolver = getContext().getContentResolver();
        // 1. 데이터 Uri 정의
        Uri target = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 2. projection 정의
        String projection[] = { MediaStore.Images.ImageColumns.DATA }; // 이미지 경로
        // 정렬 추가 - 날짜순 역순 정렬
        String order = MediaStore.Images.ImageColumns.DATE_ADDED +" DESC";

        // 3. 데이터 가져오기
        Cursor cursor = resolver.query(target, projection, null, null, order);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                String uriString = cursor.getString(0);
                datasCamera.add(uriString);
            }
            cursor.close();
        }
        return datasCamera;
    }

    public void setData(List<Memo> datas){
        this.datas = datas;
    }

    public void refreshCardAdapter() {
        cardAdapter = new CardAdapter(datas, context);
        recyclerViewMain.setAdapter(cardAdapter);
        cardAdapter.getFileUri(fileUri);
        cardAdapter.notifyDataSetChanged();
    }


}
