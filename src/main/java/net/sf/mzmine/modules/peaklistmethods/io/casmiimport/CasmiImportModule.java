/*
 * Copyright 2006-2014 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MZmine 2; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */

package net.sf.mzmine.modules.peaklistmethods.io.casmiimport;

import java.util.Collection;

import javax.annotation.Nonnull;

import net.sf.mzmine.datamodel.PeakList;
import net.sf.mzmine.datamodel.RawDataFile;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.MZmineModuleCategory;
import net.sf.mzmine.modules.MZmineProcessingModule;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.taskcontrol.TaskEvent;
import net.sf.mzmine.taskcontrol.TaskListener;
import net.sf.mzmine.taskcontrol.TaskStatus;
import net.sf.mzmine.util.ExitCode;

/**
 * Raw data import module
 */
public class CasmiImportModule implements MZmineProcessingModule, TaskListener {

    private static final String MODULE_NAME = "Import CASMI challenge task";
    private static final String MODULE_DESCRIPTION = "This module generates a raw data file and a peak list containing a single feature, based on the data from the Critical Assessment of Small Molecule Identificatin (CASMI) challenge. [http://www.casmi-contest.org]";

    @Override
    public @Nonnull String getName() {
	return MODULE_NAME;
    }

    @Override
    public @Nonnull String getDescription() {
	return MODULE_DESCRIPTION;
    }

    @Override
    @Nonnull
    public ExitCode runModule(@Nonnull ParameterSet parameters,
	    @Nonnull Collection<Task> tasks) {

	Task newTask = new CasmiImportTask(parameters);
	newTask.addTaskListener(this);
	tasks.add(newTask);

	return ExitCode.OK;
    }

    /**
     * The statusChanged method of the TaskEvent interface
     * 
     * @param e
     *            The TaskEvent which triggered this action
     */
    @Override
    public void statusChanged(TaskEvent e) {
	if (e.getStatus() == TaskStatus.FINISHED) {
	    MZmineCore.getCurrentProject().addFile(
		    (RawDataFile) e.getSource().getCreatedObjects()[0]);
	    MZmineCore.getCurrentProject().addPeakList(
		    (PeakList) e.getSource().getCreatedObjects()[1]);
	}

    }

    @Override
    public @Nonnull MZmineModuleCategory getModuleCategory() {
	return MZmineModuleCategory.PEAKLISTEXPORT;
    }

    @Override
    public @Nonnull Class<? extends ParameterSet> getParameterSetClass() {
	return CasmiImportParameters.class;
    }

}