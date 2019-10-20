package com.example.recylerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

    private FloatingActionButton fab_main, fab1_mail, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share;

    Boolean isOpen = false;
    boolean ascending = true;

    private ConstraintLayout constraintLayout;
    Date currentTime = Calendar.getInstance().getTime();
    final SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy '\n' HH:mm");
    final String formattedDate = df.format(currentTime);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        constraintLayout = findViewById(R.id.root_layout);
        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_atilock);


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {


                    fab2_share.startAnimation(fab_close);
                    fab1_mail.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_share.setClickable(false);
                    fab1_mail.setClickable(false);
                    isOpen = false;
                } else {

                    fab2_share.startAnimation(fab_open);
                    fab1_mail.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_share.setClickable(true);
                    fab1_mail.setClickable(true);
                    isOpen = true;
                }

            }
        });



        fab1_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortData(ascending);
                ascending =!ascending;
                fab2_share.startAnimation(fab_close);
                fab1_mail.startAnimation(fab_close);
                fab_main.startAnimation(fab_anticlock);
                fab2_share.setClickable(false);
                fab1_mail.setClickable(false);
                isOpen = false;
            }
        });




        rootLayout = findViewById(R.id.root_layout);
        searchInput = findViewById(R.id.search_input);
        NewsRecyclerview = findViewById(R.id.news_rv);
        mData = new ArrayList<>();


        newsAdapter = new NewsAdapter(this, mData, isDark);
        NewsRecyclerview.setAdapter(newsAdapter);
        NewsRecyclerview.setLayoutManager(new LinearLayoutManager(this));



        fab2_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Content");
                builder.setCancelable(false);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                final int[] photos = {R.drawable.user,R.drawable.uservoyager,R.drawable.circul6,R.drawable.useillust};
                final Random random = new Random();
                int i = random.nextInt(photos.length);
                final EditText edtName = view.findViewById(R.id.et_name);
                final EditText edtContent = view.findViewById(R.id.et_content);
                Button btnAdd = view.findViewById(R.id.btn_add);
                Button btnCancel = view.findViewById(R.id.btn_cancel);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int k = random.nextInt(photos.length);
                        name = edtName.getText().toString();
                        content = edtContent.getText().toString();
                        if(TextUtils.isEmpty(name)){
                            edtName.setError("Not null");
                        }
                        if(TextUtils.isEmpty(content)){
                            edtContent.setError("Not null");
                        }
                        else{
                        NewsItem newsItem = new NewsItem();
                        newsItem.setTitle(name);
                        newsItem.setContent(content);
                        newsItem.setDate(formattedDate);
                        newsItem.setUserPhoto(photos[k]);
                        mData.add(newsItem);
                        newsAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        fab2_share.startAnimation(fab_close);
                        fab1_mail.startAnimation(fab_close);
                        fab_main.startAnimation(fab_anticlock);
                        fab2_share.setClickable(false);
                        fab1_mail.setClickable(false);
                        isOpen = false;
                        }

                    }


                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        fab2_share.startAnimation(fab_close);
                        fab1_mail.startAnimation(fab_close);
                        fab_main.startAnimation(fab_anticlock);
                        fab2_share.setClickable(false);
                        fab1_mail.setClickable(false);
                        isOpen = false;
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
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                newsAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        enableSwipeToDeleteAndUndo();


    }



    private void sortData(boolean asc)
    {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc)
        {
            Collections.sort(mData, new Comparator<NewsItem>() {
                @Override
                public int compare(NewsItem o1, NewsItem o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
        }
        else
        {
            Collections.reverse(mData);
        }


        //ADAPTER
        newsAdapter = new NewsAdapter(this, mData);
        NewsRecyclerview.setAdapter(newsAdapter);

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final NewsItem item = newsAdapter.getData().get(position);

                newsAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        newsAdapter.restoreItem(item, position);
                        NewsRecyclerview.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
            @Override
            public void onMove(int oldPosition, int newPosition) {
                newsAdapter.onMove(oldPosition, newPosition);
            }


        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(NewsRecyclerview);
    }


    private void InitUpdateDialog(final int position, View view) {
        final TextView txtName = view.findViewById(R.id.et_name);
        final TextView txtContent = view.findViewById(R.id.et_content);
        txtName.setText(name);
        txtContent.setText(content);
        Date currentTime = Calendar.getInstance().getTime();final int[] photos = {R.drawable.user,R.drawable.uservoyager};
        final Random random = new Random();
        int i = random.nextInt(photos.length);
        Button btnUpdate = view.findViewById(R.id.btn_add);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnUpdate.setText("Update");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int k = random.nextInt(photos.length);
                name = txtName.getText().toString();
                content = txtContent.getText().toString();
                if(TextUtils.isEmpty(name)){
                    txtName.setError("Not null");
                }
                if(TextUtils.isEmpty(content)){
                    txtContent.setError("Not null");
                }
                else{
                NewsItem newsItem = new NewsItem();
                newsItem.setTitle(name);
                newsItem.setContent(content);
                newsItem.setDate(formattedDate);
                newsItem.setUserPhoto(photos[k]);
                newsAdapter.UpdateItem(position,newsItem);
                Toast.makeText(MainActivity.this,"Content Updated..",Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if ( v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent( event );
//    }
}




