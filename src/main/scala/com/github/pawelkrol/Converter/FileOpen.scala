package com.github.pawelkrol.Converter

import org.apache.commons.io.FilenameUtils.getName

import swing.Window
import swing.FileChooser.Result.Approve

object FileOpen {

  def apply(window: Window, list: FileList): Unit = {

    val files = selectedFiles(window)

    list ++= files.map(file => FilePath(getName(file), file))
  }

  def selectedFiles(window: Window) = {

    val selector = FileSelector(window)

    // Pop up a file selector dialog:
    val result = selector.show

    if (result == Approve)
      selector.selectedFiles.map(_.getAbsolutePath)
    else
      Seq()
  }
}
