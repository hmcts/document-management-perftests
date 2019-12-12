package uk.gov.hmcts.reform.dmstore.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.Document

object Download {

  val download: ChainBuilder =
    feed(Document.uploadedDocumentsFeeder)
      .randomSwitch( // roughly 7 times binary file and once metadata out of 8 runs
        88.0 -> exec(session => {
          session.set("download_path", "/binary").set("download_type", "binary")}),
        12.0 -> exec(session => {
          session.set("download_path", "").set("download_type", "metadata")})
      )
      .exec(
        http("Download document ${download_type}")
          .get(url = "/documents/${document_id}${download_path}")
          .headers(Map(
            "ServiceAuthorization" -> "Bearer ${service_token}",
            "user-id" -> "auto.test.cnp@gmail.com",
            "Content-Type" -> "application/json",
            "user-roles" -> "caseworker"
          ))
          .check(status is 200))
}
