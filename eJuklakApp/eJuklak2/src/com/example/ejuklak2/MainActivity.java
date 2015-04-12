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
        
        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        
        //handling intent for searching purpose
        handleIntent(getIntent());
        
        this.createMainDrawer();
    }
    
    public void createMainDrawer() {
        this.setTitle(R.string.app_name);
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        prepareMainListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            	//set ActionBar title
                setTitle(listDataHeader.get((int)id));
            	if(groupPosition==1 || groupPosition==2)createExpandableDrawer((int)id);
            	return true;
            }
        });
    }
    
    private void createExpandableDrawer(int id) {
        
    	// get the listview
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
 
        // preparing list data
        prepareListData(id);
 
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
    }
    
    @Override
    public void onBackPressed() {
        this.createMainDrawer();
    }
    
    private void prepareMainListData() {
    	listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		// Adding parent data
		listDataHeader.add("Kata Pengantar");
		listDataHeader.add("Bab 1");
		listDataHeader.add("Bab 2");
		listDataHeader.add("Bab 3");
		listDataHeader.add("Bab 4");
		listDataHeader.add("Lampiran");
		listDataChild.put(listDataHeader.get(0), new ArrayList<String>());
		listDataChild.put(listDataHeader.get(1), new ArrayList<String>());
		listDataChild.put(listDataHeader.get(2), new ArrayList<String>());
		listDataChild.put(listDataHeader.get(3), new ArrayList<String>());
		listDataChild.put(listDataHeader.get(4), new ArrayList<String>());
		listDataChild.put(listDataHeader.get(5), new ArrayList<String>());
    }
    
    private void prepareListData(int id) {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		switch(id) {
			case(1) :
				listDataHeader.add("1.1 Sejarah Fakultas Teknologi dan Sains");
				listDataHeader.add("1.2 Visi, Misi, Tujuan, dan Sasaran FTIS");
				listDataHeader.add("1.3 Keberhasilan FTIS");
				listDataHeader.add("1.4 Pengelola Fakultas");
				listDataHeader.add("1.5 Daftar Dosen FTIS");
				List<String> bab11 = new ArrayList<String>();
				List<String> bab12 = new ArrayList<String>();
				List<String> bab13 = new ArrayList<String>();
				List<String> bab14 = new ArrayList<String>();
				List<String> bab15 = new ArrayList<String>();
				bab12.add("1.2.1 Visi FTIS");
				bab12.add("1.2.2 Misi FTIS");
				bab12.add("1.2.3 Tujuan FTIS");
				bab12.add("1.2.4 Sasaran FTIS");
				listDataChild.put(listDataHeader.get(0), bab11);
				listDataChild.put(listDataHeader.get(1), bab12);
				listDataChild.put(listDataHeader.get(2), bab13);
				listDataChild.put(listDataHeader.get(3), bab14);
				listDataChild.put(listDataHeader.get(4), bab15);
				break;
				
			case(2) :
				listDataHeader.add("2.1 Mata Kuliah Pilihan");
				listDataHeader.add("2.2 Mata Kuliah Prasyarat");
				listDataHeader.add("2.3 Mata Kuliah Layanan");
				listDataHeader.add("2.4 Mata Kuliah Umum");
				listDataHeader.add("2.5 Kurikulum Program Studi Matematika");
				listDataHeader.add("2.6 Kurikulum Program Studi Fisika");
				listDataHeader.add("2.7 Kurikulum Program Studi Teknik Informatika");
				List<String> bab21 = new ArrayList<String>();
				List<String> bab22 = new ArrayList<String>();
				List<String> bab23 = new ArrayList<String>();
				List<String> bab24 = new ArrayList<String>();
				List<String> bab25 = new ArrayList<String>();
				List<String> bab26 = new ArrayList<String>();
				List<String> bab27 = new ArrayList<String>();
				bab23.add("2.3.1 Mata Kuliah Layanan Program Studi Matematika");
				bab23.add("2.3.2 Mata Kuliah Layanan Program Studi Fisika");
				bab23.add("2.3.3 Mata Kuliah Layanan Program Studi Teknik Informatika");
				bab23.add("2.3.3 Mata Kuliah Layanan Program Studi Teknik Informatika");
				
				bab24.add("2.4.1 Susunan Mata Kuliah Umum");
				bab24.add("2.4.2 Uraian Singkat Mata Kuliah Umum");
				
				bab25.add("2.5.1 Susunan Mata Kuliah");
				bab25.add("2.5.2 Mata Kuliah Pilihan Program Studi Matematika");
				bab25.add("2.5.3 Peta dan Prasyarat");
				bab25.add("2.5.4 Deskripsi Singkat Mata Kuliah Program Studi Fisika");
				
				bab26.add("2.6.1 Susunan Mata Kuliah");
				bab26.add("2.6.2 Mata Kuliah Pilihan Program Studi Fisika");
				bab26.add("2.6.3 Peta dan Prasyarat");
				bab26.add("2.6.4 Deskripsi Singkat Mata Kuliah Program Studi Fisika");
				
				bab27.add("2.7.1 Susunan Mata Kuliah");
				bab27.add("2.7.2 Mata Kuliah Pilihan Program Studi Teknik Informatika");
				bab27.add("2.7.3 Peta dan Prasyarat");
				bab27.add("2.7.4 Deskripsi Singkat Mata Kuliah Program Studi Teknik Informatika");
				listDataChild.put(listDataHeader.get(0), bab21);
				listDataChild.put(listDataHeader.get(1), bab22);
				listDataChild.put(listDataHeader.get(2), bab23);
				listDataChild.put(listDataHeader.get(3), bab24);
				listDataChild.put(listDataHeader.get(4), bab25);
				listDataChild.put(listDataHeader.get(5), bab26);
				listDataChild.put(listDataHeader.get(6), bab27);
				break;
				
			case(3) :
				List<String> bab3 = new ArrayList<String>();
				bab3.add("3.1 Penyusunan Rencana Studi");
				bab3.add("3.2 Kegiatan Perkuliahan");
				bab3.add("3.3 Tata Cara Ujian");
				bab3.add("3.4 Cuti dan Gencat Studi");
				bab3.add("3.5 Pengunduran Diri Sebagai Mahasiswa");
				break;
				
			case(4) :
				List<String> bab4 = new ArrayList<String>();
				bab4.add("4.1 Evaluasi Keberhasilan Belajar Tiap Mata Kuliah");
				bab4.add("4.2 Evaluasi Keberhasilan Belajar Dalam Satu Tahap Belajar");
				bab4.add("4.3 Kemampuan Bahasa Inggris Mahasiswa UNPAR");
				break;
				
			case(5) :
				List<String> lampiran = new ArrayList<String>();
				lampiran.add("Lampiran 1");
				lampiran.add("Lampiran 2");
				lampiran.add("Lampiran 3");
				lampiran.add("Lampiran 4");
				lampiran.add("Lampiran 5");
				lampiran.add("Lampiran 6");
				lampiran.add("Lampiran 7");
				
		}
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
	        	return true;
	        }
	        default: return super.onOptionsItemSelected(item);
        }
    }
    
    public void search(String query) {
    	Toast.makeText(getApplicationContext(), "Search Query: " + query, Toast.LENGTH_SHORT).show();
    	
    	/*
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
	    */  
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
            search(query);
            //Toast.makeText(getApplicationContext(), "Search Query: " + query, Toast.LENGTH_SHORT).show();
 
        }
 
    }
    
}