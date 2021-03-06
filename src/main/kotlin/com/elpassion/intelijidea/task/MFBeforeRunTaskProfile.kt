package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.common.console.MFCommandLineState
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ModuleRunProfile
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import javax.swing.Icon


class MFBeforeRunTaskProfile(private val task: MFBeforeRunTask, private val commandLineProvider: (MFTaskData) -> GeneralCommandLine) : ModuleRunProfile {
    override fun getName(): String = "Mainframer"

    override fun getIcon(): Icon? = null

    override fun getModules(): Array<Module> = Module.EMPTY_ARRAY

    override fun getState(executor: Executor, env: ExecutionEnvironment) = with(task.data) {
        MFCommandLineState(env, commandLineProvider(task.data))
    }
}