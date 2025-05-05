/**
 * INSTALL USING THIS LINK
 *
 *
 */

import dotenv from 'dotenv'
dotenv.config()

import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url';
import { dirname } from 'path';
import { output } from './shared.js';




import { Client, Collection, Events, GatewayIntentBits } from 'discord.js';

const client = new Client({
    intents: [
      GatewayIntentBits.Guilds,
      GatewayIntentBits.GuildMessages,
      GatewayIntentBits.MessageContent
    ]
});


client.once(Events.ClientReady, readyClient => {
  console.log(`Ready! Logged in as ${readyClient.user.tag}`);
});





client.commands = new Collection()


const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const foldersPath = path.join(__dirname, 'commands');
const commandFolders = fs.readdirSync(foldersPath);

for (const folder of commandFolders) {
  const commandsPath = path.join(foldersPath, folder);
  const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));
  for (const file of commandFiles) {
    const filePath = "file://" + path.join(commandsPath, file);
    const command = await import(filePath);
    // Set a new item in the Collection with the key as the command name and the value as the exported module
    if ('data' in command && 'execute' in command) {
      client.commands.set(command.data.name, command);
    } else {
      console.log(`[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`);
    }
  }
}


client.on(Events.InteractionCreate, async interaction => {
  if (interaction.isChatInputCommand()) {
    const command = interaction.client.commands.get(interaction.commandName);

    if (!command) {
      console.error(`No command matching ${interaction.commandName} was found.`);
      return;
    }

    try {
      await command.execute(interaction);
    } catch (error) {
      console.error(error);
      if (interaction.replied || interaction.deferred) {
        await interaction.followUp({ content: 'There was an error while executing this command!', flags: MessageFlags.Ephemeral });
      } else {
        await interaction.reply({ content: 'There was an error while executing this command!', flags: MessageFlags.Ephemeral });
      }
    }
  }
})

client.login(process.env.DISCORD_TOKEN);

process.on('SIGINT', stopBot)
process.on('SIGABRT', stopBot)
process.on('beforeExit', stopBot)

function stopBot() {
  console.log ("Logging out...")
  client.destroy()
  server.close();
}

//        SOCKET STUFF    //

import net from 'net'

const server = net.createServer((socket) => {
  console.log('Connected!')
  socket.write("Connected Successfully! \n")

  socket.on('end', () => {
    console.log('Disconnected!')
  })

  let buffer = ''
  socket.on('data', (chunk) => {
    buffer += chunk.toString()

    let parts = buffer.toString().split('\n')
    for (let i = 0; i < parts.length - 1; i++) {
      console.log("Received: " + parts[i])
      if (output.channel) {
        output.channel.send({
          content: `\`MC | ${parts[i].toString().replaceAll(String.fromCharCode(0), '')}\``,
        });
      }
    }

    buffer = parts[parts.length - 1];
  })

  socket.on('error', (err) => {
    console.error(err)
  })

  client.on(Events.MessageCreate, message => {
    console.log(`User ${message.author.username}: ${message}`);
    if (!message?.author.bot) {
      socket.write(`[${message.author.username}] ${message.content}` + "\n")
    }
  })
})
server.listen(39393)