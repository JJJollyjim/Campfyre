package nz.co.nickwebster.campfyre;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList<String> list;
	private final ArrayList<String> imageId;
	private final ArrayList<String> postTimes;
	
	public ImageAdapter(Activity context, ArrayList<String> list, ArrayList<String> imageId, ArrayList<String> postTimes) {
		super(context, R.layout.list_row_layout, list);
		this.context = context;
		this.list = list;
		this.imageId = imageId;
		this.postTimes = postTimes;
	}
	
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.list_row_layout, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.postDesign);
	final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageDesign);
	txtTitle.setText(list.get(position));
	TextView postTimeText = (TextView) rowView.findViewById(R.id.postTime3);
	postTimeText.setText(postTimes.get(position));
	
	//imageView.setImageResource(imageId[position]);
	
	//Get and display image from server
	Runnable getIP = new Runnable() {
		@Override
		public void run() {
			URL url;
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			try {
				url = new URL(imageId.get(position));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input2 = connection.getInputStream();
				final Bitmap profilePicture = BitmapFactory.decodeStream(input2);
				((Activity)context).runOnUiThread(new Runnable() {
						public void run() {
							//Set image
							imageView.setImageBitmap(profilePicture);
						}
					});
			} catch (Exception e) {
			}
		}
	};
	
	Thread getIPThread = new Thread(getIP);
	getIPThread.start();
	
	return rowView;
	}
}
