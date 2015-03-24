package g5.mindwave;

import android.graphics.Color;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andrei on 3/23/2015.
 */
public class ResourcesManager {
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    public Engine engine;
    public Activity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    public ITextureRegion mBackgroundTextureRegion,mCarTextureRegion,mWheel1,splash_region;
    public ITextureRegion menu_bagckgroundTR,menu_buttonPlayTR,menu_buttonPlayPtr;
    public Font font;

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
    public static void prepareManager(Engine engine, Activity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;

    }
    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadGameFonts();
    }

    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();

        loadGameAudio();
    }

    private void loadMenuGraphics()
    {
        try{
            ITexture menubg = new BitmapTexture(activity.getTextureManager(),new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("menu/background.png");
                }
            });
            menubg.load();
            menu_bagckgroundTR = TextureRegionFactory.extractFromTexture(menubg);
            ITexture menubtn_idle = new BitmapTexture(activity.getTextureManager(),new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("menu/play_idle.png");
                }
            });
            menubtn_idle.load();
            menu_buttonPlayTR = TextureRegionFactory.extractFromTexture(menubtn_idle);
            ITexture menubtn_pressed = new BitmapTexture(activity.getTextureManager(),new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("menu/play_pressed.png");
                }
            });
            menubtn_pressed.load();
            menu_buttonPlayPtr = TextureRegionFactory.extractFromTexture(menubtn_pressed);
        }
        catch (IOException e) {
            Debug.e(e);
        }

    }

    private void loadMenuAudio()
    {

    }

    private void loadGameGraphics()
    {
        try {
            ITexture backgroundTexture = new BitmapTexture(activity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("gfx/splash1.png");
                }
            });
            backgroundTexture.load();

            mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);

            ITexture carTexture = new BitmapTexture(activity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("gfx/car3.png");
                }
            });

            carTexture.load();

            mCarTextureRegion = TextureRegionFactory.extractFromTexture(carTexture);

            ITexture wheel1Texture = new BitmapTexture(activity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return activity.getAssets().open("gfx/roata.png");
                }
            });

            wheel1Texture.load();

            mWheel1 = TextureRegionFactory.extractFromTexture(wheel1Texture);
        }
        catch (IOException e) {
            Debug.e(e);
        }
    }

    private void loadGameFonts() {
        FontFactory.setAssetBasePath("fonts/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
      //  Log.e("ResourceManager",activity.getAssets().toString());
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    private void loadGameAudio()
    {

    }
    private BitmapTextureAtlas splashTextureAtlas;
    public void loadSplashScreen()
    { /*try {
        ITexture backgroundTexture = new BitmapTexture(activity.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return activity.getAssets().open("gfx/splash1.png");
            }
        });
        backgroundTexture.load();
        splash_region = TextureRegionFactory.extractFromTexture(backgroundTexture);*/
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash1.png", 0, 0);
        splashTextureAtlas.load();
    }




    public void unloadSplashScreen() {
       // splash_region.unload();
        splash_region = null;
    }

}
