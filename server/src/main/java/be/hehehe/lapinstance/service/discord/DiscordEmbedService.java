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
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

@Service
@RequiredArgsConstructor
public class DiscordEmbedService {

	private final DiscordEmoteService discordEmoteService;

	private enum ParticipantListStyle {
		NONE, NAME, NAME_AND_ICON;
	}

	public MessageEmbed buildEmbed(Raid raid, List<SubscriptionModel> subscriptions, String raidUrl) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(raid.getRaidType().getName(), raidUrl);
		eb.setColor(Color.BLACK);

		String desc = raid.getFormattedDate();
		if (!Strings.isNullOrEmpty(raid.getComment())) {
			desc += System.lineSeparator() + raid.getComment();
		}
		eb.setDescription(desc);

		buildSubscriptionFields(eb, subscriptions);

		return eb.build();
	}

	private void buildSubscriptionFields(EmbedBuilder eb, List<SubscriptionModel> subscriptions) {

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
		eb.addField(buildClassField(CharacterClass.SHAMAN, subscriptions));

		eb.addField(buildBlankLineField());

		eb.addField(buildResponseField(RaidSubscriptionResponse.LATE, subscriptions));
		eb.addField(buildResponseField(RaidSubscriptionResponse.BENCH, subscriptions));
		eb.addField(buildResponseField(RaidSubscriptionResponse.ABSENT, subscriptions));
	}

	private Field buildParticipantCountField(List<SubscriptionModel> subscriptions) {
		List<SubscriptionModel> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getParticipantsEmote().forMessage(), filtered, ParticipantListStyle.NONE, false);
	}

	private Field buildRoleCountField(List<SubscriptionModel> subscriptions, CharacterRole role) {
		List<SubscriptionModel> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT && s.getSpec().getRole() == role)
				.collect(Collectors.toList());

		return buildField(discordEmoteService.getEmote(role).forMessage(), filtered, ParticipantListStyle.NONE, true);
	}

	private Field buildTankField(List<SubscriptionModel> subscriptions) {
		List<SubscriptionModel> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.filter(s -> s.getSpec().getRole() == CharacterRole.TANK)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(CharacterSpec.WARRIOR_TANK).forMessage(), filtered,
				ParticipantListStyle.NAME_AND_ICON, true);
	}

	private Field buildClassField(CharacterClass characterClass, List<SubscriptionModel> subscriptions) {
		List<SubscriptionModel> filtered = subscriptions.stream()
				.filter(s -> s.getResponse() == RaidSubscriptionResponse.PRESENT)
				.filter(s -> s.getSpec().getRole() != CharacterRole.TANK)
				.filter(s -> s.getSpec().getCharacterClass() == characterClass)
				.collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(characterClass).forMessage(), filtered, ParticipantListStyle.NAME_AND_ICON, true);
	}

	private Field buildResponseField(RaidSubscriptionResponse response, List<SubscriptionModel> subscriptions) {
		List<SubscriptionModel> filtered = subscriptions.stream().filter(s -> s.getResponse() == response).collect(Collectors.toList());
		return buildField(discordEmoteService.getEmote(response).forMessage(), filtered, ParticipantListStyle.NAME, true);
	}

	private Field buildField(String icon, List<SubscriptionModel> subscriptions, ParticipantListStyle style, boolean inline) {
		String content = icon + " " + bold("" + subscriptions.size());

		if (style != ParticipantListStyle.NONE) {
			content += System.lineSeparator() + buildParticipantList(subscriptions, style == ParticipantListStyle.NAME_AND_ICON);
		}

		return new Field("", content, inline);
	}

	private String buildParticipantList(List<SubscriptionModel> subscriptions, boolean showIcons) {
		return subscriptions.stream()
				.map(s -> !showIcons ? s.getName() : discordEmoteService.getEmote(s.getSpec()).forMessage() + " " + s.getName())
				.collect(Collectors.joining(System.lineSeparator()));
	}

	private Field buildBlankLineField() {
		// manually create a blank line to increase embed width
		return new Field("", Strings.repeat(" ‎", 147), false);
	}

	private String bold(String s) {
		return "**" + s + "**";
	}

	@Value
	public static class SubscriptionModel {
		private String name;
		private RaidSubscriptionResponse response;
		private CharacterSpec spec;

	}

}
