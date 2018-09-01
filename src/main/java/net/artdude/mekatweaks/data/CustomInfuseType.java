package net.artdude.mekatweaks.data;

import net.minecraft.util.ResourceLocation;

public class CustomInfuseType {
    private final String name;
    private final String resourceLocation;

    public CustomInfuseType(final String name, final String resourceLocation) {
        this.name = name;
        this.resourceLocation = resourceLocation;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getResourceLocation() {
        String[] location = resourceLocation.split(":");

        return new ResourceLocation(location[0], location[1]);
    }
}
