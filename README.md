# CK Utilities

CK Utilities is a NeoForge mod for Minecraft 26.2. It adds copper alloy tools and a few utility items I wanted in a survival world. The mod id is `ckutilities`. I maintain it. The license is MIT.

This branch is only for Minecraft 26.2. The Minecraft 1.21.1 line is the separate `1.21.1` branch and is not merged with this one.

## Items

- Copper alloy ingot and copper alloy block
- Copper alloy pickaxe, axe, shovel, and sword
- Copper alloy hammer, which breaks a 3×3 area
- Energy drill
- Battery. It starts disabled. Sneak-right-click it to turn charging on.
- Tiny coal
- Charger. It charges a drill or battery from an enabled battery in its power slot. The recipe is copper alloy ingots around a battery.

Drill upgrades are included on this branch. Sneak-right-click the drill to open the upgrade screen, then put upgrades in or take them out. Upgrades are not consumed.

- Hammer size: 3×3, then 5×5, then 9×9
- Efficiency I through V
- Fortune I through III
- Silk Touch
- An upgrade template used to craft every upgrade. Each higher tier is crafted from the previous tier and a template. Fortune and Silk Touch cannot both be applied.

## Installing

This mod is meant to be added to a CurseForge (or other launcher) instance for its Minecraft version. This line is Minecraft 26.2 with NeoForge.

There is no CurseForge release yet.

Developers can build the source with `./gradlew runClient` on Java 25.

## Issues

Please report problems on the [GitHub issues page](https://github.com/Dawson-Taylor/CK_Utilities/issues).

## Inspiration

[JustDireThings](https://github.com/Direwolf20-MC/JustDireThings) and [Actually Additions](https://github.com/Ellpeck/ActuallyAdditions), along with some of the items in those mods, were inspiration for this project.
