package ca.teamdave.letterman;

import java.util.Vector;

/**
 * Makes sure that "background" updates get run
 */
public class UpdateManager {
    private static UpdateManager sInstance = null;
    private UpdateManager() {}
    public static UpdateManager getInstance() {
        if (sInstance == null) {
            sInstance = new UpdateManager();
        }
        return sInstance;
    }

    private Vector mComponents = new Vector();
    private final double DEFAULT_TIME = 0.02;

    public void registerComponent(UpdatingComponent updatingComponent) {
        mComponents.addElement(updatingComponent);
    }

    public void runUpdates() {
        // TODO: actually time the cycle
        for (int i = 0; i < mComponents.size(); ++i) {
            UpdatingComponent component = (UpdatingComponent) mComponents.elementAt(i);
            component.updateComponent(DEFAULT_TIME);
        }
    }
}
