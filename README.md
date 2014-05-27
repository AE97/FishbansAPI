# Fishbans API

This is a Fishbans API library based on the original project located at https://github.com/deathmarine/FishBansAPI/

This uses the Fishbans API located at http://fishbans.com/docs.php

# Maven 

Repository:
```xml
 <repository>
    <id>ae97</id>
    <url>http://repo.ae97.net/</url>
 </repository>
```

Artifact:
```xml
  <dependency>
    <artifactId>FishbansAPI</artifactId>
    <groupId>net.ae97</groupId>
    <version>1.0</version>
  </dependency>
```

The following classifiers exist to provide multiple versions for libraries:
- bare (no libraries shaded in, only FishbansAPI code)
- bukkit (built against Bukkit, relocates the gson library for use for Bukkit)
- forge (built against Forge)
- include-libs (includes the gson library for redistribution)

To use those classifiers, add <classifier> as part of the <dependency> bracket
```xml
  <dependency>
    <artifactId>FishbansAPI</artifactId>
    <groupId>net.ae97</groupId>
    <version>1.0</version>
    <classifier>bukkit</classifier>
  </dependency>
```

# Javadocs 

You may view the javadocs at http://ae97.github.io/FishbansAPI/apidocs/

# Example Usage

```java
//To get a list of bans for 'Bob'
List<Ban> bobBans = Fishbans.getBans("Bob");

//To get only MCBouncer bans for 'Bob'
List<Ban> bans = Fishbans.getBans("Bob", BanService.MCBOUNCER);

//To get the collection of sorted data, which includes UUID, name, and all bans
FishbanPlayer player = Fishbans.getFishbanPlayer("Bob");
```

# License and Usage

Copyright (C) 2014 AE97

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
