package com.example.brian.chinesechesscopy;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.views.SquareAdapter;
import com.example.brian.chinesechesscopy.R;

public class MainActivity extends AppCompatActivity {
    // Test push

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardview);
        ChineseChessModel model = new ChineseChessModel(this);
        Log.d("tag", model.toString());


        GridView gridview = (GridView) findViewById(R.id.boardview);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //drawerLayout.addView(gridview);

        SquareAdapter squareAdapter = new SquareAdapter(this, model,gridview);
        gridview.setAdapter(squareAdapter);
        model.setSquareAdapter(squareAdapter);

        model.play();


    }
}
