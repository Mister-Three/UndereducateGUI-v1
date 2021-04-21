# UndereducateGUI-v1
UndereducateGUI is a feature filled plugin to view everyone online in a GUI

### Modules

As of the release these are the current things you can enable in the GUI<br>
`showPing, showHealth showBalance, showRank, showClient and showIfVanished`<br>
<br>
Each option has a description and the dependencies within the `config.yml`, please read through it or else the GUI will not launch.
<br>
<br>
I'm open to requests to show more modules within the GUI, just open an [issue](https://github.com/undereducated/UndereducateGUI-v1/issues/new/choose) with the Module Request label

### Configuration

Nearly everything within UndereducateGUI is configurable. This includes toggling modules, changing the prefix, lores and the messages. Check out the config [here](https://github.com/undereducated/UndereducateGUI-v1/blob/master/src/main/resources/config.yml).

### Dependencies

##### Placeholder API is strongly recommended.

For the plugin to launch it requires `UndereducatedUtils`, which is my API plugin so I can have cleaner code. Get it here: [Download link](http://rotf.lol/undereducateapi)

For `showBalance` you need [Vault](https://www.spigotmc.org/resources/vault.34315/), and an Economy plugin.<br>
For `showRank` you need [Vault](https://www.spigotmc.org/resources/vault.34315/), and a Permissions plugin, and a Chat plugin.<br>
For `showClient` you need [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/), [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/), and [ClientDetection](https://github.com/undereducated/ClientDetection-v1) <br>
For `showIfVanished` you need a supported Vanish plugin like SuperVanish, PremiumVanish, VanishNoPacket or something similar.

### Commands and permissions

#### Commands

`/gui` - Opens up the GUI<br>
`/gui help` - Shows all the subcommands with their descriptions<br>
`/gui reload` - Reloads the configuration<br>
`/gui config` - Opens the configuration manager<br>


#### Permissions

`gui.use` - Gives you the ability to launch up the GUI<br>
`gui.reload` - Gives you the ability to reload the configuration<br>
`gui.configure` - Gives you the ability to configure the modules (auto-reloads)<br>
`gui.*` - All permissions<br>

