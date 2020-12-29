package com.github.pawelkrol.Converter

import java.io.File.separator
import java.nio.file.{Files, Paths}
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

import scala.swing.{FileChooser, Window}
import scala.swing.FileChooser.Result.{Approve, Error}
import scala.swing.FileChooser.SelectionMode.FilesOnly

class FileSelector(window: Window) extends FileChooser {

  import FileSelector.lastUsedDirectory

  override lazy val peer: JFileChooser = new JFileChooser(lastUsedDirectory) {
  }

  // Remove "Accept All" file filter from the list of available file filters:
  peer.setAcceptAllFileFilterUsed(false)

  fileFilter = new FileNameExtensionFilter("JPEG images (*.jpg)", "jpg")

  fileSelectionMode = FilesOnly

  multiSelectionEnabled = true

  title = "Select source JPEG image files..."

  private def directoryFromFilePath(path: String): Option[String] =
    if (path.isEmpty)
      None
    else {
      val absolutePath = Paths.get(path).toFile.getAbsolutePath

      val directory = absolutePath.substring(0, absolutePath.lastIndexOf(separator))

      if (Files.exists(Paths.get(directory)))
        Some(directory)
      else
        None
    }

  def show = {

    val result = showOpenDialog(window)

    // Open/save operations remember the last used directory (but only when load/save was successful):
    if (result == Approve)
      if (selectedFiles != null) {
        directoryFromFilePath(selectedFiles.head.getAbsolutePath) match {
          case Some(directory) =>
            lastUsedDirectory = directory
          case None =>
        }
        result
      }
      else
        Error
    else
      result
  }
}

object FileSelector {

  private var lastUsedDirectory = System.getProperty("user.dir")

  def apply(window: Window) = new FileSelector(window)
}
