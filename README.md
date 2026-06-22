# MapStitch

This mod adds a world map and a minimap using vanilla maps and makes vanilla maps cheaper and easier to manage by using the new Atlas item.

## World map

MapStitch uses filled maps from your inventory to create a world map which you can open at any time by pressing M. Making maps is now cheaper, requiring only 9 paper instead of 8 paper and a compass, but the world map won't show your position unless you have a compass in your inventory.

The inventory doesn't have much space for maps, but the brand-new Atlas item does! Create an atlas by combining a filled map and a book. The atlas can then be used like a bundle to store up to 1024 empty maps and filled maps matching the scale of the filled map used to create the atlas.

Maps from the atlas will be used to create the world map, and the atlas will automatically turn empty maps inside it into filled maps as you enter unmapped areas.

## Minimap

Having an atlas in the hotbar and a compass anywhere in your inventory will display a minimap showing the map where you're currently located.

## Configuration

### Game rules

- `mapstitch:require_compass_for_pos`: Whether a compass is required to display the minimap and the player position and coordinates in the world map (default true)

### Client settings

These can be configured either through Sodium video settings or by editing `mapstitch.json` in the config folder.

- Minimap display condition (`HANDS`, `HOTBAR`, `INVENTORY`)
  - Defines where the atlas item has to be located in order to display the minimap
  - Default is `HOTBAR`
- Minimap position (`TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`)
  - Default is `TOP_RIGHT`
- Minimap size (0.5x to 2.0x)
  - Default is 1.0x
- Minimap background (`CLEAR`, `TEXTURE`, `NONE`)
  - Clear is a transparent rectangle, Texture is the map texture from the cartography table
  - Default is `CLEAR`
- Minimap background opacity (if Clear is set as the background)
  - Default is 35%
- Minimap horizontal and vertical offsets
  - Defaults are 0 and 0
- World map text background opacity (default 35%)
  - Default is 35%
- World map help toggle
  - Defines whether to show the help toggle option
  - Default is true
