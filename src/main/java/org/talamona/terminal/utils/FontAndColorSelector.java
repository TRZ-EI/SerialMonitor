package org.talamona.terminal.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.talamona.terminal.structure.TextMetricCalculator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 11.06
 */
public class FontAndColorSelector {

/*
    private final char NERO_PICCOLO = 'P';
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'G';
    private final char BLU_GRANDE = 'C';
*/
    private char NERO_PICCOLO;
    private char ROSSO_PICCOLO;
    private char VERDE_PICCOLO;
    private char BLU_PICCOLO;
    private char ROSSO_PICCOLO_GRASSETTO;


    private char NERO_GRANDE;
    private char ROSSO_GRANDE;
    private char VERDE_GRANDE;
    private char BLU_GRANDE;

    private final char CHAR_FOR_METRIC = 'W'; // char to measure tipical dimensions
    


    private Font bigFont,smallFont, smallBoldFont;

    private Set<Character> smallFontsChars;
    private Map<Character, Color> colorMap;
    private Properties properties;
    private TextMetricCalculator textMetricCalculator;

    private static FontAndColorSelector instance;

    public static FontAndColorSelector getNewInstance(){
        if (instance == null){
            instance = new FontAndColorSelector();
        }
        return instance;
    }

    public Font selectFont(char selector){
        Font retValue = null;
        if (smallFontsChars.contains(selector)){
            retValue = (selector == ROSSO_PICCOLO_GRASSETTO)? this.smallBoldFont: this.smallFont;
        }else{
            retValue = this.bigFont;
        }
        return retValue;
    }
    public Color selectColor(char selector){
        Color c = colorMap.get(selector);
        return (c!= null)? c: Color.BEIGE;
    }
    private FontAndColorSelector(){
        try {

            this.properties = ConfigurationHolder.getInstance().getProperties();
            this.readFontMappingsFromConfigurationFile();

            this.smallFontsChars = this.fillDataForFonts();
            this.colorMap = this.fillDataForColors();
            this.smallFont = this.loadSmallFont();
            this.smallBoldFont = this.loadSmallBoldFont();
            this.bigFont = this.loadBigFont();
            this.textMetricCalculator = TextMetricCalculator.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void readFontMappingsFromConfigurationFile() {
        this.NERO_PICCOLO = this.properties.getProperty(String.valueOf(FontMappings.NERO_PICCOLO)).charAt(0);
        this.ROSSO_PICCOLO = this.properties.getProperty(String.valueOf(FontMappings.ROSSO_PICCOLO)).charAt(0);
        this.VERDE_PICCOLO = this.properties.getProperty(String.valueOf(FontMappings.VERDE_PICCOLO)).charAt(0);
        this.BLU_PICCOLO = this.properties.getProperty(String.valueOf(FontMappings.BLU_PICCOLO)).charAt(0);
        this.ROSSO_PICCOLO_GRASSETTO = this.properties.getProperty(String.valueOf(FontMappings.ROSSO_PICCOLO_GRASSETTO)).charAt(0);



        this.NERO_GRANDE = this.properties.getProperty(String.valueOf(FontMappings.NERO_GRANDE)).charAt(0);
        this.ROSSO_GRANDE = this.properties.getProperty(String.valueOf(FontMappings.ROSSO_GRANDE)).charAt(0);
        this.VERDE_GRANDE = this.properties.getProperty(String.valueOf(FontMappings.VERDE_GRANDE)).charAt(0);
        this.BLU_GRANDE = this.properties.getProperty(String.valueOf(FontMappings.BLU_GRANDE)).charAt(0);
    }

    private Map<Character,Color> fillDataForColors() {
        Map<Character, Color> retValue = new HashMap<Character, Color>();
        retValue.put(NERO_PICCOLO, Color.BLACK);
        retValue.put(NERO_GRANDE, Color.BLACK);

        retValue.put(ROSSO_PICCOLO, Color.RED);
        retValue.put(ROSSO_PICCOLO_GRASSETTO, Color.RED);
        retValue.put(ROSSO_GRANDE, Color.RED);

        retValue.put(BLU_PICCOLO, Color.BLUE);
        retValue.put(BLU_GRANDE, Color.BLUE);

        retValue.put(VERDE_PICCOLO, Color.web("00ff00"));
        retValue.put(VERDE_GRANDE, Color.web("00ff00"));
        return retValue;
    }

    private Set<Character> fillDataForFonts() {
        Set<Character> retValue = new HashSet<Character>();
        retValue.add(NERO_PICCOLO);
        retValue.add(ROSSO_PICCOLO);
        retValue.add(ROSSO_PICCOLO_GRASSETTO);
        retValue.add(VERDE_PICCOLO);
        retValue.add(BLU_PICCOLO);
        return retValue;
    }
    private Font loadSmallFont(){
        String font = this.properties.getProperty(FontProperties.SMALL_FONT.name());
        String size = this.properties.getProperty(FontProperties.SMALL_SIZE.name());
        String weight = this.properties.getProperty(FontProperties.SMALL_FONT_WEIGHT.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }
    private Font loadSmallBoldFont() {
        Font baseType = loadSmallFont();
        return Font.font(baseType.getName(), FontWeight.BOLD, baseType.getSize());
    }

    private Font loadBigFont(){
        String font = this.properties.getProperty(FontProperties.BIG_FONT.name());
        String size = this.properties.getProperty(FontProperties.BIG_SIZE.name());
        String weight = this.properties.getProperty(FontProperties.BIG_FONT_WEIGHT.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }

    public int getWidthForSmallFont(String w) {
        return this.textMetricCalculator.calculateWidth(w, this.smallFont);
   }

    public int getWidthForBigFont(String w) {
        return this.textMetricCalculator.calculateWidth(w, this.bigFont);
    }

    public int getHeightForSmallFont(String w) {
        return this.textMetricCalculator.calculateHeight(w, this.smallFont);
    }

    public int getHeightForBigFont(String w) {
        return this.textMetricCalculator.calculateHeight(w, this.bigFont);
    }
    public int getWidthForFont(Font font, String w) {
        return this.textMetricCalculator.calculateWidth(w, font);
    }

    public int getHeightForFont(Font font, String w) {
        return this.textMetricCalculator.calculateHeight(w, font);
    }

    public Font getBigFont() {
        return this.bigFont;
    }

    public Font getSmallFont() {
        return smallFont;
    }

    public Font getSmallBoldFont() {
        return this.smallBoldFont;
    }

    public Color getColorForBar() {
        String colorWebString = this.properties.getProperty(FontProperties.BAR_COLOR.name());
        return Color.web(colorWebString);
    }

    private enum FontProperties{
        SMALL_FONT,SMALL_FONT_WEIGHT,SMALL_SIZE,BIG_FONT,BIG_FONT_WEIGHT,BIG_SIZE, BAR_COLOR;
    }
    private enum FontMappings{
        NERO_PICCOLO,ROSSO_PICCOLO,ROSSO_PICCOLO_GRASSETTO,VERDE_PICCOLO,BLU_PICCOLO,
        NERO_GRANDE,ROSSO_GRANDE,VERDE_GRANDE,BLU_GRANDE;
    }


    
    
}
