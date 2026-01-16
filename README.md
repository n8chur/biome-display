# BiomeDisplay

A mod for Hytale that displays the current biome information in the UI.

The UI is extremely generic and this mod cannot be used alongside other mods which use custom HUDs.

## Usage

Toggle the GUI on/off using the following command:
```
/biome
```

## Issues

* Cannot be used alongside other mods that create custom HUDs (e.g. EyeSpy)
* UI looks like shit

## TODO

* Support [MultipleHud](https://github.com/Buuz135/MHUD) to allow rendering alongside other mods with custom HUDs
* Improve UI
* Show more information about the zone/tier (e.g. ore generation, mobs, etc.)
* Localize biome name
* Optimizations (only update when needed, clear cache when user disconnects, etc.)