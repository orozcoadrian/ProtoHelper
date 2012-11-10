package com.pwf.protohelper.platform.ui.console.command;

/**
 *
 * @author mfullen
 */
public interface Command
{
    String getName();

    void execute(String[] args);
}
