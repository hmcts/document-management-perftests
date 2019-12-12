package uk.gov.hmcts.reform.dmstore.actions.setup

import scala.util.Random

object User {

  val emailFeeder = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + ".cnp@gmail.com")))

}
