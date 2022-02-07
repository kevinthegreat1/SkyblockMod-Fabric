# Skyblock Mod Fabric

Helpful features for Hypixel Skyblock ([Fabric](https://fabricmc.net/) 1.17+)

##Features

###Fishing

Notifies you to reel in
(Only works if you are not near other players)

###Dungeon

####Configurable dungeon map

- /sbm map: show current state (on or off)
- /sbm map on/off: turn dungeon map on or off
- /sbm map scale: show current scale
- /sbm map scale \[number]
- /sbm map offset: show current offset
- /sbm map offset \[number] \[number]


####Dungeon Score

- send 300 score in chat
- /sbm score: show current state (on or off)
- /sbm score on/off: turn dungeon score on or off
- /sbm score \[message before] "\[score]" \[message after]
- Ex: /sbm score \[score] score: 300 score

####Livid color

- send livid color in chat
- /sbm livid: show current state (on or off)
- /sbm livid on/off: turn livid color on or off
- /sbm livid \[message before] "\[color]" \[message after]
- Ex: /sbm livid \[color] is sus: red is sus

###Party

- /pa \[Player\] -> /p accept \[Player\]
- /pv -> /p leave
- /pd -> /p disband

####Reparty

- /pr, /rp -> Reparty: disbands the party and invites everyone back
- Auto join reparty
- /sbm reparty: show current state (on or off)
- /sbm reparty on/off: turn reparty on or off

###Warp

- /sk, /sky -> /skyblock
- /i -> /is
- /h, /hu -> /hub
- /d, /dn, /dun, /dungeon -> /warp dungeon_hub


- /bl, /blazing, /fortress, /n, /nether -> /warp nether
- /deep, /cavern, /caverns -> /warp deep
- /dw, /dwarven, /mi, /mines -> /warp mines
- /f, /for, /forge -> /warpforge
- /c, /crystal, /ho, /hollows, /hc -> /warp crystals
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

###Chat

- /ca -> /chat all
- /cp -> /chat party
- /cg -> /chat guild
- /co -> /chat officer
- /cc -> /chat coop

###Message

- /m \[player\] -> /msg \[Player\]

###Visit

- /v \[Player\] -> /visit \[Player\]
- /vp, /visit p -> /visit portalhub

###Misc

- /sbm reload: reload config file
- Useful when you accidentally override something

Configuration file is in minecraft run directory
Configuration will be printed to the log if writing to the configuration file fails.