package g5.mindwave;

/**
 * Created by Andrei on 3/24/2015.
 */

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import g5.mindwave.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        setBackground(new Background(Color.BLACK));
       // attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
        final Text bitmapText = new Text(400, 240, resourcesManager.font, "Loading...!", new TextOptions(HorizontalAlign.CENTER), vbom);
        attachChild(bitmapText);
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }
}