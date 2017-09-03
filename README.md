# Discord Bot
This is a fairly simple Discord bot I have been working on which aims to facilitate certain aspects of Discord and provide easy access to certain information (and of course some superfluous fun commands)


## **Current Commands:**

**- neptune.chat [Message]** --> Have a conversation with Neptune!

**- neptune.rep [+/-] @User** --> Increases or decreases the reputation of a user, and states the user's new rating.

**- neptune.rep show** --> Show information about your reputation including your rating, the number of people who rated you positively, and the number of people who rated you negatively

**- neptune.rep [Username]** --> Show similar information about another user

**- neptune.setrep [Username]** --> Set the reputation of a given user (Only players given this permission in the config.json can execute this command)

**-neptune.wipdedb** --> Completely wipe the database for reputation (similarly, only players given such permission can use this feature)

**- neptune.rep** --> Display the help for all reputation commands.

**- neptune.rep leaderboards [number]** --> The default size of the leaderboards is 10, but you can specify a length and this will show the users with the highest reputation rating.

**-neptune.poll "time duration" (QUESTION) [OPTION 1], [OPTION 2]... [OPTION X]** --> The command to be used for creating polls. It is essential that this order is following ie. the question is wrapped in brackes, and all the options are wrapped in square brackets. The poll will then be given an ID (a 3-digit number) to which it will be referred to in future commands

**-neptune.vote (ID) (Option)** --> Vote on a certain poll's ID and on a certain option in that specific poll.

**-neptune.poll info (ID)** --> Provides a message containing all the information pertaining to a selected poll. This information includes: the question, all its options, the respective votes of the options.

**-neptune.poll end (ID)** --> This command can only be executed by the original creator of the poll. This ends the poll prematurely (before the aforementioned time limit is met)

**- neptune.wiki [Article Name]** --> Returns the first paragraph of a wikipedia article in an aesthetic message embed.

**- neptune.crypto [Cryptocurrency]** --> Returns information on a cryptocurrency such as its price, market cap, 24 hour volume and circulating supply.

**- neptune.pic** --> Shows a beautiful pic of a location on earth.

**- neptune.nyan** --> Displays a loveable nyan cat :>

**- neptune.hl** --> Displays a horizontal line.

**- neptune.help** --> Show this in a private message.


## **To-be implemented commands:**
**- me.serverStatus [Minecraft Server]** --> Will return information on a minecraft server including its player count, motd and your ping to it


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
<dependency>
  <groupId>org.xerial</groupId>
  <artifactId>sqlite-jdbc</artifactId>
  <version>3.20.0</version>
</dependency>
<dependency>
  <groupId>ca.pjer</groupId>
  <artifactId>chatter-bot-api</artifactId>
  <version>1.4.7</version>
</dependency>
```

