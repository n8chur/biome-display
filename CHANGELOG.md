# 1.3.3

* Config file is now created if one does not already exist.
* Further attempts at fixing concurrency issues.

# 1.3.2

* Fixed a potential crash when attempting to create user settings due to not dispatching the work to a world thread.

# 1.3.1

* Improved performance by skipping UI updates the contents and layout are unchanged on tick.
* Added better fallback if localizations are not present (uses raw values instead of presenting the user with a non-existent localization key).

# 1.3.0

* Added support for default configuration values. A config can be provided at `mods/n8chur_BiomeDisplay/config.json` in the following format:
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

# 1.2.1

* Attempting to work around an issue that could cause BiomeDisplay to flicker a full screen overlay occasionally when in a high latency environment and used alongside other mods that integration with MultipleHUD.

# 1.2.0

* Added resize command with support for `small`, `medium`, and `large` via `/biome size <size>`.

# 1.1.1

* Fixed MultipleHUD 1.0.2 support as well as some issues when integrating with other mods (like Wayfinder).

# 1.1.0

* Added support for moving the position of the window using the `/biome position` command (e.g. `/biome position top left`)
* Updated the command to toggle the HUD on or off using `/biome toggle` (was just `/biome`)
* Removed erroneously added example dirt crafting recipe.
* The mod will now default on instead of off.
* Added [Better Modlist](https://www.curseforge.com/hytale/mods/better-modlist) icon
* Minor performance and server memory improvements

# 1.0.0

Initial release