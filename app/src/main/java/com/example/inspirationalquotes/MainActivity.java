package com.example.inspirationalquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.inspirationalquotes.Entities.Joke;
import com.example.inspirationalquotes.Entities.JokeService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView header;
    private TextView inspirationalQuote;
    private Button quoteButton;
    private MainActivity activity = this;
    private Joke joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.tvHeader);
        inspirationalQuote = findViewById(R.id.tvQuote);
        quoteButton = findViewById(R.id.tvQuoteButton);

        //Sets an onClickListener for which executes the API function. Essentially, it ensures
        //pressing the button will load a quote
        quoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspirationalQuote.setText("");
                new GetJokeTask().execute();
            }
        });

        //execute API functions
        new GetJokeTask().execute();
    }

        //AsyncTask used as it is a more dynamic and effective way in establishing communication between
        //background and main thread. Additionally, AsyncTask is much better suited for handling
        //one-off tasks, such as retrieving a quote.

        private class GetJokeTask extends AsyncTask<Void, Void, Joke> {
            @Override
            protected Joke doInBackground(Void... voids) {
                //Use a Try-catch block to attempt executing a block of code. In our Try statement,
                //we attempt to retrieve data from the chucknorris.io API. The catch block will
                // execute if an error occurs in the try block.
                try {
                    //Create a Retrofit instance which converts and HTTP API into a Java Interface.
                    //This allows us to parse the retrived JSON via a GSON deserilizer
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.chucknorris.io/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JokeService service = retrofit.create(JokeService.class);
                    Call<Joke> jokeCall = service.getJoke();
                    Response<Joke> jokeResponse = jokeCall.execute();

                    //Sets the value of joke to the Json from the chucknorris.io API
                    joke = jokeResponse.body();
                    return joke;
                }
                catch (IOException e) {
                    e.printStackTrace();

                }
                return null;
            }

            //onPostExecute method is used to update background UI
            @Override
            public void onPostExecute(Joke joke){
                if(joke != null){
                    //If the API function succeeded, the user will be notified of its expected operations.
                    header.setText("Chuck Norris Inspiration!");
                    inspirationalQuote.setText(joke.getValue());
                    Toast toast = Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    //If the API function failed, an alert and notification will be displayed to
                    // notify app user.
                    header.setText("ERROR");
                    inspirationalQuote.setText("Error! Try refresh!");
                    Toast toast = Toast.makeText(activity, "ERROR!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

}
