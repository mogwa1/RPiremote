package be.devries_aerts.rpiremote;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;



public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop called (Settings)");

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart called (Settings)");

        String host = "192.168.56.2";
        String user = "bart";
        String pwd = "master12";

        EditText serveredit   = (EditText) findViewById(R.id.serverEdit);
        EditText useredit     = (EditText) findViewById(R.id.userEdit);
        EditText passwordedit = (EditText) findViewById(R.id.passwordEdit);

        serveredit.setText(host);
        useredit.setText(user);
        passwordedit.setText(pwd);
    }

}
