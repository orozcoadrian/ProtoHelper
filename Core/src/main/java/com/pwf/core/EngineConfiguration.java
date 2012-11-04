package com.pwf.core;

import java.util.Collection;

/**
 *
 * @author mfullen
 */
public interface EngineConfiguration
{
    String getLoadDirectory();

    Collection<ProtoFilter> getProtoFilters();
}
