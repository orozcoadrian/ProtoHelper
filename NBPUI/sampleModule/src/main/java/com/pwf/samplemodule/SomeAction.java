/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pwf.samplemodule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
          id = "com.pwf.samplemodule.SomeAction")
@ActionRegistration(iconBase = "com/pwf/samplemodule/icon-16.png",
                    displayName = "#CTL_SomeAction")
@ActionReferences(
{
    @ActionReference(path = "Menu/File", position = 1300, separatorBefore = 1250)
})
@Messages("CTL_SomeAction=MySuperAction")
public final class SomeAction implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        // TODO implement action body
    }
}
