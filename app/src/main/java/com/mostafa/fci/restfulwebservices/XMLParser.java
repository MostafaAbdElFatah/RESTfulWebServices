package com.mostafa.fci.restfulwebservices;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FCI on 2018-06-25.
 */

public class XMLParser {

    /**
     * parser xml file to list of items
     */

    public static List<Flower> parse(String content){

        try {
            boolean inDataItemTag = false;
            String currentTagName = "";
            Flower flower = null;
            List<Flower> flowersList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(content));

            int evenType = xmlPullParser.getEventType();
            while (evenType != XmlPullParser.END_DOCUMENT){

                switch (evenType){
                    case XmlPullParser.START_TAG:
                        currentTagName = xmlPullParser.getName();
                        if (currentTagName.equals("product")){
                            flower = new Flower();
                            flowersList.add(flower);
                            inDataItemTag = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("product"))
                            inDataItemTag = false;
                        currentTagName = "";
                        break;
                    case XmlPullParser.TEXT:
                        if (inDataItemTag) {
                            readText(xmlPullParser, currentTagName , flower);
                        }
                        break;
                }
                evenType = xmlPullParser.next();
            }
            return flowersList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    static void readText(XmlPullParser xmlPullParser , String tagName ,Flower flower ){

        switch (tagName){
            case ObjectsNames.ID:
                flower.setId(Integer.parseInt(xmlPullParser.getText()));
                break;
            case ObjectsNames.CATEGORY:
                flower.setCategory(xmlPullParser.getText());
                break;
            case ObjectsNames.NAME:
                flower.setName(xmlPullParser.getText());
                break;
            case ObjectsNames.INSTRUCTIONS:
                flower.setInstructions(xmlPullParser.getText());
                break;
            case ObjectsNames.PRICE:
                flower.setPrice(xmlPullParser.getText());
                break;
            case ObjectsNames.PHOTO:
                flower.setPhoto(xmlPullParser.getText());
                break;
            default:
                break;
        }
    }


}
