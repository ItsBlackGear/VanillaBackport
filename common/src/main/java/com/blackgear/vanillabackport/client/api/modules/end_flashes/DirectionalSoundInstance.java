package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class DirectionalSoundInstance extends AbstractTickableSoundInstance {
    private final Camera camera;
    private final float xAngle;
    private final float yAngle;
    
    public DirectionalSoundInstance(
        SoundEvent sound,
        SoundSource source,
        RandomSource random,
        Camera camera,
        float xAngle,
        float yAngle
    ) {
        super(sound, source, random);
        this.camera = camera;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
    }
    
    private void setPosition() {
        Vec3 direction = Vec3.directionFromRotation(this.xAngle, this.yAngle).scale(10.0);
        this.x = this.camera.getPosition().x + direction.x;
        this.y = this.camera.getPosition().y + direction.y;
        this.z = this.camera.getPosition().z + direction.z;
        this.attenuation = Attenuation.NONE;
    }
    
    @Override
    public void tick() {
        this.setPosition();
    }
}