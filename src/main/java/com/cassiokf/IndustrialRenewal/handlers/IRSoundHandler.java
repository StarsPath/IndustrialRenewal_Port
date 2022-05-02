//package com.cassiokf.IndustrialRenewal.handlers;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.audio.ISound;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.BlockPos;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class IRSoundHandler {
//
//    private static final Minecraft mc = Minecraft.getInstance();
//
//    private static final Map<Long, ISound> soundMap = new HashMap<>();
//
//    public static boolean isSoundPlaying(BlockPos pos)
//    {
//        return soundMap.containsKey(pos.asLong());
//    }
//
//    public static ISound playRepeatableSound(ResourceLocation soundLoc, float volume, float pitch, BlockPos pos)
//    {
//        // First, check to see if there's already a sound playing at the desired location
//        ISound s = soundMap.get(pos.asLong());
//        if (s == null || !mc.getSoundManager().isActive(s))
//        {
//            new Sound
//            // No sound playing, start one up - we assume that tile sounds will play until explicitly stopped
//            s = new PositionedSoundRecord(soundLoc, SoundCategory.BLOCKS, volume, pitch, true, 0,
//                    ISound.AttenuationType.LINEAR, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f)
//            {
//                @Override
//                public float getVolume()
//                {
//                    if (this.sound == null)
//                    {
//                        this.createAccessor(mc.getSoundHandler());
//                    }
//                    return super.getVolume();
//                }
//            };
//
//            // Start the sound
//            playSound(s);
//
//            // N.B. By the time playSound returns, our expectation is that our wrapping-detector handler has fired
//            // and dealt with any muting interceptions and, CRITICALLY, updated the soundMap with the final ISound.
//            s = soundMap.get(pos.toLong());
//        }
//
//        return s;
//    }
//}
