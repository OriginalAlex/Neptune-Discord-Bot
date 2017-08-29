# Discord Bot
This is a fairly simple Discord selfbot I have been working on which aims to facilitate certain aspects of Discord and provide easy access to certain information (and of course some superfluous fun commands)


## **Current Commands:**
**- me.wiki [Article Name]** --> Returns the first paragraph of a wikipedia article in an aesthetic message embed.

**- me.crypto [Cryptocurrency]** --> Returns information on a cryptocurrency such as its price, market cap, 24 hour volume and circulating supply

**-me.plaing [Game]** --> Edits your playing game name. Names can be of non-existent games, eg: _me.playing nothing_ will set your playing game to "nothing"

**- me.pic** --> Shows a beautiful pic of a location on earth

**-me.clipboard** --> Pastes your clipboard

**- me.nyan** --> Displays a loveable nyan cat :>

**- me.hl** --> Displays a horizontal line

**-me.exit** --> Kills the process


## **To-be implemented commands:**
**- me.serverStatus [Minecraft Server]** --> Will return information on a minecraft server including its player count, motd and your ping to it

**- me.purge [Number]** --> Will delete your last n messages in channel you talked in


## **Prerequisites:**
The following are the required dependencies:
```maven
<dependency>
    <groupId>net.dv8tion</groupId>
    <artifactId>JDA</artifactId>
    <version>3.1.1_212</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.8.3</version>
</dependency>
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.1</version>
</dependency>
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.5</version>
</dependency>
```

