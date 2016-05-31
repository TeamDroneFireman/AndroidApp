package edu.istic.tdf.dfclient.UI.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.http.TdfHttpClient;

/**
 * Created by btessiau on 30/05/16.
 */
public class GalleryListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    /**
     * Texts with the images
     */
    private final ArrayList<String> drones;

    /**
     * Images url you should show
     */
    private final ArrayList<String> imgUrl;

    /**
     * Date of the picture
     */
    private final ArrayList<String> dates;

    public GalleryListAdapter(Activity context, ArrayList<String> drones, ArrayList<String> imgUrl, ArrayList<String> dates) {
        super(context, R.layout.list_view_gallery_row, drones);

        this.context = context;
        this.drones = drones;
        this.imgUrl = imgUrl;
        this.dates = dates;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_view_gallery_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(drones.get(position));

        // TODO: 31/05/16 header 
        /*Picasso.Builder picab = new Picasso.Builder(context);

        picab.downloader(new OkHttpDownloader(context) {
            @Override
            protected HttpURLConnection openConnection(Uri uri) throws IOException {
                HttpURLConnection connection =
            }

        });

        picab.downloader(new OkHttpDownloader(context) {
            @Override
            protected HttpURLConnection openConnection(Uri uri) throws IOException {
                HttpURLConnection connection = super.openConnection(uri);
                connection.setRequestProperty(Constant.HEADER_X_API_KEY, mSharedPreferences.getString(SharedPreferenceKeys.JSESSIONID, ""));
                return connection;
            }
        });

        Picasso pica = picab.build();*/

        Picasso.with(context)
                .load(TdfHttpClient.HOST+ imgUrl.get(position))
                .noPlaceholder()
                .into(imageView);

        extratxt.setText(dates.get(position));
        return rowView;
    };
}