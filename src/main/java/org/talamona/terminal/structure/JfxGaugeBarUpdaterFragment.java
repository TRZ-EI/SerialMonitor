package org.talamona.terminal.structure;

import javafx.scene.paint.Color;
import org.talamona.terminal.gui.TRZBar;

public class JfxGaugeBarUpdaterFragment implements RunnableFragment {
    private final Color color;
    private final double value;
    private final TRZBar gauge;

    public JfxGaugeBarUpdaterFragment(Color color, double finalValue, TRZBar t) {
        this.color = color;
        this.value = finalValue;
        this.gauge = t;
    }

    @Override
    public void executeFragment() {
        if (this.gauge != null) {
            this.gauge.setColor(color);
            this.gauge.setValue(value);
        }
    }
}


