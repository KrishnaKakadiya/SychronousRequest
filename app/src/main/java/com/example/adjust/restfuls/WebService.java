package com.example.adjust.restfuls;

import android.os.AsyncTask;
import android.os.Handler;

import com.example.adjust.AdjustApplication;
import com.example.adjust.constants.Constant;
import com.example.adjust.model.AdjustResponse;
import com.example.adjust.presenter.MainActivityListener;
import com.example.adjust.presenter.WebServiceListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebService {

    public InputStream getSecuredInputStreams(String passedURL, String postData)
    {
        InputStream inputStream = null;
        try
        {
            URL postRequest = new URL(passedURL);
            TrustManager trustManager = new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException
                {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException
                {
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) postRequest.openConnection();
            if (httpsURLConnection != null)
            {
                httpsURLConnection.setHostnameVerifier(new NullHostNameVerifier());
                httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                httpsURLConnection.setConnectTimeout(Constant.TIMEOUT);
                httpsURLConnection.setReadTimeout(Constant.TIMEOUT);
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpsURLConnection.getResponseCode();
                AdjustApplication.printLogMessage(3,"Response Code : " + String.valueOf(responseCode));
                if (responseCode == 200 || responseCode == 201)
                {
                    inputStream = httpsURLConnection.getInputStream();
                }
            }
        }
        catch (KeyManagementException | NoSuchAlgorithmException | IOException e)
        {
            AdjustApplication.printLogMessage(5, e.toString());
        }
        return inputStream;
    }

    public String getResponse(final InputStream inputStream)
    {
        String response = null;
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
            bufferedReader.close();
            response = builder.toString();
        }
        catch (IOException e)
        {
            AdjustApplication.printLogMessage(5, e.toString());
        }
        return response;
    }
}

