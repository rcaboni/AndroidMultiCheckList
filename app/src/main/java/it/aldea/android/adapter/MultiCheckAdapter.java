package it.aldea.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import it.aldea.android.demo.multichecklistdemo.R;
import it.aldea.android.databean.StringWithTag;

/**
 * Created by Roberto on 18/12/2016.
 */
public class MultiCheckAdapter extends ArrayAdapter {
    public static final int MATCH_START = 1;
    public static final int MATCH_CONTAINS = 2;
    public static final int MATCH_SMART = 3;

    private final LayoutInflater inflater;
    private List<StringWithTag> items;
    private List<StringWithTag> origItems;
    private int resource;
    private TextFilter textFilter;
    private HashMap hashSelected = new HashMap();
    private boolean selectedOnTop=true;
    private int matchMode= MATCH_SMART;

    public MultiCheckAdapter(Context ctx, int resourceId, List<StringWithTag> objects, List<StringWithTag> selected) {
        this(ctx,resourceId,objects,selected,true);
    }
    public MultiCheckAdapter(Context ctx, int resourceId, List<StringWithTag> objects, List<StringWithTag> selected,boolean orderItems) {
        super(ctx, resourceId, objects);
        try {
            inflater = LayoutInflater.from(ctx);
            resource = resourceId;
            if (selected != null) {
                for (StringWithTag item : selected) {
                    hashSelected.put(item.getTag(), item.toString());
                }
            }
            items = orderItems ? sortList(objects) : objects;
            origItems = items;
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        } else {
            return items.size();
        }
    }

    @Override
    public StringWithTag getItem(int position) {
        if (items != null) {
            return items.get(position);
        } else {
            return null;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        try {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.position = position;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkBox.setChecked(hashSelected.containsKey(items.get(position).getTag()));
            holder.checkBox.setTag(position);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelected((CheckBox) v);
                }
            });

            holder.textView.setText(items.get(position).toString());
            holder.textView.setTag(position);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                    updateSelected(holder.checkBox);
                }
            });


        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return convertView;
    }
    private void updateSelected(CheckBox v) {
        if (v.isChecked()) {
            hashSelected.put(items.get((Integer) v.getTag()).getTag(),items.get((Integer) v.getTag()).toString());
        }else {
            hashSelected.remove(items.get((Integer) v.getTag()).getTag());
        }
    }
    class ViewHolder {
        TextView textView;
        CheckBox checkBox;
        int position;
    }

    public List<StringWithTag> getSelected() {
        ArrayList<StringWithTag> selectedItems = new ArrayList<StringWithTag>();
        Iterator<Object> iterator = hashSelected.keySet().iterator();
        while (iterator.hasNext()) {
            Object itemTag = iterator.next();
            selectedItems.add(new StringWithTag(hashSelected.get(itemTag).toString(),itemTag));
        }
        return selectedItems;
    }

    public boolean isSelectedOnTop() {
        return selectedOnTop;
    }

    /**
     * If true, all selected items are on top. Default TRUE
     * @param selectedOnTop
     */
    public void setSelectedOnTop(boolean selectedOnTop) {
        this.selectedOnTop = selectedOnTop;
    }

    public int getMatchMode() {
        return matchMode;
    }

    /**
     * Set Search match mode :
     * MATCH_START : Check items start with search text
     * MATCH_CONTAINS : Check items contains the search text
     * MATCH_SMART : if length search text lower 3 user MATCH START, otherwise MATCH_CONTAINS. DEFAULT
     * @param matchMode
     */
    public void setMatchMode(int matchMode) {
        this.matchMode = matchMode;
    }

    @Override
    public Filter getFilter() {

        if (textFilter == null) {
            textFilter = new TextFilter(false);
        }
        return textFilter;
    }

    private class TextFilter extends Filter {
        private boolean filterSelected = false;
        private ArrayList<StringWithTag> selectedItem = null;

        public TextFilter(boolean filterSelected) {
            this.filterSelected = filterSelected;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (List<StringWithTag>) results.values;
            notifyDataSetChanged();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the original list
                results.values = sortList(origItems);
                results.count = origItems.size();
                if (!filterSelected) {
                    selectedItem = (ArrayList) getSelected();
                }
            } else {
                List<StringWithTag> filteredItems = new ArrayList<StringWithTag>();

                if (!filterSelected) {
                    selectedItem = (ArrayList) getSelected();
                }
                boolean bSearch;
                for (StringWithTag p : origItems) {
                    bSearch = false;
                    if (!filterSelected) {
                        if (hashSelected.containsKey(p.getTag())) {
                            filteredItems.add(p);
                            bSearch = true;
                        }
                    }
                    if (!bSearch) {
                        switch (getMatchMode()) {
                            case MATCH_SMART:
                                if (constraint.length()<3) {
                                    if (p.toString().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                                        filteredItems.add(p);
                                    }
                                } else {
                                    if (p.toString().toUpperCase().contains(constraint.toString().toUpperCase())) {
                                        filteredItems.add(p);
                                    }
                                }
                                break;
                            case MATCH_START:
                                if (p.toString().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                                    filteredItems.add(p);
                                }
                                break;
                            case MATCH_CONTAINS:
                                if (p.toString().toUpperCase().contains(constraint.toString().toUpperCase())) {
                                    filteredItems.add(p);
                                }
                                break;
                        }

                    }
                }

                filteredItems = sortList(filteredItems);

                results.values = filteredItems;
                results.count = filteredItems.size();
            }
            return results;
        }
    }

    private List<StringWithTag> sortList(List<StringWithTag> filteredItems) {
        if (isSelectedOnTop()) {
            // Sorting Items
            Collections.sort(filteredItems, new Comparator<StringWithTag>() {
                @Override
                public int compare(StringWithTag item2, StringWithTag item1) {
                    boolean item1Sel = hashSelected.containsKey(item1.getTag());
                    boolean item2Sel = hashSelected.containsKey(item2.getTag());
                    if (item1Sel && item2Sel) {
                        return item2.toString().compareTo(item1.toString());
                    } else {
                        if (item1Sel) {
                            return 1;
                        } else if (item2Sel) {
                            return -1;
                        } else {
                            return item2.toString().compareTo(item1.toString());
                        }
                    }
                }
            });
        }

        return filteredItems;
    }
}

    
