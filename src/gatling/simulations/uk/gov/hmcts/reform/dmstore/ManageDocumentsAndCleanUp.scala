package uk.gov.hmcts.reform.draftstore

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.LeaseServiceToken.leaseServiceToken
import uk.gov.hmcts.reform.draftstore.actions.Upload

import scala.concurrent.duration._

class ManageDocumentsAndCleanUp extends Simulation {

  val config = ConfigFactory.load()

  private val testUsers = config.getInt("params.testUsers")
  private val testRampUpSecs = config.getInt("params.testRampUpSecs")
  private val testCleanUpDelaySecs = config.getInt("params.testCleanUpDelaySecs")


  val httpProtocol =
    http
      .baseUrl(config.getString("baseUrl"))
      .contentTypeHeader("application/json")

  val uploadAndDownloadDocuments =
    scenario("Upload documents")
      .exec(leaseServiceToken)
      .during(2.minute)(
        exec(
          Upload.create,
          pause(2.seconds, 5.seconds),
        )
      )


  val documentsAndCleanUp =
    scenario("Use draft store and then clean up")
        .exec(uploadAndDownloadDocuments)

  setUp(
    // Load test over 1 hour - settings
    documentsAndCleanUp.inject(rampUsers(testUsers).during(testRampUpSecs.seconds))
  ).protocols(httpProtocol)

}
