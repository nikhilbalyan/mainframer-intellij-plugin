package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.select.getTemplateConfigurations
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import io.reactivex.Maybe

typealias MFUiSelector = (List<MFSelectorItem>, List<MFSelectorItem>) -> Maybe<MFSelectorResult>

fun mfSelector(project: Project, uiSelector: MFUiSelector) = {
    with(RunManagerEx.getInstanceEx(project)) {
        uiSelector(getConfigurationItems(), getTemplateConfigurationItems())
    }
}

fun getSelectorResult(uiIn: List<MFSelectorItem>, uiOut: List<MFSelectorItem>, replaceAll: Boolean): MFSelectorResult {
    val toInject = getItemsToInject(uiIn, uiOut, replaceAll)
    val toRestore = getItemsToRestore(uiIn, uiOut)
    return MFSelectorResult(toInject, toRestore)
}

private fun RunManagerEx.getConfigurationItems() = allConfigurationsList
        .map { MFSelectorItem(it, isSelected = hasMainframerTask(it), name = it.configurationName()) }


private fun RunManagerEx.getTemplateConfigurationItems() = getTemplateConfigurations()
        .map { MFSelectorItem(it, isSelected = hasMainframerTask(it), name = it.templateConfigurationName()) }

private fun RunConfiguration.configurationName(): String = "[${type.displayName}] ${name}"

private fun RunConfiguration.templateConfigurationName(): String = type.displayName

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MFBeforeRunTask }

private fun getItemsToInject(uiIn: List<MFSelectorItem>, uiOut: List<MFSelectorItem>, replaceAll: Boolean) =
        getItemsToInject(if (replaceAll) emptyList() else uiIn, uiOut)

private fun getItemsToInject(uiIn: List<MFSelectorItem>, uiOut: List<MFSelectorItem>) =
        (uiOut.filter { it.isSelected } - uiIn.filter { it.isSelected }).map { it.configuration }

private fun getItemsToRestore(uiIn: List<MFSelectorItem>, uiOut: List<MFSelectorItem>) =
        (uiOut.filterNot { it.isSelected } - uiIn.filterNot { it.isSelected }).map { it.configuration }