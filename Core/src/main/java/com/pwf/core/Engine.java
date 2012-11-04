package com.pwf.core;

import com.google.protobuf.Message.Builder;
import java.util.Collection;

/**
 *
 * @author mfullen
 */
public interface Engine
{
    Collection<Builder> getProtoBuilders() throws NoLoadedMessagesException;

    Collection<Builder> getFilteredProtoBuilders(ProtoFilter filter) throws NoLoadedMessagesException;

    EngineConfiguration getConfiguration();

    boolean findMessagesOnClasspath();
}
