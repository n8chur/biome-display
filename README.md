# BiomeDisplay

A mod for Hytale that displays the current biome information in the UI.

If the [MultipleHUD](https://www.curseforge.com/hytale/mods/multiplehud) mod is present, this mod can be used alongside other mods that support multiple custom HUDS (e.g. [EyeSpy](https://www.curseforge.com/hytale/mods/eyespy)).

## Usage

### Hide or show the HUD

```
/biome toggle
```

### Move the position of the HUD

```
/biome position top center
/biome position middle right
/biome position bottom left
```

### Adjust the size of the HUD

```
/biome size small
/biome size medium
/biome size large
```

## Config

A config file can be proved at 
  * **Servers**: `mods/n8chur_BiomeDisplay/config.json` 
  * **Local**: `%appdata%/Hytale/UserData/Saves/<world_name>/mods/n8chur_BiomeDisplay/config.json`

This file will be created on first launch if one is not already provided.

The config file uses the following format:
```json
{
  "DefaultHidden": true,
  "DefaultPosition": {
    "Vertical": "top",
    "Horizontal": "right"
  },
  "DefaultSize": "small"
}
```

* `DefaultHidden`: Whether the HUD is hidden to the user when they first join a server.
  * **Values**: `true`, `false` 
  * **Default**: `false`
* `DefaultPosition`: The default position of the HUD element when a user first joins a server.
  * `Vertical`
    * **Values**: `"top"`, `"middle"`, `"bottom"`
    * **Default**: `"bottom"`
  * `Horizontal`
    * **Values**: `"left"`, `"center"`, `"right"`
    * **Default**: `"left"`
* `DefaultSize`: The default size of the HUD element when a user first joins a server.
  * **Values**: `"small"`, `"medium"`, `"large"` 
  * **Default**: `"medium"`