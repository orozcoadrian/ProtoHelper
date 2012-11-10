package com.pwf.protohelper.platform.ui.console.command.impl;

import com.pwf.protohelper.platform.ui.console.command.Command;

/**
 *
 * @author mfullen
 */
public class HelpCommand implements Command
{
    public HelpCommand()
    {
    }

    public String getName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute(String[] args)
    {
        this.helpMethod();
    }

    public void helpMethod()
    {
//        System.out.println("Commands:");
//        for (String string : this.commands.keySet())
//        {
//            System.out.println(string);
//        }
    }
}
