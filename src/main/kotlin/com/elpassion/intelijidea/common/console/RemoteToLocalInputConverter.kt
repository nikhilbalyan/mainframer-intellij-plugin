package com.elpassion.intelijidea.common.console

class RemoteToLocalInputConverter(projectName: String) {
    private val DOT_WORD = "(?:\\.\\w+)"
    private val OPTIONAL_PACKAGE_NAME = "$DOT_WORD*"
    val PATH_SEGMENT = "(?:/\\w+$OPTIONAL_PACKAGE_NAME)"
    val PATH = "$PATH_SEGMENT*?"
    private val FILE_EXTENSION = "\\.\\w+"
    private val END_PATH = "($PATH$FILE_EXTENSION)*"
    private val REMOTE_START_PATH = "(?:$PATH)*"
    private val REMOTE_PATH = "$REMOTE_START_PATH/mainframer/$projectName"
    val FILE_PATH_REGEX = "(?:$REMOTE_PATH$END_PATH)"
    private val LINE_NUMBER_START = ":(?:\\s\\()?"
    private val LINE_NUMBER_VALUE = "(\\d+)"
    private val LINE_NUMBER_END = "(?:,\\s\\d+\\))?"
    val LINE_NUMBER_REGEX = "$LINE_NUMBER_START$LINE_NUMBER_VALUE$LINE_NUMBER_END"
    val FIRST_FRAGMENT_REGEX = "(?:.*?:\\s)?"
    val LINE_WITH_REMOTE_EXCEPTION = "$FIRST_FRAGMENT_REGEX$FILE_PATH_REGEX$LINE_NUMBER_REGEX".toRegex()
}