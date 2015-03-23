package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.LoopModifier;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andrei on 3/20/2015.
 */
public class OneToRuleTHEMALL extends SimpleBaseGameActivity{
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    Camera camera;
    Scene scene;
    Sprite carSprite;
    Sprite wheelSprite1;
    Sprite wheelSprite2;
    PhysicsHandler carphysicsHandler;

    private PhysicsWorld mPhysicsWorld;

    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
    int x=0;

    private ITextureRegion mBackgroundTextureRegion,mCarTextureRegion,mWheel1,mWheel2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);

    }

    @Override
    public void onCreateResources() throws IOException {
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
     public  Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        scene = new Scene();

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, getVertexBufferObjectManager());
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        scene.attachChild(ground);

        Sprite backgroundSprite = new Sprite(400,240, this.mBackgroundTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);
        carSprite = new Sprite(200,100, this.mCarTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(carSprite);
        wheelSprite1 = new Sprite(120,68,this.mWheel1,getVertexBufferObjectManager());

        wheelSprite2 = new Sprite(295,70,this.mWheel1,getVertexBufferObjectManager());
        scene.attachChild(wheelSprite1);
        scene.attachChild(wheelSprite2);


        SequenceEntityModifier s1 =new SequenceEntityModifier(

                new RotationModifier(3, 0, 360)
                //new AlphaModifier(2, 1, 0),
                //new AlphaModifier(1, 0, 1),
                //new ScaleModifier(2, 1, 0.5f),
                //new DelayModifier(0.5f)

                //new ParallelEntityModifier(
                //new ScaleModifier(3, 0.5f, 5),
                //		new RotationByModifier(3, 360)
                //),
                //new ParallelEntityModifier(
                //new ScaleModifier(3, 5, 1),
                //		new RotationModifier(3, 360, 0)
                //)
        );
        final LoopEntityModifier entityModifier =
                new LoopEntityModifier(
                        new IEntityModifier.IEntityModifierListener() {
                            @Override
                            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(GameScene.this, "Sequence started.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                            }

                            @Override
                            public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(GameScene.this, "Sequence finished.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                            }
                        }
                        ,
                        999,
                        new LoopEntityModifier.ILoopEntityModifierListener() {
                            @Override
                            public void onLoopStarted(final LoopModifier<IEntity> pLoopModifier, final int pLoop, final int pLoopCount) {
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(GameScene.this, "Loop: '" + (pLoop + 1) + "' of '" + pLoopCount + "' started.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                            }

                            @Override
                            public void onLoopFinished(final LoopModifier<IEntity> pLoopModifier, final int pLoop, final int pLoopCount) {
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(GameScene.this, "Loop: '" + (pLoop + 1) + "' of '" + pLoopCount + "' finished.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                            }
                        },
                        s1
                );
        wheelSprite2.registerEntityModifier(s1);
        wheelSprite1.registerEntityModifier(s1);
        carphysicsHandler = new PhysicsHandler(carSprite);
        carSprite.registerUpdateHandler(carphysicsHandler);


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:",btAdapter.getName());
        if(btAdapter != null) { tgDevice = new TGDevice(btAdapter, handler);
            tgDevice.connect(true);
            Log.v("Device",""+tgDevice.getState());
        }
        camera.setChaseEntity(carSprite);
        return scene;
    }
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:
                Log.v("HelloEEG","State changed");
                switch (msg.arg1) {
                    case TGDevice.STATE_IDLE: break;
                    case TGDevice.STATE_CONNECTING:
                        Log.v("HelloEEG","Connecting");
                        Toast connecting = Toast.makeText(getApplicationContext(),"Connecting", Toast.LENGTH_SHORT);
                        connecting.show();
                        break;
                    case TGDevice.STATE_CONNECTED: tgDevice.start();
                        Log.v("HelloEEG","Connected");
                        Toast connected = Toast.makeText(getApplicationContext(),"Connected to "+tgDevice.getConnectedDevice().getName(), Toast.LENGTH_SHORT);
                        connected.show();
                        break;
                    case TGDevice.STATE_DISCONNECTED:
                        Toast disconnected = Toast.makeText(getApplicationContext(),"Disconnected", Toast.LENGTH_SHORT);
                        disconnected.show();
                        break;
                    case TGDevice.STATE_NOT_FOUND: case TGDevice.STATE_NOT_PAIRED: default:
                        break;

                } break; case TGDevice.MSG_POOR_SIGNAL:
                // Log.v("HelloEEG", "PoorSignal: " + msg.arg1);

            case TGDevice.MSG_ATTENTION:
                Log.v("HelloEEG", "Attention: " + msg.arg1);

                if(carSprite!=null&&wheelSprite1!=null&&wheelSprite2!=null)
                {
                    //wheelSprite1.setPosition(msg.arg1*5-80+200,68);
                    //wheelSprite2.setPosition(msg.arg1*5+95+200,70);
                   //carphysicsHandler.setAcceleration(msg.arg1, 0);
                   // tgDevice.stop();
                   // x=0;
                    if(msg.arg1!=0) {
                        if (x <= msg.arg1) {
                            carphysicsHandler.setAccelerationX(+1);
                        } else {
                            carphysicsHandler.setAccelerationX(-1);
                        }
                        x = msg.arg1;

                        // carSprite.setPosition(msg.arg1*5+200,100);
                        //tgDevice.start();
                    }
                  }
                break;
            case TGDevice.MSG_BLINK:
                //Log.v("HelloEEG", "Blinks:" + msg.arg1);
                break;
            case TGDevice.MSG_MEDITATION:
                //Log.v("HelloEEG", "Meditation:" +msg.arg1);
                //  progressMeditation.setProgress(msg.arg1);
                break;
            case TGDevice.MSG_RAW_DATA:
                // int rawValue = msg.arg1;
                // Log.v("HelloEEG", "Raw Data: "+ rawValue);
                break;
            case TGDevice.MSG_EEG_POWER:

                TGEegPower ep = (TGEegPower)msg.obj;
                //Log.v("HelloEEG", "Delta: " + ep.delta); default:
                break;
        }
        }
    };
    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

    }

}

