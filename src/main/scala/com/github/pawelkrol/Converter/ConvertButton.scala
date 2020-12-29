package com.github.pawelkrol.Converter

import swing.Button

class ConvertButton(
  sourceListView: FileList,
  text: String
) extends Button(text) {

  private def isEnabled = sourceListView.data.nonEmpty

  enabled = isEnabled

  listenTo(TopFrame)

  reactions += {
    case SourceFilesUpdated =>
      peer.setEnabled(isEnabled)
      repaint()
  }
}
