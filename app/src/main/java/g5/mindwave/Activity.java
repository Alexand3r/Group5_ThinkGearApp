package g5.mindwave;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.neurosky.thinkgear.TGDevice;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

/**
 * Created by Andrei on 3/23/2015.
 */
public class Activity extends BaseGameActivity {

    public static BoundCamera camera;

    ResourcesManager resourcesManager;
    public BluetoothAdapter btAdapter;

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions)
    {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }
    protected void onDestroy()
    {
        super.onDestroy();
        //System.exit(0);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // btAdapter = BluetoothAdapter.getDefaultAdapter();

       // Log.v("BT:", btAdapter.getName());

    }



public void connect(TGDevice tg)

    {tg.connect(true);}
    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new BoundCamera(0, 0, 800, 480);

        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;

    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {

        ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager(),btAdapter);
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {

        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }
}


