@startuml
    [${ title }]

    <#list requiresOxps as dep>
        [${ dep.identifier!"" }:${ dep.version!"" }]
        [${ title }] --> [${ dep.identifier!"" }:${ dep.version!"" }]: requires
    </#list>

    <#if optionalOxps?has_content>
    package "Optional" {
        <#list optionalOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
        </#list>
    }
    </#if>

    <#if conflictOxps?has_content>
    package "Conflict" {
        <#list conflictOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
        </#list>
    }
    </#if>

    <#if dependentOxps?has_content>
    <#list dependentOxps as dep>
        [${ dep.identifier!"" }:${ dep.version!"" }]
        [${ title }] --> [${ dep.identifier!"" }:${ dep.version!"" }]: enables
    </#list>
    </#if>

@enduml
