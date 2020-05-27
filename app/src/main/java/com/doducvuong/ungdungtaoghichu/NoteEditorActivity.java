package com.doducvuong.ungdungtaoghichu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    // khai báo các biến
    EditText editText;
    int noteId;

    // hàm main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        // setControl & setEvent
        setControl();
        setEvent();
    }

    // setControl
    private void setControl(){
        editText= (EditText) findViewById(R.id.editText);
    }

    // setEvent
    private void setEvent(){
        Intent intent= getIntent(); // tạo intent nhận id của note từ MainActivity
        noteId= intent.getIntExtra("noteId", -1); // dedault là -1 vì listView có index bắt đầu từ 0

        if (noteId != -1){ // check id nếu khác -1 (nếu id hợp lệ)
            editText.setText(MainActivity.notes.get(noteId)); // lấy id
        }
        else{ // nếu ko có id
            MainActivity.notes.add(""); // tạo ghi chú trống
            noteId= MainActivity.notes.size() - 1; // id bằng size của notes - 1
            MainActivity.arrayAdapter.notifyDataSetChanged(); // cập nhật mảng
        }

        // setEvent khi thay đổi nội dung text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            // trước khi thay đổi nội dung text
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            // trong khi thay đổi nội dung text
            public void onTextChanged(CharSequence s, int start, int before, int count) { // CharSequence: chuỗi kí tự
                MainActivity.notes.set(noteId, String.valueOf(s)); // set char sequence cho notes
                MainActivity.arrayAdapter.notifyDataSetChanged(); // cập nhật mảng

                // tạo Permanent Storage (bộ đệm lưu trữ cố định) để lưu trữ các ghi chú
                SharedPreferences sharedPreferences= getApplicationContext()
                        .getSharedPreferences("com.doducvuong.ungdungtaoghichu", Context.MODE_PRIVATE); // chế độ context private
                HashSet<String> set= new HashSet<>(MainActivity.notes); // tạo chuỗi băm cho notes
                sharedPreferences.edit().putStringSet("notes", set).apply(); // lưu
            }

            @Override
            // sau khi thay đổi nội dung text
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
