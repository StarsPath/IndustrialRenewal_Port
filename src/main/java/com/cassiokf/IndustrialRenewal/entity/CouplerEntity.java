//package com.cassiokf.IndustrialRenewal.entity;
//
//import com.cassiokf.IndustrialRenewal.init.ModEntity;
//import com.cassiokf.IndustrialRenewal.util.Utils;
//import net.minecraft.block.material.PushReaction;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.MoverType;
//import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.network.IPacket;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.world.World;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.fml.network.NetworkHooks;
//
//import java.util.UUID;
//
//public class CouplerEntity extends Entity {
//
////    private static final DataParameter<Optional<UUID>> CART1 = EntityDataManager.defineId(CouplerEntity.class, DataSerializers.OPTIONAL_UUID);
////    private static final DataParameter<Optional<UUID>> CART2 = EntityDataManager.defineId(CouplerEntity.class, DataSerializers.OPTIONAL_UUID);
//
////    private static final DataParameter<CompoundNBT> CART1NBT = EntityDataManager.defineId(CouplerEntity.class, DataSerializers.COMPOUND_TAG);
////    private static final DataParameter<CompoundNBT> CART2NBT = EntityDataManager.defineId(CouplerEntity.class, DataSerializers.COMPOUND_TAG);
//
//    AbstractMinecartEntity cart1;
//    AbstractMinecartEntity cart2;
//
//    Vector3d headPosition;
//    Vector3d tailPosition;
//
//    private static final double PREFERRED_DISTANCE = 1.5;
//    public double lastForceX = 0;
//    public double lastForceY = 0;
//    public double lastForceZ = 0;
//    public double lastDiff = 0;
//
//    public CouplerEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
//        super(p_i48580_1_, p_i48580_2_);
//    }
//
//    public CouplerEntity(World world, double x, double y, double z, AbstractMinecartEntity cart1, AbstractMinecartEntity cart2){
//        super(ModEntity.COUPLER_ENTITY.get(), world);
//        this.cart1 = cart1;
//        this.cart2 = cart2;
//        Utils.debug("LINKING...", cart1.getUUID(), cart2.getUUID());
//        Utils.debug("LINKING...", cart1.position(), cart2.position());
//        Utils.debug("LINKING...", x, y, z);
//        this.setPos(x, y, z);
////        setCart1(cart1.getUUID());
////        setCart2(cart2.getUUID());
//    }
//
////    public void setCart1(AbstractMinecartEntity cart1) {
////        CompoundNBT nbt = new CompoundNBT();
////        nbt.put
////    }
//
//    @Override
//    public void tick() {
//        super.tick();
//        //Utils.debug("TICKING");
//
//        if (cart1 == null || cart2 == null) {
//            Utils.debug("NULL CART");
//            return;
//        }
//
//        if(cart1 != null && cart2 != null) {
//
//            this.headPosition = cart1.position();
//            this.tailPosition = cart2.position();
//            maintainDistance(PREFERRED_DISTANCE);
//            updatePos();
//        }
//
//        if (!cart1.isAlive() || !cart2.isAlive()) {
//            Utils.debug("REMOVED");
//            //this.onBroken(true);
//            remove();
//        }
//    }
//
////    private void releaseTensionToOne(boolean is_first) {
////        Entity to = is_first? cart1 : cart2;
////        int scalar = is_first? 1 : -1;
////
////        Vector3d motion = to.getDeltaMovement();
////        Vector3d between = new Vector3d(lastForceX, lastForceY, lastForceZ);
////        Vector3d force = between.normalize().scale(getIntegratedSpringForce(lastDiff));
////        to.setDeltaMovement(motion.add(force.scale(scalar)));
////    }
////
////    public static double getIntegratedSpringForce(double distance) {
////        boolean is_neg = distance < 0;
////        double unsigned = Math.abs((1.0/3.0) * Math.pow( Math.abs(distance), 4) * 0.3);
////        return is_neg? -1*unsigned : unsigned;
////    }
//
//    public static double getSpringForce(double distance) {
//        boolean is_neg = distance < 0;
//        double unsigned = Math.abs(Math.pow( Math.abs(distance), 3) * 0.3);
//        return is_neg? -1*unsigned : unsigned;
//    }
//
//    public void maintainDistance(double distance){
////        if(headPosition.distanceTo(tailPosition) > distance){
//            //cart2.move(MoverType.SELF, cart2.position().vectorTo(cart1.position()).normalize());
////            cart2.moveRelative(1f, cart2.position().vectorTo(cart1.position()));
////            cart1.moveRelative(1f, cart1.position().vectorTo(cart2.position()));
////
//
//        Vector3d motion1 = cart1.getDeltaMovement();
//        Vector3d motion2 = cart2.getDeltaMovement();
//
//        double d = cart1.distanceTo(cart2);
//        double distance_diff = d - distance;
//        //if (distance_diff < 0) distance_diff = 0;
//
//        lastDiff = distance_diff;
//
//        Vector3d between = cart1.position().subtract(cart2.position());
//        lastForceX = between.x;
//        lastForceY = between.y;
//        lastForceZ = between.z;
//        Vector3d force = between.normalize().scale(getSpringForce(distance_diff));
//
//        cart1.setDeltaMovement(motion1.add(force.scale(-1)));
//        cart2.setDeltaMovement(motion2.add(force.scale( 1)));
//
//        cart1.setDeltaMovement(cart1.getDeltaMovement().scale(0.95));
//        cart2.setDeltaMovement(cart2.getDeltaMovement().scale(0.95));
//            //this.move(MoverType.SELF, this.position().vectorTo(cart1.position()).normalize());
////        }
//    }
//
//    public void updatePos(){
//        Vector3d v1 = cart1.position();
//        Vector3d v2 = cart2.position();
//        double x = (v1.x + v2.x)/2;
//        double y = (v1.y + v2.y)/2;
//        double z = (v1.z + v2.z)/2;
//        this.setPos(x,y,z);
//    }
//
////
////    public void setCart1(UUID id){
////        this.entityData.set(CART1, Optional.of(id));
////    }
////
////    public Optional<UUID> getCar1(){
////        return this.entityData.get(CART1);
////    }
////
////    public void setCart2(UUID id){
////        this.entityData.set(CART2, Optional.of(id));
////    }
////
////    public Optional<UUID> getCar2(){
////        return this.entityData.get(CART2);
////    }
//
//    @Override
//    public boolean skipAttackInteraction(Entity player) {
//        if (player instanceof PlayerEntity) {
//            PlayerEntity playerentity = (PlayerEntity)player;
//            return !this.level.mayInteract(playerentity, this.getOnPos()) ? true : this.hurt(DamageSource.playerAttack(playerentity), 0.0F);
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean hurt(DamageSource source, float amount) {
//        if (this.isInvulnerableTo(source)) {
//            return false;
//        } else {
//            if (this.isAlive() && !this.level.isClientSide) {
////                this.releaseTensionToBoth();
////                this.onBroken(!source.isCreativePlayer());
//                this.markHurt();
//                this.remove();
//            }
//
//            return true;
//        }
//    }
//
//    @Override
//    public boolean canBeCollidedWith() {
//        return false;
//    }
//
//    @Override
//    public boolean isPushable() { return false; }
//
//    @Override
//    public boolean canCollideWith(Entity p_241849_1_) { return false; }
//
//    @Override
//    public PushReaction getPistonPushReaction() { return PushReaction.IGNORE; }
//
//    public boolean isPickable() {
//        return !this.removed;
//    }
//
//    @Override
//    protected void defineSynchedData() {
//    }
//
//    @Override
//    protected void readAdditionalSaveData(CompoundNBT compoundNBT) {
//        if (compoundNBT.hasUUID("cart1") && compoundNBT.hasUUID("cart2")) {
//
//            Utils.debug("compound has UUID, READING UUID");
//
//            UUID cart1UUID = compoundNBT.getUUID("cart1");
//            UUID cart2UUID = compoundNBT.getUUID("cart2");
//            this.cart1 = (AbstractMinecartEntity) ((ServerWorld) level).getEntity(cart1UUID);
//            this.cart2 = (AbstractMinecartEntity) ((ServerWorld) level).getEntity(cart2UUID);
//        }
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
//        if(cart1!=null && cart2!=null){
//
//            Utils.debug("WRITING UUID");
//
//            compoundNBT.putUUID("cart1", cart1.getUUID());
//            compoundNBT.putUUID("cart2", cart2.getUUID());
//        }
//    }
//
//    @Override
//    public IPacket<?> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//}
