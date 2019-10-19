package com.example.recylerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RecyclerView NewsRecyclerview;
    NewsAdapter newsAdapter;
    List<NewsItem> mData = new ArrayList<>();
    FloatingActionButton fabSwitcher;
    boolean isDark = false;
    ConstraintLayout rootLayout;
    EditText searchInput ;
    CharSequence search="";
    String name, content;
    AlertDialog.Builder builder;
    AlertDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // let's make this activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // hide the action bar

        getSupportActionBar().hide();


        // ini view

        fabSwitcher = findViewById(R.id.fa_add);
        rootLayout = findViewById(R.id.root_layout);
        searchInput = findViewById(R.id.search_input);
        NewsRecyclerview = findViewById(R.id.news_rv);
        mData = new ArrayList<>();

        // load theme state

//        isDark = getThemeStatePref();
        if (isDark) {
            // dark theme is on

            searchInput.setBackgroundResource(R.drawable.search_input_dark_style);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.black));

        } else {
            // light theme is on
            searchInput.setBackgroundResource(R.drawable.search_input_style);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.white));

        }

        newsAdapter = new NewsAdapter(this, mData, isDark);
        NewsRecyclerview.setAdapter(newsAdapter);
        NewsRecyclerview.setLayoutManager(new LinearLayoutManager(this));


        fabSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Content");
                builder.setCancelable(false);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                final int[] photos = {R.drawable.user,R.drawable.uservoyager};
                final Random random = new Random();
                int i = random.nextInt(photos.length);
                final EditText edtName = view.findViewById(R.id.et_name);
                final EditText edtContent = view.findViewById(R.id.et_content);
                Button btnAdd = view.findViewById(R.id.btn_add);
                Button btnCancel = view.findViewById(R.id.btn_cancel);
                Date currentTime = Calendar.getInstance().getTime();
                final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String formattedDate = df.format(currentTime);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(name)){
                            edtName.setError("The item name cannot be empty");
                        }
                        if(TextUtils.isEmpty(content)){
                            edtContent.setError("The item content cannot be empty");
                        }
                        else{
                        int k = random.nextInt(photos.length);
                        name = edtName.getText().toString();
                        content = edtContent.getText().toString();
                        NewsItem newsItem = new NewsItem();
                        newsItem.setTitle(name);
                        newsItem.setContent(content);
                        newsItem.setDate(formattedDate);
                        newsItem.setUserPhoto(photos[k]);
                        mData.add(newsItem);
                        newsAdapter.notifyDataSetChanged();
                        edtName.setText("");
                        edtContent.setText("");
                        dialog.dismiss();}

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        newsAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, NewsItem newsItem) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Update User Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout,null,false);
                InitUpdateDialog(position,view);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void InitUpdateDialog(final int position, View view) {
        final TextView txtName = view.findViewById(R.id.et_name);
        final TextView txtContent = view.findViewById(R.id.et_content);
        txtName.setText(name);
        txtContent.setText(content);
        Date currentTime = Calendar.getInstance().getTime();final int[] photos = {R.drawable.user,R.drawable.uservoyager};
        final Random random = new Random();
        int i = random.nextInt(photos.length);
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(currentTime);
        Button btnUpdate = view.findViewById(R.id.btn_add);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnUpdate.setText("Update");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int k = random.nextInt(photos.length);
                name = txtName.getText().toString();
                content = txtContent.getText().toString();
                NewsItem newsItem = new NewsItem();
                newsItem.setTitle(name);
                newsItem.setContent(content);
                newsItem.setDate(formattedDate);
                newsItem.setUserPhoto(photos[k]);
                newsAdapter.UpdateItem(position,newsItem);
                Toast.makeText(MainActivity.this,"Content Updated..",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}


//        searchInput.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                        newsAdapter.getFilter().filter(s);
//                        search = s;
//
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//
//
//            }
//
//            private void saveThemeStatePref(boolean isDark) {
//
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putBoolean("isDark", isDark);
//                editor.commit();
//            }
//
//            private boolean getThemeStatePref() {
//
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
//                boolean isDark = pref.getBoolean("isDark", false);
//                return isDark;
//
//            }
//        }

