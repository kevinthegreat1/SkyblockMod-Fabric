# Skyblock Mod Fabric

Helpful features for Hypixel Skyblock ([Fabric](https://fabricmc.net/) 1.17+)

## Features

### Chat

- /ca -> /chat all
- /cp -> /chat party
- /cg -> /chat guild
- /co -> /chat officer
- /cc -> /chat coop

### Dungeon

#### Configurable dungeon map

- /sbm dungeonMap: show current state (on or off)
- /sbm dungeonMap [true|false]: turn dungeon map on or off
- /sbm dungeonMap scale: show current scale
- /sbm dungeonMap scale [scale]
- /sbm dungeonMap offset: show current offset
- /sbm dungeonMap offset [offsetX] [offsetY]

#### Dungeon Score

- send 270 and 300 score in chat
- /sbm dungeonScore 270|300: show current state (on or off)
- /sbm dungeonScore 270|300 [true|false]: turn dungeon score on or off
- /sbm dungeonScore 270|300 message [message]
    - Ex: /sbm dungeonScore 300 message 300 score reached!

#### Livid color

- send livid color in chat
- /sbm lividColor: show current state (on or off)
- /sbm lividColor [true|false]: turn livid color on or off
- /sbm lividColor [message before] "[color]" [message after]
    - Ex: /sbm lividColor [color] is sus

### Experiments

- Solvers for Enchanting Experiments
- /sbm experiment [chronomatron|ultrasequencer|superpairs]: show current state (on or off)
- /sbm experiment [chronomatron|ultrasequencer|superpairs] [true|false]: turn experiment solvers on or off

### Fishing

- Notifies you to reel in
- (Only works when nothing is in the water in the line through the player and the bobber*)
- /sbm fishingHelper: show current state (on or off)
- /sbm fishingHelper [true|false]: turn fishing helper on or off

### Help

- /sbm help: show all skyblock mod commands

### Message

- /m [player] -> /msg [player]

### Quiver Low Warning

- Notifies you when you only have 50 Arrows left in your Quiver
- /sbm quiverWarning: show current state (on or off)
- /sbm quiverWarning [true|false]: turn quiver warning on or off

### Party

- /pa [Player] -> /p accept [Player]
- /pv -> /p leave
- /pd -> /p disband

#### Reparty

- /pr, /rp -> Reparty: disbands the party and invites everyone back
- Auto join reparty
- /sbm reparty: show current state (on or off)
- /sbm reparty [true|false]: turn reparty on or off

### Visit

- /v [Player] -> /visit [Player]
- /vp, /visit p -> /visit portalhub

### Warp

- /sk, /sky -> /skyblock
- /i -> /is
- /h, /hu -> /hub
- /d, /dn, /dun, /dungeon -> /warp dungeon_hub


- /bl, /blazing, /fortress, /crimson, /isles, /ci, /crimson isles, /n, /nether -> /warp nether
- /deep, /cavern, /caverns -> /warp deep
- /dw, /dwarven, /mi, /mines -> /warp mines
- /f, /for, /forge -> /warpforge
- /cry, /crystal, /ho, /hollows, /ch, /chrystal hollows -> /warp crystals
- /g, /gold -> /warp gold
- /des, /desert, /mu, /mushroom -> /warp desert
- /sp, /spider, /spiders -> /warp spider
- /ba, /barn -> /warp barn
- /e, /end -> /warp end
- /p, /park -> /warp park


- /castle -> /warp castle
- /museum -> /warp museum
- /da, /dark -> /warp da
- /crypt, /crypts -> /warp crypt
- /nest -> /warp nest
- /magma -> /warp magma
- /void -> /warp void
- /drag, /dragon -> /warp drag
- /jungle -> /warp jungle
- /howl -> /warp howl

### Misc

- /sbm config reload: reload config file
- Useful when you accidentally override a setting
- /sbm config save: manually save config file
- Config file will automatically save when you quit the game

Configuration file is in config folder in minecraft run directory
<br>
Configuration will be printed to the log if writing to the configuration file fails.