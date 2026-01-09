/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talamona.terminal.gui.hansolo.skins;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.GaugeSkinBase;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import static eu.hansolo.medusa.tools.Helper.clamp;


/**
 * Created by Luigi Talamona on May 30, 2017
 */
public class TRZLinearSkin extends GaugeSkinBase {
    private double                preferredWidth  = 140;
    private double                preferredHeight = 350;
    private double                aspectRatio     = 2.5;
    private double                size;
    private double                width;
    private double                height;
    private double                stepSize;
    private Pane                  pane;
    private Line                  zeroMark;
    private Rectangle             barBackground;
    private Rectangle             bar;
    private double                minValuePosition;
    private double                maxValuePosition;
    private double                zeroPosition;
    private InvalidationListener  currentValueListener;
    private InvalidationListener  paneSizeListener;


    // ******************** Constructors **************************************
    public TRZLinearSkin(Gauge gauge) {
        super(gauge);
        if (gauge.isAutoScale()) gauge.calcAutoScale();
        currentValueListener  = o -> setBar(gauge.getCurrentValue());
        paneSizeListener      = o -> handleEvents("RESIZE");
        preferredWidth  = 800;
        preferredHeight = 140;

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        // Set initial size
        if (Double.compare(gauge.getPrefWidth(), 0.0) <= 0 || Double.compare(gauge.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(gauge.getWidth(), 0.0) <= 0 || Double.compare(gauge.getHeight(), 0.0) <= 0) {
            if (gauge.getPrefWidth() > 0 && gauge.getPrefHeight() > 0) {
                gauge.setPrefSize(gauge.getPrefWidth(), gauge.getPrefHeight());
            } else {
                gauge.setPrefSize(preferredWidth, preferredHeight);
            }
        }

        this.zeroMark = new Line();

        barBackground = new Rectangle();
        bar = new Rectangle();
        bar.setStroke(null);


        pane = new Pane(this.zeroMark, barBackground, bar);
        pane.setBorder(new Border(new BorderStroke(gauge.getBorderPaint(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(gauge.getBorderWidth()))));
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), CornerRadii.EMPTY, Insets.EMPTY)));

        getChildren().setAll(pane);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
        gauge.currentValueProperty().addListener(currentValueListener);
        pane.widthProperty().addListener(paneSizeListener);
        pane.heightProperty().addListener(paneSizeListener);
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);
        if ("FINISHED".equals(EVENT_TYPE)) {
            if ( gauge.isHighlightSections() ) {
                redraw();
            }
        } else if ("VISIBILITY".equals(EVENT_TYPE)) {
            resize();
            redraw();
        } else if ("SECTION".equals(EVENT_TYPE)) {
            resize();
            redraw();
        } else if ("RECALC".equals(EVENT_TYPE)) {
            height   = width / aspectRatio;
            resize();
            redraw();
        }
    }

    @Override public void dispose() {
        gauge.currentValueProperty().removeListener(currentValueListener);
        pane.widthProperty().removeListener(paneSizeListener);
        pane.heightProperty().removeListener(paneSizeListener);
        super.dispose();
    }


    // ******************** Private Methods ***********************************


    private void setBar(final double VALUE) {

        double maxValue = ( gauge.getMaxValue() - gauge.getMinValue() ) * stepSize;
        double valueWidth = 0;
        double layoutX = 0;

        if (gauge.isStartFromZero()) {

            double maxV = gauge.getMaxValue();
            double maxX = maxV * stepSize;
            double minV = gauge.getMinValue();
            double minX = minV * stepSize;
            double valX = VALUE * stepSize;

            if ( ( valX > minX || minX < 0 ) && ( valX < maxX || maxX > 0 ) ) {

                valX = clamp(minX, maxX, valX);

                if ( maxX < 0 ) {
                    layoutX = valX;
                    valueWidth = maxX - valX;
                } else if ( minX > 0 ) {
                    layoutX = minX;
                    valueWidth = valX - minX;
                } else if ( valX < 0 ) {
                    layoutX = valX;
                    valueWidth = - valX;
                } else {
                    layoutX = 0;
                    valueWidth = valX;
                }

            }

        } else {
            valueWidth = clamp(0, maxValue, ( VALUE - gauge.getMinValue() ) * stepSize);
        }
        bar.setLayoutX(layoutX);
        bar.setWidth(valueWidth);
        setBarColor(VALUE);

    }
    private void setBarColor(final double VALUE) {
        if (!gauge.getAreasVisible() && !gauge.isGradientBarEnabled()) {
            bar.setFill(gauge.getBarColor());
        } else if (gauge.isGradientBarEnabled() && gauge.getGradientBarStops().size() > 1) {
            bar.setFill(gauge.getGradientLookup().getColorAt((VALUE - gauge.getMinValue()) / gauge.getRange()));
        } else {
            bar.setFill(gauge.getBarColor());
        }
    }



    @Override protected void resize() {
        width  = gauge.getWidth() - gauge.getInsets().getLeft() - gauge.getInsets().getRight();
        height = gauge.getHeight() - gauge.getInsets().getTop() - gauge.getInsets().getBottom();

        if (width > 0 && height > 0) {
            double  currentValue   = gauge.getCurrentValue();
            height   = width / aspectRatio;
            size     = width < height ? width : height;
            stepSize = Math.abs(0.9 * width / gauge.getRange());

            pane.setMaxSize(width, height);
            pane.relocate((gauge.getWidth() - width) * 0.5, (gauge.getHeight() - height) * 0.5);

            width  = pane.getLayoutBounds().getWidth();
            height = pane.getLayoutBounds().getHeight();

            barBackground.setWidth(0.9 * width);
            barBackground.setHeight(0.14286 * height);
            barBackground.relocate((width - barBackground.getWidth()) * 0.5, (height - barBackground.getHeight()) * 0.5);
            barBackground.setStroke(null);
            barBackground.setFill(new LinearGradient(barBackground.getLayoutBounds().getMinX(), 0,
                                                     barBackground.getLayoutBounds().getMaxX(), 0,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.rgb(255, 255, 255, 0.05)),
                                                     new Stop(0.5, Color.rgb(255, 255, 255, 0.15)),
                                                     new Stop(1.0, Color.rgb(255, 255, 255, 0.05))));

            minValuePosition = barBackground.getLayoutX();
            maxValuePosition = barBackground.getLayoutX() + barBackground.getLayoutBounds().getWidth() + 80;

            // TODO: CHANGE VALUE OF maxValuePosition to change red bar max
            zeroPosition = maxValuePosition -(maxValuePosition * 0.3);

            double ypos1 = barBackground.getLayoutY() - 1;
            double ypos2 = barBackground.getLayoutY() + barBackground.getLayoutBounds().getHeight() + 1;

            this.zeroMark.setStartX(zeroPosition);
            this.zeroMark.setStartY(ypos1);
            this.zeroMark.setEndX(zeroPosition);
            this.zeroMark.setEndY(ypos2);
            this.zeroMark.setStroke(Color.BLUE);
            this.zeroMark.setStrokeWidth(3);

            bar.setHeight(0.14286 * height);
            bar.setLayoutX(0);
            bar.setLayoutY(0);
            bar.setTranslateX(gauge.isStartFromZero() ? zeroPosition : minValuePosition);
            bar.setTranslateY((height - bar.getHeight()) * 0.5);
            setBar(currentValue);
            }
    }

    @Override protected void redraw() {
        // Background stroke and fill
        pane.setBorder(new Border(new BorderStroke(gauge.getBorderPaint(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(gauge.getBorderWidth() / preferredHeight * height))));
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), CornerRadii.EMPTY, Insets.EMPTY)));

        if (gauge.getAreasVisible()) {
            setBarColor(gauge.getCurrentValue());
        } else {
            bar.setFill(gauge.getBarColor());
        }
    }
}
