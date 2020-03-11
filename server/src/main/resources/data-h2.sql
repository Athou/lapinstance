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
(9, 'Albin', 'PRIEST_HEAL', true, 4),
(10, 'Santiago', 'PRIEST_HEAL', true, 5),
(11, 'Berkeley', 'DRUID_RESTO', true, 4),
(12, 'Ross', 'DRUID_CAT', true, 4),
(13, 'Alwyn', 'ROGUE', true, 4),
(14, 'Armando', 'ROGUE', true, 5),
(15, 'Spyridon', 'WARRIOR_DPS', true, 4),
(16, 'Pearson', 'WARRIOR_DPS', true, 5),
(17, 'Shepherd', 'MAGE', true, 4),
(18, 'Raoul', 'MAGE', true, 5),
(19, 'Oswell', 'WARLOCK', true, 4),
(20, 'Swail', 'WARLOCK', true, 5),
(21, 'Ramsey', 'HUNTER', true, 4),
(22, 'Webb', 'HUNTER', true, 5);

insert into RAIDS (id, date, comment, raid_type) values
(1, DATEADD(DAY, 1, CURRENT_DATE()), 'Tout le monde à l''heure dans l''instance', 'BLACKWING_LAIR');

insert into ROSTER_MEMBERS (raid_type, user_character_id) values 
('ONYXIA', 1),
('ONYXIA', 2),
('ONYXIA', 3),
('ONYXIA', 4),
('MOLTEN_CORE', 1),
('MOLTEN_CORE', 2),
('MOLTEN_CORE', 3),
('MOLTEN_CORE', 4),
('ZUL_GURUB', 1),
('ZUL_GURUB', 2),
('ZUL_GURUB', 3),
('ZUL_GURUB', 4),
('BLACKWING_LAIR', 1),
('BLACKWING_LAIR', 3);

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