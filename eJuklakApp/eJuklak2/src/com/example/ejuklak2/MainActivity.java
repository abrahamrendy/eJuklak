package com.example.ejuklak2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
    WebSettings settings;
    IndexParsingTool indexParsingTool;
    String htmlLocation;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    int IDNOW;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        try {
			indexParsingTool = new IndexParsingTool(this, "juklakhtml.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //set web view
        htmlLocation = "file:///android_asset/juklakhtml.html";
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
        webView = new WebView(this);
        parentLayout.addView(webView);
        webView.loadUrl(htmlLocation);
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        
        //set action bar
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //handling intent for searching purpose
        handleIntent(getIntent());
        
        this.createMainDrawer();
    }
    
    public void createMainDrawer() {
    	IDNOW = 0;
        this.setTitle(R.string.app_name);
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
    	expListView.setGroupIndicator(null);
        prepareMainListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
    	listAdapter.setIdNow(IDNOW);
        expListView.setAdapter(listAdapter);
        
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setTitle(listDataHeader.get((int)id));
            	if(indexParsingTool.getHead().get((int)id).child.size() != 0) {
            		createExpandableDrawer((int)id);
            	} else {
	        		drawerLayout.closeDrawers();
            	}
                webView.loadUrl(htmlLocation + "#" + indexParsingTool.getHead().get((int)id).getId());
            	return true;
            }
        });
    }
     
    private void createExpandableDrawer(int id) {
    	IDNOW = id;
    	expListView = (ExpandableListView) findViewById(R.id.left_drawer);
    	expListView.setGroupIndicator(null);
    	prepareListData(id);
    	listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
    	listAdapter.setIdNow(IDNOW);
    	expListView.setAdapter(listAdapter);
    	
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            	if(id==0) {
            		try {
            			return true;
            		} finally {
            			createMainDrawer();
            		}
            	} else {
	                setTitle(listDataHeader.get((int)id));
	                String textId = indexParsingTool.getBab(IDNOW).child.get((int)id-1).getId();
	            	webView.loadUrl(htmlLocation + "#" + textId);
	            	if(indexParsingTool.getBab(IDNOW).child.get((int)id-1).child.size()==0) {
	        			drawerLayout.closeDrawers();
	        		}
            	}
                return false;
            }
        });
 
        // Listview Child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            	groupPosition--;
                String textId = indexParsingTool.getBab(IDNOW).child.get(groupPosition).child.get((int)id).getId();
            	String textText = indexParsingTool.getBab(IDNOW).child.get(groupPosition).child.get((int)id).getText();
                setTitle(textText);
            	webView.loadUrl(htmlLocation + "#" + textId);
        		drawerLayout.closeDrawers();
            	return false;
            }
        });
    }
    
    @Override
    public void onBackPressed() {
    	if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
	    	if(IDNOW != 0)this.createMainDrawer();
	    	else this.drawerLayout.closeDrawers();
    	}
    }
    
    private void prepareMainListData() {
    	listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		List<String> child = new ArrayList<String>();
		String text;
		int i;
		for(i=0; i<indexParsingTool.getHead().size(); i++) {
			text = indexParsingTool.getHead().get(i).getText();
			listDataHeader.add(text);
			child = new ArrayList<String>();
			if(indexParsingTool.getHead().get(i).child.size()!=0) {
				listDataChild.put(listDataHeader.get(i), child);
			}
		}
    }
    
    private void prepareListData(int id) {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		List<String> child = new ArrayList<String>();
		String text;
		int i, j;
		listDataHeader.add("Back");
		listDataChild.put("Back", child);
		for(i=0; i<indexParsingTool.getBab(id).child.size(); i++) {
			text = indexParsingTool.getBab(id).child.get(i).getText();
			listDataHeader.add(text);
			child = new ArrayList<String>();
			for(j=0; j<indexParsingTool.getBab(id).child.get(i).child.size(); j++) {
				text = indexParsingTool.getBab(id).child.get(i).child.get(j).getText();
				child.add(text);
			}
			listDataChild.put(listDataHeader.get(i+1), child);
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
	        case android.R.id.home: { 
	        	if(drawerLayout.isDrawerOpen(expListView)) {
	        		drawerLayout.closeDrawers();
	        	} else {
	        		drawerLayout.openDrawer(expListView);
	        	}
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