package com.pwf.core;

/**
 *
 * @author mfullen
 */
public class NoLoadedMessagesException extends Exception
{
    public NoLoadedMessagesException()
    {
        super("No protobuf classes have been loaded. Try calling findMessagesOnClassth() method");
    }
}
