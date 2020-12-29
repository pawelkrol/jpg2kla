package com.github.pawelkrol.Converter

import java.awt.Component
import javax.swing.JOptionPane.{showConfirmDialog, showMessageDialog}

trait MessageDialog {

  private def textWrapped(width: Int, message: String) =
    "<html><body><p style='width: %dpx;'>%s</p></body></html>".format(width, "\n".r.replaceAllIn(message, "<br/>"))

  /** Show confirm dialog with a text word-wrapped to a maximum width of `width` pixels. */
  def showConfirmDialogWrapped(parentComponent: Component, message: String, title: String, optionType: Int, width: Int = 350) =
    showConfirmDialog(parentComponent, textWrapped(width, message), title, optionType)

  /** Show message dialog with a text word-wrapped to a maximum width of `width` pixels. */
  def showMessageDialogWrapped(parentComponent: Component, message: String, title: String, messageType: Int, width: Int = 400) =
    showMessageDialog(parentComponent, textWrapped(width, message), title, messageType)
}
