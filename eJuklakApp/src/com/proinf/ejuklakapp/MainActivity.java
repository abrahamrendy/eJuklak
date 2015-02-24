package com.proinf.ejuklakapp;


import java.io.BufferedReader;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private Button createButton(String text){
		Button b = new Button(this);
		b.setText(text);
		//b.setSr
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_button));
		return b;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ScrollView sv = new ScrollView(this);
        //sv.
        LinearLayout listButton = new LinearLayout(this);
        listButton.addView(createButton("Kata Pengantar"));//kata pengantar
        listButton.addView(createButton("Bab 1"));//bab 1
        listButton.addView(createButton("Bab 2"));//bab 2
        listButton.addView(createButton("Bab 3"));//bab 3
        listButton.addView(createButton("Bab 4"));//bab 4
        listButton.addView(createButton("Lampiran"));//lampiran
        sv.addView(listButton);*/
        //listButton.set
        //listButton.setScr
        WebView view = new WebView(this);
        view.loadUrl("file:///android_asset/bab1.html");
        
        //view.setPadding(0, 0, 0, 0);
        //view.
        //setContentView(view);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/*getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/
		
        setContentView(R.layout.activity_main);
        GridLayout parentLayout = ((GridLayout)findViewById(R.id.parentLayout));
        LinearLayout listButton = (LinearLayout)findViewById(R.id.tracks);
        listButton.addView(createButton("Kata Pengantar"));//kata pengantar
        listButton.addView(createButton("Bab 1"));//bab 1
        listButton.addView(createButton("Bab 2"));//bab 2
        listButton.addView(createButton("Bab 3"));//bab 3
        listButton.addView(createButton("Bab 4"));//bab 4
        listButton.addView(createButton("Lampiran"));//lampiran
        //parentLayout.addView(sv);
        parentLayout.addView(view);
        
        //TextView tv = ((TextView) findViewById(R.id.textView1));
        //tv.setText(Html.fromHtml("<html>alalala</html>"));
    }
}
