package g5.mindwave;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import g5.mindwave.SceneManager.SceneType;
/**
 * Created by Andrei on 3/24/2015.
 */
public class mainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {
    private MenuScene menuChildScene;
    private final int MENU_PLAY = 0;
  //  private final int MENU_OPTIONS = 1;

    private void createMenuChildScene()
    {
        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.menu_buttonPlayTR, vbom), 1.2f, 1);

        menuChildScene.addMenuItem(playMenuItem);


        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 10);
       // optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 110);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    @Override
    public void createScene() {
        createBackground();
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {

    }
    private void createBackground()
    {
        attachChild(new Sprite(400, 240, resourcesManager.menu_bagckgroundTR, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
    {
        switch(pMenuItem.getID())
        {
            case MENU_PLAY:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            default:
                return false;
        }
    }

}
