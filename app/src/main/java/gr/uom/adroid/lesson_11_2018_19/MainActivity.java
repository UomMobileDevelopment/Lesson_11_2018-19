package gr.uom.adroid.lesson_11_2018_19;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                NewsEntry newsEntry = newsAdapter.getNewsEntry(position);
                String url = newsEntry.getUrl();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });



        fetchNews();
    }

    private void fetchNews(){
        FetchNewsTask fetchNewsTask = new FetchNewsTask(newsAdapter);
        fetchNewsTask.execute();
    }

}
