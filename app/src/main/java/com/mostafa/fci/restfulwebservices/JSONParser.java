package com.mostafa.fci.restfulwebservices;

import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FCI on 2018-06-25.
 */

public class JSONParser {

    // get Json Object and return Array of Objects
    public static List<Flower> parse(String content){
        if (content == "" || content == null){
            return null;
        }


        List<Flower> flowersList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(content);
            for(int i=0; i<jsonArray.length(); i++) {

               JSONObject jsonObject = jsonArray.getJSONObject(i);

               Flower flower = new Flower();
               flower.setId(jsonObject.getInt(ObjectsNames.ID));
               flower.setName(jsonObject.getString(ObjectsNames.NAME));
               flower.setCategory(jsonObject.getString(ObjectsNames.CATEGORY));
               flower.setPrice(jsonObject.getString(ObjectsNames.PRICE));
               flower.setInstructions(jsonObject.getString(ObjectsNames.INSTRUCTIONS));
               flower.setPhoto(jsonObject.getString(ObjectsNames.PHOTO));

               flowersList.add(flower);

            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return flowersList;

    }

    // moshi parsing json file like gson
    public static List<FlowerClass> moshiParsing(String content) {
        if (content == "" || content == null) {
            return null;
        }
        List<FlowerClass> flowers = null;
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, FlowerClass.class);
        JsonAdapter<List> jsonAdapter = moshi.adapter(type);
        /**
         * to convert json to list  of flowers
         * */

        try {
            flowers = jsonAdapter.fromJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Moshi moshi2 = new Moshi.Builder().build();
        JsonAdapter<FlowerClass> jsonAdapter2 = moshi2.adapter(FlowerClass.class);
        /**
         * to convert object to json
         * */
        FlowerClass flowerClass = new FlowerClass(1,"asd");
        flowerClass.setCategory("asd Category");
        flowerClass.setInstructions("asd instructions");
        flowerClass.setPhoto("asd photo");
        flowerClass.setPrice("asd price");

        String json = jsonAdapter2.toJson(flowerClass);
        System.out.println("*********************************************");
        System.out.println("JSON :" + json);
        return flowers;
    }

}
