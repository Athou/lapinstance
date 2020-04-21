insert into USERS (id, name, discord_id, disabled) values 
(1, 'Athou', '111098757910683648', false),
(2, 'Batto', '111506271345082368', false),
(3, 'Koin', '106015318253531136', false),
(4, 'FakeUser1', '000', false),
(5, 'FakeUser2', '001', false);

insert into USER_CHARACTERS (id, name, spec, main, user_id) values
(1, 'Athou', 'DRUID_RESTO', true, 1),
(2, 'Elefgé', 'WARRIOR_DPS', false, 1),
(3, 'Xérouki', 'ROGUE', true, 2),
(4, 'Knaff', 'PALADIN_HEAL', false, 2),
(5, 'Koin', 'WARRIOR_DPS', true, 3),
(6, 'Lapewf', 'PALADIN_HEAL', false, 3),
(7, 'Harmen', 'PALADIN_HEAL', true, 4),
(8, 'Whitmore', 'PALADIN_HEAL', true, 5),
(9, 'Albin', 'PRIEST_HEAL', false, 4),
(10, 'Santiago', 'PRIEST_HEAL', false, 5),
(11, 'Berkeley', 'DRUID_RESTO', false, 4),
(12, 'Ross', 'DRUID_CAT', false, 4),
(13, 'Alwyn', 'ROGUE', false, 4),
(14, 'Armando', 'ROGUE', false, 5),
(15, 'Spyridon', 'WARRIOR_DPS', false, 4),
(16, 'Pearson', 'WARRIOR_DPS', false, 5),
(17, 'Shepherd', 'MAGE', false, 4),
(18, 'Raoul', 'MAGE', false, 5),
(19, 'Oswell', 'WARLOCK', false, 4),
(20, 'Swail', 'WARLOCK', false, 5),
(21, 'Ramsey', 'HUNTER', false, 4),
(22, 'Webb', 'HUNTER', false, 5);

insert into RAIDS (id, date, comment, raid_type, discord_text_channel_id) values
(1, DATEADD(DAY, 1, CURRENT_DATE()), 'Tout le monde à l''heure dans l''instance', 'BLACKWING_LAIR', '0');

insert into RAID_SUBSCRIPTIONS (raid_id, user_id, response, character_id) values
(1, 4, 'LATE', null),
(1, 4, 'PRESENT', 8),
(1, 4, 'PRESENT', 9),
(1, 4, 'LATE', null),
(1, 4, 'PRESENT', 11),
(1, 4, 'PRESENT', 12),
(1, 4, 'PRESENT', 13),
(1, 4, 'PRESENT', 14),
(1, 4, 'ABSENT', null),
(1, 4, 'PRESENT', 16),
(1, 4, 'PRESENT', 17),
(1, 4, 'ABSENT', null),
(1, 4, 'PRESENT', 19),
(1, 4, 'PRESENT', 20),
(1, 4, 'ABSENT', null),
(1, 4, 'PRESENT', 22);