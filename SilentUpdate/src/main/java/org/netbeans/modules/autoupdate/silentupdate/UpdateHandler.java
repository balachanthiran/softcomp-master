package org.netbeans.modules.autoupdate.silentupdate;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.InstallSupport.Installer;
import org.netbeans.api.autoupdate.InstallSupport.Validator;
import org.netbeans.api.autoupdate.OperationContainer;
import org.netbeans.api.autoupdate.OperationContainer.OperationInfo;
import org.netbeans.api.autoupdate.OperationException;
import org.netbeans.api.autoupdate.OperationSupport;
import org.netbeans.api.autoupdate.OperationSupport.Restarter;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 */
public final class UpdateHandler {

    public static final String SILENT_UC_CODE_NAME = "org_netbeans_modules_autoupdate_silentupdate_update_center"; // NOI18N
    private static Set<String> _installedModuleCodeNames = new HashSet<>();

    public static boolean timeToCheck() {
        // every startup
        return true;
    }

    public static class UpdateHandlerException extends Exception {

        public UpdateHandlerException(String msg) {
            super(msg);
        }

        public UpdateHandlerException(String msg, Throwable th) {
            super(msg, th);
        }
    }

    public static void checkAndHandleModules() {
        // refresh silent update center first
        UpdateUnitProvider silentUpdateProvider = getSilentUpdateProvider();
        List<UpdateUnit> updateUnits = UpdateManager.getDefault().getUpdateUnits();

        findAndInstallModules(updateUnits);
        uninstallRemovedModules(silentUpdateProvider, updateUnits);
    }

    static void findAndInstallModules(List<UpdateUnit> updateUnits) {
        Collection<UpdateElement> moduleUpdates = new HashSet<>();
        Collection<UpdateElement> newModules = new HashSet<>();

        for (UpdateUnit unit : updateUnits) {
            if (!unit.getAvailableUpdates().isEmpty()) // module has available content
            {
                // null means the module is not installed.
                if (unit.getInstalled() == null) {
                    newModules.add(unit.getAvailableUpdates().get(0)); // add content with highest version
                } else {
                    moduleUpdates.add(unit.getAvailableUpdates().get(0)); // add content with highest version
                }
            }
        }

        if (newModuleInstallationAllowed()) {
            installModules(newModules, false);
        }
        installModules(moduleUpdates, true);
    }

    static void installModules(Collection<UpdateElement> elements, boolean forUpdate) {
        if (elements.isEmpty()) {
            if (forUpdate) {
                OutputLogger.log("No updates found.");
            } else {
                OutputLogger.log("No new modules found.");
            }
            return;
        }

        OperationContainer<InstallSupport> container;
        if (forUpdate) {
            container = OperationContainer.createForUpdate();
        } else {
            container = OperationContainer.createForInstall();
        }

        // loop all updates and add to container for update
        for (UpdateElement ue : elements) {
            if (container.canBeAdded(ue.getUpdateUnit(), ue)) {
                OutputLogger.log("Module found: " + ue);
                OperationInfo<InstallSupport> operationInfo = container.add(ue);
                if (operationInfo == null) {
                    continue;
                }
                container.add(operationInfo.getRequiredElements());
                if (!operationInfo.getBrokenDependencies().isEmpty()) {
                    // have a problem => cannot continue
                    OutputLogger.log("There are broken dependencies => cannot continue, broken deps: " + operationInfo.getBrokenDependencies());
                    return;
                }
            }
        }
        try {
            handleInstall(container);
            if (forUpdate) {
                OutputLogger.log("Update modules done.");
            } else {
                OutputLogger.log("Install new modules done.");
            }
        } catch (UpdateHandlerException ex) {
            OutputLogger.log(ex.getLocalizedMessage(), ex.getCause());
        }
    }

    static void uninstallRemovedModules(UpdateUnitProvider silentUpdateProvider, List<UpdateUnit> updateUnits) {
        // Get modules defined in the remote updates.xml
        List<UpdateUnit> remoteModules = silentUpdateProvider.getUpdateUnits();
        Set<String> unitsToUninstall = new HashSet<>();

        // Iterate through all installed or remotely available modules.
        for (UpdateUnit module : updateUnits) {
            // Updated module list no longer contains "module" and this module has previously been installed.
            // TODO determine whether we need to be able to uninstall modules that were not installed in the same runtime.
            // ATM modules removed from updates.xml while this app is not running will not be detected as changes.
            if (!remoteModules.contains(module) && _installedModuleCodeNames.contains(module.getCodeName())) {
                unitsToUninstall.add(module.getCodeName());
            }
        }
        if (!unitsToUninstall.isEmpty()) {
            doUninstall(unitsToUninstall, updateUnits);
        }

        _installedModuleCodeNames.clear();
        for (UpdateUnit module : remoteModules) {
            _installedModuleCodeNames.add(module.getCodeName());
        }
    }

    public static boolean isLicenseApproved(String license) {
        // place your code there
        return true;
    }

