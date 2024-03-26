package nl.daanplugge.seyoxwheel.configuration;

import lombok.Getter;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Getter
public class ConfigHandler {

    private final YamlConfigurationLoader yamlLoader;
    private CommentedConfigurationNode node;
    private Logger logger;

    public ConfigHandler(File file, String name, Logger logger) {
        this.logger = logger;
        yamlLoader = YamlConfigurationLoader.builder()
                .path(file.toPath().resolve(name))
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESET)
                .build();

        try {
            node = yamlLoader.load();
        } catch (IOException e) {
            logger.warning("An error occurred while loading this configuration: " + e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            yamlLoader.save(node);
        } catch (final ConfigurateException e) {
            logger.warning("Unable to save your messages configuration! Sorry! " + e.getMessage());
        }
    }

}
