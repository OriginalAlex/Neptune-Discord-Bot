# Discord Bot
This is a fairly simple Discord bot I have been working on which aims to facilitate certain aspects of Discord and provide easy access to certain information (and of course some superfluous fun commands)


## **Current Commands:**

**-neptune.rep [+/-] @User** --> Increases or decreases the reputation of a user, and states the user's new rating.

**-neptune.rep show** --> Show information about your reputation including your rating, the number of people who rated you positively, and the number of people who rated you negatively

**-neptune.rep leaderboards <number>** --> The default size of the leaderboards is 10, but you can specify a length and this will show the users with the highest reputation rating.

**- neptune.wiki [Article Name]** --> Returns the first paragraph of a wikipedia article in an aesthetic message embed.

**- neptune.crypto [Cryptocurrency]** --> Returns information on a cryptocurrency such as its price, market cap, 24 hour volume and circulating supply.

**- neptune.pic** --> Shows a beautiful pic of a location on earth.

**- neptune.nyan** --> Displays a loveable nyan cat :>

**- neptune.hl** --> Displays a horizontal line.


## **To-be implemented commands:**
**- me.serverStatus [Minecraft Server]** --> Will return information on a minecraft server including its player count, motd and your ping to it

**-me.strawpoll [arguments]** --> This will begin a strawpoll, which the creator can then manipulate, or perhaps only people in the discord with certain permissions will be able to utilize this feature.


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

