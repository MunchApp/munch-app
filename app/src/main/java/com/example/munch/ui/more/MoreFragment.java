package com.example.munch.ui.more;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MoreFragment extends Fragment {

    private View root;

    private TextView andreaTxtView;
    private TextView rafaelTxtView;
    private TextView yasiraTxtView;
    private TextView janineTxtView;
    private TextView kennyTxtView;
    private TextView lukeTxtView;
    private TextView syedTxtView;


    private TextView andreaIssuesView;
    private TextView rafaelIssuesView;
    private TextView yasiraIssuesView;
    private TextView janineIssuesView;
    private TextView lukeIssuesView;
    private TextView kennyIssuesView;
    private TextView syedIssuesView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.activity_about_page, container, false);
        initialize();
        submitThread git = new submitThread();
        git.execute("");
        return root;
    }

    private void initialize(){
        andreaTxtView = root.findViewById(R.id.andreaCommitsTxtView);
        rafaelTxtView = root.findViewById(R.id.rafaelCommitsTxtView);
        yasiraTxtView = root.findViewById(R.id.yasiraCommitsTxtView);
        janineTxtView = root.findViewById(R.id.janineCommitsTxtView);
        kennyTxtView = root.findViewById(R.id.kennyCommitsTxtView);
        lukeTxtView = root.findViewById(R.id.lukeCommitsTxtView);
        syedTxtView = root.findViewById(R.id.syedCommitsTxtView);


        andreaIssuesView = root.findViewById(R.id.andreaIssuesTxtView);
        rafaelIssuesView = root.findViewById(R.id.rafaelIssuesTxtView);
        yasiraIssuesView = root.findViewById(R.id.yasiraIssuesTxtView);
        janineIssuesView = root.findViewById(R.id.janineIssuesTxtView);
        lukeIssuesView = root.findViewById(R.id.lukeIssuesTxtView);
        kennyIssuesView = root.findViewById(R.id.kennyIssuesTxtView);
        syedIssuesView = root.findViewById(R.id.syedIssuesTxtView);
    }

    private class submitThread extends AsyncTask<String, Void, String> {

        String andreaCommits = "0";
        String rafaelCommits = "0";
        String yasiraCommits = "0";
        String janineCommits = "0";
        String lukeCommits = "0";
        String syedCommits = "0";
        String kennyCommits = "0";

        String andreaIssues;
        String janineIssues;
        String kennyIssues;
        String lukeIssues;
        String rafaelIssues;
        String syedIssues;
        String yasiraIssues;

        HashMap<String, Integer> issuesCount;


        @Override
        protected String doInBackground(final String... strings) {

            String result = "";

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://munch-server.herokuapp.com/contributors");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Gets JSON Data
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                while ((result = reader.readLine()) != null) {
                    buffer.append(result + "\n");
                    Log.d("Commits Response: ", result); // line by line printing
                }

                //Parses JSON data
                try {

                    JSONArray response = new JSONArray(buffer.toString()); //from doInBackground
                    Log.d("Response again: ", response.toString());

                    andreaCommits = Integer.toString(response.getJSONObject(0).getInt("contributions"));
                    janineCommits = Integer.toString(response.getJSONObject(1).getInt("contributions"));
                    kennyCommits = Integer.toString(response.getJSONObject(2).getInt("contributions"));
                    lukeCommits = Integer.toString(response.getJSONObject(3).getInt("contributions"));
                    rafaelCommits = Integer.toString(response.getJSONObject(4).getInt("contributions"));
                    syedCommits = Integer.toString(response.getJSONObject(5).getInt("contributions"));
                    yasiraCommits = Integer.toString(response.getJSONObject(6).getInt("contributions"));

                    andreaIssues = Integer.toString(response.getJSONObject(0).getInt("Issues"));
                    janineIssues = Integer.toString(response.getJSONObject(1).getInt("Issues"));
                    kennyIssues = Integer.toString(response.getJSONObject(2).getInt("Issues"));
                    lukeIssues = Integer.toString(response.getJSONObject(3).getInt("Issues"));
                    rafaelIssues = Integer.toString(response.getJSONObject(4).getInt("Issues"));
                    syedIssues = Integer.toString(response.getJSONObject(5).getInt("Issues"));
                    yasiraIssues = Integer.toString(response.getJSONObject(6).getInt("Issues"));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Closes weather connections
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            andreaTxtView.setText("Number of commits: " + andreaCommits);
            rafaelTxtView.setText("Number of commits: " + rafaelCommits);
            yasiraTxtView.setText("Number of commits: " + yasiraCommits);
            janineTxtView.setText("Number of commits: " + janineCommits);
            lukeTxtView.setText("Number of commits: " + lukeCommits);
            kennyTxtView.setText("Number of commits: " + kennyCommits);
            syedTxtView.setText("Number of commits: " + syedCommits);

            andreaIssuesView.setText("Number of issues: " + andreaIssues + "\n");
            rafaelIssuesView.setText("Number of issues: " + rafaelIssues + "\n");
            yasiraIssuesView.setText("Number of issues: " + yasiraIssues + "\n");
            janineIssuesView.setText("Number of issues: " + janineIssues + "\n");
            lukeIssuesView.setText("Number of issues: " + lukeIssues + "\n");
            kennyIssuesView.setText("Number of issues: " + kennyIssues + "\n");
            syedIssuesView.setText("Number of issues: " + syedIssues + "\n");
        }
    }




}