package uk.gov.hmcts.reform.draftstore.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.Document

object Upload {

  val create: ChainBuilder =
    feed(Document.documentsFeeder)
      .exec(
        http("Upload document")
          .post(url = "/documents")
          .headers(Map(
            "ServiceAuthorization" -> "Bearer ${service_token}",
            "user-id" -> "auto.test.cnp@gmail.com",
          ))
          .bodyPart(
            RawFileBodyPart("files", "${document_file}")
              .contentType("application/pdf")
              .fileName(Document.filename("${document_file}"))).asMultipartForm
          .formParam("classification", "PUBLIC")
          .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId")))

}
