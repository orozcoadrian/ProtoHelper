/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@ContainerRegistration(id = "Options",
                       categoryName = "#OptionsCategory_Name_Options",
                       iconBase = "com/pwf/samplemodule/icon-32.png",
                       keywords = "#OptionsCategory_Keywords_Options",
                       keywordsCategory = "Options")
@Messages(value =
{
    "OptionsCategory_Name_Options=Options", "OptionsCategory_Keywords_Options=o"
})
package com.pwf.samplemodule;

import org.netbeans.spi.options.OptionsPanelController.ContainerRegistration;
import org.openide.util.NbBundle.Messages;
