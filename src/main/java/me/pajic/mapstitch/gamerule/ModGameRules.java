package me.pajic.mapstitch.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public class ModGameRules {
	public static GameRule<Boolean> REQUIRE_COMPASS_FOR_POS = booleanGameRule();

	private static GameRule<Boolean> booleanGameRule() {
		return new GameRule<>(
				GameRuleCategory.PLAYER,
				GameRuleType.BOOL,
				BoolArgumentType.bool(),
				GameRuleTypeVisitor::visitBoolean,
				Codec.BOOL,
				bl -> bl ? 1 : 0,
				true,
				FeatureFlagSet.of()
		);
	}

	public static void init() {}
}
