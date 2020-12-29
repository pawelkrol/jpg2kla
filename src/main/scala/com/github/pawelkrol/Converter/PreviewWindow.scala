package com.github.pawelkrol.Converter

import java.awt.{Dimension, Graphics2D}
import java.awt.Color.GRAY

import org.c64.attitude.Afterimage.Colour.Palette
import org.c64.attitude.Afterimage.Mode.CBM
import org.c64.attitude.Afterimage.View.Shower

import swing.{BorderPanel, BoxPanel, FlowPanel, Panel, Window}
import swing.BorderPanel.Position.Center
import swing.FlowPanel.Alignment.Left
import swing.Orientation.{Horizontal, Vertical}
import swing.Swing.{EmptyBorder, LineBorder, TitledBorder}

class PreviewWindow(val window: Window, val picture: CBM) extends DialogWindow(window) with Shower {

  val palette = Palette("default")

  val imagePlus = create(scaleFactor = 1)

  title = "KoalaPainter"

  contents = new BoxPanel(Vertical) {
    border = EmptyBorder(top = 0, left = 0, bottom = 10, right = 0)

    contents += new FlowPanel(Left)(
      new BoxPanel(Horizontal) {

        val imagePanel = new Panel {
          preferredSize = new Dimension(640, 400)
          focusable = false

          override def paintComponent(g: Graphics2D) = {
            super.paintComponent(g)
            g.drawImage(imagePlus.getImage, 0, 0, null)
          }
        }

        contents += new BoxPanel(Vertical) {
          border = EmptyBorder(top = 5, left = 5, bottom = 0, right = 5)
          contents += new BoxPanel(Vertical) {
            border = TitledBorder(
              border = LineBorder(GRAY),
              title = " Converted picture preview "
            )
            contents += new BorderPanel {
              border = EmptyBorder(top = 7, left = 10, bottom = 10, right = 10)
              layout(imagePanel) = Center
            }
          }
        }
      }
    )

    contents += new BoxPanel(Horizontal) {
      border = EmptyBorder(weight = 5)
      contents += closeButton
    }
  }
}

object PreviewWindow {

  def apply(window: Window, picture: CBM) = new PreviewWindow(window, picture)
}
