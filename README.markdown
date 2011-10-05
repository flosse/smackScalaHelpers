# smackScalaHelpers

This repository holds a collection of little helpers for the
[XMPP](http://xmpp.org/) [Smack](http://www.igniterealtime.org/projects/smack/)
library. They are written in [Scala](http://www.scala-lang.org/).

Most of the helpers are just wrappers for an easier (but also less powerful)
API of existing Smack functionalities.

WARNING: They might be incomplete and unstable so be careful ;-)

## Helpers

### XEP-0133 - Service Administration

[XEP-0133](http://xmpp.org/extensions/xep-0133.html) is a recommendation for
best practices for service-level administration of servers and components
using Ad-Hoc Commands.

#### Implemented use cases

- Add User
- Delete User
- Disable User
- Re-Enable User

#### Usage

```scala

val serviceAdmin = new ServiceAdministration( connection, "example.org" )

serviceAdmin.addUser("new@example.org", "secret")
serviceAdmin.deleteUser("old@example.org")

serviceAdmin.disableUser("badguy@example.org")
serviceAdmin.reEnableUser("goodguy@example.org")

```

### XEP-0222/XEP-0223 - Persistent Storage of Public/Private Data via PubSub

[XEP-0222](http://xmpp.org/extensions/xep-0222.html) and
[XEP-0223](http://xmpp.org/extensions/xep-0223.html)
define best practices for using the XMPP publish-subscribe
extension to persistently store semi-public/private data objects.

#### Usage

```scala

val db = new PubSubStorage( connection )
val storageNode = db.createNode("myNodeId")

val myObj = storageNode.get("myItemId") match {

  case o:Some[MyObjClass] => MyObjClass.fromXmlNode(o)
  case None               => throw new Exception("could not load object")
}

myObj.changeSomething()

storageNode.set(myObj.toXmlNode, myObj.getId)

```
