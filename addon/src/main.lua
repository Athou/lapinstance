Lapinstance = LibStub("AceAddon-3.0"):NewAddon("Lapinstance", "AceConsole-3.0",
                                               "AceEvent-3.0")
local AceGUI = LibStub("AceGUI-3.0")

function Lapinstance:OnInitialize()
    self:RegisterChatCommand("lapinstance", "ChatCommand")
    self:RegisterChatCommand("lap", "ChatCommand")
end

local function log(msg) print("|cff00ff00Lapinstance|r " .. msg) end

local function arrayContains(array, value)
    for i, v in ipairs(array) do if v == value then return true end end
    return false
end

local function splitOnComma(text)
    local tokens = {}
    for token in text:gmatch("[^,]+") do table.insert(tokens, token) end
    return tokens
end

local function MassInvites(characterNames)
    if not IsInRaid() then
        log("Vous n'êtes pas en raid.")
        return
    end

    for _, characterName in pairs(characterNames) do
        InviteUnit(characterName)
    end
end

local function ReportMissingCharactersFromRaid(characterNames)
    if not IsInRaid() then
        log("Vous n'êtes pas en raid.")
        return
    end

    local raidMembers = {}
    for i = 1, MAX_RAID_MEMBERS do
        local raidMember = GetRaidRosterInfo(i)
        if raidMember then table.insert(raidMembers, raidMember) end
    end

    local missingCharacterNames = {}
    for _, characterName in pairs(characterNames) do
        if not arrayContains(raidMembers, characterName) then
            table.insert(missingCharacterNames, characterName)
        end
    end
    local missingCharacterString = table.getn(missingCharacterNames) > 0 and table.concat(missingCharacterNames, ", ") or "/"
    log("Personnages manquants: " .. missingCharacterString)


    local additionalCharacterNames = {}
    for _, raidMember in pairs(raidMembers) do
        if not arrayContains(characterNames, raidMember) then
            table.insert(additionalCharacterNames, raidMember)
        end
    end
    local additionalCharacterString = table.getn(additionalCharacterNames) > 0 and table.concat(additionalCharacterNames, ", ") or "/"
    log("Personnages pas prévus: " .. additionalCharacterString)
end

function Lapinstance:ChatCommand(input)
    local frame = AceGUI:Create("Frame")
    frame:SetTitle("Lapinstance")
    frame:SetCallback("OnClose", function(widget) AceGUI:Release(widget) end)

    local charactersEditBox = AceGUI:Create("MultiLineEditBox")
    charactersEditBox:SetLabel("Personnages:")
    charactersEditBox:SetRelativeWidth(1.0)
    charactersEditBox:DisableButton(true)
    charactersEditBox:SetFocus()
    frame:AddChild(charactersEditBox)

    local reportButton = AceGUI:Create("Button")
    reportButton:SetText("Kiképala")
    reportButton:SetWidth(200)
    reportButton:SetCallback("OnClick", function()
        ReportMissingCharactersFromRaid(splitOnComma(charactersEditBox:GetText()))
    end)
    frame:AddChild(reportButton)

    local massInviteButton = AceGUI:Create("Button")
    massInviteButton:SetText("Mass-Invites")
    massInviteButton:SetWidth(200)
    massInviteButton:SetCallback("OnClick", function()
        MassInvites(splitOnComma(charactersEditBox:GetText()))
    end)
    frame:AddChild(massInviteButton)
end

