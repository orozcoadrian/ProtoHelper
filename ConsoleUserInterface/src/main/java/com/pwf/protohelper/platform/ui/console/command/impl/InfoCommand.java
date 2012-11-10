package com.pwf.protohelper.platform.ui.console.command.impl;

import com.pwf.protohelper.platform.ui.console.command.Command;

/**
 *
 * @author mfullen
 */
public class InfoCommand implements Command
{
    public InfoCommand()
    {
    }

    public String getName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute(String[] args)
    {
        infoMethod();
    }

    public void infoMethod()
    {
        System.out.println("PluginInformation: " + this);
    }
}
