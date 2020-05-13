package selenium.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class XmlReader {
    public static void downloadFile(String urlString, String outputFileName) throws IOException {
        //CONSEGUIR EL XML
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");

        //BUFFERS PARA LECTURA
        Reader streamReader;
        if (connection.getResponseCode()>299) streamReader = new InputStreamReader(connection.getErrorStream());
        else streamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader in = new BufferedReader(streamReader);
        FileWriter writer = new FileWriter(outputFileName);
        String inputLine;

        //LEER CADA LINEA
        while ((inputLine=in.readLine())!=null){
            writer.write(inputLine);
        }

        //CERRAR TODO
        writer.close();
        in.close();
        streamReader.close();
        connection.disconnect();
    }

}
