package com.elpassion.intelijidea.common

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.util.Key

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val mainframerPath: String?,
                         private val buildCommand: String,
                         private val taskName: String) : CommandLineState(executionEnvironment) {

    override fun startProcess(): ProcessHandler =
            ColoredProcessHandler(createCommandLine().withWorkDirectory(executionEnvironment.project.basePath))

    private fun createCommandLine() = MFCommandLine(mainframerPath, buildCommand, taskName)

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        return BringConsoleToFrontExecutionResult(super.execute(executor, runner), environment, executor)
    }
}

class BringConsoleToFrontExecutionResult(
        executionResult: ExecutionResult,
        val environment: ExecutionEnvironment,
        val executor: Executor) : ExecutionResult by executionResult {

    init {
        processHandler.addProcessListener(OnSystemTextAvailableProcessAdapter({ bringConsoleToFront() }))
    }

    private fun bringConsoleToFront() {
        ExecutionManager.getInstance(environment.project).contentManager.toFrontRunContent(executor, processHandler)
    }

    class OnSystemTextAvailableProcessAdapter(val onSystemTextAvailable: () -> Unit) : ProcessAdapter() {
        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
            if (outputType === ProcessOutputTypes.STDOUT || outputType === ProcessOutputTypes.STDERR) {
                onSystemTextAvailable()
            }
        }
    }
}