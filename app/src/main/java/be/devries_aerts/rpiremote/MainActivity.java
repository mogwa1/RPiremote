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

    /** Called when the user clicks a button */
    public void toggleButtons(View view) {
        //toggleExclusiveButtons(view);
        toggleIndividualButtons(view);
    }

    public void toggleIndividualButtons(View view) {
        String command = "";
        ToggleButton thisbutton     = (ToggleButton) view;
        ToggleButton btsyncbutton   = (ToggleButton) findViewById(R.id.btsyncbutton);
        ToggleButton kodibutton     = (ToggleButton) findViewById(R.id.kodibutton);
        ToggleButton kodikidsbutton = (ToggleButton) findViewById(R.id.kodikidsbutton);
        ToggleButton delugebutton   = (ToggleButton) findViewById(R.id.delugebutton);

        if (thisbutton == btsyncbutton)   command = "btsync";
        if (thisbutton == kodibutton)     command = "kodi";
        if (thisbutton == kodikidsbutton) command = "kodikids";
        if (thisbutton == delugebutton)   command = "deluged";

        if (thisbutton.isChecked()) {
            thisbutton.setTypeface(Typeface.DEFAULT_BOLD);
            command = "./start" + command;
        } else {
            thisbutton.setTypeface(Typeface.DEFAULT);
            command = "./stop" + command;
        }

        System.out.println(command);
        sshConnect ssh = new sshConnect();
        ssh.execute(command);
    }
    
    public void toggleExclusiveButtons(View view) {
        String command = "";
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
            command = "./stopkodi\n./stopkodikids\n./stopdeluged\n./startbtsync";
        } else if (thisbutton == kodibutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodikidsbutton.setChecked(false);
            delugebutton.setChecked(false);
            command = "./stopbtsync\n./stopkodikids\n./stopdeluged\n./startkodi";
        } else if (thisbutton == kodikidsbutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodibutton.setTypeface(Typeface.DEFAULT);
            delugebutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodibutton.setChecked(false);
            delugebutton.setChecked(false);
            command = "./stopbtsync\n./stopkodi\n./stopdeluged\n./startkodikids";
        } else if (thisbutton == delugebutton) {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
            kodibutton.setTypeface(Typeface.DEFAULT);
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
            btsyncbutton.setChecked(false);
            kodibutton.setChecked(false);
            kodikidsbutton.setChecked(false);
            command = "./stopbtsync\n./stopkodi\n./stopkodikids\n./startdeluged";
        }

        sshConnect ssh = new sshConnect();
        ssh.execute(command);
    }

    public void setButtonState(boolean btsync, boolean kodi, boolean kodikids, boolean deluge) {
        ToggleButton btsyncbutton   = (ToggleButton) findViewById(R.id.btsyncbutton);
        ToggleButton kodibutton     = (ToggleButton) findViewById(R.id.kodibutton);
        ToggleButton kodikidsbutton = (ToggleButton) findViewById(R.id.kodikidsbutton);
        ToggleButton delugebutton   = (ToggleButton) findViewById(R.id.delugebutton);

        btsyncbutton.setChecked(btsync);
        kodibutton.setChecked(kodi);
        kodikidsbutton.setChecked(kodikids);
        delugebutton.setChecked(deluge);
        
        if (btsync) {
            btsyncbutton.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            btsyncbutton.setTypeface(Typeface.DEFAULT);
        }
        if (kodi) {
            kodibutton.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            kodibutton.setTypeface(Typeface.DEFAULT);
        }
        if (kodikids) {
            kodikidsbutton.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            kodikidsbutton.setTypeface(Typeface.DEFAULT);
        }
        if (deluge) {
            delugebutton.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            delugebutton.setTypeface(Typeface.DEFAULT);
        }   
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
        sshConnect ssh = new sshConnect();
        ssh.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart called");
    }

    public class sshSession {
        String host = "192.168.56.2";
        String user = "bart";
        String pwd = "master12";
        int port = 22;
        JSch jsch = new JSch();
        Session session = null;

        public Session getSession (){
            boolean createNewSession = false;
            if (session == null) {
                createNewSession = true;
            } else if (!session.isConnected()) {
                createNewSession = true;
            }
            if (createNewSession) {
                try {
                    session = jsch.getSession(user, host, port);
                    session.setPassword(pwd);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.connect();
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                    session = null;
                }
            }
            return session;
        }

        public void closeSession(){
            session.disconnect();
        }
    }

    public class sshConnect extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        //ByteArrayOutputStream Baes = new ByteArrayOutputStream();
        //ByteArrayInputStream Bais = new ByteArrayInputStream(new byte[1000]);

        @Override
        protected String doInBackground(String... data) {
            String command = "./stats";
            String output;

            if (data.length == 1) {
                //System.out.println(data[0]);
                command = data[0];
            }

            try {
                Session ses = sshSes.getSession();
                ChannelExec channel = (ChannelExec) ses.openChannel("exec");
                channel.setOutputStream(Baos);
                //channel.setErrStream(Baes);
                //channel.setInputStream(Bais);
                //Run Command
                channel.setCommand(command);
                channel.connect();
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
                channel.disconnect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            output = Baos.toString();

            if (data.length == 1)  {
                output = "";
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (!output.equals("")) {
                //System.out.println("output = "+output);

                boolean btsync = false;
                boolean kodi = false;
                boolean kodikids = false;
                boolean deluged = false;

                String[] parts = output.split("\n");
                for (String part: parts) {
                    String[] fragments = part.split(" ");
                    String process = fragments[0];
                    int status = Integer.parseInt(fragments[1]);
                    if (process.equals("btsync")) {
                        if (status == 1) {
                            btsync = true;
                        }
                    }
                    if (process.equals("kodi")) {
                        if (status == 1) {
                            kodi = true;
                        }
                    }
                    if (process.equals("kodikids")) {
                        if (status == 1) {
                            kodikids = true;
                        }
                    }
                    if (process.equals("deluged")) {
                        if (status == 1) {
                            deluged = true;
                        }
                    }
                }
                setButtonState(btsync, kodi, kodikids, deluged);
            }
        }
    }
}
