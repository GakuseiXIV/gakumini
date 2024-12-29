package gakusei.mini;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GakuminiGamerules {
    public static final GameRules.Key<GameRules.IntRule> SHIELD_DISABLE_TICKS =
            GameRuleRegistry.register("shieldDisableTicks", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(60));
    public static final GameRules.Key<GameRules.IntRule> STRONG_SHIELD_DISABLE_TICKS =
            GameRuleRegistry.register("strongShieldDisableTicks", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(100));
    public static final GameRules.Key<GameRules.IntRule> TOTEM_SICKNESS_LENGTH =
            GameRuleRegistry.register("totemSicknessLength", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(3600));

    public static void init(){}
}
