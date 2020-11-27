package net.pl3x.bukkit.weapons.command.argument;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import net.pl3x.bukkit.weapons.configuration.Lang;
import net.pl3x.bukkit.weapons.weapons.BaseWeapon;
import net.pl3x.bukkit.weapons.weapons.WeaponManager;

import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class WeaponArgument<C> extends CommandArgument<C, BaseWeapon> {

    protected WeaponArgument(
            final boolean required,
            final String name,
            final String defaultValue,
            final BiFunction<CommandContext<C>, String, List<String>> suggestionsProvider
    ) {
        super(required, name, new WeaponParser<>(), defaultValue, BaseWeapon.class, suggestionsProvider);
    }

    public static <C> WeaponArgument.Builder<C> newBuilder(final String name) {
        return new WeaponArgument.Builder<>(name);
    }

    public static <C> CommandArgument<C, BaseWeapon> of(final String name) {
        return WeaponArgument.<C>newBuilder(name).asRequired().build();
    }

    public static <C> CommandArgument<C, BaseWeapon> optional(final String name) {
        return WeaponArgument.<C>newBuilder(name).asOptional().build();
    }

    public static <C> CommandArgument<C, BaseWeapon> optional(
            final String name,
            final BaseWeapon weapon
    ) {
        return WeaponArgument.<C>newBuilder(name).asOptionalWithDefault(weapon.getId()).build();
    }

    public static final class Builder<C> extends CommandArgument.Builder<C, BaseWeapon> {

        protected Builder(final String name) {
            super(BaseWeapon.class, name);
        }

        @Override
        public CommandArgument<C, BaseWeapon> build() {
            return new WeaponArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider()
            );
        }

    }

    public static final class WeaponParser<C> implements ArgumentParser<C, BaseWeapon> {

        @Override
        public ArgumentParseResult<BaseWeapon> parse(
                final CommandContext<C> commandContext,
                final Queue<String> inputQueue
        ) {
            String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NullPointerException("No input provided"));
            }
            final BaseWeapon weapon = WeaponManager.getWeapon(input);
            if (weapon != null) {
                inputQueue.remove();
                return ArgumentParseResult.success(weapon);
            }
            return ArgumentParseResult.failure(new IllegalArgumentException(
                    Lang.WEAPON_NOT_FOUND
            ));
        }

        @Override
        public List<String> suggestions(
                final CommandContext<C> commandContext,
                final String input
        ) {
            return WeaponManager.WEAPONS_REGISTRY.values()
                    .stream()
                    .map(BaseWeapon::getId)
                    .collect(Collectors.toList());
        }

    }

}
