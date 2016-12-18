package it.aldea.android.demo.multichecklistdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import it.aldea.android.adapter.MultiCheckAdapter;
import it.aldea.android.databean.StringWithTag;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    Button button;
    ListView listView;
    MultiCheckAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

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

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new MultiCheckAdapter(this,R.layout.multicheckitem,sportList,sportSelectedList);
        adapter.setSelectedOnTop(true);
        adapter.setMatchMode(MultiCheckAdapter.MATCH_SMART);

        listView.setAdapter(adapter);

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
        button.setOnClickListener(this);
    }

    private void findViewsById() {
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.testbutton);
    }

    public void onClick(View v) {

        Toast.makeText(this, "selectedItems : " + adapter.getSelected().toString(), Toast.LENGTH_LONG).show();

    }
}
