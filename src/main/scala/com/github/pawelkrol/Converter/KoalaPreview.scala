package com.github.pawelkrol.Converter

import swing.Window

object KoalaPreview extends ConversionProcess {

  def apply(window: Window, list: FileList): Unit = {

    val jpeg = list.currentSelection

    val converter = new PicConverter(jpeg)

    // Start a separate thread to begin conversion process:
    val (errors, pictures) = convertWith(ConversionWorker(converter), window)

    if (pictures.size == 1) {
      // Preview converted KoalaPreview picture:
      PreviewWindow(window, pictures.head.picture).open()
    }
  }
}
