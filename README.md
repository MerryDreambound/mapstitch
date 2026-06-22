# MapStitch

This mod adds a world map and a minimap using vanilla maps and makes vanilla maps cheaper and easier to manage by using the new Atlas item.

## World map

MapStitch uses filled maps from your inventory to create a world map which you can open at any time by pressing M. Making maps is now cheaper, requiring only 9 paper instead of 8 paper and a compass, but the world map won't show your position unless you have a compass in your inventory.

The inventory doesn't have much space for maps, but the brand-new Atlas item does! Create an atlas by combining a filled map and a book. The atlas can then be used like a bundle to store up to 1024 empty maps and filled maps matching the scale of the filled map used to create the atlas.

Maps from the atlas will be used to create the world map, and the atlas will automatically turn empty maps inside it into filled maps as you enter unmapped areas.

## Minimap

Having an atlas in the hotbar and a compass anywhere in your inventory will display a minimap showing the map where you're currently located.

## Configuration

### Client settings

These can be configured either through Sodium video settings or by editing `mapstitch.json` in the config folder.

- Minimap position (`TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`)
- Minimap background (`CLEAR`, `TEXTURE`, `NONE`)
  - Clear is a transparent rectangle, Texture is the map texture from the cartography table
- Minimap display condition (`HANDS`, `HOTBAR`, `INVENTORY`)
  - Defines where the atlas item has to be located in order to display the minimap

### Game rules
