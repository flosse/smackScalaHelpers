package org.smackscalahelpers

import org.jivesoftware._
import smack._

import smackx._
import commands._
import smackx.packet.DataForm

//import scala.collection.JavaConversions._

class ServiceAdministration (conn:XMPPConnection, host:String){

  /* PRIVATE VALS */

  private val NS      = "http://jabber.org/protocol/admin"
  private val A_JID   = "accountjid"
  private val A_JIDS  = "accountjids"
  private val EMAIL   = "email"
  private val NAME    = "given_name"
  private val SURNAME = "surname"

  private val manager = AdHocCommandManager.getAddHocCommandsManager(conn)
  private val formTypeField = new FormField("FORM_TYPE")
  formTypeField.setType("hidden")
  formTypeField.addValue(NS)

  /* PRIVATE METHODS */

  private def runOneStageCmd( cmd:String, fields:Map[String,Array[String]] ) = { 
    val c = manager.getRemoteCommand(host, NS+"#"+cmd)
    val dF = createDataForm 
    fields
      .map( x => createField(x._1,x._2) )
      .foreach( dF.addField(_) )
    c.execute
    c.complete(new Form(dF))
  }

  private def createField( name:String, values:Array[String] ) = { 
    val f = new FormField(name)
    values.foreach( f.addValue(_) )
    f
  }

  private def createDataForm = {
    val f = new DataForm("submit")
    f.addField(formTypeField)
    f
  }

  /* PUBLIC METHODS */

  /* NOTE: Some servers don't send proper forms, so we create the answer 
   * Forms manually 
   */

  def addUser( jid:String, pw:String){ addUser( jid, pw, Map()) }

  def addUser( jid:String, pw:String, properties:Map[String,String]){ 

    var data = Map(
      A_JID -> Array(jid),
      "password" -> Array(pw),
      "password-verify" -> Array(pw) )

    if (properties.contains("email"))
      data += EMAIL -> Array( properties("email") )

    if (properties.contains("name"))
      data += NAME -> Array( properties("name") ) 

    if (properties.contains("surname"))
      data += SURNAME -> Array( properties("surname") ) 

    runOneStageCmd("add-user", data)
  }

  def deleteUser( jid:String ){ deleteUser(Array(jid)) }
  
  def deleteUser( users: Array[String] ) =
    runOneStageCmd("delete-user", Map( A_JIDS -> users ))

  def disableUser( jid:String ){ disableUser(Array(jid)) }

  def disableUser( users: Array[String] ) =
    runOneStageCmd("disable-user", Map( A_JIDS -> users ))

  def reEnableUser( jid:String ){ reEnableUser(Array(jid)) }
  
  def reEnableUser( users:Array[String] ) =
    runOneStageCmd("reenable-user", Map( A_JIDS -> users))
}
