package com.github.pawelkrol.Converter

import org.c64.attitude.Afterimage.Mode.MultiColour

sealed trait ConversionResult {

  val jpeg: FilePath
}

case class ConversionFailed(
  val jpeg: FilePath,
  val errorMsg: String
) extends ConversionResult

case class ConversionSuccessful(
  val jpeg: FilePath,
  val picture: MultiColour
) extends ConversionResult
