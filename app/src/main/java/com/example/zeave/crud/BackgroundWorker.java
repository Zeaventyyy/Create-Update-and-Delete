package com.example.zeave.crud;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class    BackgroundWorker extends AsyncTask<String, Void, String> {

    MainActivity context;

    AlertDialog alertDialog;

    BackgroundWorker(MainActivity ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://192.168.0.162/login.php";
        //192.168.0.167
        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];

                URL url = new URL(login_url);

                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);

                OutputStream outputStream = httpUrlConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data =
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                                + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();

                InputStream inputStream = httpUrlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

                inputStream.close();

                httpUrlConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return null;
        }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();

        if (result.equalsIgnoreCase("Login Success")) {
            Intent intent = new Intent(context, CrudOperationActivity.class);
            context.startActivity(intent);
        } else {
            alertDialog.setMessage("Account did not recognize");
            context.clear();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
