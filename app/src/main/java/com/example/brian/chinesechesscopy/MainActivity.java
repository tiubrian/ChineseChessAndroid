package com.example.brian.chinesechesscopy;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.brian.chinesechesscopy.controllers.SettingsListener;
import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.views.SquareAdapter;
import com.example.brian.chinesechesscopy.R;

public class MainActivity extends AppCompatActivity {
    // Test push

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardview);
        ChineseChessModel model = new ChineseChessModel(this, false, false);
        Log.d("tag", model.toString());


        GridView gridview = (GridView) findViewById(R.id.boardview);

        SquareAdapter squareAdapter = new SquareAdapter(this, model,gridview);
        gridview.setAdapter(squareAdapter);
        model.setSquareAdapter(squareAdapter);


        DrawerLayout settings = (DrawerLayout) findViewById(R.id.drawerLayout);
        Button newGameButton = (Button) findViewById(R.id.newgamebutton);
        Button cancelButton = (Button) findViewById(R.id.cancelbutton);

        Switch redCompSwitch = (Switch) findViewById(R.id.switch7);
        Switch blackCompSwitch = (Switch) findViewById(R.id.switch8);

        newGameButton.setOnClickListener(new SettingsListener(settings,"newgame",model, squareAdapter, this, gridview, redCompSwitch, blackCompSwitch));
        cancelButton.setOnClickListener(new SettingsListener(settings,"cancel",model, squareAdapter, this, gridview, redCompSwitch, blackCompSwitch));




        model.play();


    }


}
