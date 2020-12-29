package com.github.pawelkrol.Converter

import java.awt.Toolkit

import swing.{Button, Dimension, Label, MainFrame, Publisher}
import swing.{BoxPanel, FlowPanel}
import swing.Orientation.{Horizontal, Vertical}
import swing.Swing.EmptyBorder

class TopFrame extends MainFrame {

  private val appIcon = getClass.getResource("/icons/plasmagik.png")

  private val files = new FilePanel

  private val buttons = new ButtonPanel(files.sourceListView)

  contents = new BoxPanel(Vertical) {

    border = EmptyBorder(weight = 10)

    contents += new FlowPanel(FlowPanel.Alignment.Center)(files)

    contents += new FlowPanel(FlowPanel.Alignment.Center)(buttons)
  }

  iconImage = Toolkit.getDefaultToolkit.getImage(appIcon)

  listenTo(buttons, files)

  reactions += {
    case About => {
      AboutWindow(this).open()
    }
    case AddMore => {
      FileOpen(this, files.sourceListView)
      TopFrame.announceSourceFilesUpdated
    }
    case Convert => {
      BatchConverter(this, files.sourceListView, files.targetListView).proceed
      // TopFrame.announceTargetFilesUpdated
    }
    case Exit => {
      peer.setVisible(false)
      dispose()
      System.exit(0)
    }
    case Preview => {
      KoalaPreview(this, files.sourceListView)
    }
  }

  resizable = false

  title = "JPEG to KoalaPainter"
}

object TopFrame extends Publisher {

  def announceSourceFilesUpdated: Unit = {
    publish(SourceFilesUpdated)
  }

  def announceTargetFilesUpdated: Unit = {
    publish(TargetFilesUpdated)
  }
}
