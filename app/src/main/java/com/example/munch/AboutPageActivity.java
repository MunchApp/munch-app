package com.example.munch;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;

public class AboutPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        submitThread getGitStats= new submitThread();
        getGitStats.execute("");
    }

    private class submitThread extends AsyncTask<String, Void, String> {

        TextView andreaTxtView = findViewById(R.id.andreaCommitsTxtView);
        TextView rafaelTxtView = findViewById(R.id.rafaelCommitsTxtView);
        TextView yasiraTxtView = findViewById(R.id.yasiraCommitsTxtView);
        TextView janineTxtView = findViewById(R.id.janineCommitsTxtView);
        TextView lukeTxtView = findViewById(R.id.lukeCommitsTxtView);

        TextView andreaIssuesView = findViewById(R.id.andreaIssuesTxtView);
        TextView rafaelIssuesView = findViewById(R.id.rafaelIssuesTxtView);
        TextView yasiraIssuesView = findViewById(R.id.yasiraIssuesTxtView);
        TextView janineIssuesView = findViewById(R.id.janineIssuesTxtView);
        TextView lukeIssuesView = findViewById(R.id.lukeIssuesTxtView);
        TextView kennyIssuesView = findViewById(R.id.kennyIssuesTxtView);
        TextView syedIssuesView = findViewById(R.id.syedIssuesTxtView);

        String andreaCommits = "";
        String rafaelCommits = "";
        String yasiraCommits = "";
        String janineCommits = "";
        String lukeCommits = "";

        HashMap<String, Integer> issuesCount;



        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.github.com/repos/MunchApp/munch-app/stats/contributors");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Authenticator.setDefault (new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication ("RafaelHerrejon", "M@gyk1571".toCharArray());
                    }
                });

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

                    andreaCommits = Integer.toString(response.getJSONObject(0).getInt("total"));
                    rafaelCommits = Integer.toString(response.getJSONObject(1).getInt("total"));
                    yasiraCommits = Integer.toString(response.getJSONObject(2).getInt("total"));
                    janineCommits = Integer.toString(response.getJSONObject(3).getInt("total"));
                    lukeCommits = Integer.toString(response.getJSONObject(4).getInt("total"));

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


            // Number of issues

            issuesCount = new HashMap<String, Integer>();
            issuesCount.put("janinebar", 0);
            issuesCount.put("yasirayounus", 0);
            issuesCount.put("kftang", 0);
            issuesCount.put("Lmnorrell99", 0);
            issuesCount.put("ngynandrea", 0);
            issuesCount.put("RafaelHerrejon", 0);
            issuesCount.put("Majjalpee", 0);

            try {
                URL url = new URL("https://api.github.com/repos/MunchApp/munch-app/issues");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Authenticator.setDefault (new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication ("RafaelHerrejon", "M@gyk1571".toCharArray());
                    }
                });

                // Gets JSON Data
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                while ((result = reader.readLine()) != null) {
                    buffer.append(result + "\n");
                    Log.d("Issues Response: ", result); // line by line printing
                }

                //Parses JSON data
                try {
                    JSONArray response = new JSONArray(buffer.toString()); //from doInBackground
                    int issueSize = response.length();
                    Log.d("Issues size", Integer.toString(issueSize));

                    for(int i = 0; i < issueSize; i++) {
                        JSONObject issue = response.getJSONObject(i);
                        JSONArray assignees = issue.getJSONArray("assignees");

                        for(int j = 0; j < assignees.length(); j++) {

                            JSONObject eachAssignee = assignees.getJSONObject(j);
                            String assigneeName = eachAssignee.getString("login");

                            int numIssues = issuesCount.get(assigneeName)+1;
                            issuesCount.put(assigneeName, numIssues);
                        }
                    }



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

            andreaIssuesView.setText("Number of issues: " + issuesCount.get("ngynandrea") + "\n");
            rafaelIssuesView.setText("Number of issues: " + issuesCount.get("RafaelHerrejon") + "\n");
            yasiraIssuesView.setText("Number of issues: " + issuesCount.get("yasirayounus") + "\n");
            janineIssuesView.setText("Number of issues: " + issuesCount.get("janinebar") + "\n");
            lukeIssuesView.setText("Number of issues: " + issuesCount.get("Lmnorrell99") + "\n");
            kennyIssuesView.setText("Number of issues: " + issuesCount.get("kftang") + "\n");
            syedIssuesView.setText("Number of issues: " + issuesCount.get("Majjalpee") + "\n");
        }
    }

}
