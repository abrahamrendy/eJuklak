package com.example.ejuklak2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends ActionBarActivity {
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TextView title;
    WebView webView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //set title
        //title = (TextView)findViewById(R.id.title);
        
        //set web view
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
        TextView tv = new TextView(this);
        tv.setTextSize(25.0f);
        tv.setText("Hello World!");
        parentLayout.addView(new TextView(this));
        webView = new WebView(this);
        parentLayout.addView(webView);
        
        //webView = (WebView) findViewById(R.id.web_view);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(true);
        webView.loadUrl("file:///android_asset/juklakhtml.html");
        //webView.requestFocusFromTouch();z
        
        //set action bar's icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : "
                		+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), 
                		Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        
        //handling intent for searching purpose
        handleIntent(getIntent());
    }
    
    private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding parent data
		listDataHeader.add("Kata Pengantar");
		listDataHeader.add("Bab 1");
		listDataHeader.add("Bab 2");
		listDataHeader.add("Bab 3");
		listDataHeader.add("Bab 4");
		listDataHeader.add("Lampiran");

		// Adding child data
		List<String> kataPengantar = new ArrayList<String>();
		kataPengantar.add("Kata Pengantar");
		
		List<String> bab1 = new ArrayList<String>();
		bab1.add("1.1 Sejarah Fakultas Teknologi dan Sains");
		bab1.add("1.2 Visi, Misi, Tujuan, dan Sasaran FTIS");
		bab1.add("1.3 Keberhasilan FTIS");
		bab1.add("1.4 Pengelola Fakultas");
		bab1.add("1.5 Daftar Dosen FTIS");

		List<String> bab2 = new ArrayList<String>();
		bab2.add("2.1 Mata Kuliah Pilihan");
		bab2.add("2.2 Mata Kuliah Prasyarat");
		bab2.add("2.3 Mata Kuliah Layanan");
		bab2.add("2.4 Mata Kuliah Umum");
		bab2.add("2.5 Kurikulum Program Studi Matematika");
		bab2.add("2.6 Kurikulum Program Studi Fisika");
		bab2.add("2.7 Kurikulum Program Studi Teknik Informatika");

		List<String> bab3 = new ArrayList<String>();
		bab3.add("3.1 Penyusunan Rencana Studi");
		bab3.add("3.2 Kegiatan Perkuliahan");
		bab3.add("3.3 Tata Cara Ujian");
		bab3.add("3.4 Cuti dan Gencat Studi");
		bab3.add("3.5 Pengunduran Diri Sebagai Mahasiswa");

		List<String> bab4 = new ArrayList<String>();
		bab4.add("4.1 Evaluasi Keberhasilan Belajar Tiap Mata Kuliah");
		bab4.add("4.2 Evaluasi Keberhasilan Belajar Dalam Satu Tahap Belajar");
		bab4.add("4.3 Kemampuan Bahasa Inggris Mahasiswa UNPAR");

		List<String> lampiran = new ArrayList<String>();
		lampiran.add("Lampiran 1");
		lampiran.add("Lampiran 2");
		lampiran.add("Lampiran 3");
		lampiran.add("Lampiran 4");
		lampiran.add("Lampiran 5");
		lampiran.add("Lampiran 6");
		lampiran.add("Lampiran 7");

		listDataChild.put(listDataHeader.get(0), kataPengantar);
		listDataChild.put(listDataHeader.get(1), bab1);
		listDataChild.put(listDataHeader.get(2), bab2);
		listDataChild.put(listDataHeader.get(3), bab3);
		listDataChild.put(listDataHeader.get(4), bab4);
		listDataChild.put(listDataHeader.get(5), lampiran);
	}
    

    private static final int SEARCH_MENU_ID = Menu.FIRST;
    LinearLayout container;
    Button nextButton, closeButton;
    EditText findBox;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
	        case SEARCH_MENU_ID: {
                search();
	        	return true;
	        }
	        default: return super.onOptionsItemSelected(item);
        }
    }
    
    public void search() {
    	Toast.makeText(getApplicationContext(), "SEARCH BABI", Toast.LENGTH_SHORT).show();
    	container = (LinearLayout)findViewById(R.id.parent_layout);  
        
        nextButton = new Button(this);  
        nextButton.setText("Next");  
        container.addView(nextButton);  
          
        closeButton = new Button(this);
        closeButton.setText("Close");
        container.addView(closeButton);
          
        findBox = new EditText(this);  
	    findBox.setMinEms(30);  
	    findBox.setSingleLine(true);  
	    container.addView(findBox);  
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
 
            /**
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            //txtQuery.setText("Search Query: " + query);
            search();
            //Toast.makeText(getApplicationContext(), "Search Query: " + query, Toast.LENGTH_SHORT).show();
 
        }
 
    }
    
}