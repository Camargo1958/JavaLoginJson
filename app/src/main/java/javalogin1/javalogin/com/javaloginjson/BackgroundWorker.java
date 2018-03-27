package javalogin1.javalogin.com.javaloginjson;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static java.net.HttpURLConnection.*;

/**
 * A login screen that offers login via email/password.
 */
public class BackgroundWorker extends AsyncTask<String,Integer,String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://10.0.2.2/login_a1.php";
        String loginp_url = "http://10.0.2.2:8080/UserMvcREST/login/loginp"; // test 3
        //String login_url = "http://hazak58.heliohost.org/login_a1.php";
        String verify_url = "http://10.0.2.2:8080/UserMvcREST/login/verify/"; // test 2
        //String login_urlj = "http://10.0.2.2/RestJR/services/JavaRevolutions/validaUsuario"; // test 1
        String login_urlj = "http://10.0.2.2:8080/UserMvcREST/login/loginp"; // test 1
        //String login_urlj = "http://10.0.2.2/login/user"; // final version to be tested
        String register_url = "http://10.0.2.2/register.php";
        //String register_url = "http://hazak58.heliohost.org/register.php";

        if(type.equals("login")){
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(type.equals("loginp")){
            try {
                String user_name = params[1];
                String password = params[2];
                String server_response;
                URL url = new URL(loginp_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                int responseCode = httpURLConnection.getResponseCode();
                String result = "";

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(httpURLConnection.getInputStream());
                    //Log.v("Server resp:", server_response);
                    System.out.println("Server resp:" + server_response);
                    result = "Usuario OK! "+server_response;

                }else if(responseCode != HttpURLConnection.HTTP_OK)
                {
                    //Log.v("Erro de conexão:",(String.valueOf(responseCode)));
                    System.out.println("Erro de conexão:"+(String.valueOf(responseCode)));
                    result = "Erro na verificação do usuário!";
                }
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(type.equals("verify")){
            try {
                String user_name = params[1];
                String password = params[2];
                String server_response;
                verify_url=verify_url+user_name;
                System.out.println("URL:" + verify_url);
                URL url = new URL(verify_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                //httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                int responseCode = httpURLConnection.getResponseCode();
                String result = "";

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(httpURLConnection.getInputStream());
                    //Log.v("Server resp:", server_response);
                    System.out.println("Server resp:" + server_response);
                    result = "Usuario OK! "+server_response;

                }else if(responseCode != HttpURLConnection.HTTP_OK)
                {
                    //Log.v("Erro de conexão:",(String.valueOf(responseCode)));
                    System.out.println("Erro de conexão:"+(String.valueOf(responseCode)));
                    result = "Erro na verificação do usuário!";
                }
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(type.equals("loginj")){
            try {
                String user_name = params[1];
                String password = params[2];
                String server_response;
                URL url = new URL(login_urlj);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); //new for JSON
                //httpURLConnection.setRequestProperty("Accept","application/json");       //new for JSON
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                // JSON build
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("user_name", user_name);
                    jsonParam.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("JSON" + jsonParam.toString());
                DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                        os.writeBytes(jsonParam.toString());
                os.flush();
                os.close();

                int responseCode = httpURLConnection.getResponseCode();
                String result = "";

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(httpURLConnection.getInputStream());
                    //Log.v("Server resp:", server_response);
                    System.out.println("Server resp:" + server_response);
                    result = "Usuario OK! "+server_response;

                }else if(responseCode != HttpURLConnection.HTTP_OK)
                {
                    //Log.v("Erro de conexão:",(String.valueOf(responseCode)));
                    System.out.println("Erro de conexão:"+(String.valueOf(responseCode)));
                    result = "Erro na verificação do usuário!";
                }
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(type.equals("register")){
            try {
                String name = params[1];
                String surname = params[2];
                String age = params[3];
                String username = params[4];
                String password = params[5];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("surname","UTF-8")+"="+URLEncoder.encode(surname,"UTF-8")+"&"
                        +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

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
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    // Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}

