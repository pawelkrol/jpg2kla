package com.github.pawelkrol.Converter

import java.awt.Color.GRAY
import java.awt.Cursor.{WAIT_CURSOR, getPredefinedCursor}
import java.awt.Dimension
import javax.swing.Timer
import javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE

import scala.swing.{BoxPanel, Button, Dialog, ProgressBar => SwingProgressBar, FlowPanel}
import scala.swing.FlowPanel.Alignment
import scala.swing.Orientation.{Horizontal, Vertical}
import scala.swing.Swing.{ActionListener, EmptyBorder, LineBorder, TitledBorder}
import scala.swing.event.ButtonClicked

class ProgressBar(worker: ConversionWorker) extends Dialog with Closeable {

  title = "Converting..."
  modal = true
  resizable = false

  private val progressBar = new SwingProgressBar() {
    border = EmptyBorder(top = 2, left = 5, bottom = 2, right = 5)
    cursor = getPredefinedCursor(WAIT_CURSOR)
    focusable = false
    indeterminate = true
    max = 100 * 100
    min = 0
    preferredSize = new Dimension(350, 20)
    value = 0
  }

  private val abortButton = new Button("Abort")

  private val progressBarBorder = TitledBorder(
    border = LineBorder(GRAY),
    title  = "Please wait..."
  )

  val outerPanel: BoxPanel = new BoxPanel(Vertical) {

    contents += new FlowPanel(Alignment.Center)(
      new BoxPanel(Vertical) {
        border = progressBarBorder

        contents += new BoxPanel(Horizontal) {
          border = EmptyBorder(top = 10, left = 10, bottom = 10, right = 10)

          contents += progressBar
        }
      }
    )

    contents += new FlowPanel(Alignment.Center)(
      new BoxPanel(Horizontal) {
        border = EmptyBorder(top = 5, left = 5, bottom = 5, right = 5)

        contents += abortButton
      }
    )

    listenTo(abortButton)
    reactions += {
      case ButtonClicked(b) =>
        if (b.text == "Abort") {
          abort()
        }
    }
  }

  contents = outerPanel

  val abort = () => {
    // Disable "Abort" button:
    abortButton.enabled = false
    // Set "Aborting..." message:
    progressBarBorder.setTitle("Aborting...")
    repaint()
    delayWorkerAbort
  }

  private val disappearDelayInterval = 100 // in milliseconds

  private val disappearDelayTimer = new Timer(disappearDelayInterval, ActionListener({e =>
    worker.abort
  }))

  private def delayWorkerAbort: Unit = {
    disappearDelayTimer.setRepeats(false)
    disappearDelayTimer.start
  }

  listenTo(worker)
  reactions += {
    case ConversionCompleted =>
      dispose()
    case _ =>
  }

  // React on ESCAPE key the same like on "Abort" button (by stopping verification):
  enableCloseByEsc(peer = peer, onEsc = abort)

  // Require the program to handle the operation that will happen by default when the user initiates a "close" on this frame:
  peer.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE)

  override def closeOperation(): Unit = {
    // If not aborting yet, just abort:
    if (!disappearDelayTimer.isRunning)
      abort()
  }

  def beginWorkerAndOpen: Unit = {
    worker.execute
    open()
  }
}
