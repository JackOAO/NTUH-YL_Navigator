package eos.waypointbasedindoornavigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Language_change_Activity extends AppCompatActivity {

    private ListView language_list;

    //SharedPreferencesEditor


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);
        language_list = (ListView) findViewById(R.id.language_list);
        final String[] language = {"繁體中文", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_layout, language);
        language_list.setAdapter(adapter);
        language_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //單擊事件
            //onItemClick(ListView, item )
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                String language_item = (String)tv.getText();
                Log.i("language", "language  : " + language_item);
                //write the language option in SharedPreferences
                SharedPreferences languagePref = PreferenceManager.getDefaultSharedPreferences(GetApplicationContext.getAppContext());
                SharedPreferences.Editor editor = languagePref.edit();
                editor.putString("language",language_item);
                editor.commit();
                Intent intent = new Intent();
                intent = new Intent(Language_change_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent();
            intent = new Intent(Language_change_Activity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



}

