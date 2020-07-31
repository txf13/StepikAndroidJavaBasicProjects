package com.example.findflickrrecentphotosapp.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {

    public static String getResponseFromHttpGetUrl (URL url) throws IOException {

        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
         try {

             InputStream in=urlConnection.getInputStream();
             Scanner scanner=new Scanner(in);
             scanner.useDelimiter("\\A");
              boolean hasInput=scanner.hasNext();
              if (hasInput){
                  String result=scanner.next();
                  return result;
              } else {
                  return null;
              }


         } finally {
             urlConnection.disconnect();
         }
    }

    public static byte[] getUrlBytes (String urlSpec) throws IOException
    {
        URL url =new URL(urlSpec);
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        try {

            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=urlConnection.getInputStream();
            if(urlConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(urlConnection.getResponseMessage()+"with "+urlSpec);
            }
            int bytesRead =0;
            byte[] buffer=new byte[1024];

            while ((bytesRead=in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            urlConnection.disconnect();
        }

    }

}
