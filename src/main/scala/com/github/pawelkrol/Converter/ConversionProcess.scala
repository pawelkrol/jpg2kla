package com.github.pawelkrol.Converter

import org.c64.attitude.Afterimage.Mode.MultiColour

import swing.Window

trait ConversionProcess extends SafeExecutor {

  private def showProgressBar(worker: ConversionWorker, window: Window): Unit = {

    // Build progress bar in a modal dialog with "window" component as parent & currently selected JPEG files to convert:
    val progressBar = new ProgressBar(worker)
    progressBar.setLocationRelativeTo(window)

    // Initialize progress bar and open a modal dialog previewing computation's progress:
    progressBar.beginWorkerAndOpen
  }

  def convertWith(worker: ConversionWorker, window: Window): Tuple2[Seq[ConversionFailed], Seq[ConversionSuccessful]] = {

    showProgressBar(worker, window)

    // Only return the final result if conversion process has not been aborted by user:
    val result = if (worker.aborted) None else Some(worker.get)

    val (errors: Seq[ConversionFailed], pictures: Seq[ConversionSuccessful]) =
      result match {
        case Some(conversionResult) =>
          conversionResult.partition(_.isInstanceOf[ConversionFailed])
        case None =>
          (Seq(), Seq())
      }

    if (errors.size > 0) {
      val errorMsg = errors.map(_.errorMsg).take(20).mkString("\n")
      showSomethingWentWrong(window.peer, "Conversion process failed with the following error(s):\n\n%s\n...".format(errorMsg))
    }

    (errors, pictures)
  }
}
