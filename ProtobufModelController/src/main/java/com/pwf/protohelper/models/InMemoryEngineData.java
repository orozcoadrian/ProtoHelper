package com.pwf.protohelper.models;

import com.google.protobuf.Message.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mfullen
 */
public class InMemoryEngineData implements EngineDataRepository
{
    private List<Builder> builderList = new ArrayList<Builder>();

    public void add(Builder model)
    {
        this.builderList.add(model);
    }

    public void remove(Builder model)
    {
        this.builderList.remove(model);
    }

    public Collection<Builder> getAll()
    {
        return this.builderList;
    }

    public Builder findById(int id)
    {
        return this.builderList.get(id);
    }

    public void removeAll()
    {
        this.builderList.clear();
    }

    public void save()
    {
    }
}
