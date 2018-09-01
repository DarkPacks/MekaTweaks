# MekaTweaks
Simple mod to add things to Mekanism

## Example Config
Name: `mtInfuseTypes.json`

Explanation:
- `name`: Is the name of the infuse type. (Should be all lowercase)
- `resourceLocation`: Is the location to the texture to use for the infuse type.
```json
[
    {
        "name": "stone",
        "resourceLocation": "minecraft:blocks/stone"
    },
    {
        "name": "hay",
        "resourceLocation": "minecraft:blocks/hay_block_side"
    }
]
```

## Example CraftTweaker Script

Parameters:
- Infuse name should be the name of the infuse type. Can be the custom one you've added or ones already in Mekanism. (Should be all lowercase)
- Item should the item you want to be able to be _converted_.
- Worth value is the amount of the _infuse_ you want to be created from the Item. (Should be less than 50) (Default: 5)
```java
import mods.mekatweaks.InfuseObject;

// addInfuseType(String infuseName, IItemStack item, int worthValue);
InfuseObject.addInfuseType("stone", <minecraft:stone>, 10);
InfuseObject.addInfuseType("hay", <minecraft:hay_block>, 10);
```