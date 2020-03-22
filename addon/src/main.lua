Lapinstance = LibStub("AceAddon-3.0"):NewAddon("Lapinstance", "AceConsole-3.0",
                                               "AceEvent-3.0")
local AceGUI = LibStub("AceGUI-3.0")

function Lapinstance:OnInitialize()
    self:RegisterChatCommand("lapinstance", "ChatCommand")
    self:RegisterChatCommand("lap", "ChatCommand")
end

local function printLocal(msg) print("|cff00ff00Lapinstance|r " .. msg) end
local function printToRaid(msg)
    local text = msg:len() > 255 and (msg:sub(0, 252) .. "...") or msg
    SendChatMessage(text, "RAID")
end

local function arrayContains(array, value)
    for i, v in ipairs(array) do if v == value then return true end end
    return false
end

local function extractCharacterNames(text)
    local tokens = {}
    for token in text:gmatch("[^,\r\n\t%s]+") do
        local trimmed = token:gsub("%s+", "")
        if trimmed ~= "" then table.insert(tokens, trimmed) end
    end
    return tokens
end

local function collectRaidMembers()
    local raidMembers = {}
    for i = 1, MAX_RAID_MEMBERS do
        local raidMember = GetRaidRosterInfo(i)
        if raidMember then table.insert(raidMembers, raidMember) end
    end
    return raidMembers
end

local function MassInvites(characterNames)
    if not IsInRaid() then
        printLocal("Vous n'êtes pas en raid.")
        return
    end

    local raidMembers = collectRaidMembers()

    for _, characterName in pairs(characterNames) do
        if not arrayContains(raidMembers, characterName) then
            InviteUnit(characterName)
        end
    end
end

local function ReportMissingCharactersFromRaid(logFunction, characterNames)
    if not IsInRaid() then
        printLocal("Vous n'êtes pas en raid.")
        return
    end

    local raidMembers = collectRaidMembers()

    local missingCharacterNames = {}
    for _, characterName in pairs(characterNames) do
        if not arrayContains(raidMembers, characterName) then
            table.insert(missingCharacterNames, characterName)
        end
    end
    local missingCharacterString = table.getn(missingCharacterNames) > 0 and
                                       table.concat(missingCharacterNames, ", ") or
                                       "/"
    logFunction("Personnages manquants: " .. missingCharacterString)

    local additionalCharacterNames = {}
    for _, raidMember in pairs(raidMembers) do
        if not arrayContains(characterNames, raidMember) then
            table.insert(additionalCharacterNames, raidMember)
        end
    end
    local additionalCharacterString =
        table.getn(additionalCharacterNames) > 0 and
            table.concat(additionalCharacterNames, ", ") or "/"
    logFunction("Personnages pas prévus: " .. additionalCharacterString)
end

function Lapinstance:ChatCommand(input)
    local frame = AceGUI:Create("Frame")
    frame:SetTitle("Lapinstance")
    frame:SetCallback("OnClose", function(widget) AceGUI:Release(widget) end)

    local charactersEditBox = AceGUI:Create("MultiLineEditBox")
    charactersEditBox:SetLabel("Personnages:")
    charactersEditBox:SetRelativeWidth(1.0)
    charactersEditBox:SetNumLines(20)
    charactersEditBox:DisableButton(true)
    charactersEditBox:SetFocus()
    frame:AddChild(charactersEditBox)

    local reportButton = AceGUI:Create("Button")
    reportButton:SetText("Kiképala")
    reportButton:SetWidth(200)
    reportButton:SetCallback("OnClick", function()
        ReportMissingCharactersFromRaid(printLocal, extractCharacterNames(
                                            charactersEditBox:GetText()))
    end)
    frame:AddChild(reportButton)

    local raidReportButton = AceGUI:Create("Button")
    raidReportButton:SetText("Kiképala (annonce raid)")
    raidReportButton:SetWidth(200)
    raidReportButton:SetCallback("OnClick", function()
        ReportMissingCharactersFromRaid(printToRaid, extractCharacterNames(
                                            charactersEditBox:GetText()))
    end)
    frame:AddChild(raidReportButton)

    local massInviteButton = AceGUI:Create("Button")
    massInviteButton:SetText("Mass-Invites")
    massInviteButton:SetWidth(200)
    massInviteButton:SetCallback("OnClick", function()
        MassInvites(extractCharacterNames(charactersEditBox:GetText()))
    end)
    frame:AddChild(massInviteButton)
end

