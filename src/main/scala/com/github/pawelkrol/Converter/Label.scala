package com.github.pawelkrol.Converter

import java.awt.{Cursor, Font}
import java.awt.Cursor.DEFAULT_CURSOR
import java.awt.Font.{BOLD, SANS_SERIF}

import scala.swing.Label
import scala.swing.Swing.EmptyBorder

class TextLabel(label: String) extends Label {

  border = EmptyBorder(weight = 5)

  cursor = new Cursor(DEFAULT_CURSOR)

  enabled = true

  font = new Font(SANS_SERIF, BOLD, 12)

  focusable = true

  opaque = false

  text = label
}

object TextLabel {

  def apply(label: String) = new TextLabel(label)
}
