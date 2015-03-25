package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/**
 * Created by Andrei on 3/23/2015.
 */
public class ResourcesManager {
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    public Engine engine;
    public Activity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public ITextureRegion mBackgroundTextureRegion,mCarTextureRegion,mWheel1,splash_region;
    public ITextureRegion menu_bagckgroundTR,menu_buttonPlayTR,menu_buttonPlayPtr;
    public Font font;

    //Parallax- ca e greu sa faci un nenorocit de parallax


     public ITextureRegion mParallaxLayerBackTextureRegion;
     public ITextureRegion mParallaxLayerMidTextureRegion;
     public ITextureRegion mParallaxLayerFrontTextureRegion;

    private BuildableBitmapTextureAtlas menuTextureAtlas;

    // Game Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas;
    public BluetoothAdapter btAdapter;


    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
    public static void prepareManager(Engine engine, Activity activity, BoundCamera camera, VertexBufferObjectManager vbom,BluetoothAdapter btAdapter)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        getInstance().btAdapter = btAdapter;


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
    {   BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_bagckgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.png");
        menu_buttonPlayTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_idle.png");
        menu_buttonPlayPtr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_pressed.png");
        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }

    }

    private void loadMenuAudio()
    {

    }

    private void loadGameGraphics()
    {   BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
        mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background.png");
        mCarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "car3.png");
        mWheel1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "roata.png");

       //Parallax - este extrem de greu sa incarci niste poze

        mParallaxLayerBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "p_back.png");
        mParallaxLayerMidTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"p_mid.png");
        mParallaxLayerFrontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"p_front.png");
        try
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
           // this.mParticleTexture = new AssetBitmapTexture(activity.getTextureManager(), activity.getAssets(), "gfx/particle_fire.png", TextureOptions.BILINEAR);
        }
        catch (final TextureAtlasBuilderException e)
        {
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
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash1.png", 0, 0);
        splashTextureAtlas.load();
    }




    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splash_region = null;
    }

}
