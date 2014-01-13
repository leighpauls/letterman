package ca.teamdave.letterman.background;

import java.util.Vector;

/**
 * Makes sure that "background" updates get run
 */
public class BackgroundUpdateManager {
    private static BackgroundUpdateManager sInstance = null;
    private BackgroundUpdateManager() {}
    public static BackgroundUpdateManager getInstance() {
        if (sInstance == null) {
            sInstance = new BackgroundUpdateManager();
        }
        return sInstance;
    }

    private Vector mComponents = new Vector();
    private final double DEFAULT_TIME = 0.02;

    public void registerComponent(BackgroundUpdatingComponent updatingComponent) {
        mComponents.addElement(updatingComponent);
    }

    public void runUpdates() {
        // TODO: actually time the cycle
        for (int i = 0; i < mComponents.size(); ++i) {
            BackgroundUpdatingComponent component = (BackgroundUpdatingComponent) mComponents.elementAt(i);
            component.updateComponent(DEFAULT_TIME);
        }
    }
}
