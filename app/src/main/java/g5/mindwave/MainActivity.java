package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;


public class MainActivity extends ActionBarActivity {
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    ProgressBar progressMeditation;
    ProgressBar progressAttention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressMeditation =(ProgressBar) findViewById(R.id.progressBar);
        progressAttention =(ProgressBar) findViewById(R.id.progressBar2);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:",btAdapter.getName());
        if(btAdapter != null) { tgDevice = new TGDevice(btAdapter, handler);
            tgDevice.connect(true);
        Log.v("Device",""+tgDevice.getState());
        }
    }
    public void sendMessage(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) { switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:
                Log.v("HelloEEG","State changed");
                switch (msg.arg1) {
                case TGDevice.STATE_IDLE: break;
                case TGDevice.STATE_CONNECTING:
                    Log.v("HelloEEG","Connecting");
                    break;
                case TGDevice.STATE_CONNECTED: tgDevice.start();
                    Log.v("HelloEEG","Connected");
                    break;
                case TGDevice.STATE_DISCONNECTED:
                    break;
                case TGDevice.STATE_NOT_FOUND: case TGDevice.STATE_NOT_PAIRED: default:
                    break;

            } break; case TGDevice.MSG_POOR_SIGNAL:
               // Log.v("HelloEEG", "PoorSignal: " + msg.arg1);

            case TGDevice.MSG_ATTENTION:
                Log.v("HelloEEG", "Attention: " + msg.arg1);
                progressAttention.setProgress(msg.arg1);
                break;
            case TGDevice.MSG_BLINK:
                Log.v("HelloEEG", "Blinks:" + msg.arg1);
                break;
            case TGDevice.MSG_MEDITATION:
                 Log.v("HelloEEG", "Meditation:" +msg.arg1);
                progressMeditation.setProgress(msg.arg1);
                break;
            case TGDevice.MSG_RAW_DATA:
               // int rawValue = msg.arg1;
               // Log.v("HelloEEG", "Raw Data: "+ rawValue);
                break;
            case TGDevice.MSG_EEG_POWER:

                TGEegPower ep = (TGEegPower)msg.obj;
                Log.v("HelloEEG", "Delta: " + ep.delta); default:
                break;
        }
        }
    };


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
