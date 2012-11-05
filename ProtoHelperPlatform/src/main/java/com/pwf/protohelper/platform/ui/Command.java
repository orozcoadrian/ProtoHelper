package com.pwf.protohelper.platform.ui;

/**
 *
 * @author mfullen
 */
public interface Command
{
    String getName();

    void execute(String[] args);
}
