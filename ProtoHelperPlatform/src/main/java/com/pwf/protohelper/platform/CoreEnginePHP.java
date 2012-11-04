package com.pwf.protohelper.platform;

import com.pwf.core.Engine;
import com.pwf.core.EngineConfiguration;

/**
 *
 * @author mfullen
 */
public interface CoreEnginePHP extends PHPPlugin
{
    Engine getEngine(EngineConfiguration config);
}
