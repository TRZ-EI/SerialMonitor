package org.talamona.terminal.utils;

import javafx.collections.FXCollections;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 25/05/17
 * Time: 7.56
 */
public class MonoSpaceFontsFinder {

    public static MonoSpaceFontsFinder getNewInstance(){
        return new MonoSpaceFontsFinder();
    }


    public static void main(String[] args){
        List monoFonts = MonoSpaceFontsFinder.getNewInstance().getMonoFontFamilyNames();
        Iterator<String> i = monoFonts.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }
    }
    public List<String> getMonoFontFamilyNames() {

        // Compare the layout widths of two strings. One string is composed
        // of "thin" characters, the other of "wide" characters. In mono-spaced
        // fonts the widths should be the same.

        final Text thinTxt = new Text("1 l"); // note the space
        final Text thikTxt = new Text("MWX");

        List<String> fontFamilyList = Font.getFamilies();
        List<String> monoFamilyList = new ArrayList<>();

        Font font;

        for (String fontFamilyName : fontFamilyList) {
            font = Font.font(fontFamilyName, FontWeight.NORMAL, FontPosture.REGULAR, 14.0d);
            thinTxt.setFont(font);
            thikTxt.setFont(font);
            if (thinTxt.getLayoutBounds().getWidth() == thikTxt.getLayoutBounds().getWidth()) {
                monoFamilyList.add(fontFamilyName);
            }
        }

        return FXCollections.observableArrayList(monoFamilyList);
    }


}
