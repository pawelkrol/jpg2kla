package com.github.pawelkrol.Converter

import swing.{BoxPanel, Component, FlowPanel, ScrollPane, Window}
import swing.FlowPanel.Alignment.Left
import swing.Orientation.{Horizontal, Vertical}
import swing.Swing.EmptyBorder

class AboutWindow(
  window: Window
) extends DialogWindow(window) {

  private val width = 350

  private val details = "<strong>jpg2kla</strong> is a simple graphic format converter utility wrapped by an uncomplicated user interface. It enables batch conversion of multiple JPEG files into KoalaPainter format."

  private val version = "0.01 (2021-01-01)"

  private val copyrightString = "Copyright © 2021 Paweł Król"

  private val infoText = """<html>
    <p style="width: %dpx;">
      %s
      <br/>
      <br/>
    </p>
    <p>
      Version: %s
      <br/>
      <br/>
    </p>
    <center>
      %s
    </center>
  </html>""".format(width, details, version, copyrightString)

  val textArea: Component = new TextLabel(infoText)

  title = "About"

  contents = new BoxPanel(Vertical) {
    border = EmptyBorder(top = 0, left = 0, bottom = 10, right = 0)

    contents += new FlowPanel(Left)(
      new BoxPanel(Horizontal) {
        contents += new ScrollPane(textArea) {
          border = EmptyBorder(top = 0, left = 0, bottom = 0, right = 0)
        }
      }
    )

    contents += new BoxPanel(Horizontal) {
      border = EmptyBorder(weight = 5)
      contents += closeButton
    }
  }
}

object AboutWindow {

  def apply(window: Window) = new AboutWindow(window)
}
