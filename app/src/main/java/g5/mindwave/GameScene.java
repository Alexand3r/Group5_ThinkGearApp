package g5.mindwave;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier.ILoopEntityModifierListener;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.LoopModifier;
import org.xml.sax.Attributes;

import java.io.IOException;

import g5.mindwave.SceneManager.SceneType;

/**
 * Created by Andrei on 3/19/2015.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_POWER= "p";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
    //private static final android.app.Activity a = new android.app.Activity();
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CELL = "cell";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BCELL= "bcell";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_YCELL= "ycell";
    //private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    //private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    //private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    //private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";

    //Scrolling
    private float mTouchX = 0, mTouchY = 0, mTouchOffsetX = 0, mTouchOffsetY = 0;

    //Rotating


    //
    @Override
    public void createScene() {
        setOnSceneTouchListener(this);
        createBackground();
        createHUD();
        createPhysics();

        loadLevel(1);

    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);

    }
    private void createBackground()
    {	Sprite bg=new Sprite(0, 0, resourcesManager.game_bg_region, vbom);
        attachChild(bg);
		/*attachChild(new Sprite(400, 240, resourcesManager.menu_background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera)
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });*/
    }

    @Override
    public SceneType getSceneType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        camera.setZoomFactor(1.0f);
        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }
    private HUD gameHUD;

    private Text scoreText;
    private Text powerText;
    private void createHUD()
    {
        gameHUD = new HUD();

        // CREATE SCORE TEXT
        scoreText = new Text(20, 420, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }
    private int score = 0;

    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }

    private PhysicsWorld physicsWorld;

    private void createPhysics()
    {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        registerUpdateHandler(physicsWorld);
    }


    private void loadLevel(int levelID)
    {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

                camera.setBounds(-1024, -655, 1024, 655);
                camera.setBoundsEnabled(true);
                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
                final int p = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_POWER);
                final Sprite levelObject;
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
                                new IEntityModifierListener() {
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
                                new ILoopEntityModifierListener() {
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

	           /* if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
	                PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
	            }
	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
	                final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                body.setUserData("platform2");
	                physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	            }
	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
	                final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                body.setUserData("platform3");
	                physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	            }
	            else*/ if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CELL))
            {
                levelObject = new Sprite(x, y, resourcesManager.cell_region, vbom);
               // cellText = new Sprite(x,y, resourcesManager.cell_animatedr,vbom);
                final Body body = PhysicsFactory.createBoxBody(physicsWorld,  levelObject, BodyType.StaticBody, FIXTURE_DEF);

                powerText=new Text(50, 50, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.CENTER),vbom);
                powerText.setAnchorCenter(0, 0);
                powerText.setText(""+p);
                levelObject.attachChild(powerText);


                //levelObject.registerEntityModifier(entityModifier);
                body.setUserData("cell");
                physicsWorld.registerPhysicsConnector(new PhysicsConnector( levelObject, body, true, false));
            }
            else
            if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BCELL))
            {
                levelObject = new Sprite(x, y, resourcesManager.cell_animatedr, vbom);

                final Body body =  PhysicsFactory.createBoxBody(physicsWorld,  levelObject, BodyType.StaticBody, FIXTURE_DEF);

                powerText=new Text(50, 50, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.CENTER),vbom);
                //powerText.setAnchorCenter(0, 0);
                powerText.setText(""+p);
                levelObject.attachChild(powerText);

                //levelObject.registerEntityModifier(entityModifier);

                physicsWorld.registerPhysicsConnector(new PhysicsConnector( levelObject, body, true, false));

            }
            else
            if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_YCELL))
            {
                levelObject = new Sprite(x, y, resourcesManager.cell_animatedy, vbom);
                final Body body =  PhysicsFactory.createBoxBody(physicsWorld,  levelObject, BodyType.StaticBody, FIXTURE_DEF);

                powerText=new Text(50, 50, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.CENTER),vbom);
                //powerText.setAnchorCenter(0, 0);
                powerText.setText(""+p);
                levelObject.attachChild(powerText);
                //levelObject.registerEntityModifier(entityModifier);

                physicsWorld.registerPhysicsConnector(new PhysicsConnector( levelObject, body, true, false));

            }
	            /*else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom)
	                {
	                    @Override
	                    protected void onManagedUpdate(float pSecondsElapsed)
	                    {
	                        super.onManagedUpdate(pSecondsElapsed);

	                        /**
	                         * TODO
	                         * we will later check if player collide with this (coin)
	                         * and if it does, we will increase score and hide coin
	                         * it will be completed in next articles (after creating player code)

	                    }
	                };
	                levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	            } */
            else
            {
                throw new IllegalArgumentException();
            }

                levelObject.setCullingEnabled(true);
                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }





    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pTouchEvent) {
        //particleEmitter.setCenter(pTouchEvent.getX(), pTouchEvent.getY());

        if(pTouchEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            mTouchX = pTouchEvent.getMotionEvent().getX();
            mTouchY = pTouchEvent.getMotionEvent().getY();
        }
        else if(pTouchEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            float newX = pTouchEvent.getMotionEvent().getX();
            float newY = pTouchEvent.getMotionEvent().getY();

            mTouchOffsetX = (newX - mTouchX);
            mTouchOffsetY = (newY - mTouchY);

            float newScrollX = camera.getCenterX() - mTouchOffsetX;
            float newScrollY = camera.getCenterY() + mTouchOffsetY;

            camera.setCenter(newScrollX, newScrollY);

            mTouchX = newX;
            mTouchY = newY;
        }

        return true;
    }




}