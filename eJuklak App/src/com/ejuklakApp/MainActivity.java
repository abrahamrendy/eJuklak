package com.ejuklakApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ejuklak2.R;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
 
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private WebView webView;
    private WebSettings settings;
    private IndexParsingTool indexParsingTool;
    private String htmlLocation;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private int IDNOW;
    private long time1, time2;
    
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
        this.setTitle(R.string.app_name);
        
        this.createMainDrawer();
    }
    
    public void createMainDrawer() {
    	IDNOW = 0;
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
    	} else {
    		time2 = System.currentTimeMillis();
    		if(time2-time1<=2000){
				System.exit(1);;
			} else {
				Toast.makeText(getApplicationContext(), "Press back again to leave", Toast.LENGTH_SHORT).show();
				time1 = System.currentTimeMillis();
			}
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
    
}