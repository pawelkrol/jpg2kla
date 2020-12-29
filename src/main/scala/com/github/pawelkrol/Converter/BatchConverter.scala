package com.github.pawelkrol.Converter

import java.io.File
import javax.swing.JOptionPane.{CANCEL_OPTION, CLOSED_OPTION, INFORMATION_MESSAGE, OK_CANCEL_OPTION, YES_OPTION}

import swing.Window

class BatchConverter(
  window: Window,
  sourceList: FileList,
  targetList: FileList
) extends ConversionProcess with SafeExecutor {

  private val sourceFiles = sourceList.data.filter(f => fileExists(f.path))

  private val fileCount = sourceFiles.size

  private val targetFiles = sourceFiles.map(_.jpegToKoalaFilePath)

  private def fileExists(filename: String) = (new File(filename)).exists

  private def alreadyExistingTargetFiles = targetFiles.filter(f => fileExists(f.path)).map(_.name)

  private def convertConfirmed(window: Window) =
    methodExecuteSafe(window, (window) => confirmConvert(window))

  private def overwriteConfirmed(window: Window) =
    methodExecuteSafe(window, (window) => confirmOverwrite(window))

  private def convertFilesInMemory = {
    val converters = sourceFiles.map(f => new PicConverter(f, true)).toSeq

    // Start a separate thread to begin conversion process:
    val (errors, pictures) = convertWith(ConversionWorker(converters), window)

    if (pictures.size > 0) {
      // Remove successfully converted JPEG files from source list view:
      if (errors.size == 0)
        sourceList.clear
      else
        sourceList.removeResults(pictures)
      // Populate target list view with successfully converted KoalaPainter files:
      targetList ++= sourceFiles.filter(pictures.map(_.jpeg).contains(_)).map(_.jpegToKoalaFilePath)
    }

    if (errors.size > 0 || pictures.size == 0)
      false
    else
      true
  }

  def proceed: Unit =
    if (convertConfirmed(window)) {
      if (overwriteConfirmed(window)) {
        if (convertFilesInMemory) {
          showConfirmation(window)
        }
      }
    }

  def showConfirmation(window: Window) =
    showMessageDialogWrapped(
      window.peer,
      "Successfully converted %d KoalaPainter file%s.".format(fileCount, if (fileCount < 2) "" else "s"),
      "Conversion completed",
      INFORMATION_MESSAGE,
      250
    )

  def confirmConvert(window: Window) =
    showConfirmDialogWrapped(
      window.peer,
      "You are about to convert %d JPEG file%s. Do you want to proceed?".format(fileCount, if (fileCount < 2) "" else "s"),
      "Ready to convert all files",
      OK_CANCEL_OPTION
    ) match {
      case CANCEL_OPTION =>
        false
      case CLOSED_OPTION =>
        false
      case YES_OPTION =>
        true
      case x =>
        throw new RuntimeException("Unexpected answer from changes confirmation dialog: " + x)
    }

  def confirmOverwrite(window: Window) = {
    val overwrittenFiles = alreadyExistingTargetFiles
    if (overwrittenFiles.nonEmpty) {
      showConfirmDialogWrapped(
        window.peer,
        "The following files already exist and will be overwritten:\n\n%s\n...\n\nDo you want to continue and replace the existing files?".format(overwrittenFiles.take(20).mkString("\n")),
        "Target files already exist",
        OK_CANCEL_OPTION
      ) match {
        case CANCEL_OPTION =>
          false
        case CLOSED_OPTION =>
          false
        case YES_OPTION =>
          true
        case x =>
          throw new RuntimeException("Unexpected answer from changes confirmation dialog: " + x)
      }
    }
    else {
      true
    }
  }
}

object BatchConverter {

  def apply(window: Window, sourceList: FileList, targetList: FileList) =
    new BatchConverter(window, sourceList, targetList)
}
