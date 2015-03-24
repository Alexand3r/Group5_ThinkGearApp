/*package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;


 // Created by Andrei on 3/20/2015.

//public class OneToRuleTHEMALL extends SimpleBaseGameActivity {
    // TGDevice tgDevice;
    //BluetoothAdapter btAdapter;
    //Camera camera;
    Scene scene;
    Sprite carSprite;
    Sprite wheelSprite1;
    Sprite wheelSprite2;
    Body wheelBody1,wheelBody2,carBody;
    ResourcesManager resourcesManager;
    static RevoluteJoint rj1;
    static RevoluteJoint rj2;

    PhysicsHandler carPhysicsHandler;

    private PhysicsWorld mPhysicsWorld;

    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private static FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 0.0f, 0.2f);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateResources() throws IOException {
        resourcesManager = ResourcesManager.getInstance();
       // resourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
       // resourcesManager.loadGraphics();
    }

    @Override
     public  Scene onCreateScene() {


        //Engine Options

        this.mEngine.registerUpdateHandler(new FPSLogger());
        //Scene

        scene = new Scene();
        scene.setBackground(new Background(0, 0, 0));

        //Physics

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        final Rectangle ground = new Rectangle(400, 30 ,160000.0f, 2, getVertexBufferObjectManager());
        final Rectangle wall =  new Rectangle(1500,0,100,2000,getVertexBufferObjectManager());
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
       // PhysicsFactory.createBoxBody(this.mPhysicsWorld, wall, BodyDef.BodyType.StaticBody, wallFixtureDef);
        scene.attachChild(wall);
        scene.attachChild(ground);


        //Logo

        Sprite backgroundSprite = new Sprite(400,240, resourcesManager.mBackgroundTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);
        //Sprite backgroundSprite1 = new Sprite(800,240, this.mBackgroundTextureRegion,getVertexBufferObjectManager());
        //scene.attachChild(backgroundSprite1);

        //Car Body

        carSprite = new Sprite(200,100, resourcesManager.mCarTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(carSprite);

        carBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld,carSprite, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(carSprite,carBody,true,true));
        //Car Wheels


        wheelSprite1 = new Sprite(118,53,resourcesManager.mWheel1,getVertexBufferObjectManager());
        wheelBody1= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite1, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite1,wheelBody1));
        scene.attachChild(wheelSprite1);


        wheelSprite2 = new Sprite(292,55,resourcesManager.mWheel1,getVertexBufferObjectManager());
        wheelBody2= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite2, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite2,wheelBody2));
        scene.attachChild(wheelSprite2);


        float x = wheelSprite1.getX();
        float y = wheelSprite1.getY();



        //Connect back wheel to th car body using revoluteJoint

        //Roata din spate
        final RevoluteJointDef revoluteJointDef1 = new RevoluteJointDef();
        revoluteJointDef1.initialize(wheelBody1, carBody, wheelBody1.getWorldCenter());
        revoluteJointDef1.enableMotor = false;
        revoluteJointDef1.motorSpeed = 0;
        revoluteJointDef1.maxMotorTorque = 100;
        rj1 = (RevoluteJoint) this.mPhysicsWorld.createJoint(revoluteJointDef1);

        //Connect front wheel to rear axle using revoluteJoint

        final RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
        revoluteJointDef2.initialize(wheelBody2, carBody, wheelBody2.getWorldCenter());
        revoluteJointDef2.enableMotor = true;
        revoluteJointDef2.motorSpeed = 0;
        revoluteJointDef2.maxMotorTorque = 100;
        rj2 = (RevoluteJoint) this.mPhysicsWorld.createJoint(revoluteJointDef2);


        // carphysicsHandler = new PhysicsHandler(carSprite);
      //  carSprite.registerUpdateHandler(carphysicsHandler);

        //Add Gravity
        scene.registerUpdateHandler(this.mPhysicsWorld);
        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() { }
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                if(wall.collidesWith(carSprite)) {
                   wall.setColor(1, 0, 0);
                } else {
                    wall.setColor(0, 1, 0);
                }
            }
        });
       // this.enableAccelerationSensor(this);
        final Vector2 gravity = Vector2Pool.obtain(0, -9.8f);
        this.mPhysicsWorld.setGravity(gravity);
        //Vector2Pool.recycle(gravity);
        //ThinkGear Connection
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
                        //TODO Start the wheels spinning, timer, etc.

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


                break;
            case TGDevice.MSG_BLINK:
                //Log.v("HelloEEG", "Blinks:" + msg.arg1);
                break;
            case TGDevice.MSG_MEDITATION:
                Log.v("HelloEEG", "Meditation:" +msg.arg1);
                rj2.setMotorSpeed(msg.arg1);
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
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);

    }



}
*/
