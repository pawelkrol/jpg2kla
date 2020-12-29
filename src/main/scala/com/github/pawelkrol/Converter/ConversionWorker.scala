package com.github.pawelkrol.Converter

import concurrent.{Await, Future}
import concurrent.duration.Duration.Inf
import concurrent.ExecutionContext.Implicits.global

import javax.swing.SwingWorker

import org.c64.attitude.Afterimage.Mode.MultiColour

import swing.Publisher

class ConversionWorker(pictures: Seq[PicConverter]) extends SwingWorker[Seq[ConversionResult], Boolean] with Publisher {

  @volatile var aborted = false

  def abort: Unit = {
    aborted = true
    publish(ConversionCompleted)
  }

  private def isAborted: () => Boolean = () => aborted

  def doInBackground = {

    val futureConverters: List[Future[ConversionResult]] =
      pictures.map(pic => Future(pic.converted(isAborted))).toList

    val future: Future[List[ConversionResult]] = Future.sequence(futureConverters)

    val result = Await.result(future, Inf)

    publish(ConversionCompleted)

    result
  }
}

object ConversionWorker {

  def apply(picture: PicConverter) = new ConversionWorker(Seq(picture))

  def apply(pictures: Seq[PicConverter]) = new ConversionWorker(pictures)
}
