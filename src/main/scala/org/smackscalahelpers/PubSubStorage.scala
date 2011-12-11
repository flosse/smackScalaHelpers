package org.smackscalahelpers

import org.jivesoftware._
import smack._
import smackx._
import smackx.pubsub._
import smackx.pubsub.FormType
import smackx.pubsub.LeafNode
import smackx.packet._
import smackx.packet.DataForm
import scala.collection.JavaConversions._

class StorageNode( val name:String, val leafNode:LeafNode){

  import scala.xml._

  /* PRIVATE METHODS*/

  private def updateConfig(priv:Boolean){
    val cfg = new ConfigureForm( leafNode.getNodeConfiguration.createAnswerForm )
    cfg.setPersistentItems(true)
    cfg.setDeliverPayloads(false)
    if (priv == false)
      cfg.setAccessModel(AccessModel.open)
    else
      cfg.setAccessModel(AccessModel.whitelist)
    leafNode.sendConfigurationForm(cfg)
  }

  private def setToNodeMap[T]( set:Set[T] ):Map[String,Node] =
    set.asInstanceOf[Set[Item]]
      .map( x => x.getId() -> x.toXML)
      .map( x => x._1 -> XML.loadString(x._2).asInstanceOf[Node] )
      .map( x => x._1 -> x._2.child.head )
      .toMap

  private def createItem(content:Node, id:String):Node =
    Utility.trim(<item id={id} >{ content }</item>)

  /* PUBLIC METHODS */

  def set(item:Node){ set(item, "") }

  def set(item:Node, id:String){
    leafNode.publish(new Item{
      override def toXML():String = createItem(item, id).toString
    })}

  def get():Map[String,Node] =
    setToNodeMap( leafNode.getItems().toSet )

  def get(id:String):Option[Node] = {
    val items = get(Set(id))
    if (items.contains(id))
      Some(items(id))
    else None
  }

  def get(ids:Set[String]):Map[String,Node] =
    setToNodeMap( leafNode.getItems(ids).toSet )

  def delete(id:String){ leafNode.deleteItem(id) }
  def delete(ids:Set[String]){ leafNode.deleteItem(ids) }
  def deleteAll(){ leafNode.deleteAllItems }

  def setToPrivateNode(priv:Boolean = true){ updateConfig(priv) }

}

class PubSubStorage (conn:XMPPConnection){

  private val ACCESS_MODEL  = "pubsub#access_model"
  private val PERSIST_ITEMS = "pubsub#persist_items"
  private val cfg = new ConfigureForm(FormType.submit)
  private val pubSubManager = new PubSubManager(conn)

  cfg.setPersistentItems(true)
  cfg.setDeliverPayloads(false)
  cfg.setAccessModel(AccessModel.whitelist)

  private def castNodeToStorageNode(node:Node):StorageNode =
    node match{
      case l:LeafNode => new StorageNode(node.getId,l)
      case _          => throw new Exception("could not create node")
    }

  def createNode(name:String):Option[StorageNode] = {
    try{
      Some( castNodeToStorageNode( pubSubManager.createNode(name, cfg)))
    }catch{
      case _  => None
    }
  }

  def getNode(name:String):Option[StorageNode] = {
    try{
      Some(castNodeToStorageNode(pubSubManager.getNode(name)))
    }catch{
      case _  => None
    }
  }

  def deleteNode(name:String){ pubSubManager.deleteNode(name) }

}
