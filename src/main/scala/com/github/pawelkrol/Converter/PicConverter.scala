package com.github.pawelkrol.Converter

import org.c64.attitude.Afterimage.Colour.Palette
import org.c64.attitude.Afterimage.File.File
import org.c64.attitude.Afterimage.File.Import
import org.c64.attitude.Afterimage.Format.KoalaPainter
import org.c64.attitude.Afterimage.Mode

class PicConverter(jpeg: FilePath, saveAsFile: Boolean = false) {

  private def tryConvert(isAborted: () => Boolean, backgroundColour: Int): ConversionResult = {

    val fileName = FilePath.replaceExtension(jpeg.name, "jpg", "kla")

    try {
      if (isAborted())
        return ConversionFailed(jpeg, fileName + ": Aborted by user")

      val picture = File.convert(jpeg.path, Import.MultiColour(backgroundColour.toByte, Palette("default"))).asInstanceOf[Mode.MultiColour]

      if (isAborted())
        return ConversionFailed(jpeg, fileName + ": Aborted by user")

      ConversionSuccessful(jpeg, picture)
    }
    catch {
      case e: RuntimeException =>
        ConversionFailed(jpeg, fileName + ": " + e.getMessage)
    }
  }

  def converted(isAborted: () => Boolean, backgroundColour: Int = 0x00) = {

    val result = tryConvert(isAborted, backgroundColour)

    result match {
      case ConversionSuccessful(_, picture) =>
        // Save converted file on disk:
        if (saveAsFile)
          KoalaPainter(picture).save(name = jpeg.jpegToKoalaFilePath.path, overwriteIfExists = true)
      case ConversionFailed(_, _) =>
    }

    result
  }
}
