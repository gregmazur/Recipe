package home.recipe;

/**
 * Created by greg on 12.09.15.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import home.recipe.rest.ImageLoader;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables

        TextView title;
        TextView publisher;
        ImageView picture;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml

        title = (TextView) itemView.findViewById(R.id.title);
        publisher = (TextView) itemView.findViewById(R.id.publisher);

        // Locate the ImageView in listview_item.xml
        picture = (ImageView) itemView.findViewById(R.id.picture);

        // Capture position and set results to the TextViews

        title.setText(resultp.get(MainActivity.TITLE_TAG));
        publisher.setText(resultp.get(MainActivity.PUBLISHER_URL_TAG));
        // Capture position and set results to the ImageView
        // Passes picture images URL into ImageLoader.class
        imageLoader.DisplayImage(resultp.get(MainActivity.IMAGE_URL_TAG), picture);
        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemActivity.class);
                intent.putExtra(MainActivity.TITLE_TAG, resultp.get(MainActivity.TITLE_TAG));
                intent.putExtra(MainActivity.PUBLISHER_URL_TAG,resultp.get(MainActivity.PUBLISHER_URL_TAG));
                intent.putExtra(MainActivity.IMAGE_URL_TAG, resultp.get(MainActivity.IMAGE_URL_TAG));
                intent.putExtra(MainActivity.F2F_URL_TAG,resultp.get(MainActivity.F2F_URL_TAG));

                context.startActivity(intent);

            }
        });
        return itemView;
    }
}