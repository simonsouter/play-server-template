package net.cakesolutions.playserver.scalatest

import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.play.OneAppPerTest

/**
 * Created by simonsouter on 24/12/14.
 */
abstract class UnitSpec extends FlatSpec with Matchers with OneAppPerTest
