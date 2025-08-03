# Mockito Agent Configuration

## Issue Description

When running the `BeerServiceImplTest` class, the following warnings were displayed:

```
Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build as described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
WARNING: A Java agent has been loaded dynamically (C:\Users\yul\.m2\repository\net\bytebuddy\byte-buddy-agent\1.17.6\byte-buddy-agent-1.17.6.jar)
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
WARNING: Dynamic loading of agents will be disallowed by default in a future release
Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
```

## Root Cause

Mockito uses ByteBuddy to create mock objects. In recent versions, Mockito has been dynamically loading the ByteBuddy agent during test execution. However, this approach of dynamically loading Java agents will be disallowed in future JDK releases for security reasons.

## Solution

The solution is to configure Mockito as a Java agent in the Maven build process, rather than letting it dynamically load itself. This was done by adding the Maven Surefire plugin configuration to the `pom.xml` file:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>
            -javaagent:${settings.localRepository}/org/mockito/mockito-core/5.17.0/mockito-core-5.17.0.jar
        </argLine>
    </configuration>
</plugin>
```

This configuration:
1. Adds the Maven Surefire plugin, which is responsible for running tests
2. Configures the JVM arguments to include Mockito as a Java agent
3. Uses `${settings.localRepository}` to reference the local Maven repository path
4. Specifies the exact version of Mockito (5.17.0) that's being used in the project

## Why This Works

By explicitly loading Mockito as a Java agent at JVM startup (via the `-javaagent` flag), we prevent Mockito from having to dynamically load itself during test execution. This approach is future-proof and complies with the security restrictions that will be enforced in future JDK releases.

## Additional Information

For more details on Mockito's agent configuration, refer to the official documentation:
https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3