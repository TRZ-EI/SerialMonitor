

package org.talamona.terminal.gui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.talamona.terminal.comunication.SerialDataManager;
import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.StructureVisitor;
import org.talamona.terminal.structure.serial.SerialDataFacade;
import org.talamona.terminal.utils.ConfigurationHolder;
import org.talamona.terminal.utils.PomReader;
import org.talamona.terminal.utils.SerialDataEmulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;


public class DrawingText extends Application {

    private Group root;
    private BlockingQueue<String> serialBuffer;
    private SerialDataFacade facade;
    private SerialDataManager serialDataManager;

    private Multimap<Integer, javafx.scene.Node> rows = ArrayListMultimap.create();

    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private StructureVisitor visitor;
    private Canvas canvasForGrid;

    private FutureTask future;
    private Runnable aRunnable;


    @Override public void start(Stage stage) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        this.readProperties();
        this.visitor = new StructureVisitor(this);
        this.facade = SerialDataFacade.createNewInstance();
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.root = new Group();
        Scene scene = new Scene(root, 800d, 480d, Color.WHITE);
        this.drawGridOnCanvas();
        this.drawVersionAndProperties();

        stage.setScene(scene);
        stage.setTitle("TRZ bar for instruments");
        stage.show();


        this.serialDataManager = SerialDataManager.createNewInstanceBySerialBuffer(serialBuffer);
        //this.serialDataManager.setMain(this);
        Thread serialThread = new Thread(serialTask);
        serialThread.setDaemon(false);
        serialThread.start();

        //this.myRunnable = new MyRunnable(this.serialBuffer);

    }

    private void drawVersionAndProperties() {
        InputStream streamPom = this.getClass().getClassLoader().getResourceAsStream("pom.xml");
        PomReader pr = PomReader.getNewInstanceByInputStream(streamPom);
        String version = pr.readProjectVersionFromPom();
        String groupId = pr.readGroupIdFromPom();
        String artifactId = pr.readArtifactIdFromPom();

        String message = groupId + ":" + artifactId + System.lineSeparator() + "version: " + version
                + " - " + pr.getPropertyByKeyFromPomProperties("description") + System.lineSeparator()
                + pr.getPropertyByKeyFromPomProperties("releaseDate");

        Text t = new Text();
        t.setX(200);
        t.setY(200);
        t.setFill(Color.VIOLET);
        t.setText(message);
        t.setFont(Font.font("Monospaced", FontWeight.findByName("BOLD"), Integer.parseInt("25")));

        this.root.getChildren().add(t);



    }

    public void drawGridOnCanvas() {
        this.canvasForGrid = this.getSingleTonCanvasForGrid();
        this.root.getChildren().add(this.canvasForGrid);
        this.canvasForGrid.toFront();
    }

    private Canvas getSingleTonCanvasForGrid() {
        if (this.canvasForGrid == null){
            this.canvasForGrid = new Canvas(800d,480d);
            GraphicDesigner.createNewInstance().drawGridForGraphicHelp(this.canvasForGrid);
        }
        return this.canvasForGrid;


    }

    private Runnable writeTextOnScene(String value) throws UnsupportedEncodingException {
        //System.out.println("DEBUG INFO - 3 --> call writeTextOnScene:" + System.currentTimeMillis());
        Runnable retValue = null;
        Cell variable = this.facade.onSerialDataInput(value.getBytes());
        if(variable != null){
            //System.out.println("DEBUG INFO - 4 --> call accept visitor:" + System.currentTimeMillis());
            retValue = variable.accept(this.visitor);
        }
        return retValue;
    }

    private void readProperties() throws FileNotFoundException {
        String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        ConfigurationHolder.getInstance();
    }


    public void runAndWaitMyRunnable() throws InterruptedException, ExecutionException {

        while (!this.serialBuffer.isEmpty()) {
            try {
                this.aRunnable = this.writeTextOnScene(this.serialBuffer.take());
                if (this.aRunnable != null) {
                    this.future = new FutureTask(aRunnable, null);
                    if (this.future != null){
                        Platform.runLater(this.future);
                        this.future.get();
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }



    private Task<Void> serialTask = new Task<Void>() {
        @Override
        public Void call() throws Exception {

            serialDataManager.connectToSerialPort();
            /* TODO: EXPORT THIS BLOCK TO A METHOD, TO INVOKE ONLY FOR TEST REASONS */
            testSimulatingSerialData();
            while (true) {

            }

        }
    };

    public void testSimulatingSerialData() throws IOException, InterruptedException, ExecutionException {

        SerialDataEmulator sde = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(serialBuffer, 10);
        sde.runScenario("serialInputs/clean-row-before-cleaner-test.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/clean-row-after-cleaner-test.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-1-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-2-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-3-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-bars-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/barAndVariable-fragment.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova2-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlign-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables1-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables2-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables3-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers1-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers2-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers3-no-crc.txt");
        this.runAndWaitMyRunnable();
        Thread.sleep(1000);
        sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers4-no-crc.txt");
        this.runAndWaitMyRunnable();
        sde.runScenario("serialInputs/test-bars-no-crc.txt");
        this.runAndWaitMyRunnable();

    }
    public static void main(String[] args) {
        launch(args);
    }
    public Group getRoot() {
        return root;
    }
    public Multimap<Integer, javafx.scene.Node> getRows() {
        return rows;
    }
}
