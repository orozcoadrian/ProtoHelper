package com.pwf.protohelper.platform.ui.console.views;

import com.pwf.mvc.ControllersManager;
import com.pwf.mvc.View;

/**
 *
 * @author mfullen
 */
public class ErrorView implements View<String>
{
    private ControllersManager controllersManager = null;
    private String message = "";

    public ErrorView()
    {
    }

    public void update(String model)
    {
        this.message = model;
        this.setVisible(true);
    }

    public void setVisible(boolean visible)
    {
        if (visible)
        {
            System.out.println(this.message);
        }
    }

    public String getName()
    {
        return View.ERROR_VIEW_ID;
    }

    public void setControllerManager(ControllersManager controllerManager)
    {
        this.controllersManager = controllerManager;
    }

    public ControllersManager getControllerManager()
    {
        return this.controllersManager;
    }
}
