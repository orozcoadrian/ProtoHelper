package com.pwf.protohelper.models;

import com.pwf.plugin.network.client.DefaultNetworkClientSettings;
import com.pwf.plugin.network.client.NetworkClientSettings;
import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class XmlNetworkDataRepository extends InMemoryNetworkDataRepository
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmlNetworkDataRepository.class);
    private static final String DEFAULT = "";
    private static final String DEFAULT_FILE_NAME = "networkconfig.xml";
    private String filePath;
    private String directory;
    private XStream xStream = new XStream();

    public XmlNetworkDataRepository()
    {
        this(DEFAULT_FILE_NAME);
    }

    public XmlNetworkDataRepository(final String filename)
    {
        this(filename, DEFAULT);
    }

    public XmlNetworkDataRepository(String filePath, String directory)
    {
        this.filePath = filePath;
        this.directory = directory;
        this.xStream.alias("NetworkConnection", FlatNetworkData.class);
        this.xStream.alias("Connections", List.class);
        this.xStream.alias("s", NetworkClientSettings.class, DefaultNetworkClientSettings.class);
    }

    @Override
    public Collection<FlatNetworkData> load()
    {
        try
        {
            this.removeAll();
            Collection<FlatNetworkData> resultsCollection = (Collection<FlatNetworkData>) this.xStream.fromXML(new FileInputStream(filePath));

            return resultsCollection;
        }
        catch (FileNotFoundException ex)
        {
            logger.error("Error", ex);
            return null;
        }
    }

    @Override
    public void save()
    {
        try
        {
            Collection<FlatNetworkData> resultsCollection = new ArrayList<FlatNetworkData>();
            for (NetworkData networkData : this.getAll())
            {
                resultsCollection.add(FlatNetworkData.create(networkData));
            }
            this.xStream.toXML(resultsCollection, new FileOutputStream(filePath));
        }
        catch (IOException ex)
        {
            logger.error("Error", ex);
        }
    }
}
