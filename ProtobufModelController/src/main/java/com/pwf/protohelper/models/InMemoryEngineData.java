package com.pwf.protohelper.models;

import com.pwf.core.EngineData;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mfullen
 */
public class InMemoryEngineData implements EngineDataRepository
{
    private Map<URL, EngineData> engineMap = new LinkedHashMap<URL, EngineData>();

    public void add(EngineData model)
    {
        this.engineMap.put(model.getLibaryId(), model);
    }

    public void remove(EngineData model)
    {
        this.engineMap.remove(model.getLibaryId());
    }

    public Collection<EngineData> getAll()
    {
        return Collections.unmodifiableCollection(this.engineMap.values());
    }

    public EngineData findById(int id)
    {
        throw new UnsupportedOperationException();
    }

    public void removeAll()
    {
        this.engineMap.clear();
    }

    public void save()
    {
    }

    public EngineData findBy(String url)
    {
        for (EngineData engineData : this.getAll())
        {
            try
            {
                if (engineData.getLibaryId().sameFile(new URL(url)))
                {
                    return engineData;
                }
            }
            catch (MalformedURLException ex)
            {
                Logger.getLogger(InMemoryEngineData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