    static void handleInstall(OperationContainer<InstallSupport> container) throws UpdateHandlerException {
        // check licenses
        if (!allLicensesApproved(container)) {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Cannot continue because license approval is missing for some updates.");
        }

        // download
        InstallSupport support = container.getSupport();
        Validator v = null;
        try {
            v = doDownload(support);
        } catch (OperationException ex) {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while downloading, cause: ", ex);
        }
        if (v == null) {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Missing Update Validator => cannot continue.");
        }

        // verify
        Installer i = null;
        try {
            i = doVerify(support, v);
        } catch (OperationException ex) {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while verification of updates, cause: ", ex);
        }
        if (i == null) {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Missing Update Installer => cannot continue.");
        }

        // install
        Restarter r = null;
        try {
            r = doInstall(support, i);
        } catch (OperationException ex) {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while installation of updates, cause: ", ex);
        }

        // restart later
        support.doRestartLater(r);
        return;
    }

    static UpdateUnitProvider getSilentUpdateProvider() {
        List<UpdateUnitProvider> providers = UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true);
        for (UpdateUnitProvider p : providers) {
            if (SILENT_UC_CODE_NAME.equals(p.getName())) {
                try {
                    p.refresh(null, true);
                } catch (IOException ex) {
                    // caught a exception
                    OutputLogger.log("A problem caught while refreshing Update Centers, cause: ", ex);
                }
                return p;
            }
        }
        return null;
    }

    static boolean allLicensesApproved(OperationContainer<InstallSupport> container) {
        if (!container.listInvalid().isEmpty()) {
            return false;
        }
        for (OperationInfo<InstallSupport> info : container.listAll()) {
            String license = info.getUpdateElement().getLicence();
            if (!isLicenseApproved(license)) {
                return false;
            }
        }
        return true;
    }

    static Validator doDownload(InstallSupport support) throws OperationException {
        return support.doDownload(null, true);
    }

    static Installer doVerify(InstallSupport support, Validator validator) throws OperationException {

        Installer installer = support.doValidate(validator, null); // validates all plugins are correctly downloaded
        // XXX: use there methods to make sure updates are signed and trusted
        // installSupport.isSigned(installer, <an update element>);
        // installSupport.isTrusted(installer, <an update element>);
        return installer;
    }

    static Restarter doInstall(InstallSupport support, Installer installer) throws OperationException {
        return support.doInstall(installer, null);
    }

    static void doDisable(Collection<String> codeNames) {
        Collection<UpdateElement> toDisable = new HashSet<>();
        // Get local/installed modules AND modules defined in remote updates.xml
        List<UpdateUnit> allUpdateUnits = UpdateManager.getDefault().getUpdateUnits();
        for (UpdateUnit unit : allUpdateUnits) {
            UpdateElement el = unit.getInstalled();
            // filter all installed modules and enabled modules
            if (el == null || !el.isEnabled()) {
                continue;
            }
            // check if the module should be disabled.
            if (codeNames.contains(el.getCodeName())) {
                toDisable.add(el);
            }
        }

        OperationContainer<OperationSupport> oc = OperationContainer.createForDirectDisable();
        disableOrUninstall(oc, toDisable);
    }

    // codeName contains code name of modules for disable
    static void doUninstall(Collection<String> codeNames, List<UpdateUnit> updateUnits) {
        Collection<UpdateElement> toUninstall = new HashSet<>();
        // Iterate through all installed or remotely available modules.
        for (UpdateUnit unit : updateUnits) {
            UpdateElement el = unit.getInstalled();
            // filter all installed modules
            if (el == null) {
                continue;
            }
            // check if the module should be uninstalled.
            if (codeNames.contains(el.getCodeName())) { // filter given module in the parameter
                toUninstall.add(el);
            }
        }

        OperationContainer<OperationSupport> oc = OperationContainer.createForDirectUninstall();
        disableOrUninstall(oc, toUninstall);
    }

    static void disableOrUninstall(OperationContainer<OperationSupport> oc, Collection<UpdateElement> elements) {
        for (UpdateElement module : elements) {
            // check if module can be disabled/uninstalled.
            if (oc.canBeAdded(module.getUpdateUnit(), module)) {
                OperationInfo operationInfo = oc.add(module);
                if (operationInfo == null) // the module is already planned to be disabled/uninstalled.
                {
                    continue;
                }
                // get all modules depending on this module --- TODO isnt this actually modules depending on this module?
                Set requiredElements = operationInfo.getRequiredElements();
                // add all of them to modules for disable/uninstall
                oc.add(requiredElements);
            }
        }

        // check the container doesn't contain any invalid element
        assert oc.listInvalid().isEmpty();
        try {
            // get operation support for completing the disable/uninstall operation
            Restarter restarter = oc.getSupport().doOperation(null);
            // no restart needed in this case
            assert restarter == null;
        } catch (OperationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static boolean newModuleInstallationAllowed() {
        String s = NbBundle.getBundle("org.netbeans.modules.autoupdate.silentupdate.resources.Bundle").getString("UpdateHandler.NewModules");
        return Boolean.parseBoolean(s);
    }

}
