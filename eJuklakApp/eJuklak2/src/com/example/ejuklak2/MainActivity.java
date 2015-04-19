package com.example.ejuklak2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    IndexParsingTool indexParsingTool;
    String htmlLocation;
    int IDNOW;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //set title
        //title = (TextView)findViewById(R.id.title);
        
        try {
			indexParsingTool = new IndexParsingTool(this, "juklakhtml.html");
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        
        //set web view
        htmlLocation = "file:///android_asset/juklakhtml.html";
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
        webView.loadUrl(htmlLocation);
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
            	createExpandableDrawer((int)id);
                webView.loadUrl(htmlLocation + "#" + indexParsingTool.getHead().get((int)id).getId());
            	return true;
            }
        });
    }
    
    private void createExpandableDrawer(int id) {
    	IDNOW = id;
    	expListView = (ExpandableListView) findViewById(R.id.left_drawer);
    	prepareListData(id);
    	listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
    	expListView.setAdapter(listAdapter);
    	
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                setTitle(listDataHeader.get((int)id));
                String textId = indexParsingTool.getBab(IDNOW).child.get((int)id).getId();
            	webView.loadUrl(htmlLocation + "#" + textId);
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String textId = indexParsingTool.getBab(IDNOW).child.get(groupPosition).child.get((int)id).getId();
            	String textText = indexParsingTool.getBab(IDNOW).child.get(groupPosition).child.get((int)id).getText();
                setTitle(textText);
            	webView.loadUrl(htmlLocation + "#" + textId);
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
		for(int i=0; i<indexParsingTool.getHead().size(); i++) {
			listDataHeader.add(indexParsingTool.getHead().get(i).getText());
		}
    }
    
    private void prepareListData(int id) {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		String text;
		int i, j;
		for(i=0; i<indexParsingTool.getBab(id).child.size(); i++) {
			text = indexParsingTool.getBab(id).child.get(i).getText();
			listDataHeader.add(text);
			List<String> child = new ArrayList<String>();
			for(j=0; j<indexParsingTool.getBab(id).child.get(i).child.size(); j++) {
				text = indexParsingTool.getBab(id).child.get(i).child.get(j).getText();
				child.add(text);
			}
			listDataChild.put(listDataHeader.get(i), child);
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