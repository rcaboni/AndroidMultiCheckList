# AndroidMultiCheckList
Example how to create a filterable custom multichecked ListView.
The core of the solution is the MultiCheckAdapter, where you can set the MatchMode and id the selected items must be always on top of the list. 
<center>
<img src="https://raw.githubusercontent.com/rcaboni/AndroidMultiCheckList/master/screenshot.png"/>
</center>
You need to pass the layout for the row and the ArrayList with all values, eventually the just selected items ArrayList : 
<pre>
ArrayList<StringWithTag> sportList = new ArrayList<>();
        sportList.add(new StringWithTag("Basket","23"));
        sportList.add(new StringWithTag("Volley","4"));
        sportList.add(new StringWithTag("Soccer","45"));
        sportList.add(new StringWithTag("Beach Volley","2"));
        sportList.add(new StringWithTag("Ski","78"));
        sportList.add(new StringWithTag("Ski jump ","79"));

        ArrayList<StringWithTag> sportSelectedList = new ArrayList<>();
        sportSelectedList.add(new StringWithTag("Volley","4"));
        sportSelectedList.add(new StringWithTag("Beach Volley","2"));
        
        adapter = new MultiCheckAdapter(this,R.layout.multicheckitem,sportList,sportSelectedList);
        adapter.setSelectedOnTop(true);
        adapter.setMatchMode(MultiCheckAdapter.MATCH_SMART);

        listView.setAdapter(adapter);
</pre>

For filter the list you must create an EditText and call the adapter filter 
<pre>
EditText tvFilter = (EditText) findViewById(R.id.tvFilter);
        tvFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MainActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
</pre>
The filter are three mode :
     * MATCH_START : Check items start with search text
     * MATCH_CONTAINS : Check items contains the search text
     * MATCH_SMART : if length search text lower 3 user MATCH START, otherwise MATCH_CONTAINS. this is the default

The selected items are always visible (even don't match the filter),and if you want can positionated on top of the list.
