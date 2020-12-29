package com.github.pawelkrol.Converter

import collection.mutable.{HashSet, Set}

import java.awt.Color.GRAY
import java.awt.Dimension
import java.awt.event.KeyEvent.{VK_BACK_SPACE, VK_DELETE}
import javax.swing.Box.createHorizontalStrut
import javax.swing.{DefaultListModel, JList}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import swing.{BoxPanel, ListView, ScrollPane}
import swing.Orientation.{Horizontal, Vertical}
import swing.Swing.{EmptyBorder, LineBorder, TitledBorder}
import swing.event.KeyTyped

class FileList(
  headline: String,
  isEnabled: Boolean
) extends BoxPanel(Vertical) {

  val data: Set[FilePath] = HashSet()

  private def redraw: Unit = {
    this ++= Seq()
    // Disable "Convert" buttons:
    TopFrame.announceSourceFilesUpdated
  }

  def clear: Unit = {
    data.clear()
    redraw
  }

  def removeItems(itemsToRemove: Seq[FilePath]): Unit = {
    data.filterInPlace(!itemsToRemove.contains(_))
    redraw
  }

  def removeResults(itemsToRemove: Seq[ConversionResult]): Unit = {
    val removedItems = itemsToRemove.map(_.jpeg)
    removeItems(removedItems)
  }

  private def updatedData = data.map(_.name).toSeq.sorted

  border = TitledBorder(
    border = EmptyBorder,
    title = headline
  )

  private val list = new ListView[String] {
    enabled = isEnabled
    listData = updatedData

    listenTo(keys)

    reactions += {
      case event: KeyTyped => {
        if (List(VK_BACK_SPACE, VK_DELETE).contains(event.char))
          if (currentSelectionIndex.nonEmpty)
            removeItems(Seq(currentSelection))
        event.consume()
      }
    }
  }

  var currentSelectionIndex: Option[Int] = None

  def currentSelection: FilePath = data.find(_.name == list.listData(currentSelectionIndex.get)).get

  // Ensure only a single element in the list view may be selected:
  val listSelectionListener = new ListSelectionListener {
    def valueChanged(listSelectionEvent: ListSelectionEvent): Unit = {
      if (!listSelectionEvent.getValueIsAdjusting) {
        val selections = listSelectionEvent.getSource.asInstanceOf[JList[String]].getSelectedIndices
        selections.length match {
          case 0 =>
            currentSelectionIndex = None
          case 1 =>
            currentSelectionIndex = Some(selections(0))
          case 2 =>
            val selectionIndex = if (currentSelectionIndex.get == selections(0))
              selections(1)
            else
              selections(0)
            list.peer.clearSelection
            currentSelectionIndex = Some(selectionIndex)
            list.peer.setSelectedIndex(currentSelectionIndex.get)
          case _ =>
            throw new RuntimeException("Something went wrong...")
        }
        publish(SourceFileSelected)
      }
    }
  }
  list.peer.addListSelectionListener(listSelectionListener)

  contents += new BoxPanel(Vertical) {

    border = EmptyBorder(top = 2, left = 2, bottom = 2, right = 2)

    contents += new ScrollPane(list)

    preferredSize = new Dimension(232, 250)
  }

  def ++=(xs: IterableOnce[FilePath]): Unit = {
    data ++= xs
    list.listData = updatedData
    list.repaint()
  }
}
