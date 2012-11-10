package com.pwf.protohelper.platform.ui.console;

/**
 *
 * @author mfullen
 */
public final class ConsoleUtils
{
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
}
