package swindroid.suntime.ui;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class FileIO to save and retrieve data from file
 * 
 * @author (Narinder) 
 * @version (31-8-15)
 */
public class FileIO
{

    /**
     * Method to retrieve data from text file
     * @param fileName name of file
     * @return ArrayList of String
     * @throws Exception
     */
    public static ArrayList<String> readFromFile(String fileName, Context c) throws IOException, FileNotFoundException
    {
        ArrayList<String> output=new ArrayList<String>();
        //BufferedReader br=new BufferedReader(new InputStreamReader(c.getAssets().open(fileName)));
        BufferedReader br = new BufferedReader(new InputStreamReader(c.openFileInput(fileName)));
        String line=br.readLine();
        while(line != null)
        {
            output.add(line);
            line=br.readLine();
        }
        br.close();
        return output;
    }
    /**
     * Method to append data to text File
     * @param data ArrayList of String type
     * @param fileName of type String
     * @throws Exception
     */
    public static void addToFile(String data, String fileName, Context c) throws IOException, FileNotFoundException
    {
        FileOutputStream fos = c.openFileOutput(fileName, Context.MODE_APPEND);
        fos.write(data.toString().getBytes());
        fos.close();
    }

}
