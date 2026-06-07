@startuml
    [${ title }] as this #ffff99

    <#if conflictOxps?has_content>
    together {
        <#list conflictOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this -left[#red]-> [${ dep.identifier!"" }:${ dep.version!"" }]: conflicts
            <#if dep_has_next>
                <#assign nextDep = conflictOxps[dep_index + 1]>
                [${ dep.identifier!"" }:${ dep.version!"" }] -down[hidden]- [${ nextDep.identifier!"" }:${ nextDep.version!"" }]
            </#if>
        </#list>
    }
    </#if>

    <#if requiresOxps?has_content>
    together {
        <#list requiresOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this -down[#black,thickness=3]-> [${ dep.identifier!"" }:${ dep.version!"" }]: requires
        </#list>
    }
    </#if>

    <#if optionalOxps?has_content>
    together {
        <#list optionalOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this -right[#blue]-> [${ dep.identifier!"" }:${ dep.version!"" }]: supports
            <#if dep_has_next>
                <#assign nextDep = optionalOxps[dep_index + 1]>
                [${ dep.identifier!"" }:${ dep.version!"" }] -down[hidden]- [${ nextDep.identifier!"" }:${ nextDep.version!"" }]
            </#if>
        </#list>
    }
    </#if>

    <#if dependentOxps?has_content>
    together {
        <#list dependentOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this -up.> [${ dep.identifier!"" }:${ dep.version!"" }]: used by
        </#list>
    }
    </#if>
@enduml
