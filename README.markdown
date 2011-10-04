# smackScalaHelpers

This repository holds a collection of little helpers for the
[XMPP](http://xmpp.org/) [Smack](http://www.igniterealtime.org/projects/smack/)
library. They are written in [Scala](http://www.scala-lang.org/).

## Helpers

### XEP-0133 Service Administration

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
