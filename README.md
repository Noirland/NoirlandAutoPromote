# NoirlandAutoPromote #

Simple ladder promotions that happen automatically based on play time

* Automated promotions based on play time
* Hooks into GroupManager permissions plugin
* Backs up to MySQL database constantly to preserve play times
* Admin commands to adjust play time and ranks
* Promotion announcements
* Highscore listing of play time

## Config ##

*WARNING*: Ensure that all ranks, even those that do not automatically promote, are added to this configuration file

```
ranks: # List of *all* ranks on server
  DefaultRankHere: # Name, case sensitive, of the rank
    noPromote: true # Prevents the rank from being automatically promoted
    default: true # Sets the rank as the default, allowing new players to use /agree
    promoteTo: NewRankHere # Sets the rank to be promoted to
  InsertRankHere:
    playTimeNeeded: 5 # Number of hours needed to advance to the next rank
    promoteTo: NewRankHere
  AnotherRankHere:
    playTimeNeeded: 5
    promoteTo: NewRankHere
  NonPromotingRank: # "Top" of the promotion ladder
    noPromote: true

mysql: # Database configuration
    database: autopromote
    username: autopromote
    password: 'password'
    port: 3306
    host: localhost
    prefix: promote

settings:
  saveTime: 10 # Time, in minutes, to wait before saving to the database
  sortTime: 5 # Time, in minutes, to wait before recalculating high score ranks
```

## Commands ##

`/autopromote`

Shows your play time, rank on highscore list, and time till next rank

`/autopromote [player]`

Shows another player's play time, rank on highscore list, and time till next rank

`/autopromote top (page)`

Lists the highest play times on the server. Optionally, a page may be specified to show a specific section of the high scores

`/agree`

Promotes a player from the default rank

`/autopromote promote [player] [rank]`

Admin command to promote a player to a specific rank, regardless of what rank the currently are

`/autopromote reset [player] (hours)`

Admin command to reset a player's play time either to 0, or to a specified amount

`/autopromtoe reload`

Admin command to reload the values in the config, and to force save to the database

## Permissions ##

`autopromote.check` - Allows player to check their promote time

`autopromote.check.others` - Allows player to check another player's promote time

`autopromote.check.top` - Allows player to check the high score list

`autopromote.reload` - Access to reload command

`autopromote.promote` - Allows force promotions

`autopromote.reset` - Allows play time resets of another player