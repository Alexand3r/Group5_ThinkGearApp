package g5.mindwave;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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
        return scene;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }
}
