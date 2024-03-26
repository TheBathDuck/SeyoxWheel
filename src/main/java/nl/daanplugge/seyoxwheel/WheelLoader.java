package nl.daanplugge.seyoxwheel;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class WheelLoader implements PluginLoader {


    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        MavenLibraryResolver libraryResolver = new MavenLibraryResolver();
        libraryResolver.addRepository(new RemoteRepository.Builder("maven", "default", "https://repo.maven.apache.org/maven2").build());

        libraryResolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.1.0"), null));
        libraryResolver.addDependency(new Dependency(new DefaultArtifact("org.mariadb.jdbc:mariadb-java-client:3.3.3"), null));

        pluginClasspathBuilder.addLibrary(libraryResolver);
    }


}
