package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andrei on 3/20/2015.
 */
public class OneToRuleTHEMALL extends SimpleBaseGameActivity{
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    Sprite carSprite;
    Sprite wheelSprite1;
    Sprite wheelSprite2;
    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private ITextureRegion mBackgroundTextureRegion,mCarTextureRegion,mWheel1,mWheel2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);

    }
    @Override
    protected void onCreateResources() throws IOException {
       try {
           ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
               @Override
               public InputStream open() throws IOException {
                   return getAssets().open("gfx/splash1.png");
               }
           });
           backgroundTexture.load();
           this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
           ITexture carTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
               @Override
               public InputStream open() throws IOException {
                   return getAssets().open("gfx/car2.png");
               }
           });
           carTexture.load();
           this.mCarTextureRegion = TextureRegionFactory.extractFromTexture(carTexture);

           ITexture wheel1Texture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
               @Override
               public InputStream open() throws IOException {
                   return getAssets().open("gfx/wheel.png");
               }
           });
           wheel1Texture.load();
            this.mWheel1 = TextureRegionFactory.extractFromTexture(wheel1Texture);
       }
       catch (IOException e) {
           Debug.e(e);
       }
    }

    @Override
    protected Scene onCreateScene() {
        final Scene scene = new Scene();
        Sprite backgroundSprite = new Sprite(400,240, this.mBackgroundTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);
        carSprite = new Sprite(200,100, this.mCarTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(carSprite);
        wheelSprite1 = new Sprite(120,68,this.mWheel1,getVertexBufferObjectManager());

        wheelSprite2 = new Sprite(295,70,this.mWheel1,getVertexBufferObjectManager());
        scene.attachChild(wheelSprite1);
        scene.attachChild(wheelSprite2);


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:",btAdapter.getName());
        if(btAdapter != null) { tgDevice = new TGDevice(btAdapter, handler);
            tgDevice.connect(true);
            Log.v("Device",""+tgDevice.getState());
        }
        return scene;
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

                if(carSprite!=null&&wheelSprite1!=null&&wheelSprite2!=null)
                {   wheelSprite1.setPosition(msg.arg1*5-80,68);
                    wheelSprite2.setPosition(msg.arg1*5+95,70);
                    carSprite.setPosition(msg.arg1*5,100);}
                break;
            case TGDevice.MSG_BLINK:
                Log.v("HelloEEG", "Blinks:" + msg.arg1);
                break;
            case TGDevice.MSG_MEDITATION:
                Log.v("HelloEEG", "Meditation:" +msg.arg1);
                //  progressMeditation.setProgress(msg.arg1);
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
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

    }
}
