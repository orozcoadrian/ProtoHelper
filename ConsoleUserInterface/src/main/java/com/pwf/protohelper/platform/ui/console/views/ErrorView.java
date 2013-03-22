package com.pwf.protohelper.platform.ui.console.views;

import com.pwf.mvcme.MvcFramework;
import com.pwf.mvcme.View;

/**
 *
 * @author mfullen
 */
public class ErrorView implements View<String>
{
    private MvcFramework mvcFramework;
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

    public MvcFramework getMvcFramework()
    {
        return mvcFramework;
    }

    public void setMvcFramework(MvcFramework mvcFramework)
    {
        this.mvcFramework = mvcFramework;
    }
}
