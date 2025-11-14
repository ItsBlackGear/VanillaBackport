package com.blackgear.vanillabackport.core.util;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

// For processing with codecs - Echo2craft.
public class TagUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static <T> Optional<T> read(CompoundTag pTag, String pKey, Codec<T> pCodec) {
        return read(pTag, pKey, pCodec, NbtOps.INSTANCE);
    }

    public static <T> Optional<T> read(CompoundTag pTag, String pKey, Codec<T> pCodec, DynamicOps<Tag> pOps) {
        Tag tag = pTag.get(pKey);
        return tag == null
                ? Optional.empty()
                : pCodec.parse(pOps, tag).resultOrPartial(p_389874_ -> LOGGER.error("Failed to read field ({}={}): {}", pKey, tag, p_389874_));
    }

    public static <T> void store(CompoundTag pTag, String pKey, Codec<T> pCodec, T pData) {
        store(pTag, pKey, pCodec, NbtOps.INSTANCE, pData);
    }

    /*public static <T> void storeNullable(String pKey, Codec<T> pCodec, @Nullable T pData) {
        if (pData != null) {
            this.store(pKey, pCodec, pData);
        }
    }*/

    public static <T> void store(CompoundTag pTag, String pKey, Codec<T> pCodec, DynamicOps<Tag> pOps, T pData) {
        pTag.put(pKey, pCodec.encodeStart(pOps, pData).getOrThrow(false, IllegalAccessException::new));
    }

    public static long getLongOr(CompoundTag pTag, String pKey, long pDefaultValue) {
        return pTag.get(pKey) instanceof NumericTag numerictag ? numerictag.getAsLong() : pDefaultValue;
    }
}
