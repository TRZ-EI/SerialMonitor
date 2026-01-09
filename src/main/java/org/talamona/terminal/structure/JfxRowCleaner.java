package org.talamona.terminal.structure;

import javafx.scene.Node;
import org.talamona.terminal.gui.DrawingText;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/08/18
 * Time: 11:51
 */
public class JfxRowCleaner implements Runnable {
    private final DrawingText window;
    private final Collection<Node> contents;

    public JfxRowCleaner(DrawingText mainWindow, Collection<Node> contents) {
        this.window = mainWindow;
        this.contents = contents;
    }
    @Override
    public void run() {
        if (this.window != null && this.contents != null) {
            this.window.getRoot().getChildren().removeAll(this.contents);
        }
    }
}
