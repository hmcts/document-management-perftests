package uk.gov.hmcts.reform.dmstore.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.Document

object Delete {

  val delete: ChainBuilder =
    feed(Document.uploadedDocumentsFeeder)
      .exec(
        http("Delete document")
          .delete(url = "/documents/${document_id}")
          .headers(Map(
            "ServiceAuthorization" -> "Bearer ${service_token}",
            "user-id" -> "auto.test.cnp@gmail.com",
            "Content-Type" -> "application/json",
            "user-roles" -> "caseworker"
          ))
          .check(status is 200))
}
