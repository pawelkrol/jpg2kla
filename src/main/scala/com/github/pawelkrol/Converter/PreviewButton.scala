package com.github.pawelkrol.Converter

import swing.{Action, Button}

class PreviewButton(
  sourceListView: FileList,
  text: String
) extends Button(text) {

  private def isEnabled = sourceListView.currentSelectionIndex.nonEmpty

  enabled = isEnabled

  listenTo(sourceListView)

  reactions += {
    case SourceFileSelected =>
      peer.setEnabled(isEnabled)
      repaint()
  }
}
