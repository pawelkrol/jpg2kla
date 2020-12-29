package com.github.pawelkrol.Converter

import java.awt.Color.GRAY
import java.awt.Dimension
import javax.swing.Box.createHorizontalStrut

import swing.{BoxPanel, Button, ListView, ScrollPane}
import swing.Orientation.{Horizontal, Vertical}
import swing.Swing.{EmptyBorder, LineBorder, TitledBorder}
import swing.event.ButtonClicked

class FilePanel extends BoxPanel(Horizontal) {

  val sourceListView = new FileList("Source files:", true)

  private val convertButton = new ConvertButton(sourceListView, ">>")

  val targetListView = new FileList("Target files:", false)

  border = EmptyBorder(top = 5, left = 5, bottom = 5, right = 5)

  contents += sourceListView

  peer.add(createHorizontalStrut(5))

  contents += convertButton

  peer.add(createHorizontalStrut(5))

  contents += targetListView

  listenTo(convertButton, targetListView)

  reactions += {
    case ButtonClicked(b) =>
      b match {
        case `convertButton` =>
          publish(Convert)
      }
  }
}
