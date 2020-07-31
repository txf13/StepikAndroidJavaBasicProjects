package com.example.findflickrrecentphotosapp.utilities;

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
}
