package syrena.humanefarms.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import syrena.humanefarms.AnimalFoodData;
import syrena.humanefarms.HumaneFarms;
import syrena.humanefarms.mixin.access.AnimalAccess;

public class SeekFoodGoal extends MoveToBlockGoal {
    protected static final int EAT_ANIMATION_TICKS = 40;
    protected final Level level;
    protected int eatAnimationTick = 0;
    protected final double mayEatThreshold;
    protected final double alwaysEatThreshold;
    protected BlockPos moveToPos = BlockPos.ZERO;
    public SeekFoodGoal(Animal animal, double speedModifier, int searchRange, double mayEatThreshold, double alwaysEatThreshold) {
        super(animal, speedModifier, searchRange,3);
        this.level = animal.level();
        this.mayEatThreshold = mayEatThreshold;
        this.alwaysEatThreshold = alwaysEatThreshold;
        HumaneFarms.LOGGER.info("AI setup");
    }
    protected Animal toAnimal(){
        return (Animal)this.mob;
    }
    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return isDirtConvertible(level, pos) || isDestroyable(level, pos);
    }

    protected boolean isDirtConvertible(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.GRASS_BLOCK);
    }
    protected boolean isDestroyable(LevelReader level, BlockPos pos){
        return level.getBlockState(pos).is(BlockTags.EDIBLE_FOR_SHEEP);
    }
    @Override
    protected boolean findNearestBlock() {
        int horizontalSearch = this.searchRange;
        int verticalSearch = this.verticalSearchRange;
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int y = this.verticalSearchStart; y <= verticalSearch; y = y > 0 ? -y : 1 - y) {
            for (int r = 0; r < horizontalSearch; r++) {
                for (int x = 0; x <= r; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < r && x > -r ? r : 0; z <= r; z = z > 0 ? -z : 1 - z) {
                        pos.setWithOffset(mobPos, x, y - 1, z);
                        if (this.mob.isWithinHome(pos) && this.isValidTarget(this.mob.level(), pos)) {
                            this.blockPos = pos;
                            if (isDestroyable(level, pos)){
                                this.moveToPos = pos;
                            } else {
                                this.moveToPos = pos.above();
                            }
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void tick() {
        if (this.eatAnimationTick > 0) {
            HumaneFarms.LOGGER.info("Eating");
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
            if (this.eatAnimationTick == 4 && this.isValidTarget(level,blockPos)){
                if (getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)){
                    if (isDestroyable(level,moveToPos)){
                        this.level.destroyBlock(blockPos, false);
                    } else {
                        this.level.levelEvent(2001, blockPos, Block.getId(level.getBlockState(blockPos).getBlock().defaultBlockState()));
                        this.level.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 2);
                    }
                }
                this.mob.ate();
            }
        } else if (moveToPos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
            HumaneFarms.LOGGER.info("Moved");
            if (this.isValidTarget(this.level,blockPos)){
                HumaneFarms.LOGGER.info("Starting eat");
                this.eatAnimationTick = this.adjustedTickDelay(40);
                this.level.broadcastEntityEvent(this.mob, (byte)10);
                this.mob.getNavigation().stop();
            }

            this.reachedTarget = true;
            this.tryTicks--;
        } else {
            HumaneFarms.LOGGER.info("Moving");
            this.reachedTarget = false;
            this.tryTicks++;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo(moveToPos.getX() + 0.5, moveToPos.getY(), moveToPos.getZ() + 0.5, this.speedModifier);
            }
        }
    }
    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }
    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0 || (this.tryTicks <= 1200 && this.isValidTarget(this.mob.level(), this.blockPos));
    }
    public int getEatAnimationTick(){
        return this.eatAnimationTick;
    }
    @Override
    public boolean canUse() {
        AnimalFoodData foodData = ((AnimalAccess)this.toAnimal()).humanefarms$foodData();
        if (this.mob.getRandom().nextDouble() * (mayEatThreshold - alwaysEatThreshold) + alwaysEatThreshold > ((double) foodData.foodLevel / foodData.maxFoodLevel(toAnimal()))) {

            HumaneFarms.LOGGER.info("Can use");
            return this.findNearestBlock();
        }
        return false;
    }
}
