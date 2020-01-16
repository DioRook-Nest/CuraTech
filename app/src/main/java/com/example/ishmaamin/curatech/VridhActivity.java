package com.example.ishmaamin.curatech;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class VridhActivity extends AppCompatActivity {
    private static final String TAG = "UsingThingspeakAPI";
    private static final int THINGSPEAK_CHANNEL_ID = 681462;
    private static final String THINGSPEAK_API_KEY = "UQ4NN99TIDTATZ2W"; //GARBAGE KEY
    private static final String THINGSPEAK_API_KEY_STRING = "UQ4NN99TIDTATZ2W";
    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_FIELD1 = "temperature";
    private static final String THINGSPEAK_FIELD2 = "humidity";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    ThingSpeakChannel tsChannel = new ThingSpeakChannel(THINGSPEAK_CHANNEL_ID,THINGSPEAK_API_KEY);

    private LineChartView chartView;

    private ThingSpeakLineChart tsChart;
    TextView t1,t2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vridh);
        t1=(TextView)findViewById(R.id.textView2);

                tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
                    @Override
                    public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                        // Make use of your Channel feed here!
                        String ss = channelFeed.getChannel().getField2();
                        // Show Channel ID and name on the Action Bar
                        t1.setText(ss);
                        List<Feed> a = channelFeed.getFeeds();
                        // Notify last update time of the Channel feed through a Toast message
                        Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                        Toast.makeText(VridhActivity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
                    }});


                tsChannel.loadChannelFeed();
                // Create a Calendar object dated 5 minutes ago
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -5);

                // Configure LineChartView
                chartView = findViewById(R.id.chart);
                chartView.setZoomEnabled(true);
                chartView.setValueSelectionEnabled(true);
                chartView.canScrollHorizontally(1);
                chartView.canScrollVertically(1);

                // Create a line chart from Field1 of ThinkSpeak Channel 9
                tsChart = new ThingSpeakLineChart(THINGSPEAK_CHANNEL_ID,2, THINGSPEAK_API_KEY);

                // Get 200 entries at maximum
                tsChart.setNumberOfEntries(50);
                // Set value axis labels on 10-unit interval
                tsChart.setValueAxisLabelInterval(10);
                // Set date axis labels on 5-minute interval
                tsChart.setDateAxisLabelInterval(1);
                // Show the line as a cubic spline
                // tsChart.useSpline(true);
                // Set the line color
                tsChart.setLineColor(Color.parseColor("#D32F2F"));
                // Set the axis color
                tsChart.setAxisColor(Color.parseColor("#455a64"));
                // Set the starting date (5 minutes ago) for the default viewport of the chart
                tsChart.setChartStartDate(calendar.getTime());
                // Set listener for chart data update
                tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
                    @Override
                    public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                        // Set chart data to the LineChartView
                        chartView.setLineChartData(lineChartData);
                        // Set scrolling bounds of the chart
                        chartView.setMaximumViewport(maxViewport);
                        // Set the initial chart bounds
                        chartView.setCurrentViewport(initialViewport);
                    }
                });
                // Load chart data asynchronously
                tsChart.loadChartData();
            }




    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {

            t2.setText("Fetching Data from Server.Please Wait...");
        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(" https://api.thingspeak.com/channels/681462/feeds.json?api_key=UQ4NN99TIDTATZ2W&results=2");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(VridhActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject jsonObject=new JSONObject(response);
                double v1 = channel.getDouble(THINGSPEAK_FIELD1);
                if(v1>=90)
                    t1.setText("HI ALL  ");
                else
                    t1.setText("NO VALUES");

                t2.setText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}