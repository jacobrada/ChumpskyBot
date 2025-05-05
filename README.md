# ChumpskyBot: Sync Minecraft and Discord chat!

> NOTE: This package is still an early phase of development. There may be issues!

## Overview

ChumpskyBot will sync your Minecraft Server chat and your Discord Server chat! Meaning, any messages sent in your Minecraft Server will appear in a designated Discord Channel, and any messages sent in that Discord Channel will appear in-game!

## Setup

### Discord Bot
1. Install Node.js
2. Open a command terminal in the directory with package.json and run `npm install`
3. Through the Discord Developer Portal, create a Discord Bot with necesary privileges for sending messages in a channel and slash commands
4. Modify .env and the src/deploy-commands.js to use your bot's token and client ID.
5. Run src/deploy-commands.js
6. Run src/main.js to activate the bot and the communication server
    1. You may need to do some port forwarding stuff if you are running the Minecaft server from another location
    2. The default port this uses is 39393 and can be changed in src/main.js
7. Add the bot to your server and use /initialize in the channel you wish to use

### Minecraft
This currently only work for Minecraft (1.20.1) Forge (47.4).
The mod is also server sided, so as long as your server is running Forge with this mod, it will work, even compatible with vanilla clients.
1. Set up Minecraft Forge server (plenty of tutorials online)
2. Add the mod .jar file from RELEASES into the server's mod folder
3. Start your server and run the following command from the server console or a client with operator privileges
`/chom:connect <ip> <port>`
Where the IP and port are the location of where you are running the Discord bot. 
You can disconect with `/chom:disconnect`
## TODOO
This functions for my own personal purposes, but it can be improved upon drastically should I decide to make a more polished release & publish it on proper platforms.
Here are some possible additions incase I do plan to update it:
- Easier setup, cleaner config etc...
- Better communication method than raw TCP port
