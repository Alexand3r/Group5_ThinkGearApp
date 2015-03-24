package g5.mindwave;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import g5.mindwave.SceneManager.SceneType;
/**
 * Created by Andrei on 3/23/2015.
 */
public class GameScene extends BaseScene {

    private Camera mCamera;

    Sprite carSprite;
    Sprite wheelSprite1;
    Sprite wheelSprite2;
    Body wheelBody1,wheelBody2,carBody;

    static RevoluteJoint rj1;
    static RevoluteJoint rj2;

    PhysicsHandler carPhysicsHandler;

    private PhysicsWorld mPhysicsWorld;

    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private static FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 0.0f, 0.2f);

    @Override
    public void createScene() {
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        this.setBackground(new Background(0, 0, 0));

        //Physics
        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        final Rectangle ground = new Rectangle(400, 30 ,160000.0f, 2, vbom);
        final Rectangle wall =  new Rectangle(1500,0,100,2000,vbom);
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        // PhysicsFactory.createBoxBody(this.mPhysicsWorld, wall, BodyDef.BodyType.StaticBody, wallFixtureDef);
        this.attachChild(wall);
        this.attachChild(ground);


        //Logo

        Sprite backgroundSprite = new Sprite(400,240, resourcesManager.mBackgroundTextureRegion,vbom);
      this.attachChild(backgroundSprite);
        //Sprite backgroundSprite1 = new Sprite(800,240, this.mBackgroundTextureRegion,getVertexBufferObjectManager());
        //scene.attachChild(backgroundSprite1);

        //Car Body

        carSprite = new Sprite(200,100, resourcesManager.mCarTextureRegion,vbom);
       this.attachChild(carSprite);

        carBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld,carSprite, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(carSprite,carBody,true,true));
        //Car Wheels


        wheelSprite1 = new Sprite(118,53,resourcesManager.mWheel1,vbom);
        wheelBody1= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite1, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite1,wheelBody1));
        this.attachChild(wheelSprite1);


        wheelSprite2 = new Sprite(292,55,resourcesManager.mWheel1,vbom);
        wheelBody2= PhysicsFactory.createCircleBody(this.mPhysicsWorld, wheelSprite2, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wheelSprite2,wheelBody2));
        this.attachChild(wheelSprite2);


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
        revoluteJointDef2.motorSpeed = 50;
        revoluteJointDef2.maxMotorTorque = 100;
        rj2 = (RevoluteJoint) this.mPhysicsWorld.createJoint(revoluteJointDef2);


        // carphysicsHandler = new PhysicsHandler(carSprite);
        //  carSprite.registerUpdateHandler(carphysicsHandler);

        //Add Gravity
        this.registerUpdateHandler(this.mPhysicsWorld);
        this.registerUpdateHandler(new IUpdateHandler() {
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


      //  mCamera.setChaseEntity(carSprite);

    }



    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneType getSceneType() {

        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {

    }
}
