import { Button, Card, H5, Icon } from "@blueprintjs/core"
import React from "react"
import { Box, Flex } from "reflexbox"
import { UserCharacter } from "../../api"
import { characterSpecLabels } from "../../api/utils"
import { SpecIcon } from "../spec-icons/SpecIcon"

export const CharacterCard: React.FC<{ character: UserCharacter; editable: boolean; onEdit: () => void }> = props => (
    <>
        <Card elevation={2}>
            <Flex>
                {props.character.main && (
                    <Box mr={2}>
                        <Icon icon="tick-circle" iconSize={18} />
                    </Box>
                )}
                <Box flexGrow={1}>
                    <H5>{props.character.name}</H5>
                </Box>
                {props.editable && (
                    <Box>
                        <Button icon="edit" onClick={props.onEdit} />
                    </Box>
                )}
            </Flex>
            <Flex>
                <Box mr={2}>
                    <SpecIcon spec={props.character.spec} />
                </Box>
                <Box>{characterSpecLabels[props.character.spec]}</Box>
            </Flex>
        </Card>
    </>
)
