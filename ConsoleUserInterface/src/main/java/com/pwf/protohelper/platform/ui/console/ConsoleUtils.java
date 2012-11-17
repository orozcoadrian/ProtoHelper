package com.pwf.protohelper.platform.ui.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mfullen
 */
public final class ConsoleUtils
{
    private static BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

    private ConsoleUtils()
    {
    }

    public static String[] getRemainingArguments(String[] arugments)
    {
        int remainingArgsSize = arugments.length - 1;
        String[] args = new String[remainingArgsSize];
        if (remainingArgsSize > 0)
        {
            for (int i = 0; i < remainingArgsSize; i++)
            {
                args[i] = arugments[i + 1];
            }
        }
        return args;
    }

    public static String getInputFromUser()
    {
        String messageChoice = null;
        try
        {
            System.out.print(">>");
            messageChoice = bufferRead.readLine();
            return messageChoice;
        }
        catch (IOException ex)
        {
            System.out.println("Error Occurred! Please try again. " + ex.getMessage());
        }
        finally
        {
            return messageChoice;
        }
    }
}
