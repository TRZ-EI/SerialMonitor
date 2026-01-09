package org.talamona.terminal.structure;

import javafx.scene.text.Text;

public class JfxTextUpdaterFragment implements RunnableFragment {
    private final Text text;
    private final String value;
    private int rightPos;
    private double pixelScreenYPos;

    public JfxTextUpdaterFragment(Text t, String value) {
        this.text = t;
        this.value = value;
    }

    public JfxTextUpdaterFragment(Text t, String value, int rightPos, int pixelScreenYPos) {
        this.text = t;
        this.value = value;
        this.rightPos = rightPos;
        this.pixelScreenYPos = pixelScreenYPos;
    }

    @Override
    public void executeFragment() {
        if (this.text != null && this.value != null) {
            this.text.setText(this.value);
            this.text.setX(rightPos);
            this.text.setY(pixelScreenYPos);
        }
    }
}
