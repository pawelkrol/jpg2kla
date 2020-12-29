package com.github.pawelkrol.Converter

import java.awt.Component
import javax.swing.JOptionPane.ERROR_MESSAGE

import swing.Window

trait SafeExecutor extends MessageDialog {

  def showSomethingWentWrong(component: Component, message: String): Unit = {
    showMessageDialogWrapped(
      component,
      message,
      "Something went wrong",
      ERROR_MESSAGE,
      // Word wrap text to a maximum width of 350 pixels:
      350
    )
  }

  def methodExecuteSafe(window: Window, method: (Window) => Boolean) =
    try {
      method(window)
    }
    catch {
      case _ : Throwable =>
        showSomethingWentWrong(window.peer, "Program failed to process requested operation. Please try again or file a bug report.")
        false
    }
}
