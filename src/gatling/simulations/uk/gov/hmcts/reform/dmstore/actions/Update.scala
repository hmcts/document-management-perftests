package uk.gov.hmcts.reform.dmstore.actions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.Document

object Update {

  val update: ChainBuilder =
    feed(Document.uploadedDocumentsFeeder)
      .exec(
        http("Update document")
          .patch(url = "/documents/${document_id}")
          .headers(Map(
            "ServiceAuthorization" -> "Bearer ${service_token}",
            "user-id" -> "auto.test.cnp@gmail.com",
          ))
          .body(StringBody(session =>
            s"""
               |{
               |  "ttl": "${LocalDate.now().plusWeeks(1).format(DateTimeFormatter.ISO_LOCAL_DATE)}"
               |}
            """.stripMargin))
          .check(status is 200))
}
