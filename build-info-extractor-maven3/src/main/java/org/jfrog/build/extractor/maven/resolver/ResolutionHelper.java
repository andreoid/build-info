package org.jfrog.build.extractor.maven.resolver;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.jfrog.build.client.ArtifactoryClientConfiguration;
import org.jfrog.build.extractor.BuildInfoExtractorUtils;
import org.jfrog.build.extractor.maven.Maven3BuildInfoLogger;
import java.util.Properties;

/**
 * Created by liorh on 4/24/14.
 */

@Component(role = ResolutionHelper.class)
public class ResolutionHelper {

    @Requirement
    private Logger logger;

    private ArtifactoryClientConfiguration internalConfiguration;

    public void init(Properties allMavenProps) {
        if (internalConfiguration != null) {
            return;
        }

        Maven3BuildInfoLogger log = new Maven3BuildInfoLogger(logger);
        Properties allProps = BuildInfoExtractorUtils.mergePropertiesWithSystemAndPropertyFile(allMavenProps, log);
        internalConfiguration = new ArtifactoryClientConfiguration(log);
        internalConfiguration.fillFromProperties(allProps);
    }

    /**
     * Determines a deployed artifact's scope (either "project" or "build") according to the maven's request context sent as an argument.
     * @param requestContext    The deployed artifact's request context.
     * @return                  Scope value for the request context.
     */
    public String getScopeByRequestContext(String requestContext) {
        if (requestContext == null) {
            return "project";
        }
        if ("plugin".equals(requestContext)) {
            return "build";
        }
        return "project";
    }

    public String getRepoReleaseUrl() {
        return internalConfiguration.resolver.getUrl(internalConfiguration.resolver.getRepoKey());
    }

    public String getRepoSnapshotUrl() {
        return internalConfiguration.resolver.getUrl(internalConfiguration.resolver.getDownloadSnapshotRepoKey());
    }

    public String getRepoUsername() {
        return internalConfiguration.resolver.getUsername();
    }

    public String getRepoPassword() {
        return internalConfiguration.resolver.getPassword();
    }

    public String getProxyHost() {
        return internalConfiguration.proxy.getHost();
    }

    public Integer getProxyPort() {
        return internalConfiguration.proxy.getPort();
    }

    public String getProxyUsername() {
        return internalConfiguration.proxy.getUsername();
    }

    public String getProxyPassword() {
        return internalConfiguration.proxy.getPassword();
    }
}