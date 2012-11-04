package com.pwf.core.impl;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.pwf.core.Engine;
import com.pwf.core.EngineConfiguration;
import com.pwf.core.NoLoadedMessagesException;
import com.pwf.core.ProtoFilter;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class DefaultEngine implements Engine
{
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    private EngineConfiguration configuration = null;
    private Set<Class<? extends Message>> loadedClasses = null;
    private static final ProtoFilter DEFAULT_FILTER = new ProtoFilterImpl();

    public DefaultEngine(EngineConfiguration configuration)
    {
        this.setConfiguration(configuration);
    }

    public final void setConfiguration(EngineConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public Collection<Builder> getProtoBuilders() throws
            NoLoadedMessagesException
    {
        if (!this.hasLoadedClasses())
        {
            throw new NoLoadedMessagesException();
        }

        return this.getFilteredProtoBuilders(DEFAULT_FILTER);
    }

    public Collection<Builder> getFilteredProtoBuilders(ProtoFilter filter)
            throws NoLoadedMessagesException
    {
        if (!this.hasLoadedClasses())
        {
            throw new NoLoadedMessagesException();
        }
        return EngineUtils.filter(this.loadedClasses, filter);
    }

    public EngineConfiguration getConfiguration()
    {
        return this.configuration;
    }

    public boolean findMessagesOnClasspath()
    {
        this.loadedClasses = EngineUtils.getMessageFilesonClasspath(this.getConfiguration().getLoadDirectory());
        return this.hasLoadedClasses();
    }

    protected boolean hasLoadedClasses()
    {
        return this.loadedClasses != null && !this.loadedClasses.isEmpty();
    }
}
