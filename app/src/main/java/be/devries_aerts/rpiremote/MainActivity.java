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

    private sshSession sshSes = null;

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
        ToggleButton kodibutton     = (ToggleButton) findViewById(R.id.kodibutton);
        ToggleButton kodikidsbutton = (ToggleButton) findViewById(R.id.kodikidsbutton);
        ToggleButton delugebutton   = (ToggleButton) findViewById(R.id.delugebutton);

        if (!thisbutton.isChecked()) {
           thisbutton.setChecked(true);
        }
        thisbutton.setTypeface(Typeface.DEFAULT_BOLD);

        if (thisbutton == btsyncbutton) {
            kodibutton.setTypeface(Typeface.DEFAULT);
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            kodibutton.setChecked(false);
            kodikidsbutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == kodibutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodikidsbutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == kodikidsbutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodibutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodibutton.setChecked(false);
            delugebutton.setChecked(false);
        } else if (thisbutton == delugebutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodibutton.setTypeface(Typeface.DEFAULT);
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodibutton.setChecked(false);
            kodikidsbutton.setChecked(false);
        }

        sshConnect ssh = new sshConnect();
        ssh.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause called");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop called");
        sshSes.closeSession();
        sshSes = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart called");
        sshSes = new sshSession();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart called");
    }

    public static class sshSession {
        String host = "192.168.56.2";
        String user = "bart";
        String pwd = "master12";
        int port = 22;
        JSch jsch = new JSch();
        Session session = null;
        boolean connected = false;
        public Session getSession (){
            if (!connected) {
                try {
                    session = jsch.getSession(user, host, port);
                    session.setPassword(pwd);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.connect();
                    connected = true;
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                    session = null;
                }
            }
            return session;
        }

        public void closeSession(){
            connected = false;
            session.disconnect();
        }
    }

    public class sshConnect extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        ByteArrayOutputStream Baes = new ByteArrayOutputStream();
        ByteArrayInputStream Bais = new ByteArrayInputStream(new byte[1000]);

        @Override
        protected String doInBackground(String... data) {
            //sshSession jschses = new sshSession();
            //Session ses = null;
            //ses = jschses.Met1();
            try {
                Session ses = sshSes.getSession();
                ChannelExec channel = (ChannelExec) ses.openChannel("exec");
                channel.setOutputStream(Baos);
                channel.setErrStream(Baes);
                //channel.setInputStream(Bais);
                //Run Command
                channel.setCommand("./stats");
                channel.connect(10);
                //try {
                //    Thread.sleep(350);
                //} catch (Exception ee) {
                //}
                channel.disconnect();
                //session.disconnect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //System.out.println("error");
            //System.out.println(Baes.toString());
            //System.out.println("output");
            //System.out.println(Baos.toString());
            return Baos.toString();
        }

        protected void onPostExecute(String output) {
            System.out.println(output);
        }
    }

}
