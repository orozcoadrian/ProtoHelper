package com.pwf.protohelper.controllers;

import com.pwf.core.Engine;
import com.pwf.mvc.AbstractController;
import com.pwf.protohelper.models.EngineDataRepository;
import com.pwf.protohelper.models.InMemoryEngineData;
import com.google.protobuf.Message.Builder;
import com.pwf.core.NoLoadedMessagesException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class EngineController extends AbstractController
{
    private static final Logger logger = LoggerFactory.getLogger(EngineController.class);
    private EngineDataRepository engineDataRepository = null;
    private Engine engine = null;

    public EngineController(Engine engine)
    {
        this(engine, new InMemoryEngineData());
    }

    public EngineController(Engine engine, EngineDataRepository engineDataRepository)
    {
        this.setEngine(engine);
        this.setEngineDataRepository(engineDataRepository);
    }

    public final void setEngineDataRepository(EngineDataRepository engineDataRepository)
    {
        this.engineDataRepository = engineDataRepository;
    }

    public final void setEngine(Engine engine)
    {
        this.engine = engine;
    }

    public List<Builder> loadAvailableBuilders()
    {
        boolean foundMessagesOnClasspath = this.engine.findMessagesOnClasspath();
        if (foundMessagesOnClasspath)
        {
            try
            {
                for (Builder builder : this.engine.getProtoBuilders())
                {
                    this.engineDataRepository.add(builder);
                }
            }
            catch (NoLoadedMessagesException ex)
            {
                logger.error("Error:", ex);
            }
        }
        return this.getBuilders();
    }

    public List<Builder> getBuilders()
    {
        if (this.engineDataRepository.getAll().isEmpty())
        {
            return loadAvailableBuilders();
        }
        return new ArrayList<Builder>(this.engineDataRepository.getAll());
    }
}
