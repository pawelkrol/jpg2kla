package com.github.pawelkrol.Converter

import javax.swing.Box.createHorizontalStrut

import swing.{BoxPanel, Button}
import swing.Orientation.Horizontal
import swing.Swing.EmptyBorder
import swing.event.ButtonClicked

class ButtonPanel(sourceListView: FileList) extends BoxPanel(Horizontal) {

  private val addMoreButton = new Button("Add more...") {
    enabled = true
  }

  private val convertButton = new ConvertButton(sourceListView, "Convert")

  private val previewButton = new PreviewButton(sourceListView, "Preview")

  private val aboutButton = new Button("About") {
    enabled = true
  }

  private val exitButton = new Button("Exit") {
    enabled = true
  }

  border = EmptyBorder(top = 5, left = 5, bottom = 5, right = 5)

  contents += addMoreButton

  peer.add(createHorizontalStrut(25))

  contents += convertButton

  peer.add(createHorizontalStrut(25))

  contents += previewButton

  peer.add(createHorizontalStrut(25))

  contents += aboutButton

  peer.add(createHorizontalStrut(25))

  contents += exitButton

  listenTo(aboutButton, addMoreButton, convertButton, previewButton, exitButton)

  reactions += {
    case ButtonClicked(b) =>
      b match {
        case `aboutButton` =>
          publish(About)
        case `addMoreButton` =>
          publish(AddMore)
        case `convertButton` =>
          publish(Convert)
        case `exitButton` =>
          publish(Exit)
        case `previewButton` =>
          publish(Preview)
      }
  }

  addMoreButton.requestFocusInWindow()
}
