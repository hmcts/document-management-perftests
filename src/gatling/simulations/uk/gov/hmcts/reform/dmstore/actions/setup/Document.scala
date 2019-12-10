package uk.gov.hmcts.reform.dmstore.actions.setup

import scala.util.Random

object Document {

  val documents = List(
    "1MB.pdf", "1MB-b.pdf", "1MB-c.pdf"
  )

  val documentsFeeder = Iterator.continually(Map("document_file" -> "documents/".concat(documents(Random.nextInt(documents.length)))))

  def filename(s: String): String = {
    s.substring(10)
  }

}
