> ### ImmersiveFX based on latest DS Branch
**Requirements**
* JAVA 8 w/Forge 1.16.5-36.2.20+
* both client and server side
* Cloth Config API (Forge) is optional


* Minecraft 1.16.4 changed biomes, a lot.  I have noticed that some biome mods do not properly tag their biomes.  Dynamic Surroundings uses these tags to identify what sound features to give the biome.
* Particles could render strangely (water ripples, footsteps, etc.).  I think I have the proper rendering, but other mods may indirectly interfere.
* Json configs will load/process when you log in to a world.  This is to ensure that the client has the most up to date registries and datapacks from the server before applying its rules.  This may have some impact on login processing time.


-- OreCruncher, Y3Z0N
