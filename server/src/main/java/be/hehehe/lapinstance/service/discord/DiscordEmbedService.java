package be.hehehe.lapinstance.service.discord;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.CharacterRole;
import be.hehehe.lapinstance.model.CharacterSpec;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

@Service
@RequiredArgsConstructor
class DiscordEmbedService {

	private final DiscordEmoteService discordEmoteService;

	public MessageEmbed buildEmbed(Raid raid, List<RaidSubscription> subscriptions, String raidUrl) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(raid.getRaidType().getLongName(), raidUrl);
		eb.setColor(Color.BLACK);

		String desc = raid.getFormattedDate();
		if (!Strings.isNullOrEmpty(raid.getComment())) {
			desc += System.lineSeparator() + raid.getComment();
		}
		eb.setDescription(desc);

		buildSubscriptionFields(eb, subscriptions);

		return eb.build();
	}

	private void buildSubscriptionFields(EmbedBuilder eb, List<RaidSubscription> subscriptions) {

		eb.addField(buildParticipantCountField(subscriptions));

		eb.addField(buildRoleCountField(subscriptions, CharacterRole.DPS_CAC));
		eb.addField(buildRoleCountField(subscriptions, CharacterRole.DPS_RANGED));
		eb.addField(buildRoleCountField(subscriptions, CharacterRole.HEAL));

		eb.addField(buildTankField(subscriptions));
		eb.addField(buildClassField(CharacterClass.HUNTER, subscriptions));
		eb.addField(buildClassField(CharacterClass.PRIEST, subscriptions));
		eb.addField(buildClassField(CharacterClass.WARRIOR, subscriptions));
		eb.addField(buildClassField(CharacterClass.MAGE, subscriptions));
		eb.addField(buildClassField(CharacterClass.PALADIN, subscriptions));
		eb.addField(buildClassField(CharacterClass.ROGUE, subscriptions));
		eb.addField(buildClassField(CharacterClass.WARLOCK, subscriptions));
		eb.addField(buildClassField(CharacterClass.DRUID, subscriptions));

		eb.addField(buildBlankLineField());

		eb.addField(buildResponseField(RaidSubscriptionResponse.LATE, subscriptions));
		eb.addField(buildResponseField(RaidSubscriptionResponse.BENCH, subscriptions));
		eb.addField(buildResponseField(RaidSubscriptionResponse.ABSENT, subscriptions));
	}

	private Field buildParticipantCountField(List<RaidSubscription> subscriptions) {
		List<RaidSubscription> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getParticipantsEmote().forMessage(), filtered, false, false);
	}

	private Field buildRoleCountField(List<RaidSubscription> subscriptions, CharacterRole role) {
		List<RaidSubscription> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT && s.getCharacter().getSpec().getRole() == role)
				.collect(Collectors.toList());

		return buildField(discordEmoteService.getEmote(role).forMessage(), filtered, false, true);
	}

	private Field buildTankField(List<RaidSubscription> subscriptions) {
		List<RaidSubscription> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.filter(s -> s.getCharacter().getSpec().getRole() == CharacterRole.TANK)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(CharacterSpec.WARRIOR_TANK).forMessage(), filtered, true, true);
	}

	private Field buildClassField(CharacterClass characterClass, List<RaidSubscription> subscriptions) {
		List<RaidSubscription> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.filter(s -> s.getCharacter().getSpec().getRole() != CharacterRole.TANK)
				.filter(s -> s.getCharacter().getSpec().getCharacterClass() == characterClass)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(characterClass).forMessage(), filtered, true, true);
	}

	private Field buildResponseField(RaidSubscriptionResponse response, List<RaidSubscription> subscriptions) {
		List<RaidSubscription> filtered = subscriptions.stream().filter(s -> s.getResponse() == response).collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(response).forMessage(), filtered, true, true);
	}

	private Field buildField(String icon, List<RaidSubscription> subscriptions, boolean showParticipantList, boolean inline) {
		String content = icon + " " + bold("" + subscriptions.size());

		if (showParticipantList) {
			String list = subscriptions.stream()
					.map(s -> s.getCharacter() == null ? s.getUser().getName()
							: discordEmoteService.getEmote(s.getCharacter().getSpec()).forMessage() + " " + s.getCharacter().getName())
					.collect(Collectors.joining(System.lineSeparator()));
			content += System.lineSeparator() + list;
		}

		return new Field("", content, inline);
	}

	private Field buildBlankLineField() {
		// manually create a blank line to increase embed width
		return new Field("", Strings.repeat(" ‎", 147), false);
	}

	private String bold(String s) {
		return "**" + s + "**";
	}

}
