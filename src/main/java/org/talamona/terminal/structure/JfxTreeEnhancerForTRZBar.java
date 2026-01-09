package org.talamona.terminal.structure;

import javafx.scene.Node;
import org.talamona.terminal.gui.DrawingText;
import org.talamona.terminal.gui.TRZBar;

public class JfxTreeEnhancerForTRZBar implements Runnable {
    private final Node node;
    private final DrawingText window;

    public JfxTreeEnhancerForTRZBar(Node bar, DrawingText mainWindow) {
        this.node = bar;
        this.window = mainWindow;
    }
    @Override
    public void run() {
        if (this.window != null && this.node != null) {
            TRZBar trzBar = (TRZBar) this.node;
            // ZERO POS: 70% width (800 px)
            trzBar.setupZeroBar(70d, this.window.getRoot());
            trzBar.calculateBarParams();
            this.window.getRoot().getChildren().add(node);
        }

    }
}
