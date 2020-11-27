package net.pl3x.bukkit.weapons.command;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCommandMetaBuilder;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import io.leangen.geantyref.TypeToken;
import java.util.function.Function;
import net.pl3x.bukkit.weapons.command.argument.WeaponArgument;
import net.pl3x.bukkit.weapons.weapons.BaseWeapon;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandManager extends PaperCommandManager<CommandSender> {

    public CommandManager(Plugin plugin) throws Exception {

        super(
                plugin,
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
        );

        final Function<ParserParameters, CommandMeta> commandMetaFunction = p ->
                BukkitCommandMetaBuilder.builder()
                        .withDescription(p.get(StandardParameters.DESCRIPTION, "No description"))
                        .build();

        final AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(
                this,
                CommandSender.class,
                commandMetaFunction
        );

        if (this.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            this.registerBrigadier();
        }

        if (this.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        // Register BaseWeapon type parser for annotated methods
        this.getParserRegistry().registerParserSupplier(
                TypeToken.get(BaseWeapon.class),
                parserParameters ->
                        new WeaponArgument.WeaponParser<>()
        );

        ImmutableList.of(
                new CmdWeapons(plugin)
        ).forEach(annotationParser::parse);

    }

}
