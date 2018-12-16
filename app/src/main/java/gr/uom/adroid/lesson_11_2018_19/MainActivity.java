package gr.uom.adroid.lesson_11_2018_19;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.newsList);

        newsAdapter = new NewsAdapter(this, R.layout.news_list_item, new ArrayList<NewsEntry>());
        listView.setAdapter(newsAdapter);
        fetchNews();
    }

    private void fetchNews(){
        FetchNewsTask fetchNewsTask = new FetchNewsTask(newsAdapter);
        fetchNewsTask.execute();
    }

}
