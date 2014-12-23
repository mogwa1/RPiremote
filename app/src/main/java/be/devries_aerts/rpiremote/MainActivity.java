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

    /** Called when the user clicks the Settings button */
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

        //sshConnect ssh = new sshConnect();
        //ssh.execute();
    }


    public static class sshSession {
        String host = "84.196.14.200";
        String user = "bart";
        String pwd = "master12";
        int port = 2222;
        JSch jsch = new JSch();
        Session session;
        public Session Met1 (){
            try {
                session = jsch.getSession(user, host, port);
                session.setPassword(pwd);
                session.setConfig("StrictHostKeyChecking", "no");
            } catch (Exception e2){
                System.out.println(e2.getMessage());
            }return session;
        }
        public Session damesession (){
            return session;
        }
        public void close_ses(){
            session.disconnect();
        }
    }

    public class sshConnect extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        ByteArrayInputStream Bais = new ByteArrayInputStream(new byte[1000]);

        @Override
        protected String doInBackground(String... data) {
            sshSession jschses = new sshSession();
            Session ses = null;
            ses = jschses.Met1();
            try {
                ses.connect();
                ChannelExec channel = (ChannelExec) ses.openChannel("exec");
                channel.setOutputStream(Baos);
                channel.setInputStream(Bais);
                //Run Command
                channel.setCommand("stats");
                channel.connect();
                try {
                    Thread.sleep(350);
                } catch (Exception ee) {
                }
                channel.disconnect();
                //session.disconnect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return Baos.toString();

        }
    }

}
