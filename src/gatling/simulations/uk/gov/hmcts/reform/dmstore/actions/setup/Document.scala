package uk.gov.hmcts.reform.dmstore.actions.setup

import scala.collection.mutable.ListBuffer
import scala.util.Random

object Document {

  val documentsPath = "documents/"

  val documents = List(
    "1MB.pdf", "1MB-b.pdf", "1MB-c.pdf"
  )

  val documentsFeeder = Iterator.continually(Map("document_file" -> documents(Random.nextInt(documents.length))))


  var uploadedDocuments = new ListBuffer[String]()

  val uploadedDocumentsFeeder = Iterator.continually(Map("document_id" -> uploadedDocuments(Random.nextInt(uploadedDocuments.length))))

}
