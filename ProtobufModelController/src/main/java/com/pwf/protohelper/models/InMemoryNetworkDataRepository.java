package com.pwf.protohelper.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mfullen
 */
public class InMemoryNetworkDataRepository implements NetworkDataRepository
{
    private List<NetworkData> networkdata = new ArrayList<NetworkData>();

    public void add(NetworkData model)
    {
        this.networkdata.add(model);
    }

    public void remove(NetworkData model)
    {
        this.networkdata.remove(model);
    }

    public Collection<NetworkData> getAll()
    {
        return this.networkdata;
    }

    public NetworkData findById(int id)
    {
        return this.networkdata.get(id);
    }

    public void removeAll()
    {
        this.networkdata.clear();
    }

    public void save()
    {
    }

    public Collection<FlatNetworkData> load()
    {
        return null;
    }

    public void addAll(Collection<NetworkData> networkData)
    {
        for (NetworkData nd : networkData)
        {
            this.add(nd);
        }
    }
}
