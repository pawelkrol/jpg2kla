package com.github.pawelkrol.Converter

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.event.KeyEvent.VK_ESCAPE

import javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
import javax.swing.JDialog
import javax.swing.KeyStroke.getKeyStroke

trait Closeable {

  // Enable dialog close by pressing ESCAPE key:
  def enableCloseByEsc(peer: JDialog, onEsc: () => Unit): Unit = {
    peer.getRootPane.registerKeyboardAction(
      new ActionListener {
        override def actionPerformed(e: ActionEvent): Unit = {
          onEsc()
        }
      },
      getKeyStroke(VK_ESCAPE.toChar),
      WHEN_IN_FOCUSED_WINDOW
    )
  }
}
