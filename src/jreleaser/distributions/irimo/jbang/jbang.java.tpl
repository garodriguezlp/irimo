//usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA {{distributionJavaVersion}}+
//REPOS mavencentral,github=https://maven.pkg.github.com/garodriguezlp/irimo
//DEPS {{jbangDistributionGA}}:{{projectVersion}}
// {{jreleaserCreationStamp}}

public class {{jbangScriptName}} {
    public static void main(String... args) throws Exception {
        {{distributionJavaMainClass}}.main(args);
    }
}
