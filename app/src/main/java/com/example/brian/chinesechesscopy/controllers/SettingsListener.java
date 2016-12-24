package com.example.brian.chinesechesscopy.controllers;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.brian.chinesechesscopy.R;
import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.views.SquareAdapter;

/**
 * Created by Brian on 16/12/24.
 */

public class SettingsListener implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private String action;
    private ChineseChessModel model;
    private SquareAdapter squareAdapter;
    private Context mContext;
    private GridView gridview;
    private Switch redcompswitch;
    private Switch blackcompswitch;

    public SettingsListener(DrawerLayout drawerLayout, String action, ChineseChessModel model, SquareAdapter squareAdapter, Context mContext, GridView gridview, Switch redcompswitch, Switch blackcompswitch) {
        this.drawerLayout = drawerLayout;
        this.action = action;
        this.model = model;
        this.squareAdapter = squareAdapter;
        this.mContext = mContext;
        this.gridview = gridview;
        this.redcompswitch = redcompswitch;
        this.blackcompswitch = blackcompswitch;
    }

    @Override
    public void onClick(View v) {
        switch (action) {
            case "newgame":

                boolean redcompon = redcompswitch.isChecked();
                boolean blackcompon = blackcompswitch.isChecked();
                if(redcompon && blackcompon) {
                    drawerLayout.closeDrawers();
                    CharSequence text = "You cannot have two computers play each other";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(mContext, text, duration);
                    toast.show();
                    return;
                }

                ChineseChessModel model = new ChineseChessModel(mContext,redcompon,blackcompon);
                Log.d("tag", model.toString());

                SquareAdapter squareAdapter = new SquareAdapter(mContext, model,gridview);
                gridview.setAdapter(squareAdapter);
                model.setSquareAdapter(squareAdapter);

                squareAdapter.repaint();
                drawerLayout.closeDrawers();
                model.play();
                break;
            case "cancel":
                drawerLayout.closeDrawers();
                break;
            default: System.out.println("Should never come here! Settings Listener");
        }
    }
}
