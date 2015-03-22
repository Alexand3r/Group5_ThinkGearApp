package g5.mindwave;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
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

    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private ITextureRegion mBackgroundTextureRegion,mCarTextureRegion,mWheel1,mWheel2;

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
        Sprite carSprite = new Sprite(200,100, this.mCarTextureRegion,getVertexBufferObjectManager());
        scene.attachChild(carSprite);
        final Sprite wheelSprite1 = new Sprite(120,68,this.mWheel1,getVertexBufferObjectManager());

        Sprite wheelSprite2 = new Sprite(295,70,this.mWheel1,getVertexBufferObjectManager());
        scene.attachChild(wheelSprite1);
        scene.attachChild(wheelSprite2);
        RotationModifier rotMod = new RotationModifier(1f, 0, 980);

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
        return scene;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
    }
}
