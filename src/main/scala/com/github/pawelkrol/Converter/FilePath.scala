package com.github.pawelkrol.Converter

case class FilePath(
  name: String,
  path: String
) {

  def jpegToKoalaFilePath = {
    val koalaName = FilePath.replaceExtension(name, "jpg", "kla")
    val koalaPath = FilePath.replaceExtension(path, "jpg", "kla")
    FilePath(koalaName, koalaPath)
  }
}

object FilePath {

  def replaceExtension(file: String, oldExt: String, newExt: String) =
    "(?i)\\.%s$".format(oldExt).r.replaceAllIn(file, ".%s".format(newExt))
}
