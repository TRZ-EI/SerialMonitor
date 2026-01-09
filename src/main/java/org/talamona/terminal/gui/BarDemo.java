/*
 * Copyright (c) 2016 by Gerrit Grunwald
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

package org.talamona.terminal.gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.talamona.terminal.gui.hansolo.skins.TRZLinearSkin;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.FileNotFoundException;
import java.util.Random;


public class BarDemo extends Application {
    private static final Random RND            = new Random();
    private static final double MIN_CELL_SIZE  = 80;
    private static final double PREF_CELL_SIZE = 120;
    private static final double MAX_CELL_SIZE  = 150;
    private static       int    noOfNodes      = 0;
    private Gauge          gauge21;
    private AnimationTimer timer;
    private long lastTimerCall;
    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private Gauge gauge22;
    private TRZBar r;

    @Override public void init() throws FileNotFoundException {
        // TODO: isolate this

        this.readProperties();


        gauge21 = GaugeBuilder.create()
                              //.skinType(Gauge.SkinType.LINEAR)
                              //.title("Linear")
            .tickLabelDecimals(0)
            .minValue(-2)
            .maxValue(1)
            .areasVisible(true)
            .startFromZero(true)
            .orientation(Orientation.HORIZONTAL)
            .sectionsVisible(true)
            .valueVisible(false)
            .foregroundBaseColor(Color.BLUE)
            .barColor(Color.GREEN)
            .barEffectEnabled(true)
            .barBorderColor(Color.CHOCOLATE)

 
                
            .build();
        gauge21.setSkin(new TRZLinearSkin(gauge21));

        gauge22 = GaugeBuilder.create()
                //.skinType(Gauge.SkinType.LINEAR)
                //.title("Linear")
                .tickLabelDecimals(0)
                .minValue(-2)
                .maxValue(1)
                .areasVisible(true)
                .startFromZero(true)
                .orientation(Orientation.HORIZONTAL)
                .sectionsVisible(true)
                .valueVisible(false)
                .foregroundBaseColor(Color.BLUE)
                .barColor(Color.GREEN)
                .barEffectEnabled(true)
                .barBorderColor(Color.CHOCOLATE)



                .build();
        gauge22.setSkin(new TRZLinearSkin(gauge22));

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            double value = -2d;
            @Override public void handle(long now) {
                if (now > lastTimerCall + 3_000_000_000l) {
                    //double value = RND.nextDouble() * gauge21.getRange() + gauge21.getMinValue();


                    gauge21.setValue(value);
                    r.setValue(value * 10);
                    if (value <= gauge21.getMaxValue()){
                        value += 0.1d;
                    }else{
                        value = -2d;
                    }
                    lastTimerCall = now;
                }
            }
        };



    }

    @Override public void start(Stage stage) throws FileNotFoundException, InterruptedException {
        stage.setWidth(800d);
        stage.setHeight(480d);
        Group root = new Group();

        gauge21.setPrefSize(800d, 200d);
        gauge21.setLayoutX(0);
        gauge21.setLayoutY(140);

        gauge22.setPrefSize(800d, 200d);
        gauge22.setLayoutX(0);
        gauge22.setLayoutY(240);

        root.getChildren().add(gauge21);
        root.getChildren().add(gauge22);
        r = new TRZBar();

        r.setHeight(30d);
        r.setWidth(800d);
        r.setX(0d);
        r.setY(50d);
        r.setFill(null);
        r.setupZeroBar(70d, root);
        r.setMinValue(-20D);
        r.setMaxValue(10D);
        r.calculateBarParams();
        r.setColor(Color.GREEN);
        root.getChildren().add(r);
        Scene s = new Scene(root);

        stage.setScene(s);
        stage.setTitle("TRZ bar for instruments");
        stage.show();
        timer.start();


    }

    private void readProperties() throws FileNotFoundException {
        //String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        ConfigurationHolder.getInstance();
    }

    @Override public void stop() {
        System.exit(0);
    }



    public static void main(String[] args) {

        launch(args);
    }
}
