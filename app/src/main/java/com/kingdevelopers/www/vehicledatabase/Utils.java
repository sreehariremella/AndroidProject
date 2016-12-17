package com.kingdevelopers.www.vehicledatabase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saibaba on 6/26/2016.
 */
public class Utils {
    static int LOGGING_LEVEL = 3;
    private static FileWriter fileWriter=null;
    public static void logMsg(String s,Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        String logMesage = "";
        if (stackTraceElements != null)
        {
            int logLevel = stackTraceElements.length > LOGGING_LEVEL ? LOGGING_LEVEL : stackTraceElements.length;
            for (int i = 0; i < logLevel; i++)
            {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                if (stackTraceElement != null)
                {
                    logMesage = logMesage + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "()-->";
                }
            }
        }
        logMesage = logMesage + "Exception:" + ex.getMessage();
        Utils.logMsg("From MainApplication: " + logMesage);
    }

    public static void logMsg(String s) {
        logMessage(s);
        Log.v(Constants.Tag,s);
        Log.d(Constants.Tag,s);
    }

    public static void logMessage(String msg) {
        try
        {
            // new Date().toString()
            fileWriter = new FileWriter(Constants.DETAILED_LOGFILE, true);

            fileWriter.write(new SimpleDateFormat("ddMMMyyyy HH:mm:ss.SSS").format(new Date()) + "- Version : "
                    + Constants.APPLICATION_VERSION + "=> " + msg + "\r\n");
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        finally
        {
            try
            {
                fileWriter.flush();
                fileWriter.close();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }
    }
    public static void showMsg(String message, Context context)
    {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.show();
    }

    public static void LogError(String createDataBase, Exception ex) {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static int getRowID()
    {
        return (int )(Math.random() * 50000 + 1);
    }
}
