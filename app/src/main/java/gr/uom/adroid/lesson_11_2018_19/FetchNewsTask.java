
package gr.uom.adroid.lesson_11_2018_19;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchNewsTask extends AsyncTask<String, Void, List<NewsEntry>> {

    private final String LOG_TAG = FetchNewsTask.class.getSimpleName();

    private NewsAdapter newsAdapter;

    public FetchNewsTask(NewsAdapter newsAdapter) {
        this.newsAdapter = newsAdapter;
    }

    private List<NewsEntry> getNewsFromJSON(String newsJSONString)
            throws JSONException {

        final String articleListKey = "articles";
        final String authorKey = "author";
        final String titleKey = "title";
        final String descriptionKey = "description";
        final String urlKey = "url";
        final String imageUrlKey = "urlToImage";


        JSONObject newsString = new JSONObject(newsJSONString);
        JSONArray newsArray = newsString.getJSONArray(articleListKey);

        List<NewsEntry> newsEntries = new ArrayList<>();
        for (int i = 0; i < newsArray.length(); i++) {
            NewsEntry entry = new NewsEntry();
            JSONObject jsonEntry = (JSONObject)newsArray.get(i);
            entry.setAuthor(jsonEntry.getString(authorKey));
            entry.setDescription(jsonEntry.getString(descriptionKey));
            entry.setTitle(jsonEntry.getString(titleKey));
            entry.setUrl(jsonEntry.getString(urlKey));
            entry.setUrlToImage(jsonEntry.getString(imageUrlKey));

            Log.v(LOG_TAG, "News entry: " + entry);

            newsEntries.add(entry);
        }
        return newsEntries;
    }

    @Override
    protected List<NewsEntry> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String baseUrl = "https://newsapi.org/v2/top-headlines";

        try {

            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter("country", "gr")
                    .appendQueryParameter("apiKey", "0c41975ad9374d79a904203a7d4dd66f")
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return new ArrayList<>();
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return new ArrayList<>();
            }
            String newsJSONString = buffer.toString();
            Log.v(LOG_TAG, "News JSON String: " + newsJSONString);
            return getNewsFromJSON(newsJSONString);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return new ArrayList<>();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(List<NewsEntry> newsEntries) {
        newsAdapter.setNewsEntries(newsEntries);
    }
}
