package com.example.ejuklak2;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;
    private int IDNOW;
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }
 
    public void setListDataHeader(List<String> listDataHeader) {
    	this.listDataHeader = listDataHeader;
    }
    
    public void setListDataChild(HashMap<String, List<String>> listDataChild) {
    	this.listDataChild = listDataChild;
    }
    
    public void setIdNow(int IDNOW) {
    	this.IDNOW = IDNOW;
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView image = (ImageView)convertView.findViewById(R.id.arrowIcon);
        if(IDNOW==0) {
        	if(listDataChild.get(listDataHeader.get(groupPosition))==null) {
        		image.setVisibility(View.INVISIBLE);
        	} else {
        		int imageResourceId = R.drawable.arrow_right;
        		image.setImageResource(imageResourceId);
	        	image.setVisibility(View.VISIBLE);
        	}
        } else {
        	if(groupPosition==0) {
        		int imageResourceId = R.drawable.arrow_left;
	        	image.setImageResource(imageResourceId);
	        	image.setVisibility(View.VISIBLE);
	        	
	        	//set arrow to the left side
	            RelativeLayout.LayoutParams lpImage = (android.widget.RelativeLayout.LayoutParams) image.getLayoutParams();
	            lpImage.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	            lpImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	            lblListHeader.setPadding(100, 0, 0, 0);
        	} else {
		        if(listDataChild.get(listDataHeader.get(groupPosition)).size()==0) {
		        	image.setVisibility(View.INVISIBLE);
		        } else {
		        	int imageResourceId = isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down;
		        	image.setImageResource(imageResourceId);
		        	image.setVisibility(View.VISIBLE);
		        }
        	}
        }
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}