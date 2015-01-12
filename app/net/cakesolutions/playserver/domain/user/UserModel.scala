package net.cakesolutions.playserver.domain.user

import play.api.libs.json._

/**
  */
case class UserModel(age: Int,
                     name: String
                     //                      address: Option[Address]
                      )


object UserModel {

  /**
   * Format for the message.
   *
   * Used both by JSON library and reactive mongo to serialise/deserialise a message.
   */
  implicit val messageFormat = Json.format[UserModel]

}

case class Address(street: String, postCode: String)