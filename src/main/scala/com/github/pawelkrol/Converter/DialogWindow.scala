package com.github.pawelkrol.Converter

import java.awt.Font.{SANS_SERIF, getFont}

import swing.{Button, Dialog, Window}
import swing.event.ButtonClicked

class DialogWindow(window: Window) extends Dialog with Closeable {

  font = getFont(SANS_SERIF)

  modal = true

  resizable = false

  val closeButton = new Button("Close")

  listenTo(closeButton)

  private val destroy = () => {
    closeOperation()
    dispose()
  }

  reactions += {
    case ButtonClicked(closeButton) =>
      destroy()
  }

  // Enable dialog close by pressing ESCAPE key:
  enableCloseByEsc(peer = peer, onEsc = destroy)

  // Place the window in the center of the screen
  // centerOnScreen

  setLocationRelativeTo(window)

  closeButton.requestFocusInWindow()
}
