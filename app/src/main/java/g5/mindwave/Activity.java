package g5.mindwave;

import android.os.Bundle;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
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

    public static Camera camera;

    ResourcesManager resourcesManager;



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
    }


    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, 800, 480);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);

    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
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


}


