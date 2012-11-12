package com.pwf.core;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message.Builder;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mfullen
 */
public interface Engine
{
    //Collection<Builder> getProtoBuilders() throws NoLoadedMessagesException;

    //Collection<Builder> getFilteredProtoBuilders(ProtoFilter filter) throws NoLoadedMessagesException;

    EngineConfiguration getConfiguration();

    //List<ExtensionRegistry> getExtensionRegistries();

    Collection<EngineData> getAllEngineData();

    boolean findMessagesOnClasspath();
}
