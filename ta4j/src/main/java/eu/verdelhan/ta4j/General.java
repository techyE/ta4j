package eu.verdelhan.ta4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yony on 2/27/18.
 */
public final class General {

    // TODO: Exit Debug Mode.
    public final static boolean DEBUG_MODE = true;

//----------------------------------------------------------
//      Print Debug Function
//----------------------------------------------------------

    public static void printDebug(String... strings)
    {
        if (DEBUG_MODE)
        {
            for(String str: strings)
                System.out.println(str);
        }
    }

    public static void printDebug(int... ints)
    {
        if (DEBUG_MODE)
        {
            for(int integer : ints)
                System.out.println(integer);
        }
    }

    public static void printDebug(float... floats)
    {
        if (DEBUG_MODE)
        {
            for(float flt: floats)
                System.out.println(flt);
        }
    }

    public static void printDebug(Double... doubles)
    {
        if (DEBUG_MODE)
        {
            for(Double dbl: doubles)
                System.out.println(dbl);
        }
    }

    public static void printDebug(char... chars)
    {
        if (DEBUG_MODE)
        {
            for(float chr: chars)
                System.out.println(chr);
        }
    }

    public static void printDebug(Object obj)
    {
        if (DEBUG_MODE)
            System.out.println(obj);
    }

    public static void printDebug()
    {
        if (DEBUG_MODE)
            System.out.println();
    }

//========================================================

    public static void InputStreamReader(InputStream stream) throws IOException
    {

        BufferedReader data_translation    = new BufferedReader(new InputStreamReader(stream));  // Change data to a readable format

        String          line;
        int i = 0;
        line  = data_translation.readLine();
        while (line != null)
        {
            i++;
            System.out.println(line);
            line = data_translation.readLine();
        }
        System.out.println(i);
    }

//==========================================================

    public static int divRoundUp(int num, int divisor)
    {return (num + divisor - 1) / divisor;}

}