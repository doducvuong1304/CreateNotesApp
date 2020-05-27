package com.doducvuong.ungdungtaoghichu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    // khai báo các biến
    static ArrayList<String> notes= new ArrayList<>();
    ListView listView;
    static ArrayAdapter arrayAdapter; // ket noi data voi ListView

    // tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // thêm ghi chú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // setEvent cho item add_note
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note){ // add note là id cua item trong menu
            Intent intent= new Intent(getApplicationContext(), NoteEditorActivity.class); // chuyển đến NoteEditorActivity
            startActivity(intent);
            return true;
        }
        return false;
    }

    // hàm main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setControl & setEvent
        setControl();
        setEvent();
    }

    // setControl
    private void setControl(){
        listView= (ListView) findViewById(R.id.listView);
    }

    // setEvent
    private void setEvent(){

        // tạo Permanent Storage (bộ đệm lưu trữ cố định) để lưu trữ các ghi chú
        SharedPreferences sharedPreferences= getApplicationContext()
                .getSharedPreferences("com.doducvuong.ungdungtaoghichu", Context.MODE_PRIVATE); // chế độ context private
        HashSet<String> set= (HashSet<String>) sharedPreferences.getStringSet("notes", null); // tạo chuỗi băm

        // kiểm tra xem có ghi chú nào không
        if (set == null){ // nếu chưa có ghi chú nào
            notes.add("Example note"); // thêm 1 note ví dụ
        }
        else{ // nếu đã có ghi chú trong bộ đệm Permanent Storage
            notes= new ArrayList(set); // lấy toàn bộ data từ bộ đệm được lưu để hiển thị cho người dùng xem
        }

        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes); // loại ListView 1
        listView.setAdapter(arrayAdapter);

        // setEvent cho listView - khi click vào note chuyển qua edit note
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), NoteEditorActivity.class); // chuyển qua NoteEditorActivity
                intent.putExtra("noteId", position); // gửi id của note tới NoteEditor
                startActivity(intent);
            }
        });

        // setEvent xóa ghi chú - khi click giữ lâu vào ghi chú
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int itemToDelete= position; // item để xóa

                new AlertDialog.Builder(MainActivity.this). // tạo hộp thoại cảnh báo
                        setIcon(android.R.drawable.ic_dialog_alert) // set icon cảnh báo
                        .setTitle("Are you sure?") // tiêu đề cảnh báo
                        .setMessage("Do you want to delete this note?") // câu hỏi cảnh báo
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() { // setEvent cho button Yes
                            @Override
                            public void onClick(DialogInterface dialog, int which) { // which: cái ghi chú muốn xóa
                                notes.remove(itemToDelete); // xóa ghi chú
                                arrayAdapter.notifyDataSetChanged(); // cập nhật danh sách ghi chú

                        // tạo Permanent Storage (bộ đệm lưu trữ cố định) để lưu trữ các ghi chú
                        SharedPreferences sharedPreferences= getApplicationContext()
                        .getSharedPreferences("com.doducvuong.ungdungtaoghichu", Context.MODE_PRIVATE); // chế độ context private
                         HashSet<String> set= new HashSet<>(MainActivity.notes); // tạo chuỗi băm cho notes
                         sharedPreferences.edit().putStringSet("notes", set).apply(); // lưu
                            }
                        })
                        .setNegativeButton("No", null) // không xóa nữa
                        .show(); // hiển thị dialog cảnh báo
                return true; // trả về true nếu ko khi click lâu nó sẽ tưởng mình vẫn muốn click nhanh
            }
        });
    }

}
