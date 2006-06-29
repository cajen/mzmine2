/*
 * Copyright 2006 The MZmine Development Team
 * 
 * This file is part of MZmine.
 * 
 * MZmine is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * MZmine is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MZmine; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */

package net.sf.mzmine.visualizers.rawdata.twod;

import java.text.NumberFormat;

import net.sf.mzmine.io.RawDataFile;
import net.sf.mzmine.userinterface.dialogs.ParameterSetupDialog;
import net.sf.mzmine.userinterface.mainwindow.MainWindow;

/**
 * 
 */
public class TwoDSetup {

    public static void showSetupDialog(RawDataFile rawDataFile) {

        String[] paramNames = new String[7];
        paramNames[0] = "MS level";
        paramNames[1] = "Minimum retention time";
        paramNames[2] = "Maximum retention time";
        paramNames[3] = "Minimum M/Z";
        paramNames[4] = "Maximum M/Z";
        paramNames[5] = "Initial retention time resolution";
        paramNames[6] = "Initial M/Z resolution";

        double[] paramValues = new double[7];
        paramValues[0] = 1;
        paramValues[1] = rawDataFile.getDataMinRT(1);
        paramValues[2] = rawDataFile.getDataMaxRT(1);
        paramValues[3] = rawDataFile.getDataMinMZ(1);
        paramValues[4] = rawDataFile.getDataMaxMZ(1);
        paramValues[5] = 2000;
        paramValues[6] = 2000;

        NumberFormat[] numberFormats = new NumberFormat[7];
        numberFormats[0] = NumberFormat.getIntegerInstance();
        numberFormats[1] = NumberFormat.getNumberInstance();
        numberFormats[1].setMinimumFractionDigits(0);
        numberFormats[2] = NumberFormat.getNumberInstance();
        numberFormats[2].setMinimumFractionDigits(0);
        numberFormats[3] = NumberFormat.getNumberInstance();
        numberFormats[3].setMinimumFractionDigits(3);
        numberFormats[4] = NumberFormat.getNumberInstance();
        numberFormats[4].setMinimumFractionDigits(3);
        numberFormats[5] = NumberFormat.getIntegerInstance();
        numberFormats[6] = NumberFormat.getIntegerInstance();

        // Show parameter setup dialog
        MainWindow mainWin = MainWindow.getInstance();
        ParameterSetupDialog psd = new ParameterSetupDialog(mainWin,
                "2D visualizer parameters (" + rawDataFile + ")", paramNames,
                paramValues, numberFormats);
        psd.setVisible(true);

        // Check if user clicked Cancel-button
        if (psd.getExitCode() == -1) {
            return;
        }

        // Read parameter values from dialog
        int msLevel = (int) psd.getFieldValue(0);
        int scanNumbers[] = rawDataFile.getScanNumbers(msLevel);
        if (scanNumbers == null) {
            mainWin.displayErrorMessage("No scans at MS level " + msLevel);
            return;
        }

        double rtMin = psd.getFieldValue(1);
        double rtMax = psd.getFieldValue(2);
        double mzMin = psd.getFieldValue(3);
        double mzMax = psd.getFieldValue(4);

        if ((rtMax <= rtMin) || (mzMax <= mzMin)) {
            mainWin.displayErrorMessage("Invalid bounds");
            return;
        }

        int rtResolution = (int) psd.getFieldValue(5);
        if (rtResolution < 1) {
            mainWin.displayErrorMessage("Invalid retention time resolution: "
                    + rtResolution);
            return;
        }

        int mzResolution = (int) psd.getFieldValue(6);
        if (mzResolution < 1) {
            mainWin.displayErrorMessage("Invalid m/z resolution: "
                    + mzResolution);
            return;
        }

        new TwoDVisualizer(rawDataFile, msLevel, rtMin, rtMax, mzMin, mzMax,
                rtResolution, mzResolution);

    }
}
