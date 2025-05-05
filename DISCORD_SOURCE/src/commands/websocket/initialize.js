import { SlashCommandBuilder } from 'discord.js';

import { output } from '../../shared.js';

export const data = new SlashCommandBuilder()
  .setName('initialize')
  .setDescription('Initializes websocket connection to channel this was called in!')



export async function execute(interaction) {
  console.log(interaction)

  await interaction.reply('Initializing websocket server to this channel...');
  output.guild = await interaction.member.guild
  output.channel = await interaction.member.guild.channels.cache.get(interaction.channelId)

}