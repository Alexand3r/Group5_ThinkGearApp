package g5.mindwave;

import android.app.Activity;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import g5.mindwave.SceneManager.SceneType;
/**
 * Created by Andrei on 3/23/2015.
 */
public abstract class BaseScene extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    protected Engine engine;
    protected Activity activity;
    protected ResourcesManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;

    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------

    public BaseScene()
    {
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
    }

    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------

    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract SceneType getSceneType();

    public abstract void disposeScene();
}