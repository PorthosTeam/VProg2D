package com.mygdx.game;

// When we want to import assets in from the editor UI from the outside world,
// we have a problem: the assets load asynchronously. (The solution is not to
// just block until they load - asynchronous loading is good and we should love
// it dearly. BUT, even if it was the solution, that doesn't work because it
// just throws an exception when we have the editor/manager/main UI ask the
// AssetManager to block. The exception is about some low-level graphics context
// details, but long story short, it seems that we can't block to wait for
// resources to load unless we have some mechanism of telling the game's main
// thread to do so. And once we have said mechanism, we might as well make that
// mechanism "do the right thing" and just chaperone the queued asset until it
// loads all the way - and once it loads all the way, ensures it does whatever
// it was being loaded to do in the first place (well, it calls an associated
// callback - the callback is the thing that has to ensure the asset is handled
// correctly.)

import com.badlogic.gdx.assets.AssetManager;
// The libGDX array class seems to be thread safe in all the ways it's being
// used by the chaperone.
import com.badlogic.gdx.utils.Array;

class QueuedAssetChaperone
{
    private AssetManager manager;
    private Array<String> assets;
    private Array<Callback> callbacks;
    
    public QueuedAssetChaperone(AssetManager _manager)
    {
        manager = _manager;
        assets = new Array<String>();
        callbacks = new Array<Callback>();
    }
    
    // Add asset to list of assets to monitor. Assets are keyed by name in this,
    // just as they are in AssetManager itself. Callback is a functor which is
    // to simply be invoked when the asset is finally loaded.
    public void add(String asset, Callback callback)
    {
        assets.add(asset);
        callbacks.add(callback);
    }
    
    // This is the actual 'check' - it's intended to be ran regularly when you
    // know you have assets queued up, which under libGDX is when you'd run the
    // AssetManager's update() method anyway.
    public void check()
    {
        java.util.Iterator<String> assetsItr = assets.iterator();
        java.util.Iterator<Callback> callbacksItr = callbacks.iterator();
        while(assetsItr.hasNext())
        {
            String asset = assetsItr.next();
            Callback callback = callbacksItr.next();
            if(manager.isLoaded(asset))
            {
                assetsItr.remove();
                callbacksItr.remove();
                callback.call();
            }
        }
    }
}
