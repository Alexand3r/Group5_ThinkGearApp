package g5.mindwave;

import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import g5.mindwave.SceneManager.SceneType;
/**
 * Created by Andrei on 3/23/2015.
 */
public class GameScene extends BaseScene {

   // private SmoothCamera mCamera;
   public TGDevice tgDevice;
    Sprite carSprite;
    Sprite wheelSprite1;
    Sprite wheelSprite2;
    Body wheelBody1,wheelBody2,carBody;
    public Handler handler;
    static RevoluteJoint rj1;
    static RevoluteJoint rj2;
    AutoParallaxBackground autoParallaxBackground;

    private PhysicsWorld mPhysicsWorld;

    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private static FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 0.0f, 0.2f);
    private HUD gameHUD;
    private Text scoreText;
    private Text highScore;
    private int highSc;
    private int score;

    @Override
    public void createScene() {
    engine.registerUpdateHandler(new FPSLogger());


        CreateWorld();
        //Parallax - este foarte greu sa-l rendezi
        ThinkGear();
        loadParallax();

        createCar();
        SetCamera();

        createHUD();


    }

    private void loadParallax() {
        autoParallaxBackground = new AutoParallaxBackground(255, 255, 255, 5);

        this.setBackground(autoParallaxBackground);

        final Sprite parallaxLayerBackSprite = new Sprite(0, 0, resourcesManager.mParallaxLayerBackTextureRegion, vbom);
        parallaxLayerBackSprite.setOffsetCenter(0, 0);
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprite));

        final Sprite parallaxLayerMidSprite = new Sprite(0, CAMERA_HEIGHT - resourcesManager.mParallaxLayerMidTextureRegion.getHeight() - 80, resourcesManager.mParallaxLayerMidTextureRegion, vbom);
        parallaxLayerMidSprite.setOffsetCenter(0, 0);
        ParallaxEntity pMid = new ParallaxEntity(-50.0f, parallaxLayerMidSprite);
        autoParallaxBackground.attachParallaxEntity(pMid);

        final Sprite parallaxLayerFrontSprite = new Sprite(0, 0, resourcesManager.mParallaxLayerFrontTextureRegion, vbom);
        parallaxLayerFrontSprite.setOffsetCenter(0, 0);
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-150.0f, parallaxLayerFrontSprite));
        autoParallaxBackground.stop();
      //  final Sprite parallaxLayerFrontSecondSprite = new Sprite(0, 0, resourcesManager.mParallaxLayerFrontSecondTextureRegion, vbom);
      //  parallaxLayerFrontSecondSprite.setOffsetCenter(0, 0);
      //  autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, parallaxLayerFrontSecondSprite));

    }

    private void ThinkGear() {
        engine.runOnUpdateThread(new Runnable() {
                                     @Override
                                     public void run() {

                                         handler = new Handler(Looper.getMainLooper()) {

                                             @Override
                                             public void handleMessage(Message msg) {
                                                 switch (msg.what) {
                                                     case TGDevice.MSG_STATE_CHANGE:
                                                         Log.v("HelloEEG", "State changed");
                                                         switch (msg.arg1) {
                                                             case TGDevice.STATE_IDLE: break;
                                                             case TGDevice.STATE_CONNECTING:
                                                                 Log.v("HelloEEG","Connecting");
                                                                 Toast connecting = Toast.makeText(activity.getApplicationContext(),"Connecting", Toast.LENGTH_SHORT);
                                                                 connecting.show();
                                                                 break;
                                                             case TGDevice.STATE_CONNECTED:
                                                                 tgDevice.start();
                                                                 Log.v("HelloEEG","Connected");
                                                                 //TODO Start the wheels spinning, timer, etc.

                                                                 Toast connected = Toast.makeText(activity.getApplicationContext(),"Connected to "+tgDevice.getConnectedDevice().getName(), Toast.LENGTH_SHORT);
                                                                 connected.show();

                                                                 break;
                                                             case TGDevice.STATE_DISCONNECTED:
                                                                 Toast disconnected = Toast.makeText(activity.getApplicationContext(),"Disconnected", Toast.LENGTH_SHORT);
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
                                                         Log.v("Motor", Float.toString(rj2.getMotorSpeed()));
                                                         if(started&&msg.arg1>0)
                                                         { start();
                                                         if(stopped)
                                                         {rj2.setMotorSpeed(msg.arg1*100);
                                                         }}
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
                                         if (resourcesManager.btAdapter != null) {
                                             Log.v("test",resourcesManager.btAdapter.getName());
                                             tgDevice = new TGDevice(btAdapter, handler);
                                             Log.v("tst", tgDevice.toString());
                                             tgDevice.connect(true);
                                             Log.v("Device", "" + tgDevice.getState());
                                         }
                                     }
                                 }
        );
    }
    boolean stopped = true;
    boolean started = true;
    public void stop()
    {
        resourcesManager.musicEngineRev.stop();
    autoParallaxBackground.stop();
        rj2.enableMotor(false);
       // rj2.setMotorSpeed(-rj2.getJointSpeed()*50);
        rj2.setLimits(0,50);

        rj2.enableLimit(true);
       // rj2.setMaxMotorTorque(0);
        stopped = false;
    }
    public void start()
    {
        resourcesManager.musicEngineRev.play();
        rj2.enableMotor(true);
        autoParallaxBackground.start();
        resourcesManager.mMusic.pause();
        started = false;
        scoreTimeHandler();
    }

    public void createCar()
    {
        //Car Body

        carSprite = new Sprite(200,100, resourcesManager.mCarTextureRegion,vbom);
        carBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld,carSprite, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(carSprite,carBody,true,true));
        MassData carBodyMass = carBody.getMassData();
        carBodyMass.mass= 3.9f;
        carBody.setMassData(carBodyMass);
        this.attachChild(carSprite);

        resourcesManager.mMusic.play();
        //Car Wheels


        wheelSprite1 = new Sprite(118,53,resourcesManager.mWheel1,vbom);
        wheelBody1= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite1, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite1,wheelBody1));
        MassData wheel1Mass = wheelBody1.getMassData();
        wheel1Mass.mass = 2.9f;
        wheelBody1.setMassData(wheel1Mass);
        this.attachChild(wheelSprite1);


        wheelSprite2 = new Sprite(292,55,resourcesManager.mWheel1,vbom);
        wheelBody2= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite2, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite2,wheelBody2));
        wheelBody2.setMassData(wheel1Mass);
        this.attachChild(wheelSprite2);

        //Connect back wheel to the car body using revoluteJoint

        final RevoluteJointDef revoluteJointDef1 = new RevoluteJointDef();
        revoluteJointDef1.initialize(wheelBody1, carBody, wheelBody1.getWorldCenter());
        revoluteJointDef1.enableMotor = false;
        revoluteJointDef1.motorSpeed = 1000;
        revoluteJointDef1.maxMotorTorque = 100;
        rj1 = (RevoluteJoint) this.mPhysicsWorld.createJoint(revoluteJointDef1);

        //Connect front wheel to the car body using revoluteJoint

        final RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
        revoluteJointDef2.initialize(wheelBody2, carBody, wheelBody2.getWorldCenter());
        revoluteJointDef2.enableMotor = true;
        //speed
        revoluteJointDef2.motorSpeed = 50;
        //Torque, more torque = more wheel 'burnout'
        revoluteJointDef2.maxMotorTorque = 10;
        rj2 = (RevoluteJoint) this.mPhysicsWorld.createJoint(revoluteJointDef2);


    }
        public void SetCamera()
    {
        camera.setBounds(0, 240, 99999,240);
        camera.setBoundsEnabled(true);
        camera.setChaseEntity(carSprite);
    }

    public void CreateWorld()

    {
    //Physics
    this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
    final Rectangle ground = new Rectangle(400, 30 ,160000.0f, 2, vbom);
        ground.setColor(Color.BLACK);
    final Rectangle wall =  new Rectangle(1500,0,100,2000,vbom);
    final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
    PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
    this.attachChild(wall);
    this.attachChild(ground);


    //Background

    //Sprite backgroundSprite = new Sprite(400,240, resourcesManager.mBackgroundTextureRegion,vbom);
   // this.attachChild(backgroundSprite);
    //Sprite backgroundSprite1 = new Sprite(800,240, this.mBackgroundTextureRegion,getVertexBufferObjectManager());
    //scene.attachChild(backgroundSprite1);
    this.registerUpdateHandler(this.mPhysicsWorld);
    this.registerUpdateHandler(new IUpdateHandler() {
        @Override
        public void reset() { }
        @Override
        public void onUpdate(final float pSecondsElapsed) {
            if(wall.collidesWith(carSprite)) {
                wall.setColor(1, 0, 0);
                stop();
            } else {
                wall.setColor(0, 1, 0);
            }
        }
    });
     //this.enableAccelerationSensor(this);
    final Vector2 gravity = Vector2Pool.obtain(0, -9.8f);
    this.mPhysicsWorld.setGravity(gravity);
    //Vector2Pool.recycle(gravity);

}

    public void createHUD() {

        gameHUD = new HUD();

        scoreText = new Text(20, 420, resourcesManager.font, "Score:0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0 ,0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        highScore = new Text(250, 420, resourcesManager.font, "High Score:0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        highScore.setAnchorCenter(0, 0);
        highScore.setText("High Score: 0");
        gameHUD.attachChild(highScore);
        camera.setHUD(gameHUD);
        scoreTimeHandler();

    }

    private void updateScore() {

        score++;
        scoreText.setText("Score: " + score);
        
        if (score >= highSc) {
            highSc = score;
        }
          highScore.setText("High Score: " + highSc);
    }

    private void scoreTimeHandler() {
         TimerHandler scoreTimerHandler;
         float scoreUp = 1;

                 scoreTimerHandler = new TimerHandler(scoreUp, true, new ITimerCallback() {
             @Override
             public void onTimePassed(TimerHandler pTimerHandler) {
                 if(rj2.isMotorEnabled()) {
                     updateScore();}
                 }
             });
       engine.registerUpdateHandler(scoreTimerHandler);

                 }

    @Override
    public void onBackKeyPressed() {
       /* carSprite.setPosition(200, 100);

        ThinkGear();
        //rj2.enableMotor(true);
        autoParallaxBackground.stop();
        rj2.enableLimit(false);
        score = 0;
        started = true;
        stopped = true;
       GameScene gameScene;
        gameScene = null;
        gameScene = new GameScene();
        engine.setScene(gameScene);*/
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {

        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        camera.setChaseEntity(null);
        this.detachChildren();
    }
}
