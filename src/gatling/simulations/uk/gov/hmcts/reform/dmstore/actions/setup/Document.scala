package uk.gov.hmcts.reform.dmstore.actions.setup

import scala.collection.mutable.ListBuffer
import scala.util.Random

object Document {

  val documentsPath = "documents/"

  val documents = List(
    "100MB.pdf", "1MB-b.pdf",	"2MB-d.pdf", "2MB-h.pdf",	"50MB.pdf", "Dummy.txt",
    "1MB-c.pdf", "240KB.txt", "2MB-e.pdf",	"2MB-i.pdf", "Dummy2.txt",
    "1MB.pdf", "2MB-b.pdf", "2MB-f.pdf", "2MB-j.pdf",	"5MB.pdf", "Dummy3.txt",
    "10MB.pdf", "20MB.pdf", "2MB-c.pdf", "2MB-g.pdf", "2MB.pdf", "90MB.pdf"
  )

  val extToContentType = Map(
    "pdf" -> "application/pdf",
    "txt" -> "text/plain",
  )

  val documentsFeeder = Iterator.continually(Map("document_file" -> documents(Random.nextInt(documents.length))))


  var uploadedDocuments = new ListBuffer[String]()

  val uploadedDocumentsFeeder = Iterator.continually(Map("document_id" -> uploadedDocuments(Random.nextInt(uploadedDocuments.length))))


  def contentType(fileName: String): String = {
    extToContentType(fileName.substring(fileName.length - 3))
  }

}
