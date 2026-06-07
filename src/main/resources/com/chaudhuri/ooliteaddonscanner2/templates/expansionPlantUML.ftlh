@startuml
    [${ title }] as this #ffff99

    <#if conflictOxps?has_content>
    package "Conflict" as conflict {
        <#list conflictOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }] #red
            [${ dep.identifier!"" }:${ dep.version!"" }] <-[#red]- this: conflicts
            
            <#if dep_has_next>
                <#assign nextDep = conflictOxps[dep_index + 1]>
                [${ dep.identifier!"" }:${ dep.version!"" }] -down[hidden]- [${ nextDep.identifier!"" }:${ nextDep.version!"" }]
            </#if>
        </#list>
    }
    </#if>

    <#if requiresOxps?has_content>
    this -down[hidden]- requirements
    package "Requirements" as requirements {
        <#list requiresOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this --> [${ dep.identifier!"" }:${ dep.version!"" }]: requires
        </#list>
    }
    </#if>

    <#if optionalOxps?has_content>
    this -right[hidden]- optional
    package "Optional" as optional {

        <#list optionalOxps as dep>
            [${ dep.identifier!"" }:${ dep.version!"" }]
            this -> [${ dep.identifier!"" }:${ dep.version!"" }] #blue: supports
            
            <#if dep_has_next>
                <#assign nextDep = optionalOxps[dep_index + 1]>
                [${ dep.identifier!"" }:${ dep.version!"" }] -down[hidden]- [${ nextDep.identifier!"" }:${ nextDep.version!"" }]
            </#if>
        </#list>
    }
    </#if>

    <#if dependentOxps?has_content>
    <#list dependentOxps as dep>
        [${ dep.identifier!"" }:${ dep.version!"" }]
         [${ dep.identifier!"" }:${ dep.version!"" }] <-- this: enables
    </#list>
    </#if>

@enduml
