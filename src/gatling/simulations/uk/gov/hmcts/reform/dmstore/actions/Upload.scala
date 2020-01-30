package uk.gov.hmcts.reform.dmstore.actions

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.Document

object Upload {

  val config = ConfigFactory.load()

  private val testStandardToLargeRatio = config.getInt("params.testStandardToLargeRatio")
  private val standardProbability = 100.0 / (testStandardToLargeRatio + 1) * testStandardToLargeRatio
  private val largeProbability = 100.00 - standardProbability


  val upload: ChainBuilder =
    randomSwitch(
      standardProbability -> feed(Document.documentsFeeder),
      largeProbability -> feed(Document.largeDocumentsFeeder),
    )
    .exec(
      http("Upload document")
        .post(url = "/documents")
        .headers(Map(
          "ServiceAuthorization" -> "Bearer ${service_token}",
          "user-id" -> "auto.test.cnp@gmail.com",
        ))
        .bodyPart(
          RawFileBodyPart("files", Document.documentsPath.concat("${document_file}"))
            .contentType(session => session("document_file").validate[String].map(s => Document.contentType(s)))
            .fileName("${document_file}")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .check(status is 200, jsonPath("$._embedded.documents[0]._links.self.href").saveAs("fileUrl")))
    .exec(session => {
      val fileUrl = session("fileUrl").as[String]
      val fileId = fileUrl.substring(fileUrl.lastIndexOf('/') + 1)
      Document.uploadedDocuments += fileId
      println(Document.uploadedDocuments)
      session })

}
