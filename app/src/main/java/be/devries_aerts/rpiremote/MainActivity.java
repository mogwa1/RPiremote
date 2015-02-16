package be.devries_aerts.rpiremote;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "be.devries-aerts.RPiremote.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the Settings button */
    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Toggle buttons */
    public void toggleButtons(View view) {
        ToggleButton thisbutton     = (ToggleButton) view;
        ToggleButton btsyncbutton   = (ToggleButton) findViewById(R.id.btsyncbutton);
        ToggleButton xbmcbutton     = (ToggleButton) findViewById(R.id.xbmcbutton);
        ToggleButton xbmckidsbutton = (ToggleButton) findViewById(R.id.xbmckidsbutton);
        ToggleButton delugebutton   = (ToggleButton) findViewById(R.id.delugebutton);

        if (!thisbutton.isChecked()) {
           thisbutton.setChecked(true);
        }
        thisbutton.setTypeface(Typeface.DEFAULT_BOLD);

        if (thisbutton == btsyncbutton) {
            xbmcbutton.setTypeface(Typeface.DEFAULT);
            xbmckidsbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            xbmcbutton.setChecked(false);
            xbmckidsbutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == xbmcbutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            xbmckidsbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            xbmckidsbutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == xbmckidsbutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            xbmcbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            xbmcbutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == delugebutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            xbmcbutton.setTypeface(Typeface.DEFAULT);
            xbmckidsbutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            xbmcbutton.setChecked(false);
            xbmckidsbutton.setChecked(false);
        }

        sshConnect sshconnect = new sshConnect();
        sshconnect.execute();

    }

    public class sshConnect extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSch jsch = new JSch();
            try {
                Session session = jsch.getSession("bart", "192.168.1.120", 22);
                session.setPassword("master12");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(3000);
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setOutputStream(System.out);
                channel.setErrStream(System.err);
                channel.setCommand("bin/stats");
                channel.connect(3000);
                channel.disconnect();
            } catch(Exception e){
                System.out.println(e);
            }
            return null;
        }
    }

    private class sshSession {
        JSch jsch;
        Session session;

        sshSession() {
            jsch = new JSch();
        }

        sshSession(String user, String host, int port, String pwd) {
            jsch = new JSch();
            open(user, host, port, pwd);
        }

        public void open(String user, String host, int port, String pwd) {
            try {
                session = jsch.getSession(user, host, port);
                session.setPassword(pwd);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(3000);
            } catch(Exception e){
                System.out.println(e);
            }
        }

        public Session getSession() {
            return session;
        }

        public void close() {
            session.disconnect();
        }
    }



}
