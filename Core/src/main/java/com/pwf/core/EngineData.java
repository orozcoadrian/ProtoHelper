package com.pwf.core;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import java.net.URL;
import java.util.Set;

/**
 *
 * @author mfullen
 */
public class EngineData
{
    private URL libaryId;
    private Set<Class<? extends Message>> loadedClasses;
    private ExtensionRegistry extensionRegistry;
    private Message.Builder transportMessage;

    public ExtensionRegistry getExtensionRegistry()
    {
        return extensionRegistry;
    }

    public URL getLibaryId()
    {
        return libaryId;
    }

    public Set<Class<? extends Message>> getLoadedClasses()
    {
        return loadedClasses;
    }

    public Message.Builder getTransportMessage()
    {
        return transportMessage;
    }

    public void setExtensionRegistry(ExtensionRegistry extensionRegistry)
    {
        this.extensionRegistry = extensionRegistry;
    }

    public void setLibaryId(URL libaryId)
    {
        this.libaryId = libaryId;
    }

    public void setLoadedClasses(Set<Class<? extends Message>> loadedClasses)
    {
        this.loadedClasses = loadedClasses;
    }

    public void setTransportMessage(Message.Builder transportMessage)
    {
        this.transportMessage = transportMessage;
    }

    @Override
    public String toString()
    {
        String tmString = this.getTransportMessage() != null ? this.getTransportMessage().getClass().getName() : "N/A";
        String id = this.getLibaryId() != null ? this.getLibaryId().toString() : "N/A";

        return String.format("URL: %s TransportMessage: %s", id, tmString);
    }
}
