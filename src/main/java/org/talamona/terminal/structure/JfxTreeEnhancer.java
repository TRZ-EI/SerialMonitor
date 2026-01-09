package org.talamona.terminal.structure;

import javafx.scene.Node;
import org.talamona.terminal.gui.DrawingText;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/08/18
 * Time: 11:19
 */
public class JfxTreeEnhancer implements Runnable {

    private final Node node;
    private final DrawingText window;

    public JfxTreeEnhancer(Node myText, DrawingText mainWindow) {
        this.node = myText;
        this.window = mainWindow;
    }
    @Override
    public void run() {
        if (this.window != null && this.node != null) {
            this.window.getRoot().getChildren().add(node);
        }

    }
}
