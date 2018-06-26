package com.mostafa.fci.restfulwebservices;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FlowerAdapter extends ArrayAdapter<Flower> {


    Context context;
    ArrayList<Flower> flowerList;
    private LruCache<Integer, Bitmap> imageCache ;

    public FlowerAdapter(Context context, int resource, ArrayList<Flower> flowerList) {
        super(context, resource,flowerList);
        this.flowerList = flowerList;
        this.context = context;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        this.imageCache = new LruCache<>(cacheSize);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.child_row,parent,false);
        Flower flower = flowerList.get(position);

        Bitmap bitmap = imageCache.get(flower.getId());
        if (bitmap != null){
            ImageView imageView = view.findViewById(R.id.flowerImage);
            imageView.setImageBitmap(flower.getBitmap());
        }else {
            FlowerAndView container = new FlowerAndView();
            container.view = view;
            container.flower = flower;
            new ImageLoader().execute(container);
        }

        TextView textView = view.findViewById(R.id.flowerName);
        textView.setText(flower.getName());
        return view;
    }

    class  FlowerAndView{
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void , FlowerAndView>{

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container = params[0];
            Flower flower = container.flower;
            String imageURL = URLs.mainURL + URLs.photosFlowersURL + flower.getPhoto();
            try {
                InputStream inputStream = (InputStream) new URL(imageURL).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                flower.setBitmap(bitmap);
                inputStream.close();
                container.bitmap = bitmap;
                return container;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {

            ImageView imageView = result.view.findViewById(R.id.flowerImage);
            imageView.setImageBitmap(result.bitmap);
            //result.flower.setBitmap(result.bitmap);
            imageCache.put(result.flower.getId(),result.bitmap);

        }
    }

}
