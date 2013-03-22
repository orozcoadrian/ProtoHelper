package com.pwf.protohelper.controllers;

import com.pwf.core.Engine;
import com.pwf.core.EngineData;
import com.pwf.mvcme.MvcMeController;
import com.pwf.protohelper.models.EngineDataRepository;
import com.pwf.protohelper.models.InMemoryEngineData;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class EngineController extends MvcMeController
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

    protected List<EngineData> loadAvailableEngineData()
    {
        boolean foundMessagesOnClasspath = this.engine.findMessagesOnClasspath();
        if (foundMessagesOnClasspath)
        {
            for (EngineData engineData : this.engine.getAllEngineData())
            {
                this.engineDataRepository.create(engineData);
            }
        }
        return this.getAllEngineData();
    }

    public List<EngineData> getAllEngineData()
    {
        if (this.engineDataRepository.getAll().isEmpty())
        {
            return loadAvailableEngineData();
        }
        return new ArrayList<EngineData>(this.engineDataRepository.getAll());
    }

    public void loadExtensionRegistry()
    {
        this.engine.getExtensionRegistries();
    }
}
