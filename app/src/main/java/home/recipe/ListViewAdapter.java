package home.recipe;

/**
 * Created by greg on 12.09.15.
 */
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import home.recipe.entity.Recipe;
import home.recipe.cache.ImageLoader;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Recipe> data;
    private ImageLoader imageLoader;
    private Recipe resultp = new Recipe();
    private WebView mWebView;


    public ListViewAdapter(Context context,
                           ArrayList<Recipe> arraylist) {
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

        TextView title;
        TextView publisher;
        ImageView picture;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item, parent, false);
        resultp = data.get(position);


        title = (TextView) itemView.findViewById(R.id.title);
        publisher = (TextView) itemView.findViewById(R.id.publisher);

        picture = (ImageView) itemView.findViewById(R.id.picture);
        title.setText(resultp.getTitle());
        publisher.setText(resultp.getPublisherName());
        imageLoader.DisplayImage(resultp.getImage_url(), picture);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                resultp = data.get(position);
                String url = resultp.getF2f_url();
                Log.v("URL in ADAPTER",url);
                Intent intent = new Intent(context, SingleItemActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}