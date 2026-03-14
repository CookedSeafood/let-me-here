package net.hederamc.letmehere.network.protocol.common;

import io.netty.buffer.ByteBuf;
import net.hederamc.letmehere.LetMeHere;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public record SitC2SPayload(Vec3 pos) implements CustomPacketPayload {
    public static final Identifier SIT_PAYLOAD_ID = Identifier.fromNamespaceAndPath(LetMeHere.MOD_NAMESPACE, "sit");
    public static final CustomPacketPayload.Type<SitC2SPayload> ID = new CustomPacketPayload.Type<>(SIT_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, SitC2SPayload> CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC,
            SitC2SPayload::pos,
            SitC2SPayload::new);

    public SitC2SPayload(double x, double y, double z) {
        this(new Vec3(x, y, z));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
