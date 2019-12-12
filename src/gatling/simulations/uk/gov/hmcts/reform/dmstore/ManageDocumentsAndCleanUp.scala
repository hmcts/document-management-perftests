package uk.gov.hmcts.reform.dmstore

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.dmstore.actions.setup.LeaseServiceToken.leaseServiceToken
import uk.gov.hmcts.reform.dmstore.actions.{Download, Update, Upload}

import scala.concurrent.duration._

class ManageDocumentsAndCleanUp extends Simulation {

  val config = ConfigFactory.load()

  private val testUsers = config.getInt("params.testUsers")
  private val testRampUpSecs = config.getInt("params.testRampUpSecs")
  private val testBinaryToMetadataRatio = config.getInt("params.testBinaryToMetadataRatio")
  private val testCleanUpDelaySecs = config.getInt("params.testCleanUpDelaySecs")


  val httpProtocol =
    http
      .baseUrl(config.getString("baseUrl"))
      .contentTypeHeader("application/json")

  val uploadAndDownloadDocuments =
    scenario("Upload documents")
      .exec(leaseServiceToken)
      .exec(
        Upload.upload,
        pause(2.seconds, 5.seconds),
      )
      .repeat(testBinaryToMetadataRatio, "n")(
        exec(
          Download.download,
          pause(2.seconds, 5.seconds),
        )
      )


  val documentsAndCleanUp =
    scenario("Use document store and then clean up")
        .exec(uploadAndDownloadDocuments)

  setUp(
    // Load test over x hours - settings
    documentsAndCleanUp.inject(rampUsers(testUsers).during(testRampUpSecs.seconds))
  ).protocols(httpProtocol)

}
